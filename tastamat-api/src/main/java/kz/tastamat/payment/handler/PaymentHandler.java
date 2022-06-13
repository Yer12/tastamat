package kz.tastamat.payment.handler;

import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import kz.tastamat.dao.impl.PaymentDaoImpl;
import kz.tastamat.dao.impl.ProfileDaoImpl;
import kz.tastamat.dao.impl.UserDaoImpl;
import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.db.model.enums.PaymentStatus;
import kz.tastamat.db.model.params.SearchParams;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.payment.bean.PaymentBean;
import kz.tastamat.payment.dto.*;
import kz.tastamat.payment.enums.KaspiCommand;
import kz.tastamat.payment.utils.KKB;
import kz.tastamat.payment.verticle.PaymentVerticle;
import kz.tastamat.user.bean.UserBean;
import kz.tastamat.utils.EncryptUtils;
import kz.tastamat.utils.JsonUtils;
import kz.tastamat.utils.QueryParamsUtils;
import kz.tastamat.utils.XmlUtils;
import kz.toolpar.message.client.MessageClient;
import kz.zx.api.app.DbHandler;
import kz.zx.api.enums.Key;
import kz.zx.exceptions.ApiException;
import kz.zx.json.Mapper;
import kz.zx.utils.PaginatedList;
import kz.zx.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class PaymentHandler extends DbHandler {

	private Logger log = LoggerFactory.getLogger(PaymentHandler.class);

	private static final DeliveryOptions MAKE_PAYMENT = new DeliveryOptions().addHeader(Key.action, PaymentVerticle.Action.MAKE_PAYMENT.name());
	private static final DeliveryOptions STATUS_PAYMENT = new DeliveryOptions().addHeader(Key.action, PaymentVerticle.Action.STATUS_PAYMENT.name());

	private final MessageClient messageClient;

	protected String link, webhook;

	public PaymentHandler(Vertx vertx, JsonObject config) {
		super(vertx);
		this.messageClient = MessageClient.create(vertx);

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

	public void epayInitialize(Long userId, PaymentDto payment, Handler<AsyncResult<PaymentDto>> handler) {
		validatePayment(payment);
		blocking(jooq -> {
			return PaymentBean.build(jooq).epayInitialize(userId, payment);
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

	public void epaySucceeded(Long id, Handler<AsyncResult<Void>> handler) {
		blocking(jooq -> {
			return PaymentBean.build(jooq).getFullInfo(id);
		}, pr -> {
			if (pr.succeeded()) {
				PaymentDto dto = pr.result();

				if (PaymentStatus.NEW.equals(dto.status)) {
					blocking(dsl -> {
						return PaymentBean.build(dsl).succeeded(id);
					}, dr -> {
						if (dr.succeeded()) {
							handler.handle(Future.succeededFuture());
						} else {
							handler.handle(Future.failedFuture(dr.cause()));
						}
					});
				} else {
					handler.handle(Future.succeededFuture());
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

	public void epaySuccess(String xml, JsonObject epayConfigs, Handler<AsyncResult<PaymentInfoDto>> handler) {
		blocking(jooq -> {
			return PaymentBean.build(jooq).epaySuccess(xml, epayConfigs);
		}, pr -> {
			if (pr.succeeded()) {
				handler.handle(Future.succeededFuture());
			} else {
				handler.handle(Future.failedFuture(pr.cause()));
			}
		});
	}

	public void epayStatus(Long id, JsonObject epayConfig, Handler<AsyncResult<PaymentInfoDto>> handler) {
		String paymentId = EncryptUtils.obfuscate(id, 211192, 6);
		String document = KKB.build64(epayConfig.getString("path") + "/kkbsign.cfg", paymentId);

		try {
			String encoded = URLEncoder.encode(document, "UTF-8");
			String url = epayConfig.getString("statusUrl")+"?"+encoded;
			log.info("epay status url {}", url);
			this.messageClient.sendHttpGet(new kz.toolpar.message.client.dto.MessageInfoDto()
					.setUrl(url)
			).setHandler(ir -> {
				if (ir.succeeded()) {
					blocking(jooq -> {
						return PaymentBean.build(jooq).epayStatus(ir.result().getString("result"), epayConfig);
					}, pr -> {
						if (pr.succeeded()) {
							handler.handle(Future.succeededFuture(PaymentInfoDto.build(pr.result())));
						} else {
							handler.handle(Future.failedFuture(pr.cause()));
						}
					});

				} else {
					handler.handle(Future.failedFuture(ir.cause()));
				}
			});
		} catch (UnsupportedEncodingException e) {
			handler.handle(Future.failedFuture(e));
		}
	}

	private void validatePayment(PaymentDto payment) {
		if (payment.amount == null) {
			JsonObject error = JsonUtils.getDictionary("empty.payment.amount", "", "Сумма не указана", "", "");
			throw ApiException.data(error.toString());
		}
	}

	public void confirm(JsonObject epayConfig) {
		blocking(dsl -> {
			return new PaymentDaoImpl(dsl).findByStatus(PaymentStatus.APPROVED);
		}, br -> {
			if (br.succeeded()) {
				List<PaymentDto> payments = br.result();
				log.info("payments size {}", payments.size());
				for (PaymentDto payment : payments) {
					String paymentId = EncryptUtils.obfuscate(payment.id, 211192, 6);
					String document = KKB.build64(epayConfig.getString("path") + "/kkbsign.cfg", paymentId, payment.ref, payment.code, payment.amount.toString());

					try {
						String encoded = URLEncoder.encode(document, "UTF-8");
						String url = epayConfig.getString("confirmUrl")+"?"+encoded;
						log.info("epay confirm payment {}, url {}", payment.id, url);
						this.messageClient.sendHttpGet(new kz.toolpar.message.client.dto.MessageInfoDto()
								.setUrl(url)
						).setHandler(ir -> {
							if (ir.succeeded()) {
								blocking(jooq -> {
									return PaymentBean.build(jooq).epayConfirm(ir.result().getString("result"), epayConfig);
								}, pr -> {
									if (pr.failed()) {
										log.error("error payment confirm {}", payment.id);
									}
								});

							} else {
								log.error("error payment confirm {}", payment.id);
							}
						});
					} catch (UnsupportedEncodingException e) {
						log.error("error payment confirm {}", payment.id);
					}
				}
			}
		});
	}

	public void kaspi(MultiMap params, Handler<AsyncResult<String>> handler) {
		SearchParams searchParams = QueryParamsUtils.build(params);

		log.info("kaspi params {}", JsonObject.mapFrom(searchParams));

		if(StringUtils.isEmpty(searchParams.account)){
			PaymentKaspiResponse response = new PaymentKaspiResponse();
			response.txn_id = searchParams.txn_id;
			response.result = 4L;
			XmlUtils.toXml(vertx, response, res -> {
				if (res.succeeded()) {
					handler.handle(Future.succeededFuture(res.result()));
				} else {
					handler.handle(Future.failedFuture(res.cause()));
				}
			});
		} else {
			blocking(dsl -> {
				UserDto userDto = new UserDaoImpl(dsl).findByPhone(searchParams.account).orElseThrow(() -> ApiException.notFound("user.not.found"));
				return new ProfileDaoImpl(dsl).findByUser(userDto.id).orElseThrow(() -> ApiException.notFound("profile.not.found"));
			}, dr -> {
				if (dr.succeeded()) {
					ProfileDto profileDto = dr.result();

					if (KaspiCommand.CHECK.key().equals(searchParams.command)) {
						PaymentKaspiResponse response = new PaymentKaspiResponse();
						response.txn_id = searchParams.txn_id;
						response.result = 0L;
						XmlUtils.toXml(vertx, response, res -> {
							if (res.succeeded()) {
								handler.handle(Future.succeededFuture(res.result()));
							} else {
								handler.handle(Future.failedFuture(res.cause()));
							}
						});
					} else if (KaspiCommand.PAY.key().equals(searchParams.command)) {
						blocking(jooq -> {
							return PaymentBean.build(jooq).kaspi(profileDto.id, searchParams);
						}, pr -> {
							if (pr.succeeded()) {
								PaymentDto payment = pr.result();

								PaymentKaspiResponse response = new PaymentKaspiResponse();
								response.txn_id = searchParams.txn_id;
								response.prv_txn = payment.id ^ 211192;
								response.result = 0L;
								response.sum = payment.amount.doubleValue();
								response.comment = "OK";
								XmlUtils.toXml(vertx, response, res -> {
									if (res.succeeded()) {
										handler.handle(Future.succeededFuture(res.result()));
									} else {
										handler.handle(Future.failedFuture(res.cause()));
									}
								});
							} else {
								handler.handle(Future.failedFuture(pr.cause()));
							}
						});
					} else {
						handler.handle(Future.failedFuture(ApiException.business("unknown.command")));
					}
				} else {
					PaymentKaspiResponse response = new PaymentKaspiResponse();
					response.txn_id = searchParams.txn_id;
					response.result = 5L;
					XmlUtils.toXml(vertx, response, res -> {
						if (res.succeeded()) {
							handler.handle(Future.succeededFuture(res.result()));
						} else {
							handler.handle(Future.failedFuture(res.cause()));
						}
					});
				}
			});
		}
	}

	public void vip(MultiMap params, Handler<AsyncResult<JsonObject>> handler) {
		SearchParams searchParams = QueryParamsUtils.vip(params);

		if (!"WyeEDGXae9fGPr7R".equals(searchParams.password)) {
			handler.handle(Future.failedFuture(ApiException.business("operation.forbidden")));
		} else {
			blocking(jooq -> {
				return PaymentBean.build(jooq).vip(searchParams.phone, searchParams.sum);
			}, pr -> {
				if (pr.succeeded()) {
					PaymentDto payment = pr.result();
                    handler.handle(Future.succeededFuture(JsonObject.mapFrom(payment)));
				} else {
					handler.handle(Future.failedFuture(pr.cause()));
				}
			});
		}
	}
}
