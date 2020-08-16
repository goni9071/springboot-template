package com.maumjido.springboot.template.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemProperties {
  private static SystemProperties systemProperties = new SystemProperties();
  private static Logger logger = Logger.getLogger(SystemProperties.class.getName());

  private Properties properties = null;

  public static String getProperty(String key) {
    String property = systemProperties.properties.getProperty(key);
    if (property == null)
      throw new RuntimeException(key + " can not found.");
    return property;
  }

  public static String getProperty(String key, String defaultValue) {
    String property = systemProperties.properties.getProperty(key);
    if (property == null)
      return defaultValue;
    return property;
  }

  public SystemProperties() {
    properties = new Properties();
    InputStream is = SystemProperties.class.getResourceAsStream("/system.properties");
    try {
      properties.load(is);
    } catch (IOException e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
    }
  }

  public Properties getProperties() {
    return properties;
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

}
