package com.maumjido.springboot.template.exception;

public class SessionExpired extends RuntimeException {

  private static final long serialVersionUID = -9218894624533726526L;
  private String url;

  public SessionExpired(String msg) {
    super(msg);
  }

  public SessionExpired(String msg, String url) {
    super(msg);
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
