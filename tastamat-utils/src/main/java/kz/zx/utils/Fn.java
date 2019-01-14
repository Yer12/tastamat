package kz.zx.utils;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by didar on 8/29/16.
 */
public class Fn {

	public static <T> T notNull(Object obj, Supplier<T> supplier) {
		if (obj == null) {
			return null;
		}
		return supplier.get();
	}

	public static <T, R> R notNull(T obj, Function<T, R> supplier) {
		if (obj == null) {
			return null;
		}
		return supplier.apply(obj);
	}

	public static <T, R> R notNull(T obj, R defaultValue, Function<T, R> supplier) {
		if (obj == null) {
			return defaultValue;
		}
		return supplier.apply(obj);
	}

	public static <T> T notNull(Object obj, T defaultObj, Supplier<T> supplier) {
		if (obj == null) {
			return defaultObj;
		}
		return supplier.get();
	}

}
