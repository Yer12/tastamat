package kz.tastamat.payment.utils;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

public class KKB {

	private static boolean invert = true;
	private static String keystoretype = "JKS";
	private static String signalgorythm = "SHA1withRSA";

	public static synchronized String build64(String paramString1, String paramString2, String paramString3) {
		FileInputStream localFileInputStream1 = null;
		FileInputStream localFileInputStream2 = null;
		try {

			localFileInputStream1 = new FileInputStream(paramString1);
			byte[] arrayOfByte1 = new byte[localFileInputStream1.available()];
			localFileInputStream1.read(arrayOfByte1);
			String str1 = new String(arrayOfByte1);
			String str2 = value(str1, "keystore");
			String str3 = value(str1, "alias");
			String str4 = value(str1, "keypass");
			String str5 = value(str1, "storepass");
			String str6 = value(str1, "template");
			String str7 = value(str1, "certificate");
			String str8 = value(str1, "merchant_id");
			String str9 = value(str1, "currency");
			String str10 = value(str1, "merchant_name");

			localFileInputStream2 = new FileInputStream(str6);
			byte[] arrayOfByte2 = new byte[localFileInputStream2.available()];
			localFileInputStream2.read(arrayOfByte2);
			String str11 = new String(arrayOfByte2);
			str11 = replace(str11, "%order_id%", paramString3);
			str11 = replace(str11, "%amount%", paramString2);
			str11 = replace(str11, "%amount%", paramString2);
			str11 = replace(str11, "%certificate%", str7);
			str11 = replace(str11, "%merchant_id%", str8);
			str11 = replace(str11, "%currency%", str9);
			str11 = replace(str11, "%merchant_name%", str10);
			String str12 = sign64(str11, str2, str3, str4, str5);
			str11 = str11 + "<merchant_sign type=\"RSA\">" + str12 + "</merchant_sign>";
			str11 = "<document>" + str11 + "</document>";
			byte[] arrayOfByte3 = str11.getBytes();
			char[] arrayOfChar = KKBBase64.encode(arrayOfByte3);
			try {
				localFileInputStream1.close();
				localFileInputStream2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new String(arrayOfChar);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static synchronized String build64(String paramString1, String paramString3) {
		FileInputStream localFileInputStream1 = null;
		FileInputStream localFileInputStream2 = null;
		try {

			localFileInputStream1 = new FileInputStream(paramString1);
			byte[] arrayOfByte1 = new byte[localFileInputStream1.available()];
			localFileInputStream1.read(arrayOfByte1);
			String str1 = new String(arrayOfByte1);
			String str2 = value(str1, "keystore");
			String str3 = value(str1, "alias");
			String str4 = value(str1, "keypass");
			String str5 = value(str1, "storepass");
			String str6 = "conf/epay/template_status.xml";
			String str7 = value(str1, "certificate");
			String str8 = value(str1, "merchant_id");

			localFileInputStream2 = new FileInputStream(str6);
			byte[] arrayOfByte2 = new byte[localFileInputStream2.available()];
			localFileInputStream2.read(arrayOfByte2);
			String str11 = new String(arrayOfByte2);
			str11 = replace(str11, "%order_id%", paramString3);
			str11 = replace(str11, "%merchant_id%", str8);
			String str12 = sign64(str11, str2, str3, str4, str5);
			str11 = str11 + "<merchant_sign type=\"RSA\" cert_id=\""+str7+"\">"+str12+"</merchant_sign>";
			str11 = "<document>" + str11 + "</document>";
//			byte[] arrayOfByte3 = str11.getBytes();
//			char[] arrayOfChar = KKBBase64.encode(arrayOfByte3);
			try {
				localFileInputStream1.close();
				localFileInputStream2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return str11;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static synchronized String build64(String paramString1, String orderId, String ref, String code, String amount) {
		FileInputStream localFileInputStream1 = null;
		FileInputStream localFileInputStream2 = null;
		try {

			localFileInputStream1 = new FileInputStream(paramString1);
			byte[] arrayOfByte1 = new byte[localFileInputStream1.available()];
			localFileInputStream1.read(arrayOfByte1);
			String str1 = new String(arrayOfByte1);
			String str2 = value(str1, "keystore");
			String str3 = value(str1, "alias");
			String str4 = value(str1, "keypass");
			String str5 = value(str1, "storepass");
			String str6 = "conf/epay/template_confirm.xml";
			String str7 = value(str1, "certificate");
			String str8 = value(str1, "merchant_id");

			localFileInputStream2 = new FileInputStream(str6);
			byte[] arrayOfByte2 = new byte[localFileInputStream2.available()];
			localFileInputStream2.read(arrayOfByte2);
			String str11 = new String(arrayOfByte2);
			str11 = replace(str11, "%order_id%", orderId);
			str11 = replace(str11, "%merchant_id%", str8);
			str11 = replace(str11, "%ref%", ref);
			str11 = replace(str11, "%code%", code);
			str11 = replace(str11, "%amount%", amount);
			String str12 = sign64(str11, str2, str3, str4, str5);
			str11 = str11 + "<merchant_sign type=\"RSA\" cert_id=\""+str7+"\">"+str12+"</merchant_sign>";
			str11 = "<document>" + str11 + "</document>";
//			byte[] arrayOfByte3 = str11.getBytes();
//			char[] arrayOfChar = KKBBase64.encode(arrayOfByte3);
			try {
				localFileInputStream1.close();
				localFileInputStream2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return str11;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static synchronized String sign64(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {
		FileInputStream localObject2 = null;
		try {
			KKBBase64 localBase64 = new KKBBase64();
			byte[] arrayOfByte1 = paramString1.getBytes();
			char[] arrayOfChar1 = paramString4.toCharArray();
			char[] arrayOfChar2 = paramString5.toCharArray();
			KeyStore localObject1 = KeyStore.getInstance(keystoretype);
			localObject2 = new FileInputStream(paramString2);
			localObject1.load(localObject2, arrayOfChar2);
			Signature localObject3 = Signature.getInstance(signalgorythm);
			PrivateKey localPrivateKey = (PrivateKey) localObject1.getKey(paramString3, arrayOfChar1);
			localObject3.initSign(localPrivateKey);
			localObject3.update(arrayOfByte1);

			byte[] arrayOfByte2 = ((Signature) localObject3).sign();
			if (invert) {
				int i = 0;
				for (int j = arrayOfByte2.length; i < j / 2; i++) {
					byte k = arrayOfByte2[i];
					arrayOfByte2[i] = arrayOfByte2[(j - i - 1)];
					arrayOfByte2[(j - i - 1)] = k;
				}
			}
			char[] arrayOfChar3 = KKBBase64.encode(arrayOfByte2);
			try {
				localObject2.close();
			} catch (Exception e) {
			}
			return new String(arrayOfChar3);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public synchronized static boolean verifyResponse(String xml, String signature64, String keystore, String alias, String storepass) {
		boolean invert = true;
		String keystoretype = new String("JKS");
		String signalgorythm = new String("SHA1withRSA");
		try (FileInputStream localFileInputStream = new FileInputStream(keystore)){
			byte[] xmlBA = xml.getBytes();
			byte[] signature64BA = KKBBase64.decode(signature64.toCharArray());
			char[] storepassCA = storepass.toCharArray();
			KeyStore localKeyStore = KeyStore.getInstance(keystoretype);
			localKeyStore.load(localFileInputStream, storepassCA);
			Signature localSignature = Signature.getInstance(signalgorythm);
			localSignature.initVerify(localKeyStore.getCertificate(alias));
			localSignature.update(xmlBA);
			if (invert) {
				int i = 0;
				for (int j = signature64BA.length; i < j / 2; i++) {
					byte k = signature64BA[i];
					signature64BA[i] = signature64BA[(j - i - 1)];
					signature64BA[(j - i - 1)] = k;
				}
			}
			return localSignature.verify(signature64BA);
		} catch (Exception localException) {
			System.err.println("verify exception " + localException.toString());
		}
		return false;
	}

	public static synchronized boolean verify(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5) {
		try (FileInputStream localFileInputStream = new FileInputStream(paramString3)) {
			KKBBase64 localBase64 = new KKBBase64();
			byte[] arrayOfByte1 = paramString1.getBytes();
			byte[] arrayOfByte2 = KKBBase64.decode(paramString2.toCharArray());
			char[] arrayOfChar = paramString5.toCharArray();
			KeyStore localKeyStore = KeyStore.getInstance(keystoretype);
			localKeyStore.load(localFileInputStream, arrayOfChar);
			Signature localSignature = Signature.getInstance(signalgorythm);
			localSignature.initVerify(localKeyStore.getCertificate(paramString4));
			localSignature.update(arrayOfByte1);
			if (invert) {
				int i = 0;
				for (int j = arrayOfByte2.length; i < j / 2; i++) {
					byte k = arrayOfByte2[i];
					arrayOfByte2[i] = arrayOfByte2[(j - i - 1)];
					arrayOfByte2[(j - i - 1)] = k;
				}
			}
			return localSignature.verify(arrayOfByte2);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String value(String paramString1, String paramString2) {
		int i = paramString1.indexOf(paramString2);
		i = paramString1.indexOf('"', i) + 1;
		int j = paramString1.indexOf('"', i);
		return paramString1.substring(i, j);
	}

	private static String replace(String paramString1, String paramString2, String paramString3) {
		int i = paramString1.indexOf(paramString2);
		int j = i + paramString2.length();
		return paramString1.substring(0, i) + paramString3 + paramString1.substring(j);
	}
}
