package kz.tastamat.payment.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class KKBBase64 {
	private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
	private static byte[] codes = new byte[256];

	public static char[] encode(byte[] var0) {
		char[] var1 = new char[(var0.length + 2) / 3 * 4];
		int var2 = 0;

		for(int var3 = 0; var2 < var0.length; var3 += 4) {
			boolean var4 = false;
			boolean var5 = false;
			int var6 = 255 & var0[var2];
			var6 <<= 8;
			if(var2 + 1 < var0.length) {
				var6 |= 255 & var0[var2 + 1];
				var5 = true;
			}

			var6 <<= 8;
			if(var2 + 2 < var0.length) {
				var6 |= 255 & var0[var2 + 2];
				var4 = true;
			}

			var1[var3 + 3] = alphabet[var4?var6 & 63:64];
			var6 >>= 6;
			var1[var3 + 2] = alphabet[var5?var6 & 63:64];
			var6 >>= 6;
			var1[var3 + 1] = alphabet[var6 & 63];
			var6 >>= 6;
			var1[var3] = alphabet[var6 & 63];
			var2 += 3;
		}

		return var1;
	}

	public static byte[] decode(char[] var0) {
		int var1 = var0.length;

		for(int var2 = 0; var2 < var0.length; ++var2) {
			if(var0[var2] > 255 || codes[var0[var2]] < 0) {
				--var1;
			}
		}

		int var3 = var1 / 4 * 3;
		if(var1 % 4 == 3) {
			var3 += 2;
		}

		if(var1 % 4 == 2) {
			++var3;
		}

		byte[] var4 = new byte[var3];
		int var5 = 0;
		int var6 = 0;
		int var7 = 0;

		for(int var8 = 0; var8 < var0.length; ++var8) {
			byte var9 = var0[var8] <= 255?codes[var0[var8]]:-1;
			if(var9 >= 0) {
				var6 <<= 6;
				var5 += 6;
				var6 |= var9;
				if(var5 >= 8) {
					var5 -= 8;
					var4[var7++] = (byte)(var6 >> var5 & 255);
				}
			}
		}

		if(var7 != var4.length) {
			throw new Error("Miscalculated data length (wrote " + var7 + " instead of " + var4.length + ")");
		} else {
			return var4;
		}
	}

	public static void main(String[] var0) {
		boolean var1 = false;
		if(var0.length == 0) {
			System.out.println("usage:  java Base64 [-d[ecode]] filename");
			System.exit(0);
		}

		for(int var2 = 0; var2 < var0.length; ++var2) {
			if("-decode".equalsIgnoreCase(var0[var2])) {
				var1 = true;
			} else if("-d".equalsIgnoreCase(var0[var2])) {
				var1 = true;
			}
		}

		String var3 = var0[var0.length - 1];
		File var4 = new File(var3);
		if(!var4.exists()) {
			System.out.println("Error:  file \'" + var3 + "\' doesn\'t exist!");
			System.exit(0);
		}

		if(var1) {
			char[] var7 = readChars(var4);
			byte[] var8 = decode(var7);
			writeBytes(var4, var8);
		} else {
			byte[] var5 = readBytes(var4);
			char[] var6 = encode(var5);
			writeChars(var4, var6);
		}
	}

	private static byte[] readBytes(File var0) {
		ByteArrayOutputStream var1 = new ByteArrayOutputStream();

		try {
			FileInputStream var2 = new FileInputStream(var0);
			BufferedInputStream var3 = new BufferedInputStream(var2);
			boolean var4 = false;
			byte[] var5 = new byte[16384];

			int var7;
			while((var7 = var3.read(var5)) != -1) {
				if(var7 > 0) {
					var1.write(var5, 0, var7);
				}
			}

			var3.close();
		} catch (Exception var6) {
			var6.printStackTrace();
		}

		return var1.toByteArray();
	}

	private static char[] readChars(File var0) {
		CharArrayWriter var1 = new CharArrayWriter();

		try {
			FileReader var2 = new FileReader(var0);
			BufferedReader var3 = new BufferedReader(var2);
			boolean var4 = false;
			char[] var5 = new char[16384];

			int var7;
			while((var7 = var3.read(var5)) != -1) {
				if(var7 > 0) {
					var1.write(var5, 0, var7);
				}
			}

			var3.close();
		} catch (Exception var6) {
			var6.printStackTrace();
		}

		return var1.toCharArray();
	}

	private static void writeBytes(File var0, byte[] var1) {
		try {
			FileOutputStream var2 = new FileOutputStream(var0);
			BufferedOutputStream var3 = new BufferedOutputStream(var2);
			var3.write(var1);
			var3.close();
		} catch (Exception var4) {
			var4.printStackTrace();
		}
	}

	private static void writeChars(File var0, char[] var1) {
		try {
			FileWriter var2 = new FileWriter(var0);
			BufferedWriter var3 = new BufferedWriter(var2);
			var3.write(var1);
			var3.close();
		} catch (Exception var4) {
			var4.printStackTrace();
		}
	}

	public KKBBase64() {
	}

	static {
		for(int var0 = 0; var0 < 256; ++var0) {
			codes[var0] = -1;
		}

		for(int var1 = 65; var1 <= 90; ++var1) {
			codes[var1] = (byte)(var1 - 65);
		}

		for(int var2 = 97; var2 <= 122; ++var2) {
			codes[var2] = (byte)(26 + var2 - 97);
		}

		for(int var3 = 48; var3 <= 57; ++var3) {
			codes[var3] = (byte)(52 + var3 - 48);
		}

		codes[43] = 62;
		codes[47] = 63;
	}
}
