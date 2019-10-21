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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.sso.oauth.strategy.CiamTokenVerificationProvider;
import org.eclipse.vorto.repository.sso.oauth.strategy.HydraTokenVerificationProvider;
import org.eclipse.vorto.repository.sso.oauth.strategy.KeycloakTokenVerificationProvider;
import org.eclipse.vorto.repository.sso.oauth.strategy.LegacyTokenVerificationProvider;
import org.eclipse.vorto.repository.sso.oauth.strategy.PublicKeyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.google.common.base.Strings;

@Component
public class TokenVerifier {

  private static final String ISSUER = "iss";

  @Value("${eidp.oauth2.client.clientId: #{null}}")
  private String ciamClientId;

  @Value("${oauth2.verification.eidp.issuer: #{null}}")
  private String ciamJwtIssuer;

  @Value("${oauth2.verification.eidp.publicKeyUri: #{null}}")
  private String ciamPublicKeyUri;
  
  @Value("${oauth2.verification.hydra.issuer: #{null}}")
  private String hydraJwtIssuer;

  @Value("${oauth2.verification.hydra.publicKeyUri: #{null}}")
  private String hydraPublicKeyUri;
  
  @Value("${oauth2.verification.legacy.issuer: #{null}}")
  private String legacyJwtIssuer;

  @Value("${oauth2.verification.legacy.publicKeyUri: #{null}}")
  private String legacyPublicKeyUri;

  @Value("${oauth2.verification.keycloak.issuer: #{null}}")
  private String keycloakJwtIssuer;

  @Value("${oauth2.verification.keycloak.publicKeyUri: #{null}}")
  private String keycloakPublicKeyUri;

  @Value("${oauth2.verification.keycloak.resource.client_id: #{null}}")
  private String resourceClientId;

  @Autowired
  private IUserAccountService userAccountService;
  
  @Autowired
  @Qualifier("githubUserInfoTokenServices")
  private UserInfoTokenServices githubTokenService;
  
  private Map<String, TokenVerificationProvider> tokenVerificationProviders = new HashMap<>();
  
  public TokenVerifier() {
  }

  private Optional<TokenVerificationProvider> getVerificationProvider(JwtToken jwtToken) {
    String issuer = (String) jwtToken.getPayloadMap().get(ISSUER);
    if (issuer != null) {
      return Optional.ofNullable(tokenVerificationProviders.get(issuer));
    }

    return Optional.empty();
  }

  @PostConstruct
  public void init() {
    if (ciamJwtIssuer != null) {
      tokenVerificationProviders.put(ciamJwtIssuer, new CiamTokenVerificationProvider(
          PublicKeyHelper.supplier(new RestTemplate(), ciamPublicKeyUri), 
          userAccountService, ciamClientId));
    }

    if (keycloakJwtIssuer != null) {
      tokenVerificationProviders.put(keycloakJwtIssuer, new KeycloakTokenVerificationProvider(
          PublicKeyHelper.supplier(new RestTemplate(), keycloakPublicKeyUri), 
          userAccountService, ciamClientId, resourceClientId));
    }
    
    if (hydraJwtIssuer != null) {
      tokenVerificationProviders.put(hydraJwtIssuer, 
          new HydraTokenVerificationProvider(PublicKeyHelper.supplier(new RestTemplate(), hydraPublicKeyUri), 
              userAccountService));
    }
    
    if (legacyJwtIssuer != null) {
      tokenVerificationProviders.put(legacyJwtIssuer, 
          new LegacyTokenVerificationProvider(PublicKeyHelper.supplier(new RestTemplate(), legacyPublicKeyUri), 
              userAccountService));
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
