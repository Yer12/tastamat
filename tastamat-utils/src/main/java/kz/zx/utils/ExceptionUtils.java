package kz.zx.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author: Nurlan Rakhimzhanov <nr@bee.kz>
 * @Date: 04.09.2016 18:13
 */
public class ExceptionUtils {

	public static String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}

}
