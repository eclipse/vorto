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
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LegacyTokenVerificationProvider extends HydraTokenVerificationProvider {
  
  private static final String VORTO = "vorto";
  private static final String RESOURCE_ACCESS = "resource_access";
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
  
  @Override
  @SuppressWarnings("unchecked")
  protected Map<Namespace, Integer> getScopes(JwtToken jwtToken) {
    Map<String, Object> resources = (Map<String, Object>) 
        Objects.requireNonNull(jwtToken.getPayloadMap().get(RESOURCE_ACCESS));
    return resources.entrySet().stream().map(this::resourceToScope)
        .filter(scope -> VORTO.equals(scope[0]))
        .collect(Collectors.toMap(this::toNamespace, this::toRank));
  }
  
  @SuppressWarnings("unchecked")
  private String[] resourceToScope(Map.Entry<String, Object> entry) {
    String[] resources = entry.getKey().split(":");
    
    if (resources == null || resources.length < 3) {
      throw new MalformedElement(entry.getKey());
    }
    
    Map<String, Object> permission = (Map<String, Object>) entry.getValue();
    if (permission == null) {
      throw new MalformedElement("No roles in " + entry.getKey());
    }
    
    Collection<String> roles = (Collection<String>) permission.get("roles");
    if (roles == null || roles.size() < 1) {
      throw new MalformedElement("No roles in " + entry.getKey());
    }
    
    return new String[] { resources[1], resources[2], roles.iterator().next()};
  }
}
