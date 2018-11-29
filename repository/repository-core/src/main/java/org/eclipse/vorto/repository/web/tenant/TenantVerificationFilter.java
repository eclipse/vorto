/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.web.tenant;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.vorto.repository.sso.FilterUtils;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class TenantVerificationFilter extends GenericFilterBean {

  private static final String TENANT_ID = "tid";

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  private Pattern releasedApiPrefixPattern = Pattern.compile("/api/v([^/]+)/([^/]+)/");
  private Pattern unreleasedApiPrefixPattern = Pattern.compile("/rest/([^/]+)/");

  @Value("${server.config.singleTenantMode:#{true}}")
  private boolean singleTenantMode;

  @Autowired
  private OAuth2ClientContext oauthClientContext;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (!singleTenantMode && (request instanceof HttpServletRequest)) {
      HttpServletRequest httpRequest = (HttpServletRequest) request;
      Optional<String> resourceTenant = getTenantFromUrl(httpRequest.getRequestURL().toString());

      if (resourceTenant.isPresent()) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<String> userTenant = getTenantFromJwtToken(authentication, httpRequest);

        if (!(userTenant.isPresent() && resourceTenant.get().equals(userTenant.get()))) {
          String errorMessage = "ERROR: tenant in JWT token and tenant for resource doesn't match.";
          int error = HttpServletResponse.SC_FORBIDDEN;

          if (!userTenant.isPresent()) {
            errorMessage = "ERROR: Either no JWT token or no '" + TENANT_ID + "' in JWT token.";
            error = HttpServletResponse.SC_UNAUTHORIZED;
          }

          LOGGER.warn(errorMessage);
          ((HttpServletResponse) response).sendError(error);
          return;
        }
      }
    }

    chain.doFilter(request, response);
  }

  private Optional<String> getTenantFromUrl(String url) {
    Matcher matcher = releasedApiPrefixPattern.matcher(url);
    if (matcher.find()) {
      return Optional.ofNullable(matcher.group(2));
    }
    matcher = unreleasedApiPrefixPattern.matcher(url);
    if (matcher.find()) {
      return Optional.ofNullable(matcher.group(1));
    }
    return Optional.empty();
  }

  private Optional<String> getTenantFromJwtToken(Authentication authentication,
      HttpServletRequest request) {
    Optional<JwtToken> jwtToken = getJwtTokenFromAuthentication(authentication);
    if (!jwtToken.isPresent()) {
      jwtToken = FilterUtils.getBearerToken(request).flatMap(token -> JwtToken.instance(token));
    }

    return jwtToken.map(token -> (String) token.getPayloadMap().get(TENANT_ID));
  }

  private Optional<JwtToken> getJwtTokenFromAuthentication(Authentication authentication) {
    if (authentication != null && authentication instanceof OAuth2Authentication
        && oauthClientContext != null && oauthClientContext.getAccessToken() != null
        && oauthClientContext.getAccessToken().getAdditionalInformation() != null) {
      return JwtToken.instance(
          (String) oauthClientContext.getAccessToken().getAdditionalInformation().get("id_token"));
    }

    return Optional.empty();
  }

}
