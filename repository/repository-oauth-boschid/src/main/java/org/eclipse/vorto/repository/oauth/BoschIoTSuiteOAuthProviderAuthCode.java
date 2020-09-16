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

import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.oauth.internal.JwtToken;
import org.eclipse.vorto.repository.oauth.internal.PublicKeyHelper;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.*;
import java.util.function.Supplier;

import static org.eclipse.vorto.repository.oauth.BoschIDOAuthProvider.JWT_CLIENT_ID;

@Component
public class BoschIoTSuiteOAuthProviderAuthCode extends AbstractOAuthProvider {

  public static final String FAMILY_NAME = "family_name";
  public static final String GIVEN_NAME = "given_name";
  public static final String EMAIL = "email";
  private static final String RS256_ALG = "RS256";

  private final BoschIoTSuiteOAuthProviderConfiguration configuration;

  private final String clientId;

  private final DefaultUserAccountService userAccountService;

  private final UserNamespaceRoleService userNamespaceRoleService;


  private static final String ID = "BOSCH-IOT-SUITE-AUTH";

  @Autowired
  public BoschIoTSuiteOAuthProviderAuthCode(
      @Value("${suite.oauth2.client.clientId}") String clientId,
      @Value("${oauth2.verification.hydra.publicKeyUri: #{null}}") String hydraPublicKeyUri,
      @Autowired BoschIoTSuiteOAuthProviderConfiguration suiteOAuthProviderConfiguration,
      @Autowired DefaultUserAccountService userAccountService,
      @Autowired UserNamespaceRoleService userNamespaceRoleService
  ) {
    super(PublicKeyHelper.supplier(new RestTemplate(), hydraPublicKeyUri), userAccountService,
        userNamespaceRoleService);
    this.clientId = Objects.requireNonNull(clientId);
    this.configuration = Objects.requireNonNull(suiteOAuthProviderConfiguration);
    this.userAccountService = Objects.requireNonNull(userAccountService);
    this.userNamespaceRoleService = Objects.requireNonNull(userNamespaceRoleService);
  }

  public BoschIoTSuiteOAuthProviderAuthCode(
      String clientId,
      Supplier<Map<String, PublicKey>> publicKeySupplier,
      BoschIoTSuiteOAuthProviderConfiguration suiteOAuthProviderConfiguration,
      DefaultUserAccountService userAccountService,
      UserNamespaceRoleService userNamespaceRoleService
  ) {
    super(publicKeySupplier, userAccountService,
        userNamespaceRoleService);
    this.clientId = Objects.requireNonNull(clientId);
    this.configuration = Objects.requireNonNull(suiteOAuthProviderConfiguration);
    this.userAccountService = Objects.requireNonNull(userAccountService);
    this.userNamespaceRoleService = Objects.requireNonNull(userNamespaceRoleService);
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
    return jwtToken != null && jwtToken.trim().length() > 0 && maybeJwtToken.isPresent();
  }

  @Override
  protected Authentication createAuthentication(HttpServletRequest httpRequest,
      JwtToken accessToken) {
    return authenticate(httpRequest, accessToken.getJwtToken());
  }

  @Override
  public Authentication authenticate(HttpServletRequest request, String jwtToken) {
        return JwtToken.instance(jwtToken)
            .filter(token -> verify(request, token))
            .map(this::createAuthentication)
            .orElse(null);
  }

  @Override
  public boolean verify(HttpServletRequest httpRequest, JwtToken jwtToken) {
    if (!verifyAlgorithm(jwtToken)) {
      return false;
    }

    if (!super.verifyPublicKey(jwtToken)) {
      return false;
    }

    if (!super.verifyExpiry(jwtToken)) {
      return false;
    }

    return true;
  }

  private boolean verifyAlgorithm(JwtToken jwtToken) {
    return jwtToken.getHeaderMap().get("alg").equals(RS256_ALG);
  }

  private OAuth2Authentication createAuthentication(JwtToken accessToken) {
    Map<String, Object> tokenPayload = accessToken.getPayloadMap();

    Optional<String> email = Optional.ofNullable((String) tokenPayload.get(JWT_EMAIL));
    Optional<String> name =
        Optional.ofNullable((String) tokenPayload.get(JWT_NAME)).map(str -> str.split("@")[0]);

    String userId = getUserId(tokenPayload).orElseThrow(() -> new InvalidTokenException(
        "Cannot generate a userId from your provided token. Maybe 'sub' or 'client_id' is not present in JWT token?"));
    return Optional.ofNullable(userAccountService.getUser(userId))
        .map(user -> createAuthentication(this.clientId, userId, name.orElse(userId), email.orElse(null), userNamespaceRoleService.getRolesOnAllNamespaces(user)))
        .orElse(null);
  }

  @Override
  protected String getIssuer() {
    return ID;
  }

  @Override
  protected Optional<String> getUserId(Map<String, Object> map) {
    try {
      // service token with CIAM context
      String userId = (String) getFromMap(getFromMap(map,"ext"), "orig_id").get("sub");
      return Optional.ofNullable(userId);
    } catch (CiamIdNotFoundException e) {
      // technical user token without CIAM context
      Optional<String> userId = Optional.ofNullable((String) map.get(JWT_SUB));
      if (!userId.isPresent()) {
        return Optional.ofNullable((String) map.get(JWT_CLIENT_ID));
      }
      return userId;
    }
  }

  @Override
  protected OAuth2Authentication createAuthentication(String clientId, String userId, String name,
      String email, Set<IRole> roles) {
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        name, "N/A", SpringUserUtils.toAuthorityList(roles));

    Map<String, Object> detailsMap = new HashMap<>();
    detailsMap.put(JWT_SUB, userId);
    detailsMap.put(JWT_NAME, name);
    detailsMap.put(JWT_EMAIL, email);
    authToken.setDetails(detailsMap);

    OAuth2Request request =
        new OAuth2Request(null, clientId, null, true, null, null, null, null, null);

    return new OAuth2Authentication(request, authToken);
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

  private Map<String, Object> getFromMap(Map<String, Object> map, String key) throws CiamIdNotFoundException {
    if (map.containsKey(key)) {
      return (Map<String, Object>) map.get(key);
    }
    throw new CiamIdNotFoundException();
  }
}

