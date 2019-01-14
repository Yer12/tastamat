package kz.zx.api.app;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import kz.zx.api.enums.ConfigKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deedarb on 7/20/17.
 */
public class AppContext {

	private Map<String, HikariDataSource> dataSourceMap;
	private Map<String, Object> context;

	private static AppContext appContext;

	public static AppContext instance() {
		return appContext;
	}

	private AppContext() {}

	private AppContext(JsonObject config) {
		dataSourceMap = new HashMap<>();

		if (config.containsKey(ConfigKey.DATASOURCES.key())) {
			JsonArray dataSourceArr = config.getJsonArray(ConfigKey.DATASOURCES.key());

			for (int i = 0; i < dataSourceArr.size(); i++) {
				JsonObject dsc = dataSourceArr.getJsonObject(i);
				HikariConfig ds = new HikariConfig();
				ds.setUsername(dsc.getString(ConfigKey.USERNAME.key()));
				ds.setPassword(dsc.getString(ConfigKey.PASSWORD.key()));
				ds.setJdbcUrl(dsc.getString(ConfigKey.JDBC_URL.key()));
				ds.setMaximumPoolSize(dsc.getInteger(ConfigKey.MAX_POOL_SIZE.key()));
				dataSourceMap.put(dsc.getString(ConfigKey.NAME.key()), new HikariDataSource(ds));
			}
		}

		context = new HashMap<>();
	}

	public <T> T get(String name) {
		return (T) context.get(name);
	}

	public void set(String name, Object object) {
		context.put(name, object);
	}

	public static AppContext init(JsonObject config) {
		if (appContext == null) {
			appContext = new AppContext(config);
		}
		return appContext;
	}

	public Map<String, HikariDataSource> getDataSourceMap() {
		return dataSourceMap;
	}
}
