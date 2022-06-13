package kz.tastamat.payment.route;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import kz.tastamat.enums.ConfigKey;
import kz.tastamat.payment.handler.PaymentHandler;
import kz.zx.api.app.BaseRoute;
import kz.zx.exceptions.ApiException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OpenPaymentRoute extends BaseRoute {

	private Logger log = LoggerFactory.getLogger(OpenPaymentRoute.class);

	final PaymentHandler handler;

	private OpenPaymentRoute(Vertx vertx) {
		super(vertx);
		handler = new PaymentHandler(vertx, config);
	}

	public static OpenPaymentRoute build(Vertx vertx) {
		return new OpenPaymentRoute(vertx);
	}

	@Override
	public Router route() {
		Router router = Router.router(vertx);

		router.get("/vip").handler(ctx -> {
			MultiMap params = transformQueryParams(ctx.request().params());
			handler.vip(params, ar -> {
				if (ar.succeeded()) {
					ctx.response().setStatusCode(200).end(ar.result().encode());
				} else {
					ctx.fail(ar.cause());
				}
			});
		});

		router.get("/kaspi").handler(ctx -> {
			MultiMap params = transformQueryParams(ctx.request().params());

//			final String authorization = ctx.request().getHeader("Authorization");
//			if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
//				// Authorization: Basic base64credentials
//				String base64Credentials = authorization.substring("Basic".length()).trim();
//				byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
//				String credentials = new String(credDecoded, StandardCharsets.UTF_8);
//				// credentials = username:password
//				final String[] values = credentials.split(":", 2);
//
//				log.info(values[0]+" | "+values[1]);
//
//				JsonObject kaspiConfig = config.getJsonObject("kaspi");
//
//				if(!kaspiConfig.getString("login").equals(values[0]) || !kaspiConfig.getString("password").equals(values[1])){
//					ctx.fail(ApiException.unauthorized("not.authorized"));
//				} else {
					handler.kaspi(params, ar -> {
						if (ar.succeeded()) {
							ctx.response().setStatusCode(200).end(ar.result());
						} else {
							ctx.fail(ar.cause());
						}
					});
//				}
//			} else {
//				ctx.fail(ApiException.unauthorized("not.authorized"));
//			}
		});

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

		router.post("/epayinfo/kkb/success").handler(this::handlePaymentResult);
		router.post("/epayinfo/kkb/error").handler(this::handlePaymentErrorResult);

		return router;
	}

	private void handlePaymentResult(RoutingContext ctx) {
		String bodyAsString = ctx.getBodyAsString();

		JsonObject epayConfigs = config.getJsonObject(ConfigKey.EPAY.key());

		String xml;
		try {
			xml = URLDecoder.decode(bodyAsString.replace("response=", ""), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw ApiException.business("epay.fail");
		}

		log.info("payment response xml: {}", xml);

		handler.epaySuccess(xml, epayConfigs, ar -> {
			if (ar.succeeded()) {
				ctx.response().end("0");
			} else {
				ctx.response().end("0");
			}
		});
	}

	private void handlePaymentErrorResult(RoutingContext ctx) {
		String bodyAsString = ctx.getBodyAsString();
		log.warn("payment error xml: {}", bodyAsString);
		ctx.response().end("0");
	}
}
