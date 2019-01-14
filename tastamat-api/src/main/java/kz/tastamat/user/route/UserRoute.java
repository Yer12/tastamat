package kz.tastamat.user.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.user.handler.UserHandler;
import kz.zx.api.app.BaseRoute;

public class UserRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(UserRoute.class);

	final UserHandler handler;

	private UserRoute(Vertx vertx) {
		super(vertx);
		handler = new UserHandler(vertx);
	}

	public static UserRoute build(Vertx vertx) {
		return new UserRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.get("/current-user").handler(ctx -> {
			ctx.response().end("asdasd");
//			handler.handleGetCurrentUser(ctx.get("uid"), ar -> okUser(ar, ctx));
		});

		return router;
	}

	private void okUser(AsyncResult<UserDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			UserDto user = ar.result();
			ok(JsonObject.mapFrom(user), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}
}
