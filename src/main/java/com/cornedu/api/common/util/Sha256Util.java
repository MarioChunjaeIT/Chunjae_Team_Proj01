package com.cornedu.api.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Util {
    public static String encrypt(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
