package com.maumjido.springboot.template.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

public class PrettyLog implements Serializable {
  private static final ThreadLocal<PrettyLog> threadLocal = new ThreadLocal<>();

  public static PrettyLog newInstance(String id) {
    PrettyLog prettyLog = new PrettyLog(id);
    threadLocal.remove();
    threadLocal.set(prettyLog);
    return prettyLog;
  }

  public static PrettyLog getInstance() {
    return threadLocal.get();
  }

  // Logger elapsedLogger = LoggerFactory.getLogger("ELAPSED_TIME");
  private String title;
  /**
   * 
   */
  private static final long serialVersionUID = 2767581563457196867L;
  private final String PREFIX = "|";
  private Stack<StepLog> logStack = new Stack<StepLog>();
  private List<Message> messageList = new ArrayList<Message>();
  long elapsed;
  private boolean ignore;

  public PrettyLog(String id) {
    start(id);
  }

  public void start(String id) {
    start(id, null);
  }

  public void start(String id, String keyContains) {
    if (ignore)
      return;
    StepLog stepLog = new StepLog();
    stepLog.setId(id);
    stepLog.setStopWatch(System.currentTimeMillis());
    stepLog.setViewOneLine(logStack.isEmpty() ? false : logStack.peek().getViewOneLine());
    stepLog.setDepth(logStack.isEmpty() ? 1 : logStack.size() + 1);
    stepLog.setKeyContains(keyContains);
    logStack.push(stepLog);
    append(null, (stepLog.getDepth() > 1 ? " " : "") + "+---------------------------------------------------");
    append("START", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS").format(Calendar.getInstance(Locale.KOREA).getTime()));
    append("ID", id);
  }

  public void title(String title) {
    append("TITLE", title);
    if (logStack.size() == 1) {
      this.title = title;
    }
  }

  /**
   * PrettyLog 한줄로 출력.
   */
  public void setViewOneLine() {
    if (ignore)
      return;
    if (logStack.isEmpty() == false) {
      logStack.peek().setViewOneLine(true);
    }
  }

  public void append(String key, Object value) {
    if (ignore)
      return;
    StepLog stepLog = logStack.isEmpty() ? null : logStack.peek();
    if (value instanceof String) {
      String response = (String) value;
      if (response.length() > 2000) {
        response = response.substring(0, 1000) + " ::: SKIP";
      }
      value = response;
    }
    if (stepLog != null && !stepLog.isShow() && (stepLog.getKeyContains() == null || stepLog.getKeyContains().equals(key))) {
      stepLog.setShow(true);
    }
    messageList.add(new Message(key, String.valueOf(value), stepLog));
  }

  public void stop() {
    if (ignore)
      return;
    if (logStack.isEmpty() == false) {
      StepLog stepLog = logStack.peek();
      elapsed = System.currentTimeMillis() - stepLog.getStopWatch();
      messageList.add(new Message("ELAPSED", elapsed + " ms", stepLog));
      // elapsedLogger.info("{},{} ms", stepLog.getId(), elapsed);
      append(null, (stepLog.getDepth() > 1 ? " " : "") + "+---------------------------------------------------");
      logStack.pop();
    }
  }

  public long getElapsed() {
    if (ignore)
      return 0;
    return elapsed;
  }

  public String prettyPrint() {
    if (ignore)
      return null;
    StringBuilder rootLog = new StringBuilder("\n");
    Message preMessage = null;
    for (Message message : messageList) {
      StepLog stepLog = message.getStepLog();
      int depth = 0;
      Boolean isViewOneLine = false;
      if (stepLog != null) {
        depth = stepLog.getDepth();
        isViewOneLine = stepLog.getViewOneLine();
        if (!stepLog.isShow()) {
          continue;
        }
      }

      String key = message.getKey();
      String value = message.getValue();
      if (isViewOneLine) {
        if (key == null) {
          if (preMessage != null && preMessage.getStepLog() != null && preMessage.getStepLog().getViewOneLine()) {
            rootLog.append("\n");
          }
          prefix(rootLog, depth - 1);
          rootLog.append(value);
          rootLog.append("\n");
        } else {
          prefix(rootLog, depth);
          rootLog.append("(").append(key).append(")").append(value);
        }
      } else {
        int rpad = 20;
        if (key == null) {
          prefix(rootLog, depth - 1);
          rootLog.append(value);
        } else {
          prefix(rootLog, depth);
          rootLog.append(String.format("%-" + rpad + "s", key)).append(" : ").append(value);
        }
        rootLog.append("\n");
      }
      preMessage = message;
    }
    return rootLog.toString();
  }

  private void prefix(StringBuilder rootLog, int depth) {
    if (ignore)
      return;
    for (int i = 0; i < depth; i++) {
      if (depth > 1 && i > 0) {
        rootLog.append(" " + PREFIX);
      } else {
        rootLog.append(PREFIX);
      }
    }
  }

  public String getTitle() {
    return title;
  }

  class Message {
    private String key;
    private String value;
    private StepLog stepLog;

    public Message(String key, String value, StepLog stepLog) {
      this.key = key;
      this.value = value;
      this.stepLog = stepLog;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public StepLog getStepLog() {
      return stepLog;
    }

    public void setStepLog(StepLog stepLog) {
      this.stepLog = stepLog;
    }
  }

  class StepLog {
    private int depth;
    private Long stopWatch;
    private String id;
    private Boolean viewOneLine;
    private boolean isShow;
    private String keyContains;

    public Long getStopWatch() {
      return stopWatch;
    }

    public void setKeyContains(String keyContains) {
      this.keyContains = keyContains;
    }

    public String getKeyContains() {
      return keyContains;
    }

    public void setStopWatch(Long stopWatch) {
      this.stopWatch = stopWatch;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public Boolean getViewOneLine() {
      return viewOneLine;
    }

    public void setViewOneLine(Boolean viewOneLine) {
      this.viewOneLine = viewOneLine;
    }

    public int getDepth() {
      return depth;
    }

    public void setDepth(int depth) {
      this.depth = depth;
    }

    public Boolean isShow() {
      return isShow;
    }

    public void setShow(Boolean isShow) {
      this.isShow = isShow;
    }
  }

  public void ignore() {
    this.ignore = true;
  }
}