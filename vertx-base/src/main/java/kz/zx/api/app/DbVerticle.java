package kz.zx.api.app;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by deedarb on 7/27/17.
 */
public class DbVerticle extends BaseVerticle {

	private Map<String, DbUnit> dataSourceMap;

	@Override
	public void start() throws Exception {
		dataSourceMap = new HashMap<>();
		AppContext.instance().getDataSourceMap().forEach((name, ds) -> dataSourceMap.put(name, new DbUnit(name, ds, vertx)));
	}

	public DbUnit dbUnit(String name) {
		return dataSourceMap.get(name);
	}

	public DbUnit dbUnit() {
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
