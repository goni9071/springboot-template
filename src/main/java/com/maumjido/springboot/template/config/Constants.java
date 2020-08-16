
package com.maumjido.springboot.template.config;

import com.maumjido.springboot.template.util.SystemProperties;

public class Constants {
  public static final String PRETTY_LOGGER_NAME = "PRETTY_LOG";
  public static final int DEFAULT_PAGE_NUMBER = 0;
  public static final int DEFAULT_PAGE_SIZE = 10;
  public static final int LOGIN_FAIL_LIMIT = 5;// 로그인 실패 허용 횟수
  public static final String CONTEXT_PATH = SystemProperties.getProperty("context.path", "");
  public static final String PROFILE = SystemProperties.getProperty("profile", "");
  public static final String RESOURCE_VERSION = SystemProperties.getProperty("resource.version", "1.0");
}
