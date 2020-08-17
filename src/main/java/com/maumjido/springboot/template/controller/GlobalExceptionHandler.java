package com.maumjido.springboot.template.controller;

import java.nio.file.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maumjido.springboot.template.config.DefaultConstants;
import com.maumjido.springboot.template.exception.BadRequestException;
import com.maumjido.springboot.template.exception.JsonBadRequestException;
import com.maumjido.springboot.template.exception.MsgException;
import com.maumjido.springboot.template.exception.SessionExpired;
import com.maumjido.springboot.template.util.JsonResult;

@ControllerAdvice
public class GlobalExceptionHandler {

  Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MultipartException.class)
  @ResponseStatus(HttpStatus.OK)
  public HttpEntity<String> handler(MultipartException e, HttpServletRequest request) {
    logError(e, request);
    String message = "파일은 1MB 이하로 업로드 가능합니다.";
    String contextPath = request.getContextPath();
    return new HttpEntity<String>(getJsonBody(message, contextPath), getJsonHeaders());
  }

  @ExceptionHandler(TypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handler(TypeMismatchException e, HttpServletRequest request) {
    return getModelAndView("잘못된 접근 입니다.");
  }

  @ExceptionHandler(FileSizeLimitExceededException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handler(FileSizeLimitExceededException e, HttpServletRequest request) {
    return getModelAndView("파일크기 " + e.getPermittedSize() + "bytes 제한 입니다.");
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handler(MissingServletRequestParameterException e, HttpServletRequest request) {
    logError(e, request);
    return getModelAndView("잘못된 접근 입니다.");
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ModelAndView handler(AccessDeniedException e, HttpServletRequest request) {
    logError(e, request);
    return getModelAndView("잘못된 접근 입니다.");
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ModelAndView handler(MaxUploadSizeExceededException e, HttpServletRequest request) {
    logError(e, request);
    return getModelAndView("잘못된 접근 입니다.");
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseStatus(HttpStatus.OK)
  public ModelAndView handler(BadRequestException e, HttpServletRequest request) {
    logError(e, request);
    return getModelAndView("잘못된 접근 입니다.");
  }

  @ExceptionHandler(MsgException.class)
  @ResponseStatus(HttpStatus.OK)
  public ModelAndView handler(MsgException e, HttpServletRequest request) {
    logError(e, request);
    return getModelAndView(e.getMessage());
  }

  @ExceptionHandler(JsonBadRequestException.class)
  public HttpEntity<String> handler(JsonBadRequestException e, HttpServletRequest request, HttpServletResponse response) {
    logError(e, request);
    String message = "잘못된 접근 입니다.";
    String url = null;

    response.setStatus(200);
    if (e != null && e.getCause() instanceof MsgException) {
      message = e.getCause().getMessage();
    } else if (e != null && e.getCause() instanceof SessionExpired) {
      message = ((SessionExpired) e.getCause()).getMessage();
      url = ((SessionExpired) e.getCause()).getUrl();
      response.setStatus(403);
    } else if (e != null && e.getCause() != null && e.getCause().getCause() instanceof MsgException) {
      message = e.getCause().getCause().getMessage();
    } else {
      url = request.getContextPath();
    }
    return new HttpEntity<String>(getJsonBody(message, url), getJsonHeaders());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ModelAndView handler(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
    logError(e, request);
    return getModelAndView("잘못된 접근 입니다.");
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  public ModelAndView handler(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
    logError(e, request);
    return getModelAndView("잘못된 접근 입니다.");
  }

  @ExceptionHandler(SessionExpired.class)
  @ResponseStatus(HttpStatus.OK)
  public ModelAndView handler(SessionExpired e, HttpServletRequest request) {
    ModelAndView model = new ModelAndView();
    model.setViewName("/alertAndGo");
    model.addObject("message", e.getMessage());
    String url = e.getUrl();
    if (url == null) {
      model.addObject("url", DefaultConstants.CONTEXT_PATH + "/acct/login");
    } else {
      model.addObject("url", url);
    }
    return model;
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ModelAndView handler(Exception e, HttpServletRequest request) {
    logError(e, request);
    String message = "잘못된 접근 입니다.";
    if (e != null && e.getCause() instanceof MsgException) {
      message = e.getCause().getMessage();
    }
    return getModelAndView(message);
  }

  private void logError(Exception e, HttpServletRequest request) {
    String accept = request.getHeader("Accept");
    String contentType = request.getContentType();
    String method = request.getMethod();
    String uri = request.getRequestURI();
    String msg = String.format("uri:%s accept:%s content-type:%s method:%s", uri, accept, contentType, method);
    if (e != null && (e instanceof MsgException || e.getCause() instanceof MsgException)) {
    } else {
      logger.error(msg, e);
    }

  }

  private String getJsonBody(String message, String url) {
    JsonResult jsonResult = new JsonResult();
    jsonResult.setCode(JsonResult.Code.FAIL);
    jsonResult.setMsg(message);
    if (url != null) {
      jsonResult.setUrl(url);
    }
    String jsonBody = "{\"code\":\"FAIL\"}";
    try {
      jsonBody = new ObjectMapper().writeValueAsString(jsonResult);
    } catch (JsonProcessingException e) {
      logger.error("jsonBody Error", e);
    }
    return jsonBody;
  }

  private HttpHeaders getJsonHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
    return headers;
  }

  private ModelAndView getModelAndView(String message) {
    ModelAndView model = new ModelAndView();
    model.setViewName("/error");
    model.addObject("message", message);
    return model;
  }
}
