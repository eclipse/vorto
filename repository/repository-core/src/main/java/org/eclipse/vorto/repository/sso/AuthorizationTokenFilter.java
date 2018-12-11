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
package org.eclipse.vorto.repository.sso;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.filter.GenericFilterBean;

public class AuthorizationTokenFilter extends GenericFilterBean {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  private UserInfoTokenServices userInfoService;

  public AuthorizationTokenFilter(UserInfoTokenServices userInfoService) {
    this.userInfoService = userInfoService;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      Optional<String> authToken = FilterUtils.getBearerToken((HttpServletRequest) request);
      if (authToken.isPresent()) {
        try {
          Authentication authentication = userInfoService.loadAuthentication(authToken.get());
          if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
            SecurityContextHolder.getContext().setAuthentication(null);
            return;
          }
        } catch (InvalidTokenException e) {
          LOGGER.warn("Invalid token.", e);
          ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED,
              e.getMessage());
          return;
        } catch (Exception e) {
          LOGGER.warn("Server Error.", e);
          ((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
              e.getMessage());
          return;
        }
      }
    }

    chain.doFilter(request, response);
  }

}
