package kz.zx.api.app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.impl.NoStackTraceThrowable;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.ResponseTimeHandler;
import kz.zx.api.enums.ConfigKey;
import kz.zx.exceptions.ApiException;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.Logger;

import java.util.Arrays;
import java.util.List;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

/**
 * Created by deedarb on 7/21/17.
 */
@SuppressWarnings("Duplicates")
public abstract class AbstractWebServiceVerticle extends AbstractVerticle {

	public static final String APPLICATION_JSON = "application/json; charset=UTF-8";
	public static final String APPLICATION_JSON_NOCHARSET = "application/json";
	public static final String MULTIPART_FORMDATA = "multipart/form-data";
	private Logger log = LoggerFactory.getLogger(AbstractWebServiceVerticle.class);

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		init();

		HttpServerOptions httpServerOptions = new HttpServerOptions();
		JsonObject webConfig = config().getJsonObject(ConfigKey.WEB.key());
		httpServerOptions.setPort(webConfig.getInteger(ConfigKey.PORT.key()));
		httpServerOptions.setHost(webConfig.getString(ConfigKey.HOST.key()));

		vertx.createHttpServer(httpServerOptions)
			.requestHandler(router()::accept)
			.listen(ar -> {
				if (ar.succeeded()) {
					log.info("web services is started...");
					startFuture.complete();
				} else {
					startFuture.fail(ar.cause());
				}
			});
	}

	private Router router() {
		Router router = Router.router(vertx);

		AppHealth.route(router, vertx);

		router.route().failureHandler(e -> {
			if (e.failure() instanceof ApiException) {
				ApiException ex = (ApiException) e.failure();
				e.response().setStatusCode(580).setStatusMessage(ex.getErrorMessage()).end(ex.getMessage());
			} else if(e.failure() instanceof NoStackTraceThrowable){
				ApiException ex = ApiException.unexpected(e.failure().getMessage(), e.failure());
				log.error("unexpected: " + ex.id, ex);
				e.response().setStatusCode(580).setStatusMessage(ex.getErrorMessage()).end(ex.getMessage());
			} else {
				ApiException ex = ApiException.unexpected("oops", e.failure());
				log.error("unexpected: " + ex.id, ex);
				e.response().setStatusCode(580).setStatusMessage(ex.getErrorMessage()).end(ex.getMessage());
			}
		});

		route(router);
		String prefix = config().getJsonObject(ConfigKey.WEB.key()).getString(ConfigKey.PREFIX.key());
		router.mountSubRouter("/"+prefix+"/rest", restRouter());

		return router;
	}

	private Router restRouter() {
		Router router = Router.router(vertx);
		router.route().consumes(APPLICATION_JSON);
		router.route().produces(APPLICATION_JSON);
		router.route().handler(ResponseTimeHandler.create());
		router.route().handler(BodyHandler.create());
		router.route().handler(context -> {
			context.response().headers().add(CONTENT_TYPE, APPLICATION_JSON);
			context.next();
		});


		//preferred_username

		restRouter(router);

		return router;
	}

	public void route(Router router) {
		//blank, should be overriden
	}

	public void init() {

	}

	public void restRouter(Router router) {
		//blank, should be overriden
	}

	public MultiMap transformQueryParams(MultiMap map) {
		MultiMap result = MultiMap.caseInsensitiveMultiMap();

		for (String key : map.names()) {
			String queryParam = map.getAll(key).get(0);

			if (queryParam.contains(",")) {
				List<String> list = Arrays.asList(queryParam.split(","));
				result.add(key, list);
			} else {
				result.add(key, map.get(key));
			}
		}
		return result;
	}

	protected void okEmpty(AsyncResult<Void> asyncResult, RoutingContext ctx) {
		if (asyncResult.succeeded()) {
			ctx.response().setStatusCode(204).end();
		} else {
			ctx.fail(asyncResult.cause());
		}
	}

	protected void ok(AsyncResult<JsonObject> ar, RoutingContext ctx) {
		if (ar.succeeded()) {
			ctx.response().setStatusCode(200).end(ar.result().toString());
		} else {
			ctx.fail(ar.cause());
		}
	}

	protected void ok(JsonObject jsonObject, RoutingContext ctx) {
		ctx.response().setStatusCode(200).end(jsonObject.toString());
	}
}
