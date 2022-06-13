package kz.tastamat.order.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.core.dto.OpenRequest;
import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.order.dto.ActionDto;
import kz.tastamat.order.dto.OrderInfoDto;
import kz.tastamat.order.handler.OrderHandler;
import kz.zx.api.app.BaseRoute;
import kz.zx.utils.PaginatedList;

import java.util.List;
import java.util.stream.Collectors;

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

		router.get("/").handler(ctx -> {
			MultiMap params = transformQueryParams(ctx.request().params());
			params.add("user", ctx.get("user_id").toString());
			handler.getOrders(params, ar -> {
				if (ar.succeeded()) {
					PaginatedList<OrderDto> list = ar.result();

					List<JsonObject> jsonList = list.stream().map(JsonObject::mapFrom).collect(Collectors.toList());

					JsonObject res = new JsonObject();
					res.put("count", list.getCount());
					res.put("list", jsonList);
					ok(res, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});


		router.get("/tracking/:identifier").handler(ctx -> {
			String identifier = ctx.pathParam("identifier");
			handler.getTracking(identifier).setHandler(ar -> {
				if (ar.succeeded()) {
					ok(ar.result(), ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

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

		router.post("/book-drop").handler(ctx -> {
			OrderDto orderDto = ctx.getBodyAsJson().mapTo(OrderDto.class);

			orderDto.creatorId = ctx.get("user_id");
			handler.create(orderDto, ar -> okEmpty(ar, ctx));
		});

		router.put("/withdraw").handler(ctx -> {
			ActionDto actionDto = ctx.getBodyAsJson().mapTo(ActionDto.class);
			actionDto.userId = ctx.get("user_id");
			handler.withdraw(actionDto, ar -> okEmpty(ar, ctx));
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
