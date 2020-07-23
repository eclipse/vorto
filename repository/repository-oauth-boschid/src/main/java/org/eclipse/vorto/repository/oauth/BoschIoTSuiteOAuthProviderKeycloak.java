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
import org.eclipse.vorto.repository.oauth.internal.JwtToken;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class BoschIoTSuiteOAuthProviderKeycloak extends BoschIoTSuiteOAuthProviderHydra {
  
  private static final String AZP = "azp";

  @Autowired
  public BoschIoTSuiteOAuthProviderKeycloak(
      @Value("${oauth2.verification.legacy.issuer: #{null}}") String legacyJwtIssuer,
      @Value("${oauth2.verification.legacy.publicKeyUri: #{null}}") String legacyPublicKeyUri,
      @Autowired DefaultUserAccountService userAccountService,
      @Autowired UserNamespaceRoleService userNamespaceRoleService) {
    super(legacyJwtIssuer, legacyPublicKeyUri, userAccountService, userNamespaceRoleService);
  }
  
  public BoschIoTSuiteOAuthProviderKeycloak(Supplier<Map<String, PublicKey>> publicKeySupplier,
      DefaultUserAccountService userAccountService, UserNamespaceRoleService userNamespaceRoleService) {
    super(publicKeySupplier, userAccountService, userNamespaceRoleService);
  }

  @Override
  protected Optional<String> getClientId(JwtToken token) {
    if (token.getPayloadMap().containsKey(AZP)) {
      return Optional.ofNullable((String) token.getPayloadMap().get(AZP));
    }
    return Optional.empty();
  }
}
