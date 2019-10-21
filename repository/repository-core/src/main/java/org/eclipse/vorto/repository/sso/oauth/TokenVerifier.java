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
package org.eclipse.vorto.repository.sso.oauth;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import com.google.common.base.Strings;

@Component
public class TokenVerifier {

  private static final String ISSUER = "iss";
  
  @Autowired
  @Qualifier("githubUserInfoTokenServices")
  private UserInfoTokenServices githubTokenService;
  
  @Autowired
  private Collection<TokenVerificationProvider> tokenVerificationProviders;
  
  private Map<String, TokenVerificationProvider> tokenVerificationProviderMap = new HashMap<>();
  
  public TokenVerifier() {
  }

  private Optional<TokenVerificationProvider> getVerificationProvider(JwtToken jwtToken) {
    String issuer = (String) jwtToken.getPayloadMap().get(ISSUER);
    if (issuer != null) {
      return Optional.ofNullable(tokenVerificationProviderMap.get(issuer));
    }

    return Optional.empty();
  }

  @PostConstruct
  public void init() {
    for(TokenVerificationProvider tokenVerificationProvider : tokenVerificationProviders) {
      if (tokenVerificationProvider.getIssuer() != null) {
        tokenVerificationProviderMap.put(tokenVerificationProvider.getIssuer(), 
            tokenVerificationProvider);
      }
    }
  }
  
  public OAuth2Authentication verify(HttpServletRequest request, String accessToken)
      throws AuthenticationException, InvalidTokenException {

    if (accessToken == null) {
      throw new InvalidTokenException("The JWT token is empty.");
    }
    
    Optional<JwtToken> maybeJwtToken = JwtToken.instance(accessToken);

    if (maybeJwtToken.isPresent()) {
      JwtToken jwtToken = maybeJwtToken.get();
      Optional<TokenVerificationProvider> verificationProvider = getVerificationProvider(jwtToken);
      if (verificationProvider.isPresent()) {
        TokenVerificationProvider provider = verificationProvider.get();
        if (provider.verify(request, jwtToken)) {
          return provider.createAuthentication(request, jwtToken);
        }
      }
    } else if (!Strings.nullToEmpty(accessToken).trim().isEmpty()) {
      // we have a bearer token but it is not a JWT token, most likely is it a github token
      return githubTokenService.loadAuthentication(accessToken);
    }
    
    throw new InvalidTokenException("No provider was able to verify this token.");
  }
}
