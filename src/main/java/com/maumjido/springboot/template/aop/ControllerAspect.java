
package com.maumjido.springboot.template.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.maumjido.springboot.template.config.Constants;
import com.maumjido.springboot.template.exception.JsonBadRequestException;
import com.maumjido.springboot.template.exception.MsgException;
import com.maumjido.springboot.template.util.JsonResult;
import com.maumjido.springboot.template.util.JsonUtil;
import com.maumjido.springboot.template.util.PrettyLog;
import com.maumjido.springboot.template.util.ValidateUtil;

@Aspect
public class ControllerAspect {
  private static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);
  private static Logger prettyLogger = LoggerFactory.getLogger(Constants.PRETTY_LOGGER_NAME);

  @Around("@within(org.springframework.stereotype.Controller) && (@annotation(org.springframework.web.bind.annotation.RequestMapping)"//
      + " || "//
      + "@annotation(org.springframework.web.bind.annotation.PostMapping)"//
      + " || "//
      + "@annotation(org.springframework.web.bind.annotation.GetMapping))"//
  )
  public Object commonController(ProceedingJoinPoint joinPoint) throws Throwable {
    // String methodName = joinPoint.getSignature().getName();

    PrettyLog prettyLog = PrettyLog.getInstance();
    Object retVal = null;
    Object[] args = joinPoint.getArgs();
    MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
    Method method = methodSignature.getMethod();
    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    assert args.length == parameterAnnotations.length;
    for (int i = 0; i < args.length; i++) {
      Object argument = args[i];
      if (argument instanceof MultipartFile) {
        MultipartFile uploadedFile = (MultipartFile) argument;
        if (!uploadedFile.isEmpty()) {
          String[] allowExt = { "jpg", "jpeg", "png", "gif", "bmp" };
          String msg = "이미지 파일만 업로드 가능합니다.";
          String fileName = uploadedFile.getOriginalFilename();
          boolean isOk = false;
          for (String ext : allowExt) {
            if (fileName.toLowerCase().endsWith(ext)) {
              isOk = true;
              break;
            }
          }
          try {
            ValidateUtil.isTrue(isOk, msg);
          } catch (MsgException e) {
            throw new JsonBadRequestException(e);
          }
        }
      }
    }

    try {
      retVal = joinPoint.proceed();
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
        prettyLog.append("ERROR", msg);
        prettyLog.stop();
        prettyLogger.info(prettyLog.prettyPrint());
      }

      if (method.getReturnType().isAssignableFrom(JsonResult.class)) {
        throw new JsonBadRequestException(e);
      } else {
        throw e;
      }
    }

    if (logger.isDebugEnabled()) {
      if (prettyLog != null && retVal instanceof JsonResult) {
        String response = JsonUtil.toJson(retVal);
        if (response.length() > 1000) {
          response = response.substring(0, 1000) + " ::: SKIP";
        }
        prettyLog.append("RESPONSE", response);
      }
    }
    return retVal;
  }
}
