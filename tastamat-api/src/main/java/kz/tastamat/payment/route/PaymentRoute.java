package kz.tastamat.payment.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.db.model.dto.PaymentDto;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.payment.dto.PaymentInfoDto;
import kz.tastamat.payment.dto.PaymentResponse;
import kz.tastamat.payment.handler.PaymentHandler;
import kz.tastamat.payment.utils.KKB;
import kz.tastamat.utils.EncryptUtils;
import kz.zx.api.app.BaseRoute;
import kz.zx.utils.PaginatedList;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(PaymentRoute.class);

	final PaymentHandler handler;

	private PaymentRoute(Vertx vertx) {
		super(vertx);
		handler = new PaymentHandler(vertx, config);
	}

	public static PaymentRoute build(Vertx vertx) {
		return new PaymentRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.get("/:id/epayinfo/status").handler(this::handleGetEpayStatus);

		router.get("/epayinfo").handler(this::handleGetEpayInfo);

		router.put("/:id/epaysucceeded").handler(ctx -> {
			Long id = Long.parseLong(ctx.pathParam("id"));
			handler.epaySucceeded(id, ar -> {
				if (ar.succeeded()) {
					okEmpty(ar, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.get("/").handler(ctx -> {
			MultiMap params = transformQueryParams(ctx.request().params());
			params.add("user", ctx.get("user_id").toString());
			handler.getPayments(params, ar -> {
				if (ar.succeeded()) {
					PaginatedList<PaymentDto> list = ar.result();

					List<JsonObject> jsonList = list.stream().map(p-> JsonObject.mapFrom(PaymentInfoDto.build(p))).collect(Collectors.toList());

					JsonObject res = new JsonObject();
					res.put("count", list.getCount());
					res.put("list", jsonList);
					ok(res, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.post("/").handler(ctx -> {
			PaymentDto payment = ctx.getBodyAsJson().mapTo(PaymentDto.class);

			payment.userId = ctx.get("user_id");
			handler.request(payment.userId, payment, ar -> okPaymentResponse(ar, ctx));
		});

		router.put("/:id/succeeded").handler(ctx -> {
			Long id = Long.parseLong(ctx.pathParam("id"));
			handler.succeeded(id, ar -> {
				if (ar.succeeded()) {
					okEmpty(ar, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.put("/:id/status").handler(ctx -> {
			Long id = Long.parseLong(ctx.pathParam("id"));
			handler.status(id, ar -> {
				if (ar.succeeded()) {
					okPaymentInfo(ar, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		return router;
	}

	private void handleGetEpayInfo(RoutingContext ctx) {

		PaymentDto paymentDto = new PaymentDto();
		paymentDto.amount = Long.valueOf(queryParam("amount", ctx));
		paymentDto.userId = ctx.get("user_id");

		handler.epayInitialize(paymentDto.userId, paymentDto, ar -> {
			if (ar.succeeded()) {
				PaymentDto paymentResult = ar.result();
				JsonObject epayConfigs = config.getJsonObject(ConfigKey.EPAY.key());
				String paymentId = EncryptUtils.obfuscate(paymentResult.id, 211192, 6);
				String base64encoded = KKB.build64(epayConfigs.getString("path") + "/kkbsign.cfg", paymentResult.amount.toString(), paymentId);
				JsonObject responsePayload = new JsonObject()
						.put("paymentId", paymentResult.id)
						.put("postLink", epayConfigs.getString("postLink"))
						.put("postFailureLink", epayConfigs.getString("postFailureLink"))
						.put("postFailureBackLink", epayConfigs.getString("postFailureBackLink"))
						.put("postBackLink", epayConfigs.getString("postBackLink"))
						.put("orderInfo", base64encoded);

				ok(responsePayload, ctx);
			} else {
				ctx.fail(ar.cause());
			}
		});
	}

	private void handleGetEpayStatus(RoutingContext ctx) {

		JsonObject epayConfigs = config.getJsonObject(ConfigKey.EPAY.key());

		Long id = Long.parseLong(ctx.pathParam("id"));
		handler.epayStatus(id,epayConfigs, ar -> {
			if (ar.succeeded()) {
				okPaymentInfo(ar, ctx);
			} else {
				ctx.fail(ar.cause());
			}
		});
	}

	private void okPaymentResponse(AsyncResult<PaymentResponse> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			PaymentResponse dto = ar.result();
			ok(JsonObject.mapFrom(dto), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}

	private void okPaymentInfo(AsyncResult<PaymentInfoDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			PaymentInfoDto dto = ar.result();
			ok(JsonObject.mapFrom(dto), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}
}
