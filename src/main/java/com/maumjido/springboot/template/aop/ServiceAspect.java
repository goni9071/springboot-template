
package com.maumjido.springboot.template.aop;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.springboot.template.exception.MsgException;
import com.maumjido.springboot.template.util.JsonUtil;
import com.maumjido.springboot.template.util.PrettyLog;

@Aspect
public class ServiceAspect {
  Logger logger = LoggerFactory.getLogger(ServiceAspect.class);

  @Around("@within(org.springframework.stereotype.Service)")
  public Object commonService(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    PrettyLog prettyLog = PrettyLog.getInstance();
    List<Object> args = new ArrayList<Object>();
    for (Object arg : joinPoint.getArgs()) {
      args.add(arg);
    }
    Object retVal = null;
    try {
      if (prettyLog != null) {
        prettyLog.start(String.format("%s,%s,%s", "SERVICE", joinPoint.getTarget().getClass().getSimpleName(), methodName));
        prettyLog.append("PARAM", JsonUtil.toJson(args));
      }
      retVal = joinPoint.proceed();
      return retVal;
    } catch (Exception e) {
      if (prettyLog != null) {
        String msg = "Null";

        if (e != null) {
          msg = e.getMessage();
          if (e instanceof MsgException) {
            String customCause = ((MsgException) e).getCustomCause();
            if (customCause != null) {
              msg += "(" + customCause + ")";
            }
          }
        }
        prettyLog.append("EXCEPTION", msg);
      }
      throw e;
    } finally {
      if (prettyLog != null) {
        if (logger.isDebugEnabled()) {
          prettyLog.append("RESPONSE", JsonUtil.toJson(retVal));
        }
        prettyLog.stop();
      }
    }
  }
}
