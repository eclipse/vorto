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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

/**
 * See: https://wiki.eclipse.org/OpenID
 *
 * Endpoint (URL's)
 *
 * Authorization endpoint: https://accounts.eclipse.org/oauth2/authorize
 *
 * Token endpoint: https://accounts.eclipse.org/oauth2/token
 *
 * User information endpoint: https://accounts.eclipse.org/oauth2/UserInfo
 */
@Configuration
public class EclipseOAuthProviderConfiguration extends AbstractOAuthProviderConfiguration {

  private static final String LOGOUT_URL = "/logout";

  @Value("#{servletContext.contextPath}")
  private String servletContextPath;


  public EclipseOAuthProviderConfiguration(
      @Value("${eclipse.oauth2.resource.userInfoUri}") String eclipseUserInfoEndpointUrl,
      @Value("${eclipse.oauth2.client.clientId}") String eclipseClientId) {
    super(new UserInfoTokenServices(eclipseUserInfoEndpointUrl, eclipseClientId));

  }

  @Override
  public String getFilterProcessingUrl() {
    return "eclipse/login";
  }

  @Override
  protected String getUserAttributeId() {
    return "login";
  }

  @Bean
  @ConfigurationProperties("eclipse.oauth2.client")
  public AuthorizationCodeResourceDetails eclipse() {
    return new AuthorizationCodeResourceDetails();
  }

  @Override
  protected AuthorizationCodeResourceDetails createDetails() {
    return eclipse();
  }

  @Override
  public String getLogoutUrl(HttpServletRequest request) {
    return getBaseUrl(request) + servletContextPath + LOGOUT_URL;
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
    return "webjars/repository-web/dist/images/eclipse.png";
  }

}
