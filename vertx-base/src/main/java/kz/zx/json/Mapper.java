package kz.zx.json;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import kz.zx.api.enums.Errors;
import kz.zx.exceptions.ApiException;

/**
 * Created by deedarb on 8/1/17.
 */
public class Mapper {

	public static <T> T map(Class<T> type, JsonObject jsonObject) {
		try {
			return Json.mapper.convertValue(jsonObject, type);
		} catch (Throwable e) {
			throw new ApiException(Errors.JSON_MAP, "json.conversion:" + type.getCanonicalName(), e);
		}
	}
}
