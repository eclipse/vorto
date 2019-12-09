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
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class RepositoryAuthProviders {
  
  @Autowired
  private Collection<ITokenVerificationProvider> tokenVerificationProviders;
  
  public RepositoryAuthProviders() {
  }

  public Optional<ITokenVerificationProvider> getProviderFor(String jwtToken) {
    return tokenVerificationProviders.stream().filter(provider -> provider.canHandle(jwtToken)).findFirst();
  }
  
  public Optional<ITokenVerificationProvider> getProviderFor(Authentication auth) {
    return tokenVerificationProviders.stream().filter(authProvider -> authProvider.canHandle(auth)).findFirst();
  }
  
  public OAuth2Authentication verify(HttpServletRequest request, String accessToken)
      throws AuthenticationException, InvalidTokenException {

    if (accessToken == null) {
      throw new InvalidTokenException("The JWT token is empty.");
    }
      
    Optional<ITokenVerificationProvider> verificationProvider = getProviderFor(accessToken);
    if (verificationProvider.isPresent()) {
      ITokenVerificationProvider provider = verificationProvider.get();
      if (provider.verify(request, accessToken)) {
        return provider.createAuthentication(request, accessToken);
      }
    }
    
    throw new InvalidTokenException("No provider was able to verify this token.");
  }
}
