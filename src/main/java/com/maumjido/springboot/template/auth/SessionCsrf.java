package com.maumjido.springboot.template.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.maumjido.springboot.template.util.SecurityUtil;

public class SessionCsrf {

  public static Object getToken(HttpServletRequest request) {
    HttpSession session = request.getSession();
    String csrfToken = (String) session.getAttribute(SessionCsrf.class.getName());
    if (csrfToken == null) {
      csrfToken = SecurityUtil.getRandomAlphaNumeric(16);
      session.setAttribute(SessionCsrf.class.getName(), csrfToken);
    }

    return csrfToken;
  }

}
