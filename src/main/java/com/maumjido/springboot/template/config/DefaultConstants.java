
package com.maumjido.springboot.template.config;

import com.maumjido.springboot.template.util.SystemProperties;

public class DefaultConstants {
  public static final String PRETTY_LOGGER_NAME = "PRETTY_LOG";
  public static final int DEFAULT_PAGE_NUMBER = Integer.parseInt(SystemProperties.getProperty("default.page.number", "0"));
  public static final int DEFAULT_PAGE_SIZE = Integer.parseInt(SystemProperties.getProperty("default.page.size", "10"));
  public static final String CONTEXT_PATH = SystemProperties.getProperty("context.path", "");
  public static final String PROFILE = SystemProperties.getProperty("profile", "");
  public static final String RESOURCE_VERSION = SystemProperties.getProperty("resource.version", "1.0");
}
