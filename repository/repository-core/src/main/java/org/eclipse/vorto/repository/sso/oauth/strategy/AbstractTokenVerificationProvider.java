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

import java.security.PublicKey;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.eclipse.vorto.repository.sso.oauth.TokenVerificationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

public abstract class AbstractTokenVerificationProvider implements TokenVerificationProvider {

  protected static final String JWT_EMAIL = "email";
  private static final String JWT_EXPIRY = "exp";
  protected static final String JWT_NAME = "name";
  protected static final String JWT_SUB = "sub";
  private static final String KEY_ID = "kid";

  private Supplier<Map<String, PublicKey>> publicKeySupplier;
  private Map<String, PublicKey> publicKeys = null;
  protected IUserAccountService userAccountService;

  public AbstractTokenVerificationProvider(Supplier<Map<String, PublicKey>> publicKeySupplier,
      IUserAccountService userAccountService) {
    this.userAccountService = Objects.requireNonNull(userAccountService);
    this.publicKeySupplier = Objects.requireNonNull(publicKeySupplier);
  }

  protected abstract Optional<String> getUserId(Map<String, Object> map);

  protected OAuth2Authentication createAuthentication(String ciamClientId, String userId, String name, 
      String email, Set<Role> roles) {
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        name, "N/A", SpringUserUtils.toAuthorityList(roles));

    Map<String, Object> detailsMap = new HashMap<String, Object>();
    detailsMap.put(JWT_SUB, userId);
    detailsMap.put(JWT_NAME, name);
    detailsMap.put(JWT_EMAIL, email);
    authToken.setDetails(detailsMap);
    
    OAuth2Request request =
        new OAuth2Request(null, ciamClientId, null, true, null, null, null, null, null);

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
  public boolean verify(HttpServletRequest httpRequest, JwtToken jwtToken) {
    if (!verifyPublicKey(jwtToken)) {
      return false;
    }

    if (!verifyExpiry(jwtToken)) {
      return false;
    }

    return verifyUserExist(jwtToken);
  }

}
