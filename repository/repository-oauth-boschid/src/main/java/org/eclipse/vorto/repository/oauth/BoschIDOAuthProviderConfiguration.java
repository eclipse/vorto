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
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class BoschIDOAuthProviderConfiguration extends AbstractOAuthProviderConfiguration {

  @Value("${eidp.oauth2.resource.logoutEndpointUrl:#{null}}")
  private String logoutEndpointUrl;

  @Value("${eidp.oauth2.resource.logoutRedirectUrl:#{null}}")
  private String logoutRedirectUrl;
  
  public BoschIDOAuthProviderConfiguration(@Value("${eidp.oauth2.resource.userInfoUri}") String endpointUrl,
                                           @Value("${eidp.oauth2.client.clientId}") String clientId) {
    super(new UserInfoTokenServices(endpointUrl, clientId));
    this.tokenService.setPrincipalExtractor(new EidpPrincipalExtractor());
  }

  @Override
  public String getFilterProcessingUrl() {
    return "eidp/login";
  }

  @Override
  protected String getUserAttributeId() {
    return "sub";
  }
  
  @Override
  protected OAuth2RestTemplate createOAuthTemplate() {
    return new EidpOAuth2RestTemplate(eidp(), oauth2ClientContext);
  }
  
  @Bean
  @ConfigurationProperties("eidp.oauth2.client")
  public EidpResourceDetails eidp() {
    return new EidpResourceDetails();
  }

  @Override
  protected AuthorizationCodeResourceDetails createDetails() {
    return eidp();
  }

  @Override
  public String getLogoutUrl(HttpServletRequest request) {
    String idToken = (String) oauth2ClientContext.getAccessToken().getAdditionalInformation().get("id_token");
    return String.format("%s?id_token_hint=%s&post_logout_redirect_uri=%s", logoutEndpointUrl,
        idToken, logoutRedirectUrl);
  }

  @Override
  public String getLogoHref() {
    return "webjars/repository-web/dist/images/bosch-social.png";
  }

  @Override
  protected String getProviderID() {
    return BoschIDOAuthProvider.ID;
  }
}
