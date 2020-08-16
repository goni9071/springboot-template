package com.maumjido.springboot.template.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecurityUtil {
  public static String sha256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    digest.reset();
    digest.update(input.getBytes("UTF-8"));
    return byteArrayToHex(digest.digest());
  }

  public static String sha512(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("SHA-512");
    digest.reset();
    digest.update(input.getBytes("UTF-8"));
    return byteArrayToHex(digest.digest());
  }

  public static String encodeBase64(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    return Base64.getEncoder().encodeToString(input.getBytes());
  }

  public static String decodeBase64(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    return new String(Base64.getDecoder().decode(input));
  }

  public static String byteArrayToHex(byte buf[]) {
    StringBuffer strbuf = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      strbuf.append(String.format("%02X", buf[i]));
    }

    return strbuf.toString();
  }

  public static byte[] hexToByteArray(String s) {
    byte[] retValue = null;
    if (s != null && s.length() != 0) {
      retValue = new byte[s.length() / 2];
      for (int i = 0; i < retValue.length; i++) {
        retValue[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
      }
    }
    return retValue;
  }

  public static String getRandomAlphaNumeric(int len) {
    char[] charSet = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
        'v', 'w', 'x', 'y', 'z' };

    int idx = 0;
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < len; i++) {
      idx = (int) (charSet.length * Math.random());
      sb.append(charSet[idx]);
    }

    return sb.toString();
  }
}
