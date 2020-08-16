
package com.maumjido.springboot.template.exception;

public class JsonBadRequestException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -9050430039918696237L;

  public JsonBadRequestException(String msg) {
    super(msg);
  }

  public JsonBadRequestException(Exception e) {
    super(e);
  }
}
