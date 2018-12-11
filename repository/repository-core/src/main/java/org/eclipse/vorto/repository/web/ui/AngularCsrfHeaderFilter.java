/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.web.ui;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

public class AngularCsrfHeaderFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    CsrfToken csrf = getCsrfToken(request);
    if (csrf != null) {
      Cookie cookie = getWebCookies(request);
      String token = csrf.getToken();
      if (cookie == null || token != null && !token.equals(cookie.getValue())) {
        addNewCookieToReponse(response, token);
      }
    }
    filterChain.doFilter(request, response);
  }

  private void addNewCookieToReponse(HttpServletResponse response, String token) {
    Cookie cookie;
    cookie = new Cookie("XSRF-TOKEN", token);
    cookie.setPath("/");
    response.addCookie(cookie);
  }

  private Cookie getWebCookies(HttpServletRequest request) {
    return WebUtils.getCookie(request, "XSRF-TOKEN");
  }

  private CsrfToken getCsrfToken(HttpServletRequest request) {
    return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
  }

}
