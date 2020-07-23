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
public class BoschIoTSuiteOAuthProviderAuthCode implements IOAuthProvider {

  public static final String FAMILY_NAME = "family_name";
  public static final String GIVEN_NAME = "given_name";
  public static final String EMAIL = "email";

  private final BoschIoTSuiteOAuthProviderConfiguration configuration;

  private final String clientId;

  private static final String ID = "BOSCH-IOT-SUITE-AUTH-CODE";

  @Autowired
  public BoschIoTSuiteOAuthProviderAuthCode(
      @Value("${suite.oauth2.client.clientId}") String clientId,
      @Autowired BoschIoTSuiteOAuthProviderConfiguration suiteOAuthProviderConfiguration
  ) {
    this.clientId = Objects.requireNonNull(clientId);
    this.configuration = Objects.requireNonNull(suiteOAuthProviderConfiguration);
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public boolean canHandle(Authentication auth) {
    if (!(auth instanceof OAuth2Authentication)) {
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
    return null; // this method is never called
  }

  @SuppressWarnings("rawtypes")
  @Override
  public OAuthUser createUser(Authentication authentication) {
    OAuthUser user = new OAuthUser();
    user.setUserId(authentication.getName());
    user.setDisplayName(authentication.getName());

    if (authentication instanceof OAuth2Authentication) {
      Authentication userAuthentication = ((OAuth2Authentication)authentication).getUserAuthentication();
      if (userAuthentication.getDetails() != null && ((Map)userAuthentication.getDetails()).containsKey(EMAIL)) {
        Map<String, String> userDetails = ((Map<String, String>)userAuthentication.getDetails());
        if (userDetails.containsKey(EMAIL)) {
          String email = (String)((Map)userAuthentication.getDetails()).get(EMAIL);
          user.setEmail(email);
          user.setDisplayName(email);
        }

        if (userDetails.containsKey(FAMILY_NAME) && userDetails.containsKey(GIVEN_NAME)) {
          user.setDisplayName(userDetails.get(GIVEN_NAME) + " " + userDetails.get(FAMILY_NAME));
        }
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

