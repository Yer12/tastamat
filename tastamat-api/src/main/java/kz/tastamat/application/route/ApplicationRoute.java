package kz.tastamat.application.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.application.dto.ApplicationCreateRequest;
import kz.tastamat.application.handler.ApplicationHandler;
import kz.tastamat.db.model.dto.OrderDto;
import kz.tastamat.order.dto.ActionDto;
import kz.tastamat.order.dto.OrderInfoDto;
import kz.tastamat.order.handler.OrderHandler;
import kz.zx.api.app.BaseRoute;
import kz.zx.utils.PaginatedList;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(ApplicationRoute.class);

	final ApplicationHandler handler;

	private ApplicationRoute(Vertx vertx) {
		super(vertx);
		handler = new ApplicationHandler(vertx);
	}

	public static ApplicationRoute build(Vertx vertx) {
		return new ApplicationRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.post("/").handler(ctx -> {
			ApplicationCreateRequest applicationCreateRequest = ctx.getBodyAsJson().mapTo(ApplicationCreateRequest.class);
			handler.createApplication(applicationCreateRequest).setHandler(ar -> {
				if (ar.succeeded()) {
					ok(ar.result(), ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.get("/price-check/:identifier").handler(ctx -> {
			String identifier = ctx.pathParam("identifier");
			handler.checkPrice(identifier).setHandler(ar -> {
				if (ar.succeeded()) {
					ok(ar.result(), ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.get("/list").handler(ctx -> {
			MultiMap params = transformQueryParams(ctx.request().params());
			handler.getList(params).setHandler(ar -> {
				if (ar.succeeded()) {
					ok(ar.result(), ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.get("/by-id/:identifier").handler(ctx -> {
			String identifier = ctx.pathParam("identifier");
			handler.getById(identifier).setHandler(ar -> {
				if (ar.succeeded()) {
					ok(ar.result(), ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
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
