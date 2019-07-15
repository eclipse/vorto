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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.web.client.RestTemplate;

public class CiamUserStrategy extends AbstractVerifyAndIdStrategy {

  protected static final String JWT_CLIENT_ID = "client_id";
  private String ciamClientId;

  public CiamUserStrategy(RestTemplate restTemplate, String publicKeyUri,
      IUserAccountService userAccountService, String clientId) {
    super(restTemplate, publicKeyUri, userAccountService);
    this.ciamClientId = Objects.requireNonNull(clientId);
  }

  /**
   * Authenticates the user from the CIAM issued token by checking if the user is registered in the
   * Repository
   */
  @Override
  public OAuth2Authentication createAuthentication(JwtToken accessToken) {
    OAuth2Request request =
        new OAuth2Request(null, this.ciamClientId, null, true, null, null, null, null, null);

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

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        name.orElse(userId), "N/A", SpringUserUtils.toAuthorityList(user.getAllRoles()));

    Map<String, String> detailsMap = new HashMap<String, String>();
    detailsMap.put(JWT_SUB, userId);
    detailsMap.put(JWT_NAME, name.orElse(userId));
    detailsMap.put(JWT_EMAIL, email.orElse(null));
    authToken.setDetails(detailsMap);

    return new OAuth2Authentication(request, authToken);
  }

  protected Optional<String> getUserId(Map<String, Object> map) {
    Optional<String> userId = Optional.ofNullable((String) map.get(JWT_SUB));
    if (!userId.isPresent()) {
      return Optional.ofNullable((String) map.get(JWT_CLIENT_ID));
    }

    return userId;
  }
}
