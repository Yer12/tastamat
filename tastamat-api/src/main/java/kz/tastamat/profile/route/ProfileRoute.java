package kz.tastamat.profile.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.db.model.dto.ProfileDto;
import kz.tastamat.profile.dto.ProfileInfoDto;
import kz.tastamat.profile.handler.ProfileHandler;
import kz.zx.api.app.BaseRoute;

public class ProfileRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(ProfileRoute.class);

	final ProfileHandler handler;

	private ProfileRoute(Vertx vertx) {
		super(vertx);
		handler = new ProfileHandler(vertx);
	}

	public static ProfileRoute build(Vertx vertx) {
		return new ProfileRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.get("/:id").handler(ctx -> {
			Long id = Long.parseLong(ctx.pathParam("id"));
			handler.getProfile(id, ar -> {
				if(ar.succeeded()){
					okProfile(ar, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.get("/users/:id").handler(ctx -> {
			Long id = Long.parseLong(ctx.pathParam("id"));
			handler.getUserProfile(id, ar -> {
				if(ar.succeeded()){
					okProfileInfo(ar, ctx);
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.put("/template").handler(ctx -> {
			ProfileInfoDto profileDto = ctx.getBodyAsJson().mapTo(ProfileInfoDto.class);

			profileDto.user = ctx.get("user_id");
			handler.template(profileDto, ar -> okProfile(ar, ctx));
		});

		return router;
	}

	private void okProfile(AsyncResult<ProfileDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			ProfileDto profile = ar.result();
			ok(JsonObject.mapFrom(ProfileInfoDto.build(profile)), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}

	private void okProfileInfo(AsyncResult<ProfileInfoDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			ProfileInfoDto profile = ar.result();
			ok(JsonObject.mapFrom(profile), ctx);
		} else {
			ctx.fail(ar.cause());
		}
	}
}
