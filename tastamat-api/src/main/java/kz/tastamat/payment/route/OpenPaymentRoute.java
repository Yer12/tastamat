package kz.tastamat.payment.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.core.dto.OpenRequest;
import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.payment.dto.PaymentInfoDto;
import kz.tastamat.payment.handler.PaymentHandler;
import kz.zx.api.app.BaseRoute;

public class OpenPaymentRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(OpenPaymentRoute.class);

	final PaymentHandler handler;

	private OpenPaymentRoute(Vertx vertx) {
		super(vertx);
		handler = new PaymentHandler(vertx);
	}

	public static OpenPaymentRoute build(Vertx vertx) {
		return new OpenPaymentRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.get("/:identificator").handler(ctx -> {
			String identificator = ctx.pathParam("identificator");
			handler.trigger(identificator, ar -> {
				if (ar.succeeded()) {
					okEmpty(ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		return router;
	}
}
