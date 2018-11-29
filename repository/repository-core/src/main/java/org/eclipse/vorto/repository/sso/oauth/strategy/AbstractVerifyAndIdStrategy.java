/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.sso.oauth.strategy;

import java.security.PublicKey;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.eclipse.vorto.repository.sso.oauth.JwtVerifyAndIdStrategy;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractVerifyAndIdStrategy implements JwtVerifyAndIdStrategy {

  protected static final String JWT_EMAIL = "email";
  private static final String JWT_EXPIRY = "exp";
  protected static final String JWT_NAME = "name";
  protected static final String JWT_SUB = "sub";
  private static final String KEY_ID = "kid";

  private PublicKeyHelper publicKeyHelper;
  private Map<String, PublicKey> publicKeys = null;
  private String publicKeyUri = null;
  protected IUserAccountService userAccountService;

  public AbstractVerifyAndIdStrategy(RestTemplate restTemplate, String publicKeyUri,
      IUserAccountService userAccountService) {
    this.publicKeyHelper = PublicKeyHelper.instance(restTemplate);
    this.publicKeyUri = Objects.requireNonNull(publicKeyUri);
    this.userAccountService = Objects.requireNonNull(userAccountService);
  }

  protected abstract Optional<String> getUserId(Map<String, Object> map);

  @Override
  public boolean verify(JwtToken jwtToken) {
    if (publicKeys == null || publicKeys.isEmpty()) {
      publicKeys = publicKeyHelper.getPublicKey(publicKeyUri);
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

    if (VerificationHelper.verifyJwtToken(publicKey, jwtToken)) {
      Map<String, Object> payloadMap = jwtToken.getPayloadMap();
      if (payloadMap.containsKey(JWT_EXPIRY)) {
        Optional<Instant> expirationDate =
            Optional.ofNullable(Double.valueOf((double) payloadMap.get(JWT_EXPIRY)).longValue())
                .map(Instant::ofEpochSecond);
        if (expirationDate.isPresent() && expirationDate.get().isBefore(Instant.now())) {
          return false;
        }
      }

      String userId = getUserId(payloadMap).orElseThrow(() -> new InvalidTokenException(
          "Cannot generate a userId from your provided token. Maybe 'sub' or 'client_id' is not present in JWT token?"));

      return userAccountService.exists(userId);
    }

    return false;
  }

}
