package kz.zx.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by deedarb on 8/9/17.
 */
public class Codec {

	public static String md5Hex(byte[] bytes) {
		return DigestUtils.md5Hex(bytes);
	}

	public static String sha256Hex(byte[] bytes) {
		return DigestUtils.sha256Hex(bytes);
	}

}
