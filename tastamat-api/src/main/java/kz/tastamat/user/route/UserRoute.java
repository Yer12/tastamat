package kz.tastamat.user.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.user.dto.UserInfoDto;
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
			handler.getFullInfo(ctx.get("user_id"), ar -> okUserInfo(ar, ctx));
		});

		return router;
	}

	private void okUserInfo(AsyncResult<UserInfoDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			UserInfoDto user = ar.result();
			ok(JsonObject.mapFrom(user), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}

	private void okUser(AsyncResult<UserDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			UserDto user = ar.result();
			ok(JsonObject.mapFrom(user), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}

	private void okProfile(AsyncResult<ProfileDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			ProfileDto profile = ar.result();
			ok(JsonObject.mapFrom(profile), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}
}
