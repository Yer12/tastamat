package kz.tastamat.order.handler;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import kz.tastamat.core.dto.DropResponse;
import kz.tastamat.core.dto.OpenRequest;
import kz.tastamat.core.dto.OpenResponse;
import kz.tastamat.core.handler.CoreHandler;
import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.db.model.enums.OrderStatus;
import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.params.SearchParams;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.order.bean.OrderBean;
import kz.tastamat.order.dto.ActionDto;
import kz.tastamat.order.dto.OrderInfoDto;
import kz.tastamat.order.dto.StatusRequest;
import kz.tastamat.order.dto.StatusResponse;
import kz.tastamat.payment.handler.PaymentHandler;
import kz.tastamat.profile.bean.ProfileBean;
import kz.tastamat.profile.dto.ProfileInfoDto;
import kz.tastamat.sms.dto.SmsDto;
import kz.tastamat.sms.handler.SmsHandler;
import kz.tastamat.utils.EncryptUtils;
import kz.tastamat.utils.JsonUtils;
import kz.tastamat.utils.QueryParamsUtils;
import kz.zx.api.app.DbHandler;
import kz.zx.exceptions.ApiException;
import kz.zx.utils.PaginatedList;

import java.util.Date;

public class OrderHandler extends DbHandler {

	private Logger log = LoggerFactory.getLogger(OrderHandler.class);

	private final CoreHandler coreHandler;
	private final SmsHandler smsHandler;
	private PaymentHandler paymentHandler;

	private WebClient webClient;

	protected String prefix;
	protected Long price;

	public OrderHandler(Vertx vertx) {
		super(vertx);
		JsonObject config = vertx.getOrCreateContext().config();

		JsonObject billing = config.getJsonObject(ConfigKey.BILLING.key());
		this.price = billing.getLong(ConfigKey.PRICE.key());

		JsonObject core = config.getJsonObject(ConfigKey.CORE.key());
		this.prefix = core.getString(ConfigKey.PREFIX.key());

		this.smsHandler = new SmsHandler(vertx);
		this.coreHandler = new CoreHandler(vertx);
		this.paymentHandler = new PaymentHandler(vertx, config);

		this.webClient = WebClient.create(vertx);
	}

	public void getOrders(MultiMap params, Handler<AsyncResult<PaginatedList<OrderDto>>> handler) {
		SearchParams searchParams = QueryParamsUtils.build(params);
		blocking(ctx -> {
			return OrderBean.build(ctx).getOrders(searchParams);
		}, ar -> {
			if(ar.succeeded()){
				handler.handle(Future.succeededFuture(ar.result()));
			} else {
				handler.handle(Future.failedFuture(ar.cause()));
			}
		});
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
				ProfileInfoDto profile = ur.result();

				if(profile.wallet < this.price){
					JsonObject error = JsonUtils.getDictionary("not.enough.money", "", "Недостаточно средств", "", "");
					handler.handle(Future.failedFuture(ApiException.business(error.toString())));
				} else {
					blocking(jooq -> {
						return OrderBean.build(jooq).create(orderDto);
					}, pr -> {
						if (pr.succeeded()) {
							OrderDto dto = pr.result();
							orderDto.identificator = dto.identificator;
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

													PaymentDto paymentDto = new PaymentDto();
													paymentDto.amount = this.price;
													paymentDto.status = PaymentStatus.SPENT;
													paymentHandler.create(orderDto.creatorId, paymentDto, sr -> {
														if (sr.failed()) {
															log.error("sms error {}", sr.cause());
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

	public void withdraw(ActionDto actionDto, Handler<AsyncResult<Void>> handler) {
		blocking(dsl -> {
			return OrderBean.build(dsl).getFullInfo(actionDto.id);
		}, br -> {
			if (br.succeeded()) {
				OrderDto order = br.result();

				boolean ex = false;

				if(!OrderStatus.SENT.equals(order.status)){
					ex = true;
					JsonObject statusError = JsonUtils.getDictionary("open.invalid.status", "", "Статус не валиден", "", "");
					handler.handle(Future.failedFuture(ApiException.business(statusError.toString())));
				}

				if(!actionDto.userId.equals(order.creatorId)){
					ex = true;
					JsonObject statusError = JsonUtils.getDictionary("open.invalid.user", "", "Вы не являетесь владельцем заказа", "", "");
					handler.handle(Future.failedFuture(ApiException.business(statusError.toString())));
				}

				if(!ex){
					coreHandler.withdraw(actionDto.locker, order.pickCode, or -> {
						if(or.succeeded()){
							blocking(dsl -> {
								return OrderBean.build(dsl).withdraw(order.id);
							}, rr -> {
								if (rr.succeeded()) {
									handler.handle(Future.succeededFuture());
								} else {
									handler.handle(Future.failedFuture(rr.cause()));
								}
							});
						} else {
							handler.handle(Future.failedFuture(or.cause()));
						}
					});
				}

			} else {
				handler.handle(Future.failedFuture(br.cause()));
			}
		});
	}

	public void pick(String code, Handler<AsyncResult<Void>> handler){
		blocking(dsl -> {
			return OrderBean.build(dsl).getFullInfoByPickCode(code);
		}, br -> {
			if (br.succeeded()) {
				OrderDto order = br.result();

				if(OrderStatus.SENT.equals(order.status)){
					blocking(dsl -> {
						return OrderBean.build(dsl).end(order.id);
					}, rr -> {
						if (rr.succeeded()) {
							handler.handle(Future.succeededFuture());
						} else {
							handler.handle(Future.failedFuture(rr.cause()));
						}
					});
				} else {
					JsonObject pickError = JsonUtils.getDictionary("pick.invalid.status", "", "Статус посылки не валиден", "", "");
					handler.handle(Future.failedFuture(ApiException.business(pickError.toString())));
				}
			} else {
				handler.handle(Future.failedFuture(br.cause()));
			}
		});
	}

	public void status(StatusRequest request, Handler<AsyncResult<StatusResponse>> handler){
		StatusResponse response = new StatusResponse();
		response.date = new Date();
		response.result = StatusResponse.Result.SUCCESS;
		if(OrderStatus.END.equals(request.status)){
			blocking(dsl -> {
				return OrderBean.build(dsl).getFullInfoByIdentificator(request.identificator);
			}, br -> {
				if (br.succeeded()) {
					OrderDto order = br.result();

					if(OrderStatus.SENT.equals(order.status)){
						blocking(dsl -> {
							return OrderBean.build(dsl).end(order.id);
						}, rr -> {
							if (rr.succeeded()) {
								handler.handle(Future.succeededFuture(response));
							} else {
								handler.handle(Future.failedFuture(rr.cause()));
							}
						});
					} else {
						JsonObject pickError = JsonUtils.getDictionary("pick.invalid.status", "", "Статус посылки не валиден", "", "");
						handler.handle(Future.failedFuture(ApiException.business(pickError.toString())));
					}
				} else {
					handler.handle(Future.failedFuture(br.cause()));
				}
			});
		} else {
			handler.handle(Future.succeededFuture(response));
		}
	}

	public Future<JsonObject> getTracking(String identifier) {
		Future<JsonObject> future = Future.future();
		coreHandler.getTracking(identifier, rr -> {
			if(rr.succeeded()){
				future.complete(rr.result());
			} else {
				future.fail(rr.cause());
			}
		});

		return future;
	}
}
