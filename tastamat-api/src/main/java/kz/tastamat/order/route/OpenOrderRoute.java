package kz.tastamat.order.route;

import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
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

		return router;
	}
}
