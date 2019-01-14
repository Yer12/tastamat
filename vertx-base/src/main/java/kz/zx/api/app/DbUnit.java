package kz.zx.api.app;

import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import kz.zx.api.enums.ConfigKey;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by deedarb on 9/19/17.
 */
public class DbUnit {

	private HikariDataSource dataSource;
	private WorkerExecutor db;
	private Vertx vertx;

	public DbUnit(HikariDataSource dataSource, Vertx vertx){
		this.vertx = vertx;
		this.dataSource = dataSource;
		this.db = vertx.createSharedWorkerExecutor(dataSource.getDataSourceProperties().getProperty(ConfigKey.NAME.key()), dataSource.getMaximumPoolSize());
	}

	public DbUnit(String name, HikariDataSource dataSource, Vertx vertx) {
		this.vertx = vertx;
		this.dataSource = dataSource;
		this.db = vertx.createSharedWorkerExecutor("zx-" + name + "-ds-pool", dataSource.getMaximumPoolSize());
	}

	public <T> T run(Function<DSLContext, T> function) {
		try (Connection connection = dataSource.getConnection()) {
			DSLContext ctx = DSL.using(connection, SQLDialect.POSTGRES_9_4);
			return function.apply(ctx);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public void run(Consumer<DSLContext> function) {
		try (Connection connection = dataSource.getConnection()) {
			DSLContext ctx = DSL.using(connection, SQLDialect.POSTGRES_9_4);
			function.accept(ctx);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public <T> void blocking(Function<DSLContext, T> function, Handler<AsyncResult<T>> handler) {
		db.executeBlocking((Future<T> future) -> {
			try (Connection connection = dataSource.getConnection()) {
				DSLContext ctx = DSL.using(connection, SQLDialect.POSTGRES_9_4);
				T result = function.apply(ctx);
				future.complete(result);
			} catch (Exception ex) {
				future.fail(ex);
			}
		}, false, ar -> vertx.runOnContext(v -> handler.handle(ar)));
	}

	public void blocking(Consumer<DSLContext> function, Handler<AsyncResult<Void>> handler) {
		db.executeBlocking((Future<Void> future) -> {
			try (Connection connection = dataSource.getConnection()) {
				DSLContext ctx = DSL.using(connection, SQLDialect.POSTGRES_9_4);
				function.accept(ctx);
				future.complete();
			} catch (Exception e) {
				future.fail(e);
			}
		}, false, ar -> vertx.runOnContext(v -> handler.handle(ar)));
	}

	public <T> void blocking(BiConsumer<DSLContext, Future> function, Handler<AsyncResult<T>> handler) {
		db.executeBlocking((Future<T> future) -> {
			try (Connection connection = dataSource.getConnection()) {
				DSLContext ctx = DSL.using(connection, SQLDialect.POSTGRES_9_4);
				function.accept(ctx, future);
			} catch (Exception e) {
				future.fail(e);
			}
		}, false, ar -> vertx.runOnContext(v -> handler.handle(ar)));
	}

}
