package kz.tastamat.order.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.order.dto.StatusRequest;
import kz.tastamat.order.dto.StatusResponse;
import kz.tastamat.order.handler.OrderHandler;
import kz.zx.api.app.BaseRoute;

public class OpenOrderRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(OpenOrderRoute.class);

	final OrderHandler handler;

	private OpenOrderRoute(Vertx vertx) {
		super(vertx);
		handler = new OrderHandler(vertx);
	}

	public static OpenOrderRoute build(Vertx vertx) {
		return new OpenOrderRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.put("/:code/pick").handler(ctx -> {
			String code = ctx.pathParam("code");
			handler.pick(code, ar -> okEmpty(ar, ctx));
		});

		router.put("/pick").handler(ctx -> {
			StatusRequest request = ctx.getBodyAsJson().mapTo(StatusRequest.class);
			handler.pick(request.identificator, ar -> okEmpty(ar, ctx));
		});

		router.put("/status").handler(ctx -> {
			StatusRequest request = ctx.getBodyAsJson().mapTo(StatusRequest.class);
			handler.status(request, ar -> okStatusResponse(ar, ctx));
		});

		return router;
	}

	private void okStatusResponse(AsyncResult<StatusResponse> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			StatusResponse response = ar.result();
			ok(JsonObject.mapFrom(response), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}
}
