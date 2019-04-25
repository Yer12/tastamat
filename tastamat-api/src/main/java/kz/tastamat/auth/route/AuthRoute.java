package kz.tastamat.auth.route;

import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.auth.dto.ConfirmDto;
import kz.tastamat.db.model.dto.UserDto;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.auth.dto.AuthDto;
import kz.tastamat.auth.dto.LoginDto;
import kz.tastamat.user.dto.UserInfoDto;
import kz.tastamat.user.handler.UserHandler;
import kz.tastamat.utils.JsonUtils;
import kz.zx.api.app.BaseRoute;
import kz.zx.exceptions.ApiException;

public class AuthRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(AuthRoute.class);

	final UserHandler handler;

	final JWTAuth jwtAuth;

	private AuthRoute(Vertx vertx) {
		super(vertx);
		handler = new UserHandler(vertx);

//		JWTAuthOptions authConfig = new JWTAuthOptions()
//				.setKeyStore(new KeyStoreOptions()
//						.setType("jceks")
//						.setPath("conf/keystore.jceks")
//						.setPassword("secret"));

		JsonObject auth = vertx.getOrCreateContext().config().getJsonObject(ConfigKey.AUTH.key());
		String authType = auth.getString(ConfigKey.TYPE.key());
		String authPath = auth.getString(ConfigKey.PATH.key());
		String authPassword = auth.getString(ConfigKey.PASSWORD.key());
		JWTAuthOptions authConfig = new JWTAuthOptions()
				.setKeyStore(new KeyStoreOptions()
						.setType(authType)
						.setPath(authPath)
						.setPassword(authPassword));


		jwtAuth = JWTAuth.create(vertx, authConfig);
	}

	public static AuthRoute build(Vertx vertx) {
		return new AuthRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.get("/phones/:phone/exists").handler(ctx -> {
			String phone = ctx.pathParam("phone");
			handler.handleExists(phone, ar -> {
				if (ar.succeeded()) {
					JsonObject object = new JsonObject();
					object.put("exists", ar.result());
					ok(object, ctx);
				} else {
					error(ar, ctx);
				}
			});
		});

		router.post("/initialize").handler(ctx -> {
			UserInfoDto userInfoDto = ctx.getBodyAsJson().mapTo(UserInfoDto.class);
			handler.initialize(userInfoDto, ar -> {
				if (ar.succeeded()) {
					okUserInfo(ar, ctx);
				} else {
					error(ar, ctx);
				}
			});
		});

		router.put("/password").handler(ctx -> {
			ConfirmDto confirmDto = ctx.getBodyAsJson().mapTo(ConfirmDto.class);
			handler.password(confirmDto, ar -> {
				if (ar.succeeded()) {
					okAuth(ar, ctx);
				} else {
					errorAuth(ar, ctx);
				}
			});
		});

		router.put("/login").handler(ctx -> {
			LoginDto loginDto = ctx.getBodyAsJson().mapTo(LoginDto.class);
			handler.login(loginDto, ar -> {
				if (ar.succeeded()) {
					okAuth(ar, ctx);
				} else {
					errorAuth(ar, ctx);
				}
			});
		});

		router.put("/phones/:phone/sms").handler(ctx -> {
			String phone = ctx.pathParam("phone");
			handler.sms(phone, ar -> {
				if (ar.succeeded()) {
					okConfirm(ar, ctx);
				} else {
					error(ar, ctx);
				}
			});
		});

		router.put("/phones/confirm").handler(ctx -> {
			ConfirmDto confirmDto = ctx.getBodyAsJson().mapTo(ConfirmDto.class);
			handler.confirm(confirmDto, ar -> {
				if (ar.succeeded()) {
					okConfirm(ar, ctx);
				} else {
					error(ar, ctx);
				}
			});
		});

		return router;
	}

	private void okUserInfo(AsyncResult<UserInfoDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			UserInfoDto user = ar.result();
			ok(JsonObject.mapFrom(user), ctx);
		} else {
			error(ar, ctx);
		}
	}

	private void okConfirm(AsyncResult<UserInfoDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			UserInfoDto user = ar.result();
			ConfirmDto confirm = ConfirmDto.build(user);
			ok(JsonObject.mapFrom(confirm), ctx);
		} else {
			error(ar, ctx);
		}
	}

	private void okAuth(AsyncResult<UserInfoDto> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			UserInfoDto user = ar.result();
			AuthDto auth = new AuthDto();
			JWTOptions options = new JWTOptions();
			auth.user = user;
			auth.token = jwtAuth.generateToken(new JsonObject().put("sub", user.id), options);
			ok(JsonObject.mapFrom(auth), ctx);
		} else {
			errorAuth(ar, ctx);
		}
	}

	private void error(AsyncResult ar, RoutingContext ctx) {
		log.error("error {}", ar.cause());
		if(ar.cause() instanceof ApiException){
			ctx.fail(ar.cause());
		} else {
			JsonObject error = JsonUtils.getDictionary("auth.unexpected", "", "Непредвиденная ошибка", "", "");
			ctx.fail(ApiException.unauthorized(error.toString()));
		}
	}

	private void errorAuth(AsyncResult ar, RoutingContext ctx) {
		log.error("error {}", ar.cause());
		JsonObject error = JsonUtils.getDictionary("auth.unauthorized", "", "Ошибка авторизации", "", "");
		ctx.fail(ApiException.unauthorized(error.toString()));
	}
}
