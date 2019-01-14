package kz.zx.utils;

public class NumberUtils {


	public static Long longValue(Long number, Long defaultValue) {
		return number != null ? number : defaultValue;
	}
}
