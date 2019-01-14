package kz.zx.api.app;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by deedarb on 10/29/17.
 */
public class DbHandler extends BaseHandler {

	private Map<String, DbUnit> dataSourceMap;

	public DbHandler(Vertx vertx) {
		this(vertx, vertx.getOrCreateContext().config());
	}

	public DbHandler(Vertx vertx, JsonObject config) {
		super(vertx, config);

		dataSourceMap = new HashMap<>();
		AppContext.instance().getDataSourceMap().forEach((name, ds) -> dataSourceMap.put(name, new DbUnit(name, ds, vertx)));
	}

	protected DbUnit dbUnit(String name) {
		return dataSourceMap.get(name);
	}

	protected DbUnit dbUnit() {
		return dataSourceMap.get("default");
	}

	protected <T> void blocking(Function<DSLContext, T> function, Handler<AsyncResult<T>> handler) {
		dbUnit().blocking(function, handler);
	}

	protected void blocking(Consumer<DSLContext> function, Handler<AsyncResult<Void>> handler) {
		dbUnit().blocking(function, handler);
	}

	protected <T> void blocking(BiConsumer<DSLContext, Future> function, Handler<AsyncResult<T>> handler) {
		dbUnit().blocking(function, handler);
	}
}
