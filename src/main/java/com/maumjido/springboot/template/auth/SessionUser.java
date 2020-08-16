
package com.maumjido.springboot.template.auth;

import javax.servlet.http.HttpServletRequest;

public abstract class SessionUser {
  public static SessionUser getInstance(HttpServletRequest request) {
    return (SessionUser) request.getSession().getAttribute(SessionUser.class.getName());
  }
}
