package kz.tastamat.payment.handler;

import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.params.SearchParams;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.payment.bean.PaymentBean;
import kz.tastamat.payment.dto.*;
import kz.tastamat.payment.verticle.PaymentVerticle;
import kz.tastamat.utils.JsonUtils;
import kz.tastamat.utils.QueryParamsUtils;
import kz.zx.api.app.DbHandler;
import kz.zx.api.enums.Key;
import kz.zx.exceptions.ApiException;
import kz.zx.json.Mapper;
import kz.zx.utils.PaginatedList;

import java.util.Arrays;

public class PaymentHandler extends DbHandler {

	private Logger log = LoggerFactory.getLogger(PaymentHandler.class);

	private static final DeliveryOptions MAKE_PAYMENT = new DeliveryOptions().addHeader(Key.action, PaymentVerticle.Action.MAKE_PAYMENT.name());
	private static final DeliveryOptions STATUS_PAYMENT = new DeliveryOptions().addHeader(Key.action, PaymentVerticle.Action.STATUS_PAYMENT.name());

	protected String link, webhook;

	public PaymentHandler(Vertx vertx) {
		super(vertx);
		JsonObject config = vertx.getOrCreateContext().config();

		JsonObject billing = config.getJsonObject(ConfigKey.PAYPOST.key());
		this.link = billing.getString(ConfigKey.LINK.key());
		this.webhook = billing.getString(ConfigKey.WEBHOOK.key());

	}

	public void getPayments(MultiMap params, Handler<AsyncResult<PaginatedList<PaymentDto>>> handler) {
		SearchParams searchParams = QueryParamsUtils.build(params);
		blocking(ctx -> {
			return PaymentBean.build(ctx).find(searchParams);
		}, ar -> {
			if(ar.succeeded()){
				handler.handle(Future.succeededFuture(ar.result()));
			} else {
				handler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	public void request(Long userId, PaymentDto payment, Handler<AsyncResult<PaymentResponse>> handler) {
		initialize(userId, payment, cr -> {
			if (cr.succeeded()) {
				submit(cr.result(), sr -> {
					if (sr.succeeded()) {
						handler.handle(Future.succeededFuture(sr.result()));
					} else {
						handler.handle(Future.failedFuture(sr.cause()));
					}
				});
			} else {
				handler.handle(Future.failedFuture(cr.cause()));
			}
		});
	}

	public void initialize(Long userId, PaymentDto payment, Handler<AsyncResult<PaymentDto>> handler) {
		validatePayment(payment);
		blocking(jooq -> {
			return PaymentBean.build(jooq).initialize(userId, payment);
		}, pr -> {
			if (pr.succeeded()) {
				PaymentDto dto = pr.result();
				handler.handle(Future.succeededFuture(dto));
			} else {
				handler.handle(Future.failedFuture(pr.cause()));
			}
		});
	}

	public void submit(PaymentDto dto, Handler<AsyncResult<PaymentResponse>> handler) {
		String amount = dto.amount.toString();
		String link = this.link;
		String webhook = this.webhook + dto.identificator;

		PaymentRequest request = PaymentRequest.build(amount, link, webhook);
		vertx.eventBus().send(PaymentVerticle.ADDRESS, JsonObject.mapFrom(request), MAKE_PAYMENT, (AsyncResult<Message<JsonObject>> ar) -> {
			if (ar.succeeded()) {
				PaymentResponse result = Mapper.map(PaymentResponse.class, ar.result().body());
				result.id = dto.id;
				blocking(dsl -> {
					return PaymentBean.build(dsl).pid(dto.id, result.payment);
				}, dr -> {
					if (dr.succeeded()) {
						handler.handle(Future.succeededFuture(result));
					} else {
						handler.handle(Future.failedFuture(dr.cause()));
					}
				});
			} else {
				handler.handle(Future.failedFuture(ApiException.unexpected(ar.cause().getMessage())));
			}
		});
	}

	public void create(Long userId, PaymentDto payment, Handler<AsyncResult<PaymentDto>> handler) {
		validatePayment(payment);
		blocking(jooq -> {
			return PaymentBean.build(jooq).create(userId, payment);
		}, pr -> {
			if (pr.succeeded()) {
				PaymentDto dto = pr.result();
				handler.handle(Future.succeededFuture(dto));
			} else {
				handler.handle(Future.failedFuture(pr.cause()));
			}
		});
	}

	public void succeeded(Long id, Handler<AsyncResult<Void>> handler) {
		blocking(jooq -> {
			return PaymentBean.build(jooq).getFullInfo(id);
		}, pr -> {
			if (pr.succeeded()) {
				PaymentDto dto = pr.result();

				if (!PaymentStatus.NEW.equals(dto.status)) {
					JsonObject error = JsonUtils.getDictionary("invalid.payment.status", "", "Статус платежа невалиден", "", "");
					handler.handle(Future.failedFuture(error.encode()));
				} else {
					blocking(dsl -> {
						return PaymentBean.build(dsl).succeeded(id);
					}, dr -> {
						if (dr.succeeded()) {
							handler.handle(Future.succeededFuture());
						} else {
							handler.handle(Future.failedFuture(dr.cause()));
						}
					});
				}
			} else {
				handler.handle(Future.failedFuture(pr.cause()));
			}
		});
	}

	public void trigger(String identificator, Handler<AsyncResult<Void>> handler) {
		blocking(dsl -> {
			return PaymentBean.build(dsl).getFullInfoByIdentifier(identificator);
		}, dr -> {
			if (dr.succeeded()) {
				PaymentDto dto = dr.result();
				status(dto.pid, res -> {
					if (res.succeeded()) {
						PaymentStatusResponse response = res.result();
						blocking(dsl -> {
							return PaymentBean.build(dsl).status(dto.id, response.status);
						}, sr -> {
							if (dr.succeeded()) {
								handler.handle(Future.succeededFuture());
							} else {
								handler.handle(Future.failedFuture(sr.cause()));
							}
						});
					} else {
						handler.handle(Future.failedFuture(res.cause()));
					}
				});
			} else {
				handler.handle(Future.failedFuture(dr.cause()));
			}
		});
	}

	public void status(Long id, Handler<AsyncResult<PaymentInfoDto>> handler) {
		blocking(jooq -> {
			return PaymentBean.build(jooq).getFullInfo(id);
		}, pr -> {
			if (pr.succeeded()) {
				PaymentDto dto = pr.result();
				status(dto.pid, res -> {
					if (res.succeeded()) {
						PaymentStatusResponse response = res.result();
						blocking(dsl -> {
							return PaymentBean.build(dsl).status(id, response.status);
						}, dr -> {
							if (dr.succeeded()) {
								PaymentDto paymentDto = dr.result();
								handler.handle(Future.succeededFuture(PaymentInfoDto.build(paymentDto)));
							} else {
								handler.handle(Future.failedFuture(dr.cause()));
							}
						});
					} else {
						handler.handle(Future.failedFuture(res.cause()));
					}
				});
			} else {
				handler.handle(Future.failedFuture(pr.cause()));
			}
		});
	}

	public void status(String pid, Handler<AsyncResult<PaymentStatusResponse>> handler) {
		PaymentStatusRequest request = PaymentStatusRequest.build(pid);
		vertx.eventBus().send(PaymentVerticle.ADDRESS, JsonObject.mapFrom(request), STATUS_PAYMENT, (AsyncResult<Message<JsonObject>> ar) -> {
			if (ar.succeeded()) {
				PaymentStatusResponse result = Mapper.map(PaymentStatusResponse.class, ar.result().body());
				handler.handle(Future.succeededFuture(result));
			} else {
				handler.handle(Future.failedFuture(ApiException.unexpected(ar.cause().getMessage())));
			}
		});
	}

	private void validatePayment(PaymentDto payment) {
		if (payment.amount == null) {
			JsonObject error = JsonUtils.getDictionary("empty.payment.amount", "", "Сумма не указана", "", "");
			throw ApiException.data(error.toString());
		}
	}

}
