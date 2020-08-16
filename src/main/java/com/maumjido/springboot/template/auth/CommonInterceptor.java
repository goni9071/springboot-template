
package com.maumjido.springboot.template.auth;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.collect.Sets;
import com.maumjido.springboot.template.config.DefaultConstants;
import com.maumjido.springboot.template.util.FormatUtil;
import com.maumjido.springboot.template.util.IfUtil;
import com.maumjido.springboot.template.util.JsonResult;
import com.maumjido.springboot.template.util.JsonUtil;
import com.maumjido.springboot.template.util.PrettyLog;
import com.maumjido.springboot.template.util.RequestUtil;

public class CommonInterceptor extends HandlerInterceptorAdapter {

  private static Logger prettyLogger = LoggerFactory.getLogger("PRETTY_LOGGER");

  private static Set<String> loginFree = Sets.newHashSet();
  static {
    loginFree.add("/acct/login");
    loginFree.add("/error");
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse res, Object handler) throws ServletException, IOException {
    PrettyLog prettyLog = PrettyLog.newInstance(RequestUtil.getRequestURI(request));
    if (res.getStatus() != 200) {
      prettyLog.append("STATUS", res.getStatus() + "");
      prettyLog.append("HEADER", RequestUtil.getHeaderString(request));
      Object uri = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
      prettyLog.append("FORWARD_REQUEST_URI", uri);
      if (String.valueOf(uri).endsWith("js.map")) {
        prettyLog.ignore();
      }
    }

    if (request.getMethod().equals("POST")) {
      if (!SessionCsrf.getToken(request).equals(RequestUtil.getHeader(request, "X-CSRF-TOKEN")) && !SessionCsrf.getToken(request).equals(request.getParameter("_csrfToken"))) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      }
    }
    boolean result = true;
    prettyLog.append("LAYER", "CONTROLLER");
    prettyLog.append("TYPE", isJsonResult(handler) ? "JSON" : "HTML");

    request.setAttribute("formatUtil", new FormatUtil());
    request.setAttribute("ifUtil", new IfUtil());

    SessionUser sessionUser = SessionUser.getInstance(request);
    request.setAttribute("sessionUser", sessionUser);
    if (!loginFree.contains(request.getServletPath()) && sessionUser == null) {
      if (isJsonResult(handler)) {
        res.setStatus(403);
        result = false;
      } else {
        res.sendRedirect(DefaultConstants.CONTEXT_PATH + "/acct/login");
        result = false;
      }
    }
    if (!result) {
      prettyLog.stop();
      String prettyPrint = prettyLog.prettyPrint();
      if (prettyPrint != null) {
        prettyLogger.info(prettyPrint);
      }
    }
    return result;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    super.postHandle(request, response, handler, modelAndView);
    PrettyLog prettyLog = PrettyLog.getInstance();
    Map<String, Object> logMap = new HashMap<String, Object>();

    if (!isJsonResult(handler)) {
      if (modelAndView != null) {
        ModelMap modelMap = modelAndView.getModelMap();
        if (modelMap != null) {

          for (Entry<String, Object> entry : modelMap.entrySet()) {
            Object value = entry.getValue();
            if ((value instanceof Serializable) && value instanceof BindingResult == false) {
              logMap.put(entry.getKey(), value);
            }
          }

        }
        String viewName = modelAndView.getViewName();
        prettyLog.append("VIEW", viewName);
        if (viewName != null && !viewName.startsWith("redirect:")) {
          modelMap.addAttribute("_csrfToken", SessionCsrf.getToken(request));
        }
      }
      if (prettyLogger.isDebugEnabled() || response.getStatus() != HttpStatus.OK.value()) {
        prettyLog.append("RESPONSE", JsonUtil.toJson(logMap));
      }
    }
    prettyLog.stop();

    String prettyPrint = prettyLog.prettyPrint();
    if (prettyPrint != null) {
      prettyLogger.info(prettyPrint);
    }
    if (isJavascriptRequest(request)) {
      response.setContentType("application/javascript");
    }

  }

  private boolean isJsonResult(Object handler) {
    if (handler instanceof HandlerMethod) {
      HandlerMethod method = (HandlerMethod) handler;
      if (method.getMethod().getReturnType().equals(JsonResult.class)) {
        return true;
      }
    }
    return false;
  }

  private boolean isJavascriptRequest(HttpServletRequest req) {
    return req.getRequestURI().startsWith("/script");
  }
}
