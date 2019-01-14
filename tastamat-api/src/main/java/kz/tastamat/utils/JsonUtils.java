package kz.tastamat.utils;

import io.vertx.core.json.JsonObject;

public class JsonUtils {

	public static final String CODE = "code";
	public static final String KK = "kk";
	public static final String RU = "ru";
	public static final String EN = "en";
	public static final String AR = "ar";

	public static JsonObject getJsonObject() {
		return new JsonObject();
	}

	public static JsonObject getDictionary(String code, String kk, String ru, String en, String ar) {
		JsonObject dictionary = new JsonObject();
		dictionary.put(CODE, code);
		dictionary.put(KK, kk);
		dictionary.put(RU, ru);
		dictionary.put(EN, en);
		dictionary.put(AR, ar);

		return dictionary;
	}
}
