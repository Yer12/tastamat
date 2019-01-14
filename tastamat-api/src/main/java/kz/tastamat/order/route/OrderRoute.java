package kz.tastamat.order.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.core.dto.OpenRequest;
import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.order.dto.OrderInfoDto;
import kz.tastamat.order.handler.OrderHandler;
import kz.zx.api.app.BaseRoute;

public class OrderRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(OrderRoute.class);

	final OrderHandler handler;

	private OrderRoute(Vertx vertx) {
		super(vertx);
		handler = new OrderHandler(vertx);
	}

	public static OrderRoute build(Vertx vertx) {
		return new OrderRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.get("/:identificator").handler(ctx -> {
			String identificator = ctx.pathParam("identificator");
			handler.getOrder(identificator, ar -> {
				if(ar.succeeded()){
					okOrderInfo(ar, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.post("/").handler(ctx -> {
			OrderDto orderDto = ctx.getBodyAsJson().mapTo(OrderDto.class);

			orderDto.creatorId = ctx.get("user_id");
			handler.create(orderDto, ar -> okEmpty(ar, ctx));
		});

		router.put("/open").handler(ctx -> {
			OpenRequest openRequest = ctx.getBodyAsJson().mapTo(OpenRequest.class);
			handler.open(openRequest, ar -> okEmpty(ar, ctx));
		});

		return router;
	}

	private void okOrderInfo(AsyncResult<OrderInfoDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			OrderInfoDto dto = ar.result();
			ok(JsonObject.mapFrom(dto), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}
}
