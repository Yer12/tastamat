package kz.zx.utils;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by didar on 8/21/16.
 */
public class StringUtils {

	public static String DEFAULT_DELIM = ", ";//разделитель по умолчанию
	public static String SPACE_DELIM = " ";

//	public List<String> asList(String str){
//		return Arrays.asList(str.split(","));
//	}

	//проверить строку на пустоту
	public static boolean isEmptyString(String value) {
		return value == null || value.trim().isEmpty();
	}

	//проверка на непустую строку
	public static boolean isNotEmptyString(String str) {
		return !isEmptyString(str);
	}

	public static Map<String, String> getQueryParamMap(URL url) {
		Map<String, String> map = new HashMap<>();
		if(url.getQuery() != null){
			String[] params = url.getQuery().split("&");
			for (String param : params) {
				String name = param.split("=")[0];
				String value = param.split("=")[1];
				map.put(name, value);
			}
		}
		return map;
	}

	//взять правую часть строки
	public static String right(String value, int length) {
		if (value != null && value.length() > length) {
			return value.substring(value.length() - length);
		}
		return value;
	}

	//взять левую часть строки
	public static String left(String value, int length) {
		if (value != null && value.length() > length) {
			return value.substring(0, length);
		}
		return value;
	}

	//склеивание строк с разделителем
	public static String merge(String[] arr) {
		return merge(Arrays.asList(arr));
	}

	//склеивание строк с разделителем
	public static String merge(String[] arr, String delim) {
		return merge(Arrays.asList(arr), delim);
	}

	//склеивание строк с разделителем
	public static String merge(List<String> list) {
		return merge(list, DEFAULT_DELIM);
	}

	//склеивание строк с разделителем
	public static String merge(List<String> list, String delim) {
		StringBuilder sb = new StringBuilder();
		boolean can = false;
		for (String text : list) {
			if (isEmptyString(text))
				continue;
			if (can)
				sb.append(delim);
			sb.append(text);
			can = true;
		}
		return sb.toString();
	}

	//вернуть первую непустую строку
	public static String getFirstNotEmptyString(String... strings) {
		if (strings == null)
			return null;
		for (String string : strings) {
			if (isNotEmptyString(string)) {
				return string;
			}
		}
		return null;
	}

	public static String firstCharacterUpperCase(String text) {
		String[] words = split(text.toLowerCase(), SPACE_DELIM);
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
		}
		return sb.toString();
	}

	/**
	 * Разбивка текста разделителем по умолчанию
	 */
	public static String[] split(String text) {
		return split(text, DEFAULT_DELIM);
	}

	/**
	 * Разбивка текста разделителем
	 */
	public static String[] split(String text, String regex) {
		return text != null
			? text.split(regex)
			: new String[0];
	}

	public static String concat(String delimeter, String... str) {
		StringBuilder sb = new StringBuilder();
		for (String s : str) {
			if (isNotEmptyString(s)) {
				if (sb.length() != 0) {
					sb.append(delimeter);
				}
				sb.append(s);
			}
		}
		return sb.toString();
	}

	public static boolean isNotEmpty(String str) {
		return isNotEmptyString(str);
	}

	public static boolean isEmpty(String str) {
		return isEmptyString(str);
	}

	public static String trim(String str) {
		return str != null ? str.trim() : "";
	}

	public static String firstUpperCase(String word) {
		if (word == null || word.isEmpty())
			return word;
		return word.substring(0, 1).toUpperCase() + word.toLowerCase().substring(1);
	}

	public static String displayName(String lastName, String firstName, String patronymic) {
		StringBuilder sb = new StringBuilder();
		if (lastName != null) {
			sb.append(lastName);
		}
		if (firstName != null && !firstName.isEmpty()) {
			sb.append(" ").append(firstName);
		}
		if (patronymic != null && !patronymic.isEmpty()) {
			sb.append(" ").append(patronymic);
		}
		return sb.toString();
	}


	public static String padStart(String string, int minLength, char padChar) {
		if (string.length() >= minLength) {
			return string;
		} else {
			StringBuilder sb = new StringBuilder(minLength);

			for (int i = string.length(); i < minLength; ++i) {
				sb.append(padChar);
			}

			sb.append(string);
			return sb.toString();
		}
	}

	public static String padEnd(String string, int minLength, char padChar) {
		if (string.length() >= minLength) {
			return string;
		} else {
			StringBuilder sb = new StringBuilder(minLength);
			sb.append(string);

			for (int i = string.length(); i < minLength; ++i) {
				sb.append(padChar);
			}

			return sb.toString();
		}
	}
}