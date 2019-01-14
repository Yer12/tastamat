package kz.tastamat.locker.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.locker.dto.LockerInfoDto;
import kz.tastamat.locker.handler.LockerHandler;
import kz.zx.api.app.BaseRoute;
import kz.zx.utils.PaginatedList;

import java.util.List;
import java.util.stream.Collectors;

public class LockerRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(LockerRoute.class);

	final LockerHandler handler;

	private LockerRoute(Vertx vertx) {
		super(vertx);
		handler = new LockerHandler(vertx);
	}

	public static LockerRoute build(Vertx vertx) {
		return new LockerRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.get("/").handler(ctx -> {
			MultiMap params = transformQueryParams(ctx.request().params());
			handler.handleGetLockers(params, ar -> {
				if (ar.succeeded()) {
					PaginatedList<LockerInfoDto> list = ar.result();

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

		router.get("/:id").handler(ctx -> {
			Long id = Long.parseLong(ctx.pathParam("id"));
			handler.handleGetLocker(id, ar -> {
				if (ar.succeeded()) {
					okLockerInfo(ar, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		return router;
	}

	private void okLockerInfo(AsyncResult<LockerInfoDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			LockerInfoDto dto = ar.result();
			ok(JsonObject.mapFrom(dto), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}
}
