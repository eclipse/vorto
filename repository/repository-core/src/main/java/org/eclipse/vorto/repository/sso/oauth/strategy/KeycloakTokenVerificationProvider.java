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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeycloakTokenVerificationProvider extends AbstractTokenVerificationProvider {

  private static final String RESOURCE_ACCESS = "resource_access";
  private static final String CLIENT_ID = "clientId";

  private String resourceClientId;

  private String ciamClientId;
  
  private String keycloakJwtIssuer;

  @Autowired
  public KeycloakTokenVerificationProvider(
      @Value("${oauth2.verification.keycloak.issuer: #{null}}") String keycloakJwtIssuer,
      @Value("${oauth2.verification.keycloak.publicKeyUri: #{null}}") String keycloakPublicKeyUri,
      @Value("${eidp.oauth2.client.clientId: #{null}}") String ciamClientId, 
      @Value("${oauth2.verification.keycloak.resource.client_id: #{null}}") String resourceClientId,
      @Autowired IUserAccountService userAccountService) {
    super(PublicKeyHelper.supplier(new RestTemplate(), keycloakPublicKeyUri), userAccountService);
    this.ciamClientId = ciamClientId;
    this.resourceClientId = resourceClientId;
    this.keycloakJwtIssuer = keycloakJwtIssuer;
  }
  
  @Override
  public String getIssuer() {
    return keycloakJwtIssuer;
  }

  /**
   * Authenticates the user from the Keycloak issued token by checking if the user is registered in
   * the Repository as well as reading the user's roles from the JWT Token
   */
  @SuppressWarnings("unchecked")
  @Override
  public OAuth2Authentication createAuthentication(HttpServletRequest httpRequest, JwtToken accessToken) {
    OAuth2Request request =
        new OAuth2Request(null, this.ciamClientId, null, true, null, null, null, null, null);

    Map<String, Object> map = accessToken.getPayloadMap();

    Optional<String> email = Optional.ofNullable((String) map.get(JWT_EMAIL));
    Optional<String> name =
        Optional.ofNullable((String) map.get(JWT_NAME)).map(str -> str.split("@")[0]);

    // FIXME: Use Spring Security Adapter for Keycloak to read roles from token and create
    // authentication object
    Optional<Map<String, Object>> resourceAccess =
        Optional.ofNullable((Map<String, Object>) map.get(RESOURCE_ACCESS));
    if (!resourceAccess.isPresent()) {
      throw new InvalidTokenException(
          "Keycloak issued token does not define a property 'resource_access' containing roles.");
    }
    Map<String, Object> client = (Map<String, Object>) resourceAccess.get().get(resourceClientId);
    List<String> roles = (List<String>) client.get("roles");

    String userId = getUserId(map).orElseThrow(() -> new InvalidTokenException(
        "Cannot generate a userId from your provided token. Maybe 'sub' or 'client_id' is not present in JWT token?"));
    User user = userAccountService.getUser(userId);

    if (user == null) {
      new InvalidTokenException("User from token is not a registered user in the repository!");
    }

    String userRole = "";
    UsernamePasswordAuthenticationToken authToken = null;
    if (roles != null) {
      for (String role : roles) {
        userRole = "ROLE_" + role.toUpperCase() + ",";
      }
      authToken = new UsernamePasswordAuthenticationToken(name.orElse(userId), "N/A",
          AuthorityUtils.commaSeparatedStringToAuthorityList(userRole));
    } else {
      authToken = new UsernamePasswordAuthenticationToken(name.orElse(userId), "N/A",
          SpringUserUtils.toAuthorityList(user.getAllRoles()));
    }

    Map<String, String> detailsMap = new HashMap<String, String>();
    detailsMap.put(JWT_SUB, userId);
    detailsMap.put(JWT_NAME, name.orElse(userId));
    detailsMap.put(JWT_EMAIL, email.orElse(null));
    authToken.setDetails(detailsMap);

    return new OAuth2Authentication(request, authToken);
  }


  @Override
  protected Optional<String> getUserId(Map<String, Object> map) {
    Optional<String> userId = Optional.ofNullable((String) map.get(CLIENT_ID));
    if (!userId.isPresent()) {
      return Optional.ofNullable((String) map.get(JWT_SUB));
    }

    return userId;
  }
}
