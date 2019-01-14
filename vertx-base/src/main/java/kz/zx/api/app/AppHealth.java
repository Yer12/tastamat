package kz.zx.api.app;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by deedarb on 7/20/17.
 */
public class AppHealth {

	public static void route(Router router, Vertx vertx) {
		long startupTime = System.currentTimeMillis();
		HealthChecks healthChecks = HealthChecks.create(vertx);
		healthChecks.register("uptime", future -> future.complete(Status.OK(new JsonObject().put("uptime", System.currentTimeMillis() - startupTime))));

		AppContext.instance().getDataSourceMap().forEach((name, ds) -> {
			healthChecks.register("db-" + name, future -> {
				try (Connection connection = ds.getConnection();
					 PreparedStatement ps = connection.prepareStatement("select current_timestamp");
					 ResultSet rs = ps.executeQuery()
				) {
					if (rs.next()) {
						Timestamp currentTimestamp = rs.getTimestamp(1);
						future.complete(Status.OK(new JsonObject().put("dbTime", currentTimestamp.getTime())));
					} else {
						future.fail("rs.empty");
					}
				} catch (SQLException ex) {
					future.fail(ex);
				}
			});
		});

		router.route("/health").handler(HealthCheckHandler.createWithHealthChecks(healthChecks));
	}

}
