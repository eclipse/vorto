/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.oauth.internal.filter;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.vorto.repository.oauth.IOAuthProvider.OAuthAuthenticationException;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.oauth.internal.BearerTokenFilterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class BearerTokenFilter extends GenericFilterBean {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  private IOAuthProviderRegistry registery;

  public BearerTokenFilter(IOAuthProviderRegistry registry) {
    this.registery = registry;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      Optional<String> authToken = BearerTokenFilterUtils.getBearerToken((HttpServletRequest) request);
      if (authToken.isPresent()) {
        try {
          
          Authentication authentication = registery.getByToken(authToken.get()).authenticate((HttpServletRequest) request, authToken.get());
          SecurityContextHolder.getContext().setAuthentication(authentication);
          chain.doFilter(request, response);
          SecurityContextHolder.getContext().setAuthentication(null);
          return;
          
        } catch (OAuthAuthenticationException e) {
          LOGGER.warn("Could not authenticate bearer / jwt token", e);
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
