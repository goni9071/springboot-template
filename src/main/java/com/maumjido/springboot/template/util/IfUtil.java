package com.maumjido.springboot.template.util;

import java.util.Collection;

public class IfUtil {

  public static Object decode(Object cond, Object... targets) {
    for (int i = 0; i < targets.length; i = i + 2) {
      Object target = targets[i];
      if (i + 1 == targets.length) {
        return targets[i];
      }
      if (cond == null) {
        if (target == null) {
          return targets[i + 1];
        }
      } else if (cond.equals(target)) {
        return targets[i + 1];
      }
    }
    return null;
  }

  public static String contains(Object src, Object value, String ifTrue, String ifFalse) {
    if (src == null) {
      return ifFalse;
    }
    if (src instanceof Collection<?>) {
      return ((Collection<?>) src).contains(value) ? ifTrue : ifFalse;
    } else if (src instanceof String) {
      return ((String) src).contains(String.valueOf(value)) ? ifTrue : ifFalse;
    }
    return ifFalse;
  }

  public static <T> T nvl(T src, T defaultValue) {
    if (src == null) {
      return defaultValue;
    }
    return src;
  }

  public static String evl(String src, String defaultValue) {
    if (src == null || src.trim().isEmpty()) {
      return defaultValue;
    }
    return src;
  }
}
