package com.wesleyramos.codehero.Helpers;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5 {

    public static String create(String secret) {
        byte[] md5Secret = secret.getBytes();
        BigInteger md5Data = null;

        try {
            md5Data = new BigInteger(1, encryptMD5(md5Secret));
        }catch (Exception e){
            e.printStackTrace();
        }

        String md5Str = md5Data.toString(16);
        return md5Str.length() < 32 ? 0 + md5Str : md5Str;

    }

    private static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }
}
