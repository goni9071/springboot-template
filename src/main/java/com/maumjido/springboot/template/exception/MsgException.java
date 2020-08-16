
package com.maumjido.springboot.template.exception;

public class MsgException extends RuntimeException {

  private boolean needTrace;
  private String cause;
  /**
  * 
  */
  private static final long serialVersionUID = -9218894624533726526L;

  public MsgException(String msg, Exception e) {
    super(msg, e);
    setNeedTrace(true);
  }

  public MsgException(String msg) {
    super(msg);
  }

  public MsgException(String msg, String cause) {
    super(msg);
    this.cause = cause;
  }

  public boolean isNeedTrace() {
    return needTrace;
  }

  public void setNeedTrace(boolean needTrace) {
    this.needTrace = needTrace;
  }

  public String getCustomCause() {
    return cause;
  }

}
