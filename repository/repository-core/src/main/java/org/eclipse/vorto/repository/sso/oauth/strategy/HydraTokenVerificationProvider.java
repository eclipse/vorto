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
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class HydraTokenVerificationProvider extends AbstractTokenVerificationProvider {

  private static final int READONLY = 0;
  
  private static final int FULLACCESS = 1;
  
  public HydraTokenVerificationProvider(Supplier<Map<String, PublicKey>> publicKeySupplier,
      IUserAccountService userAccountService) {
    super(publicKeySupplier, userAccountService);
  }

  @Override
  public OAuth2Authentication createAuthentication(JwtToken jwtToken) {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public boolean verify(HttpServletRequest httpRequest, JwtToken jwtToken) {
    if (!jwtToken.getHeaderMap().get("alg").equals("RS256")) {
      System.out.println("here 1");
      return false;
    }
      
    
    if (!(jwtToken.getPayloadMap().get("iss").equals("https://access.bosch-iot-suite.com/v2") || 
          jwtToken.getPayloadMap().get("iss").equals("https://access.bosch-iot-suite.com/auth/realms/iot-suite"))) {
      System.out.println("here 2");
      return false;
    }
      
    
    if (!super.verify(httpRequest, jwtToken)) {
      System.out.println("here 3");
      return false;
    }
    
    return allowAccess(httpRequest, resource(httpRequest), jwtToken);
  }
  
  private boolean allowAccess(HttpServletRequest httpRequest, Optional<String> resource, JwtToken jwtToken) {
    return httpRequest.getMethod().equals("GET") && 
        (!resource.isPresent() ||
            getScopes(jwtToken).entrySet().stream()
              .anyMatch(scope -> scope.getKey().owns(ModelId.fromPrettyFormat(resource.get())) &&
                  equalOrBetter(scope.getValue(), READONLY)));
  }

  private boolean equalOrBetter(int scope, int requestedScope) {
    return requestedScope <= scope;
  }

  // returns a map of namespaces and their given privilege
  @SuppressWarnings("unchecked")
  private Map<Namespace, Integer> getScopes(JwtToken jwtToken) {
    Collection<String> scopes = (Collection<String>) Objects.requireNonNull(jwtToken.getPayloadMap().get("scp"));
    return scopes.stream()
      .map(this::toScope)
      .filter(scope -> "vorto".equals(scope[0]))
      .collect(Collectors.toMap(this::toNamespace, this::toRank));
  }
  
  // parses string of the form "service:[serviceName]:[service]/[privilege]"
  private String[] toScope(String scope) {
    String[] scopeElements = scope.split(":");
    
    if (scopeElements.length != 3) {
      throw new MalformedElement(scope);
    } 
    
    String[] serviceElements = scopeElements[2].split("/");
    
    if (serviceElements.length != 2) {
      throw new MalformedElement(scope, scopeElements[2]);
    }
    
    return new String[] { scopeElements[1], serviceElements[0], serviceElements[1] };
  }
  
  private Namespace toNamespace(String[] scope) {
    return Namespace.newNamespace(scope[1]);
  }
  
  private int toRank(String[] scope) {
    if (scope[2].equals("full-access")) {
      return FULLACCESS;
    } else if (scope[2].equals("read-access")) {
      return READONLY;
    } else {
      throw new MalformedElement(String.format("service:%s:%s/%s", (Object[]) scope), scope[2]);
    }
  }

  private Optional<String> resource(HttpServletRequest request) {
    Objects.requireNonNull(request.getRequestURI());
    String resourceUrl = request.getRequestURI().replace(request.getContextPath(), "");
    return ResourceIdentificationHelper.identifyResource(resourceUrl);
  }

  @Override
  protected Optional<String> getUserId(Map<String, Object> map) {
    Optional<String> userId = Optional.ofNullable((String) map.get(JWT_SUB));
    
    if (!userId.isPresent()) {
      return Optional.ofNullable((String) map.get("client_id"));
    }
    
    if (!userId.isPresent()) {
      return Optional.ofNullable((String) map.get("azp"));
    }

    return userId;
  }

}
