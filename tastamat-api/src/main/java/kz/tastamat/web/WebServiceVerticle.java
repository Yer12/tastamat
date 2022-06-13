package kz.tastamat.web;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import kz.tastamat.app.handler.SecurityHandler;
import kz.tastamat.application.route.ApplicationRoute;
import kz.tastamat.auth.route.AuthRoute;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.locker.route.LockerRoute;
import kz.tastamat.order.route.OpenOrderRoute;
import kz.tastamat.order.route.OrderRoute;
import kz.tastamat.payment.route.OpenPaymentRoute;
import kz.tastamat.payment.route.PaymentRoute;
import kz.tastamat.profile.route.ProfileRoute;
import kz.tastamat.user.route.UserRoute;
import kz.zx.api.app.AbstractWebServiceVerticle;

public class WebServiceVerticle extends AbstractWebServiceVerticle {

	private Logger log = LoggerFactory.getLogger(WebServiceVerticle.class);

	@Override
	public void route(Router router) {
		cors(router);

		final SecurityHandler securityHandler = new SecurityHandler(vertx);

		JsonObject auth = config().getJsonObject(ConfigKey.AUTH.key());
		String authType = auth.getString(ConfigKey.TYPE.key());
		String authPath = auth.getString(ConfigKey.PATH.key());
		String authPassword = auth.getString(ConfigKey.PASSWORD.key());
		JWTAuthOptions authConfig = new JWTAuthOptions()
				.setKeyStore(new KeyStoreOptions()
						.setType(authType)
						.setPath(authPath)
						.setPassword(authPassword));

		JWTAuth jwtAuth = JWTAuth.create(vertx, authConfig);

		router.route("/insta/rest/a/*").handler(JWTAuthHandler.create(jwtAuth));

		router.route("/insta/rest/a/*").handler(ctx -> {
			JsonObject principal = ctx.user().principal();

			log.info(principal.toString());

			ctx.put("user_id", principal.getLong("sub"));
			ctx.next();
		});

	}

	@Override
	public void restRouter(Router router) {
		router.mountSubRouter("/auth", AuthRoute.build(vertx).route());

		router.mountSubRouter("/a/users", UserRoute.build(vertx).route());

		router.mountSubRouter("/a/profiles", ProfileRoute.build(vertx).route());

		router.mountSubRouter("/a/lockers", LockerRoute.build(vertx).route());

		router.mountSubRouter("/a/orders", OrderRoute.build(vertx).route());

		router.mountSubRouter("/a/applications", ApplicationRoute.build(vertx).route());

		router.mountSubRouter("/orders", OpenOrderRoute.build(vertx).route());

		router.mountSubRouter("/a/payments", PaymentRoute.build(vertx).route());

		router.mountSubRouter("/payments", OpenPaymentRoute.build(vertx).route());

	}

	public void cors(Router router){
		router.route().handler(CorsHandler.create(".+")
				.maxAgeSeconds(600)
				.allowedMethod(HttpMethod.GET)
				.allowedMethod(HttpMethod.POST)
				.allowedMethod(HttpMethod.PUT)
				.allowedMethod(HttpMethod.DELETE)
				.allowedMethod(HttpMethod.OPTIONS)
				.allowedHeader("Content-Type")
				.allowedHeader("Accept")
				.allowedHeader("Accept-Language")
				.allowedHeader("Authorization"));
	}
}
