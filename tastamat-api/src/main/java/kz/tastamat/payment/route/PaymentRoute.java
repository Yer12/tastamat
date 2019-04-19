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
import kz.tastamat.payment.dto.PaymentInfoDto;
import kz.tastamat.payment.dto.PaymentResponse;
import kz.tastamat.payment.handler.PaymentHandler;
import kz.zx.api.app.BaseRoute;
import kz.zx.utils.PaginatedList;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(PaymentRoute.class);

	final PaymentHandler handler;

	private PaymentRoute(Vertx vertx) {
		super(vertx);
		handler = new PaymentHandler(vertx);
	}

	public static PaymentRoute build(Vertx vertx) {
		return new PaymentRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

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
			handler.create(payment, ar -> okPaymentResponse(ar, ctx));
		});

		router.put("/:id/status").handler(ctx -> {
			Long id = Long.parseLong(ctx.pathParam("id"));
			handler.status(id, ar -> {
				if(ar.succeeded()){
					okPaymentInfo(ar, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		return router;
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
