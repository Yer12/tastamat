package kz.tastamat.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacUtils {

	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

	public static String hmacSign(String token, String bodyStr) {
		try {
			SecretKeySpec secretTokenSpec = new SecretKeySpec(token.getBytes(), HMAC_SHA256_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(secretTokenSpec);

			byte[] rawHmac = mac.doFinal(bodyStr.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(rawHmac);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
