package kz.zx.utils;

import io.vertx.core.eventbus.DeliveryOptions;
import kz.zx.enums.Key;

public class ActionUtils {
	public static DeliveryOptions action(Enum action){
		DeliveryOptions opts = new DeliveryOptions();
		opts.addHeader(Key.action, action.name());
		return opts;
	}
}
