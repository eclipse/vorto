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
package org.eclipse.vorto.repository.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class GithubOAuthProviderConfiguration extends AbstractOAuthProviderConfiguration {

  private static final String LOGOUT_URL = "/logout";

  private String contextPath;

  public GithubOAuthProviderConfiguration(
      @Value("${github.oauth2.resource.userInfoUri}") String githubUserInfoEndpointUrl,
      @Value("${github.oauth2.client.clientId}") String githubClientId,
      @Value("${server.contextPath}") String contextPath) {
    super(new UserInfoTokenServices(githubUserInfoEndpointUrl, githubClientId));
    if (contextPath.endsWith("/")) {
      contextPath = contextPath.substring(0, contextPath.length() - 1);
    }
    this.contextPath = contextPath;
  }

  @Override
  public String getFilterProcessingUrl() {
    return "github/login";
  }

  @Override
  protected String getUserAttributeId() {
    return "login";
  }

  @Bean
  @ConfigurationProperties("github.oauth2.client")
  public AuthorizationCodeResourceDetails github() {
    return new AuthorizationCodeResourceDetails();
  }

  @Override
  protected AuthorizationCodeResourceDetails createDetails() {
    return github();
  }

  @Override
  public String getLogoutUrl(HttpServletRequest request) {
    return getBaseUrl(request) + contextPath + LOGOUT_URL;
  }

  private String getBaseUrl(HttpServletRequest request) {
    if (request.getRequestURI().equals("/") || request.getRequestURI().equals("")) {
      return request.getRequestURL().toString();
    } else {
      return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }
  }

  @Override
  public String getLogoHref() {
    return "webjars/repository-web/dist/images/github-social.png";
  }

  @Override
  protected String getProviderID() {
    return GithubRepositoryAuthProvider.ID;
  }
}
