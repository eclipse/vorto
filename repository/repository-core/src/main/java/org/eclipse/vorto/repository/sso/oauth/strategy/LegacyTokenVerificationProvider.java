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
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LegacyTokenVerificationProvider extends HydraTokenVerificationProvider {
  
  private static final String AZP = "azp";

  @Autowired
  public LegacyTokenVerificationProvider(
      @Value("${oauth2.verification.legacy.issuer: #{null}}") String legacyJwtIssuer,
      @Value("${oauth2.verification.legacy.publicKeyUri: #{null}}") String legacyPublicKeyUri,
      @Autowired IUserAccountService userAccountService) {
    super(legacyJwtIssuer, legacyPublicKeyUri, userAccountService);
  }
  
  public LegacyTokenVerificationProvider(Supplier<Map<String, PublicKey>> publicKeySupplier, 
      IUserAccountService userAccountService) {
    super(publicKeySupplier, userAccountService);
  }
  
  @Override
  protected Optional<String> getClientId(JwtToken token) {
    if (token.getPayloadMap().containsKey(AZP)) {
      return Optional.ofNullable((String) token.getPayloadMap().get(AZP));
    }
    
    return Optional.empty();
  }
}
