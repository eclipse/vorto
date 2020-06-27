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

import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CiamTokenVerificationProvider extends AbstractTokenVerificationProvider {

  private String ciamClientId;
  
  private String ciamJwtIssuer;
  
  protected static final String JWT_CLIENT_ID = "client_id";
  
  @Autowired
  public CiamTokenVerificationProvider(
      @Value("${oauth2.verification.eidp.publicKeyUri: #{null}}") String ciamPublicKeyUri,
      @Value("${oauth2.verification.eidp.issuer: #{null}}") String ciamJwtIssuer,
      @Value("${eidp.oauth2.client.clientId: #{null}}") String ciamClientId,
      @Autowired IUserAccountService userAccountService) {
    super(PublicKeyHelper.supplier(new RestTemplate(), ciamPublicKeyUri), userAccountService);
    this.ciamClientId = ciamClientId;
    this.ciamJwtIssuer = ciamJwtIssuer; 
  }
  
  @Override
  public String getIssuer() {
    return ciamJwtIssuer;
  }

  /**
   * Authenticates the user from the CIAM issued token by checking if the user is registered in the
   * Repository
   */
  @Override
  public OAuth2Authentication createAuthentication(HttpServletRequest httpRequest, JwtToken accessToken) {
    Map<String, Object> tokenPayload = accessToken.getPayloadMap();

    Optional<String> email = Optional.ofNullable((String) tokenPayload.get(JWT_EMAIL));
    Optional<String> name =
        Optional.ofNullable((String) tokenPayload.get(JWT_NAME)).map(str -> str.split("@")[0]);

    String userId = getUserId(tokenPayload).orElseThrow(() -> new InvalidTokenException(
        "Cannot generate a userId from your provided token. Maybe 'sub' or 'client_id' is not present in JWT token?"));
    User user = userAccountService.getUser(userId);

    if (user == null) {
      new InvalidTokenException("User from token is not a registered user in the repository!");
    }

    return createAuthentication(this.ciamClientId, userId, name.orElse(userId), email.orElse(null), user.getAllRoles()); 
  }

  protected Optional<String> getUserId(Map<String, Object> map) {
    Optional<String> userId = Optional.ofNullable((String) map.get(JWT_SUB));
    if (!userId.isPresent()) {
      return Optional.ofNullable((String) map.get(JWT_CLIENT_ID));
    }

    return userId;
  }
}
