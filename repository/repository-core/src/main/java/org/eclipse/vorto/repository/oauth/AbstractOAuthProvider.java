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
package org.eclipse.vorto.repository.oauth;

import java.security.PublicKey;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.oauth.internal.JwtToken;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.eclipse.vorto.repository.oauth.internal.VerificationHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

public abstract class AbstractOAuthProvider implements IOAuthProvider {

  protected static final String JWT_EMAIL = "email";
  private static final String JWT_EXPIRY = "exp";
  protected static final String JWT_NAME = "name";
  protected static final String JWT_SUB = "sub";
  private static final String KEY_ID = "kid";
  private static final String ISSUER = "iss";

  private Supplier<Map<String, PublicKey>> publicKeySupplier;
  private Map<String, PublicKey> publicKeys = null;
  protected IUserAccountService userAccountService;

  public AbstractOAuthProvider(Supplier<Map<String, PublicKey>> publicKeySupplier,
      IUserAccountService userAccountService) {
    this.userAccountService = Objects.requireNonNull(userAccountService);
    this.publicKeySupplier = Objects.requireNonNull(publicKeySupplier);
  }

  protected abstract String getIssuer();
  
  protected abstract Optional<String> getUserId(Map<String, Object> map);

  @Override
  public boolean canHandle(String accessToken) {
    Optional<JwtToken> jwtToken = JwtToken.instance(accessToken);
    if (jwtToken.isPresent()) {
      String issuer = (String) jwtToken.get().getPayloadMap().get(ISSUER);
      return issuer.equals(getIssuer());
    }
    return false;
  }
  
  private Authentication createAuthentication(HttpServletRequest request, String accessToken) {
    Optional<JwtToken> jwtToken = JwtToken.instance(accessToken);
    if (jwtToken.isPresent()) {
      return createAuthentication(request, jwtToken.get());
    }
    return null;
  }
  
  protected abstract Authentication createAuthentication(HttpServletRequest httpRequest, JwtToken accessToken);

  protected OAuth2Authentication createAuthentication(String clientId, String userId, String name, 
      String email, Set<Role> roles) {
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        name, "N/A", SpringUserUtils.toAuthorityList(roles));

    Map<String, Object> detailsMap = new HashMap<String, Object>();
    detailsMap.put(JWT_SUB, userId);
    detailsMap.put(JWT_NAME, name);
    detailsMap.put(JWT_EMAIL, email);
    authToken.setDetails(detailsMap);
    
    OAuth2Request request =
        new OAuth2Request(null, clientId, null, true, null, null, null, null, null);

    return new OAuth2Authentication(request, authToken);
  }
  
  protected boolean verifyPublicKey(JwtToken jwtToken) {
    if (publicKeys == null || publicKeys.isEmpty()) {
      publicKeys = publicKeySupplier.get();
    }

    String keyId = (String) jwtToken.getHeaderMap().get(KEY_ID);
    if (keyId == null) {
      throw new InvalidTokenException(
          String.format("AccessToken '%s' doesn't have a kid in header", jwtToken.getJwtToken()));
    }

    PublicKey publicKey = publicKeys.get(keyId);
    if (publicKey == null) {
      throw new InvalidTokenException(
          String.format("There are no public keys with kid '%s'", keyId));
    }
    
    return VerificationHelper.verifyJwtToken(publicKey, jwtToken);
  }
  
  protected boolean verifyExpiry(JwtToken jwtToken) {
    Map<String, Object> payloadMap = jwtToken.getPayloadMap();
    if (payloadMap.containsKey(JWT_EXPIRY)) {
      Optional<Instant> expirationDate =
          Optional.ofNullable(Double.valueOf((double) payloadMap.get(JWT_EXPIRY)).longValue())
              .map(Instant::ofEpochSecond); 
      if (expirationDate.isPresent() && expirationDate.get().isBefore(Instant.now())) {
        return false;
      }
    }
    
    return true;
  }
  
  protected boolean verifyUserExist(JwtToken jwtToken) {
    String userId = getUserId(jwtToken.getPayloadMap()).orElseThrow(() -> new InvalidTokenException(
        "Cannot generate a userId from your provided token. Maybe 'sub' or 'client_id' is not present in JWT token?"));
    return userAccountService.exists(userId);
  }
  
  @Override
  public Authentication authenticate(HttpServletRequest httpRequest, String accessToken) throws OAuthAuthenticationException {
    Optional<JwtToken> maybeJwtToken = JwtToken.instance(accessToken);
    if (maybeJwtToken.isPresent()) {
      boolean isValid = verify(httpRequest, maybeJwtToken.get());
      if (!isValid) {
        return createAuthentication(httpRequest, accessToken);
      } else {
        throw new IOAuthProvider.OAuthAuthenticationException("Authentication failed");

      }
    } else {
      throw new IOAuthProvider.OAuthAuthenticationException(new InvalidTokenException("Specified token is invalid. Cannot authenticate"));
    }
  }
  
  
  private static final String KEY_EMAIL = "email";
  
  /**
   * Creates an OAuth user for the given authentication object
   * 
   * @param authentication
   * @return
   */
  @SuppressWarnings("rawtypes")
  public OAuthUser createUser(Authentication authentication) {
    OAuthUser user = new OAuthUser();
    user.setUserId(authentication.getName());
    user.setDisplayName(authentication.getName());

    if (authentication instanceof OAuth2Authentication) {
      Authentication userAuthentication = ((OAuth2Authentication)authentication).getUserAuthentication();
      if (userAuthentication.getDetails() != null && ((Map)userAuthentication.getDetails()).containsKey(KEY_EMAIL)) {
        user.setEmail((String)((Map)userAuthentication.getDetails()).get(KEY_EMAIL));
      }
    }
    
    Set<String> roles = new HashSet<>();
    authentication.getAuthorities().stream().forEach(e -> roles.add(e.getAuthority()));
    user.setRoles(roles);
    
    return user;
  }
  
  public boolean verify(HttpServletRequest httpRequest, JwtToken jwtToken) {
    if (!verifyPublicKey(jwtToken)) {
      return false;
    }

    if (!verifyExpiry(jwtToken)) {
      return false;
    }

    return verifyUserExist(jwtToken);
  }
  
  @Override
  public boolean supportsWebflow() {
    return false;
  }

  @Override
  public Optional<IOAuthFlowConfiguration> getWebflowConfiguration() {
    return Optional.empty();
  }
}
