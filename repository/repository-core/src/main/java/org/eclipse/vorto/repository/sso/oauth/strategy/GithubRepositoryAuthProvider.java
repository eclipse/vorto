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
package org.eclipse.vorto.repository.sso.oauth.strategy;

import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.eclipse.vorto.repository.sso.oauth.ITokenVerificationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class GithubRepositoryAuthProvider implements ITokenVerificationProvider {

  @Autowired
  @Qualifier("githubUserInfoTokenServices")
  private UserInfoTokenServices githubTokenService;
  
  private String clientId;
  
  @Autowired
  public GithubRepositoryAuthProvider(@Value("${github.oauth2.client.clientId}") String clientId,
      @Autowired @Qualifier("githubUserInfoTokenServices") UserInfoTokenServices githubTokenService) {
    this.clientId = Objects.requireNonNull(clientId);
    this.githubTokenService = Objects.requireNonNull(githubTokenService);
  }

  @Override
  public String getId() {
    return "GITHUB";
  }

  @Override
  public OAuth2Authentication createAuthentication(HttpServletRequest request, String accessToken) {
    return githubTokenService.loadAuthentication(accessToken);
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
  public boolean verify(HttpServletRequest request, String jwtToken) {
    return canHandle(jwtToken);
  }
}

