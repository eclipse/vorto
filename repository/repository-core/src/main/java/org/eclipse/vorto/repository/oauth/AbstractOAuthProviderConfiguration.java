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

import org.eclipse.vorto.repository.web.listeners.AuthenticationSuccessHandler;
import org.eclipse.vorto.repository.web.security.UserDBAuthoritiesExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import javax.servlet.Filter;

public abstract class AbstractOAuthProviderConfiguration implements IOAuthFlowConfiguration {

  @Autowired
  protected AuthenticationSuccessHandler successHandler;

  @Autowired
  protected AccessTokenProvider accessTokenProvider;

  @Autowired
  protected OAuth2ClientContext oauth2ClientContext;

  protected UserInfoTokenServices tokenService;

  public AbstractOAuthProviderConfiguration(UserInfoTokenServices tokenService) {
    this.tokenService = tokenService;
    this.tokenService.setAuthoritiesExtractor(new UserDBAuthoritiesExtractor(getUserAttributeId(), getProviderID()));
  }

  @Override
  public UserInfoTokenServices getUserInfoTokenService() {
    return this.tokenService;
  }

  @Override
  public Filter createFilter() {
    OAuth2RestTemplate restTemplate = createOAuthTemplate();

    restTemplate.setAccessTokenProvider(accessTokenProvider);

    OAuth2ClientAuthenticationProcessingFilter filter =
        new OAuth2ClientAuthenticationProcessingFilter("/"+getFilterProcessingUrl());
    filter.setAuthenticationSuccessHandler(successHandler);
    tokenService.setRestTemplate(restTemplate);
    tokenService.setAuthoritiesExtractor(authoritiesExtractor(getUserAttributeId(), getProviderID()));
    filter.setRestTemplate(restTemplate);
    filter.setTokenServices(tokenService);

    return filter;
  }

  protected OAuth2RestTemplate createOAuthTemplate() {
    return new OAuth2RestTemplate(createDetails(), oauth2ClientContext);
  }
  
  protected abstract AuthorizationCodeResourceDetails createDetails();

  @Override
  public abstract String getFilterProcessingUrl();

  protected abstract String getUserAttributeId();

  protected abstract String getProviderID();
  
  @Bean
  @Scope("prototype")
  public AuthoritiesExtractor authoritiesExtractor(String userAttributeId, String providerID) {
    return new UserDBAuthoritiesExtractor(userAttributeId, providerID);
  }
}
