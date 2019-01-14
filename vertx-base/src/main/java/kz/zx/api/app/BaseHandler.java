package kz.zx.api.app;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import kz.zx.api.enums.Key;

/**
 * Created by deedarb on 8/1/17.
 */
public class BaseHandler {

	protected Vertx vertx;
	protected JsonObject config;
	protected EventBus eventBus;

	public BaseHandler(Vertx vertx) {
		this(vertx, vertx.getOrCreateContext().config());
	}

	public BaseHandler(Vertx vertx, JsonObject config) {
		this.vertx = vertx;
		this.config = config;
		this.eventBus = vertx.eventBus();
	}

	public DeliveryOptions action(Enum action){
		DeliveryOptions opts = new DeliveryOptions();
		opts.addHeader(Key.action, action.name());
		return opts;
	}

}
