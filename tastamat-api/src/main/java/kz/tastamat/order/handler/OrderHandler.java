package kz.tastamat.order.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.core.dto.DropResponse;
import kz.tastamat.core.dto.OpenRequest;
import kz.tastamat.core.dto.OpenResponse;
import kz.tastamat.core.handler.CoreHandler;
import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.order.bean.OrderBean;
import kz.tastamat.order.dto.OrderInfoDto;
import kz.tastamat.profile.bean.ProfileBean;
import kz.tastamat.sms.dto.SmsDto;
import kz.tastamat.sms.handler.SmsHandler;
import kz.tastamat.utils.JsonUtils;
import kz.zx.api.app.DbHandler;
import kz.zx.exceptions.ApiException;

public class OrderHandler extends DbHandler {

	private Logger log = LoggerFactory.getLogger(OrderHandler.class);

	private final CoreHandler coreHandler;
	private final SmsHandler smsHandler;

	protected String prefix;
	protected Long price;

	public OrderHandler(Vertx vertx) {
		super(vertx);
		JsonObject config = vertx.getOrCreateContext().config();

		JsonObject billing = config.getJsonObject(ConfigKey.BILLING.key());
		this.price = billing.getLong(ConfigKey.PRICE.key());

		JsonObject core = config.getJsonObject(ConfigKey.CORE.key());
		this.prefix = core.getString(ConfigKey.PREFIX.key());

		this.coreHandler = new CoreHandler(vertx);
		this.smsHandler = new SmsHandler(vertx);
	}

	public void getOrder(String identificator, Handler<AsyncResult<OrderInfoDto>> handler) {
		coreHandler.getOrder(identificator, rr -> {
			if(rr.succeeded()){
				OrderInfoDto order = rr.result();
				handler.handle(Future.succeededFuture(order));
			} else {
				handler.handle(Future.failedFuture(rr.cause()));
			}
		});
	}

	public void create(OrderDto orderDto, Handler<AsyncResult<Void>> handler) {

		blocking(jq -> {
			return ProfileBean.build(jq).getFullInfoByUser(orderDto.creatorId);
		}, ur -> {
			if(ur.succeeded()){
				ProfileDto profile = ur.result();

				if(profile.wallet < this.price){
					JsonObject error = JsonUtils.getDictionary("not.enough.money", "", "Недостаточно средств", "", "");
					handler.handle(Future.failedFuture(ApiException.business(error.toString())));
				} else {
					blocking(jooq -> {
						return OrderBean.build(jooq).create(orderDto);
					}, pr -> {
						if (pr.succeeded()) {
							OrderDto dto = pr.result();
							coreHandler.drop(orderDto, rr -> {
								if(rr.succeeded()){
									DropResponse result = rr.result();

									blocking(dsl -> {
										Long sum = profile.wallet - this.price;
										return ProfileBean.build(dsl).wallet(profile.id, sum);
									}, pro -> {
										if(pro.succeeded()){
											blocking(dsl -> {
												return OrderBean.build(dsl).drop(result.dropCode, result.pickCode, dto.id);
											}, dr -> {
												if (dr.succeeded()) {
													SmsDto smsDto = new SmsDto();
													smsDto.phone = dto.recipientPhone;
													smsDto.message = "Уважаемый(ая) "+dto.recipientName+"!\n" +
															profile.template+"\n" +
															"Подробности:\n"+
															this.prefix+result.pickCode;
													smsHandler.sendSms(smsDto , sr -> {
//													blocking(dsl -> {
//														kz.kazpost.db.model.dto.SmsDto dto = new kz.kazpost.db.model.dto.SmsDto();
//														dto.response = sr.succeeded()?sr.result().toString():sr.cause().getMessage().toString();
//														dto.type = SmsType.DROP;
//														return SmsBean.build(dsl).create(order.id, dto);
//													}, res -> {
//													});

														if(sr.succeeded()){
															blocking(dsl -> {
																return OrderBean.build(dsl).sms(dto.id);
															}, res -> {
															});
														}
													});

													handler.handle(Future.succeededFuture());
												} else {
													handler.handle(Future.failedFuture(rr.cause()));
												}
											});
										} else {
											handler.handle(Future.failedFuture(pro.cause()));
										}
									});
								} else {
									handler.handle(Future.failedFuture(rr.cause()));
								}
							});
						} else {
							handler.handle(Future.failedFuture(pr.cause()));
						}
					});
				}
			} else {
				handler.handle(Future.failedFuture(ur.cause()));
			}
		});
	}

	public void open(OpenRequest request, Handler<AsyncResult<Void>> handler) {
		coreHandler.open(request, rr -> {
			if(rr.succeeded()){
				OpenResponse order = rr.result();
				handler.handle(Future.succeededFuture());
			} else {
				handler.handle(Future.failedFuture(rr.cause()));
			}
		});
	}
}
