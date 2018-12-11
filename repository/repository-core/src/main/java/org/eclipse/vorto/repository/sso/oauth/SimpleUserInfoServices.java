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
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.sso.boschid.EidpResourceDetails;
import org.eclipse.vorto.repository.sso.oauth.strategy.CiamUserStrategy;
import org.eclipse.vorto.repository.sso.oauth.strategy.KeycloakUserStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("simpleUserInfoServices")
public class SimpleUserInfoServices extends UserInfoTokenServices {

  private static final String ISSUER = "iss";

  @Value("${eidp.oauth2.client.clientId: #{null}}")
  private String ciamClientId;

  @Value("${oauth2.verification.eidp.issuer: #{null}}")
  private String ciamJwtIssuer;

  @Value("${oauth2.verification.eidp.publicKeyUri: #{null}}")
  private String ciamPublicKeyUri;

  @Value("${oauth2.verification.keycloak.issuer: #{null}}")
  private String keycloakJwtIssuer;

  @Value("${oauth2.verification.keycloak.publicKeyUri: #{null}}")
  private String keycloakPublicKeyUri;

  @Value("${oauth2.verification.keycloak.resource.client_id: #{null}}")
  private String resource_client_id;

  @Autowired
  private IUserAccountService userAccountService;

  private Map<String, JwtVerifyAndIdStrategy> verifyAndIdStrategies = new HashMap<>();


  @Autowired
  public SimpleUserInfoServices(
      @Value("${eidp.oauth2.resource.userInfoUri}") String userInfoEndpointUrl,
      @Autowired EidpResourceDetails eidp) {
    super(userInfoEndpointUrl, eidp.getClientId());
  }

  private Optional<JwtVerifyAndIdStrategy> getStrategy(JwtToken jwtToken) {
    String issuer = (String) jwtToken.getPayloadMap().get(ISSUER);
    if (issuer != null) {
      return Optional.ofNullable(verifyAndIdStrategies.get(issuer));
    }

    return Optional.empty();
  }

  @PostConstruct
  public void init() {
    if (ciamJwtIssuer != null) {
      verifyAndIdStrategies.put(ciamJwtIssuer, new CiamUserStrategy(new RestTemplate(),
          ciamPublicKeyUri, userAccountService, ciamClientId));
    }

    if (keycloakJwtIssuer != null) {
      verifyAndIdStrategies.put(keycloakJwtIssuer, new KeycloakUserStrategy(new RestTemplate(),
          keycloakPublicKeyUri, userAccountService, ciamClientId, resource_client_id));
    }
  }

  @Override
  public OAuth2Authentication loadAuthentication(String accessToken)
      throws AuthenticationException, InvalidTokenException {

    JwtToken jwtToken = JwtToken.instance(accessToken).orElseThrow(() -> new InvalidTokenException(
        "The access token '" + accessToken + "' is not a proper JWT token."));

    Optional<JwtVerifyAndIdStrategy> strategyToUse = getStrategy(jwtToken);
    if (strategyToUse.isPresent()) {
      JwtVerifyAndIdStrategy strategy = strategyToUse.get();
      if (strategy.verify(jwtToken)) {
        return strategy.createAuthentication(jwtToken);
      }

      throw new InvalidTokenException("The JWT token '" + accessToken
          + "' cannot be verified. Either it is malformed, the user isn't registered, or it has already expired.");
    }
    if (accessToken != null) {
      return super.loadAuthentication(accessToken);
    }

    throw new InvalidTokenException(
        "No strategy for authenticating the JWT token. Tokens must have 'iss' whose issuers are configured in Thingtype.");
  }

  @Override
  public OAuth2AccessToken readAccessToken(String accessToken) {
    throw new UnsupportedOperationException("Not supported: read access token");
  }
}
