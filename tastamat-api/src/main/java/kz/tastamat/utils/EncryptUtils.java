package kz.tastamat.utils;

import kz.zx.utils.StringUtils;

public class EncryptUtils {

    public static String obfuscate(Long val, Integer salt, Integer minLength) {
        String s = Long.toBinaryString(val);
        s = StringUtils.padStart(s, Integer.toBinaryString(salt).length(), '0');
        s = new StringBuilder(s).reverse().toString();
        Integer a = Integer.parseInt(s, 2);
        a = salt ^ a;
        s = a.toString();
        s = StringUtils.padStart(s, minLength, '0');
        return s;
    }

    public static Long deobfuscate(String val, Integer salt) {

        Integer a = salt ^ Integer.parseInt(val);
        String s = Long.toBinaryString(a);
        s = StringUtils.padStart(s, Integer.toBinaryString(salt).length(), '0');
        s = new StringBuilder(s).reverse().toString();
        return Long.valueOf(Integer.parseInt(s, 2));
    }

    public static Long deobfuscate(String val, Integer salt, Integer minLength) {
        String padded = StringUtils.padStart(val, minLength, '0');
        Integer a = salt ^ Integer.parseInt(padded);
        String s = Long.toBinaryString(a);
        s = StringUtils.padStart(s, Integer.toBinaryString(salt).length(), '0');
        s = new StringBuilder(s).reverse().toString();
        return Long.valueOf(Integer.parseInt(s, 2));
    }
}
