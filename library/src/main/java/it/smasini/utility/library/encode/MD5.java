package it.smasini.utility.library.encode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Simone on 27/02/17.
 */

public class MD5 {

    public static byte[] md5Byte(String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static String md5(String s) {
        return convertToHex(md5Byte(s));
    }

    private static String convertToHex(byte[] data) {
        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : data) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }
        return hexString.toString();
    }
}
