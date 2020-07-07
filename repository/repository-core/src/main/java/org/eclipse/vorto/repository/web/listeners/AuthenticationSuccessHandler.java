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
package org.eclipse.vorto.repository.web.listeners;

import com.google.common.base.Strings;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Autowired
  private UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    handle(request, response, authentication);
    clearAuthenticationAttributes(request);
  }

  protected void handle(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    OAuth2Authentication auth = (OAuth2Authentication) authentication;

    Optional<User> user = Optional.ofNullable(userRepository.findByUsername(auth.getName()));
    String redirectURL = buildRedirectURL(request);
    String targetUrl = user.map(e -> redirectURL).orElse("/#/signup");

    if (response.isCommitted()) {
      logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
      return;
    }

    redirectStrategy.sendRedirect(request, response, targetUrl);
  }

  /**
   * Fetches previous location from request parameter if any present, otherwise defaults to /#/.
   * If the request parameter represents an absolute URI, it has been likely tampered with and
   * should be discarded in favor of the home /#/ for security reasons.
   * As the expected relative path will start with "/", the default hash will be prepended to it.
   * @param request
   * @return redirect URL - if the redirect URL equals /#/login the return value will be /#/ instead
   * to avoid redirecting to the same login page again. 
   */
  private String buildRedirectURL(HttpServletRequest request) {
    String redirectURL = Optional
        // param can be absent
        .ofNullable(request.getParameter("redirect"))
        // validating value if present
        .map(u -> {
          // absolute URL or empty param value: ignoring
          if (Strings.isNullOrEmpty(u) || !u.startsWith("/")) {
            return "/#/";
          }
          // building relative path prepended by hash
          return "/#".concat(u);
        }).orElse("/#/");

    if ("/#/login".equals(redirectURL)) {
      return "/#/";
    }

    return redirectURL;
  }

  public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
    this.redirectStrategy = redirectStrategy;
  }

  protected RedirectStrategy getRedirectStrategy() {
    return redirectStrategy;
  }
}
