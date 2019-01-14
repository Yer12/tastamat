package kz.zx.api.app;

import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by deedarb on 10/29/17.
 */
public abstract class BaseRoute {

	protected Vertx vertx;
	protected JsonObject config;

	public BaseRoute(Vertx vertx) {
		this.vertx = vertx;
		this.config = vertx.getOrCreateContext().config();
	}

	public abstract Router route();

	public MultiMap transformQueryParams(MultiMap map) {
		MultiMap result = MultiMap.caseInsensitiveMultiMap();

		for (String key : map.names()) {
			String queryParam = map.getAll(key).get(0);

//			if (queryParam.contains(",")) {
//				List<String> list = Arrays.asList(queryParam.split(","));
//				result.add(key, list);
//			} else {
				result.add(key, map.get(key));
//			}
		}
		return result;
	}

	protected String queryParam(String key, RoutingContext ctx){
		List<String> params = ctx.queryParam(key);
		return params.isEmpty() ? null : params.get(0);
	}

	protected String queryParam(String key, String defaultValue, RoutingContext ctx){
		List<String> params = ctx.queryParam(key);
		return params.isEmpty() ? defaultValue : params.get(0);
	}

	protected void okEmpty(AsyncResult<Void> asyncResult, RoutingContext ctx) {
		if (asyncResult.succeeded()) {
			ctx.response().setStatusCode(204).end();
		} else {
			ctx.fail(asyncResult.cause());
		}
	}

	protected void okEmpty(RoutingContext ctx) {
		ctx.response().setStatusCode(204).end();
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

	protected void ok(JsonArray jsonArray, RoutingContext ctx) {
		ctx.response().setStatusCode(200).end(jsonArray.toString());
	}
}
