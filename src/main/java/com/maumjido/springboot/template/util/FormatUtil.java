package com.maumjido.springboot.template.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FormatUtil {
  private static TimeZone timeZone = TimeZone.getTimeZone("GMT+09:00");

  private static String MARK = "*";

  public static String getNumber(Object obj) {
    if (obj == null) {
      return "0";
    }
    try {
      String strVal = String.valueOf(obj);
      int pointIndex = strVal.indexOf(".");
      Long val = Long.parseLong(pointIndex < 0 ? strVal : strVal.substring(0, pointIndex));
      String result = String.format("%,d", val);
      if (pointIndex > 0) {
        String decimal = strVal.substring(pointIndex);
        if (!decimal.matches(".0+")) {
          result += decimal;
        }
      }
      return result;
    } catch (NumberFormatException e) {
      return String.valueOf(obj);
    } catch (Exception e) {
      return String.valueOf(obj);
    }
  }

  public static String getDate(Object source) {
    if (source == null) {
      return "";
    }
    if (source instanceof Date) {
      return getDate((Date) source);
    } else {
      return getDate(String.valueOf(source));
    }
  }

  public static String getDatetime(Object source) {
    if (source == null) {
      return "";
    }
    if (source instanceof Date) {
      return getDateTime((Date) source);
    } else if (source instanceof Long) {
      return getDateTime(new Date((Long) source));
    } else {
      return getDateTime(String.valueOf(source));
    }
  }

  public static String getDatetime(Object source, String glue) {
    if (source == null) {
      return "";
    }
    if (source instanceof Date) {
      return getDateTime((Date) source, glue);
    } else {
      return getDateTime(String.valueOf(source), glue);
    }
  }

  private static String getDate(Date source) {
    return getDate(dateToString(source, "yyyyMMdd"));
  }

  private static String getDate(String source) {
    return getDate(source, "-");
  }

  public static String getDate(Date source, String dateSeparator) {
    return getDate(dateToString(source, "yyyyMMdd"), dateSeparator);
  }

  public static String getDate(String source, String dateSeparator) {
    if (source == null) {
      return null;
    }
    source = source.trim();
    if (source.matches("^[0-9]{8}.*")) {
      return source.replaceAll("^(....)(..)(..).*", "$1" + dateSeparator + "$2" + dateSeparator + "$3");
    } else if (source.matches("^(....).?(..).?(..).*")) {
      return source.replaceAll("^(....).?(..).?(..).*", "$1" + dateSeparator + "$2" + dateSeparator + "$3");
    } else {
      return source;
    }
  }

  public static String getDateTime(Date date) {
    return dateToString(date, "yyyy-MM-dd HH:mm:ss");
  }

  public static String getDateTime(Date date, String dateSeparator) {
    return dateToString(date, "yyyy" + dateSeparator + "MM" + dateSeparator + "dd HH:mm:ss");
  }

  public static String getDateTime(String source) {
    return getDateTime(source, "-");
  }

  public static String getDateTime(String source, String dateSeparator) {
    if (source == null) {
      return null;
    }
    source = source.trim();
    if (source.matches("(....)-(..)-(..) ..:..:...*")) {
      return source.replaceAll("(....)-(..)-(..) (..):(..):(..).*", "$1" + dateSeparator + "$2" + dateSeparator + "$3 $4:$5:$6");
    } else if (source.matches("^(....)(..)(..)$")) {
      return source.replaceAll("^(....)(..)(..)$", "$1" + dateSeparator + "$2" + dateSeparator + "$3");
    } else if (source.matches("^(....)(..)(..)(..)(..)$")) {
      return source.replaceAll("^(....)(..)(..)(..)(..)$", "$1" + dateSeparator + "$2" + dateSeparator + "$3 $4:$5");
    } else if (source.matches("^(....)(..)(..)(..)(..)(..)$")) {
      return source.replaceAll("^(....)(..)(..)(..)(..)(..)$", "$1" + dateSeparator + "$2" + dateSeparator + "$3 $4:$5:$6");
    } else {
      return source;
    }
  }

  public static String getDateMinute(String source) {
    return getDateMinute(source, "-");
  }

  public static String getDateMinute(String source, String dateSeparator) {
    if (source == null) {
      return null;
    }
    source = source.trim();
    if (source.matches("(....).(..).(..) ..:...*")) {
      return source.replaceAll("(....).(..).(..) (..):(..).*", "$1" + dateSeparator + "$2" + dateSeparator + "$3 $4:$5");
    } else if (source.matches("^(....)(..)(..).*")) {
      return source.replaceAll("^(....)(..)(..).*", "$1" + dateSeparator + "$2" + dateSeparator + "$3");
    } else {
      return source;
    }
  }

  public static String getTime(String source) {
    if (source == null) {
      return null;
    }
    source = source.trim();
    if (source.matches("^(..)(..)(..)(...)$")) {
      return source.replaceAll("^(..)(..)(..)(...)$", "$1:$2:$3.$4");
    } else if (source.matches("^(..)(..)(..)$")) {
      return source.replaceAll("^(..)(..)(..)$", "$1:$2:$3");
    } else if (source.matches("^(..)(..)$")) {
      return source.replaceAll("^(..)(..)$", "$1:$2");
    } else {
      return source;
    }
  }

  public static String getMobile(String source) {
    if (source == null) {
      return null;
    } else {
      return source.replaceAll("^(\\d\\d\\d?)-?(\\d\\d\\d\\d?)-?(\\d\\d\\d\\d)$", "$1-$2-$3");
    }
  }

  public static String getBizNo(String source) {
    if (source == null) {
      return null;
    } else {
      return source.replaceAll("^(\\d\\d\\d)(\\d\\d)(\\d\\d\\d\\d\\d)$", "$1-$2-$3");
    }
  }

  public static String getEmailId(String source) {
    if (source == null) {
      return null;
    } else {
      return source.replaceAll("([^@]+)@.*", "$1");
    }
  }

  public static String getEmailServerName(String source) {
    if (source == null) {
      return null;
    } else {
      return source.replaceAll("[^@]+@(.*)", "$1");
    }
  }

  public static String getSecurityBirthday(String source) {
    if (source == null) {
      return null;
    }
    int length = source.length();
    if (length == 8) {
      return source.substring(0, 2) + multiMark(2) + source.substring(length - 4, length);
    } else {
      return getSecurity(source);
    }
  }

  public static String getSecurityMobile(String source) {
    if (source == null) {
      return null;
    }
    source = getMobile(source);
    if (source.length() != 12 && source.length() != 13) {
      return getSecurity(source);
    } else if (source.length() == 12) {// 010-123-4567
      return source.substring(0, 4) + multiMark(3) + "-" + source.substring(source.length() - 4, source.length());
    } else {// 010-1234-5678
      return source.substring(0, 4) + multiMark(4) + "-" + source.substring(source.length() - 4, source.length());
    }
  }

  public static String getSecurityEmail(String mail) {
    if (mail == null) {
      return null;
    }
    String[] datas = mail.split("@");
    if (datas.length != 2) {
      return getSecurity(mail);
    }
    String[] bodys = datas[1].split("\\.", 2);
    if (bodys.length != 2) {
      return getSecurity(mail);
    }
    String header = getSecurity(datas[0]);
    return header + "@" + getSecurity(bodys[0]) + "." + bodys[1];
  }

  public static String getSecurity(String source) {
    if (source == null) {
      return null;
    }
    int length = source.length();
    if (length == 2) {
      return source.substring(0, 1) + MARK;
    }
    int markCnt = 0;
    if (length < 2) {
      markCnt = 0;
    } else if (length == 3) {
      markCnt = 1;
    } else if (length < 6) {
      markCnt = 2;
    } else if (length > 5) {
      markCnt = (int) (length / 2.3);
    }
    int prefixCnt = (length - markCnt);
    if (prefixCnt > 1) {
      prefixCnt = prefixCnt / 2;
    }
    int suffixCnt = length - markCnt - prefixCnt;
    String result = source.substring(0, prefixCnt) + multiMark(markCnt);
    result += source.substring(length - suffixCnt, length);
    return result;
  }

  private static String multiMark(int loop) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < loop; i++) {
      sb.append(MARK);
    }
    return sb.toString();
  }

  public static String getFileSize(long fileSizeByte) {
    DecimalFormat oneDecimal = new DecimalFormat("0.0");
    long absNumber = Math.abs(fileSizeByte);
    double result = fileSizeByte;
    String suffix = "";
    if (absNumber < 1024) {
      oneDecimal = new DecimalFormat("0");
      suffix = "Byte";
    } else if (absNumber < 1024 * 1024) {
      result = fileSizeByte / 1024.0;
      suffix = "KB";
    } else if (absNumber < 1024 * 1024 * 1024) {
      result = fileSizeByte / (1024.0 * 1024);
      suffix = "MB";
    } else {
      result = fileSizeByte / (1024.0 * 1024 * 1024);
      suffix = "GB";
    }
    return oneDecimal.format(result) + suffix;
  }

  public static String lpad(Object src, String t, int length) {
    if (src == null) {
      return null;
    }
    String strSrc = String.valueOf(src).trim();
    String result = strSrc;
    for (int i = 0; i < length - strSrc.length(); i++) {
      result = t + result;
    }
    return result;
  }

  /**
   * 개행문자를 Br태그로 변경
   * 
   * @param source
   * @return
   */
  public static String returnToBr(String source) {
    if (source == null) {
      return null;
    }
    return source.replaceAll("\r\n", "<br/>").replaceAll("\r", "<br/>").replaceAll("\n", "<br/>");
  }

  public static String spaceToNbsp(String source) {
    if (source == null) {
      return null;
    }
    return source.replaceAll("^ ", "&nbsp;").replaceAll("\n ", "\n&nbsp;");
  }

  public static boolean isTel(String source) {
    if (source == null) {
      return false;
    }
    return source.matches("^(\\d\\d\\d?)-?(\\d\\d\\d\\d?)-?(\\d\\d\\d\\d)$");
  }

  public static String concat(String a, String b) {
    return a + b;
  }

  public static String getFilename(String a) {
    if (a == null) {
      return "";
    }
    int c = a.lastIndexOf(File.pathSeparator);
    if (c >= 0) {
      a = a.substring(c + 1);
    }
    int d = a.lastIndexOf(".");
    if (d >= 0) {
      a = a.substring(0, d);
    }
    return a;
  }

  public static String getDayName(Object date) {
    String strDate = getDate(date);
    if (strDate == null || strDate.length() != 10) {
      return "";
    }
    Calendar calendar = getCalendar(stringToDate(strDate, "yyyy-MM-dd"));
    int curDay = calendar.get(Calendar.DAY_OF_WEEK);
    String[] days = { "", "일", "월", "화", "수", "목", "금", "토" };
    return days[curDay];
  }

  public static Date stringToDate(String dateString, String format) {
    SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
    simpleFormat.setTimeZone(timeZone);
    try {
      return simpleFormat.parse(dateString);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static Calendar getCalendar(Date date) {
    Calendar cal = Calendar.getInstance(timeZone, Locale.KOREAN);
    cal.setTime(date);
    return cal;
  }

  public static String dateToString(Date date, String format) {
    SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
    simpleFormat.setTimeZone(timeZone);
    return simpleFormat.format(date.getTime());
  }

  public static String dateToString(Calendar date, String format) {
    SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
    simpleFormat.setTimeZone(timeZone);
    return simpleFormat.format(date.getTime());
  }
}
