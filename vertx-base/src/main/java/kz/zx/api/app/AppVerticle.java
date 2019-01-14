package kz.zx.api.app;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import kz.zx.api.enums.ConfigKey;
import io.vertx.core.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deedarb on 7/17/17.
 */
public class AppVerticle extends AbstractVerticle {

	private Logger log = LoggerFactory.getLogger(AppVerticle.class);

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		String name = config().getString("name");
		log.info("starting {} with config: {}", name, config());

		JsonArray verticlesArr = config().getJsonArray(ConfigKey.VERTICLES.key());
		List<Future> futures = new ArrayList<>();

		Json.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		for (int i = 0; i < verticlesArr.size(); i++) {
			JsonObject conf = verticlesArr.getJsonObject(i);

			if (conf.getBoolean(ConfigKey.ENABLED.key(), false)) {
				Integer instanceCount = conf.getInteger(ConfigKey.INSTANCE_COUNT.key(), 1);

				DeploymentOptions deploymentOptions = new DeploymentOptions();
				deploymentOptions.setConfig(config());

				deploymentOptions.setInstances(instanceCount);
				deploymentOptions.setWorker(conf.getBoolean(ConfigKey.WORKER.key(), false));
				if (!deploymentOptions.isWorker()) {
					Integer poolSize = conf.getInteger(ConfigKey.POOL_SIZE.key(), 5);
					deploymentOptions.setWorkerPoolSize(poolSize);
				}

				String verticleClassName = conf.getString(ConfigKey.VERTICLE_CLASS.key());

				Future<String> future = Future.future();
				futures.add(future);

				vertx.deployVerticle(verticleClassName, deploymentOptions, dr -> {
					log.info("deployed web: {} - {}, instances: {}", verticleClassName, dr.result(), instanceCount);
					if (dr.succeeded()) {
						future.complete(dr.result());
					} else {
						future.fail(dr.cause());
					}
				});
			}
		}

		CompositeFuture.all(futures).setHandler(ar -> {
			if (ar.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail(ar.cause());
			}
		});
	}


}
