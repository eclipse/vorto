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

import org.eclipse.vorto.repository.oauth.internal.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class BoschIoTSuiteOAuthProviderV3 implements IOAuthProvider {

  @Autowired
  private BoschIoTSuiteOAuthProviderConfiguration configuration;

  private String clientId;

  private static final String ID = "BOSCH-IOT-SUITE-AUTH-CODE";

  @Autowired
  public BoschIoTSuiteOAuthProviderV3(@Value("${suite.oauth2.client.clientId}") String clientId, BoschIoTSuiteOAuthProviderConfiguration suiteOAuthProviderConfiguration) {
    this.clientId = Objects.requireNonNull(clientId);
    this.configuration = Objects.requireNonNull(suiteOAuthProviderConfiguration);
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public boolean canHandle(Authentication auth) {
    if (auth == null || !(auth instanceof OAuth2Authentication)) {
      return false;
    }
    
    OAuth2Authentication oauth2Auth = (OAuth2Authentication) auth;
    
    if (oauth2Auth.getOAuth2Request() == null) {
      return false;
    }
    
    return clientId.equals(oauth2Auth.getOAuth2Request().getClientId());
  }

  @Override
  public boolean canHandle(String jwtToken) {
    Optional<JwtToken> maybeJwtToken = JwtToken.instance(jwtToken);
    return jwtToken != null && jwtToken.trim().length() > 0 && !maybeJwtToken.isPresent();
  }

  @Override
  public Authentication authenticate(HttpServletRequest request, String jwtToken) {
    return configuration.getUserInfoTokenService().loadAuthentication(jwtToken);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public OAuthUser createUser(Authentication authentication) {
    OAuthUser user = new OAuthUser();
    user.setUserId(authentication.getName());
    user.setDisplayName(authentication.getName());

    if (authentication instanceof OAuth2Authentication) {
      Authentication userAuthentication = ((OAuth2Authentication)authentication).getUserAuthentication();
      if (userAuthentication.getDetails() != null && ((Map)userAuthentication.getDetails()).containsKey("email")) {
        user.setEmail((String)((Map)userAuthentication.getDetails()).get("email"));
      }
    }
    
    Set<String> roles = new HashSet<>();
    authentication.getAuthorities().forEach(e -> roles.add(e.getAuthority()));
    user.setRoles(roles);
    
    return user;
  }

  @Override
  public boolean supportsWebflow() {
    return true;
  }

  @Override
  public Optional<IOAuthFlowConfiguration> getWebflowConfiguration() {
    return Optional.of(this.configuration);
  }

  @Override
  public String getLabel() {
    return "Bosch IoT SuiteAuth";
  }
}

