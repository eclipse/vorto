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
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.TenantUser;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.sso.oauth.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.google.common.collect.Sets;

@Component
public class HydraTokenVerificationProvider extends AbstractTokenVerificationProvider {

  private static final String RS256_ALG = "RS256";

  private static final String CLIENT_ID = "client_id";

  private static final String READ_ACCESS = "read-access";

  private static final String FULL_ACCESS = "full-access";

  private static final String VORTO_SERVICE = "vorto";

  private static final String SCOPE = "scp";

  private static final int READONLY = 0;
  
  private static final int FULLACCESS = 1;
  
  private String hydraJwtIssuer;
  
  @Autowired
  public HydraTokenVerificationProvider(
      @Value("${oauth2.verification.hydra.issuer: #{null}}") String hydraJwtIssuer,
      @Value("${oauth2.verification.hydra.publicKeyUri: #{null}}") String hydraPublicKeyUri,
      @Autowired IUserAccountService userAccountService) {
    super(PublicKeyHelper.supplier(new RestTemplate(), hydraPublicKeyUri), userAccountService);
    this.hydraJwtIssuer = hydraJwtIssuer;
  }
  
  public HydraTokenVerificationProvider(Supplier<Map<String, PublicKey>> publicKeySupplier, 
      IUserAccountService userAccountService) {
    super(publicKeySupplier, userAccountService);
  }
  
  @Override
  public String getIssuer() {
    return hydraJwtIssuer; 
  }

  @Override
  public OAuth2Authentication createAuthentication(HttpServletRequest httpRequest, JwtToken jwtToken) {
    TenantUser user = getTechnicalUserForRequest(jwtToken, httpRequest);
    
    OAuth2Authentication auth = createAuthentication(user.getUser().getUsername(), user.getUser().getUsername(), 
        user.getUser().getUsername(), null, getRoles(user));
    
    return auth;
  }

  private Set<Role> getRoles(TenantUser user) {
    return user.getRoles().stream().map(userRole -> userRole.getRole()).collect(Collectors.toSet());
  }
  
  private TenantUser getTechnicalUserForRequest(JwtToken jwtToken, HttpServletRequest httpRequest) {
    Optional<Resource> resource = resource(httpRequest);
    if (resource.isPresent()) {
      Optional<Scope> scopeToUse = getScopes(jwtToken).stream()
          .filter(scope -> scopeApplies(scope.namespace,resource.get())).findFirst();
      if (scopeToUse.isPresent()) {
         
      }
    }
    
    return null;
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
     
    if (!verifyTechnicalUserExist(jwtToken)) {
      return false;
    }
    
    return allowAccess(httpRequest, resource(httpRequest), jwtToken);
  }

  private boolean verifyTechnicalUserExist(JwtToken jwtToken) {
    return getScopes(jwtToken).stream()
        .allMatch(scope -> userAccountService.exists(scope.technicalUser));
  }
  
  private boolean verifyAlgorithm(JwtToken jwtToken) {
    return jwtToken.getHeaderMap().get("alg").equals(RS256_ALG);
  }
  
  private boolean allowAccess(HttpServletRequest httpRequest, Optional<Resource> resource, JwtToken jwtToken) {
    return httpRequest.getMethod().equals("GET") && 
        (!resource.isPresent() || hasPermissionForResource(jwtToken, resource.get()));
  }
  
  private boolean hasPermissionForResource(JwtToken jwtToken, Resource resource) {
    return getScopes(jwtToken).stream()
        .anyMatch(scope -> scopeApplies(scope.namespace, resource) && 
            equalOrBetterPermission(scope.rank, READONLY));
  }

  private boolean scopeApplies(Namespace namespace, Resource resource) {
    if (resource.getType() == Resource.Type.ModelId) {
      return namespace.owns(ModelId.fromPrettyFormat(resource.getName()));
    } else {
      return namespace.owns(resource.getName());
    } 
  } 
  
  private boolean equalOrBetterPermission(int scope, int requestedScope) {
    return requestedScope <= scope;
  }

  @SuppressWarnings("unchecked")
  protected Collection<Scope> getScopes(JwtToken jwtToken) {
    Collection<String> scopes = (Collection<String>) Objects.requireNonNull(jwtToken.getPayloadMap().get(SCOPE));
    return scopes.stream()
      .map(this::toScope)
      .filter(scope -> VORTO_SERVICE.equals(scope.serviceName))
      .collect(Collectors.toList());
  }
  
  // parses string of the form "service:[serviceName]:[service]/[privilege]"
  private Scope toScope(String scope) {
    String[] scopeElements = scope.split(":");
    
    if (scopeElements.length != 3) {
      throw new MalformedElement(scope);
    } 
    
    String[] serviceElements = scopeElements[2].split("/");
    
    if (serviceElements.length != 2) {
      throw new MalformedElement(scope, scopeElements[2]);
    }
    
    return new Scope(scopeElements[1], serviceElements[0], serviceElements[1]);
  }
  
  protected class Scope {
    private static final String SEPARATOR = "_";
    
    String serviceName;
    String serviceInstanceId;
    String roleIdentifier;
    Namespace namespace;
    String technicalUser;
    int rank;
    
    public Scope(String serviceName, String serviceInstanceId, String roleIdentifier) {
      this.serviceName = serviceName;
      this.serviceInstanceId = serviceInstanceId;
      this.roleIdentifier = roleIdentifier;
      
      String[] nsAndUser = serviceInstanceId.split(SEPARATOR);
      if (nsAndUser.length < 2) {
        throw new MalformedElement(String.format("service:%s:%s/%s", serviceName, 
            serviceInstanceId, roleIdentifier));
      }
      
      this.namespace = Namespace.newNamespace(nsAndUser[0]);
      this.technicalUser = nsAndUser[1];
      this.rank = getRank();
    }
    
    private int getRank() {
      if (roleIdentifier.equals(FULL_ACCESS)) {
        return FULLACCESS;
      } else if (roleIdentifier.equals(READ_ACCESS)) {
        return READONLY;
      } else {
        throw new MalformedElement(String.format("service:%s:%s/%s", serviceName, 
            serviceInstanceId, roleIdentifier));
      }
    }
  }

  private Optional<Resource> resource(HttpServletRequest request) {
    Objects.requireNonNull(request.getRequestURI());
    String resourceUrl = request.getRequestURI().replace(request.getContextPath(), "");
    return ResourceIdentificationHelper.identifyResource(resourceUrl);
  }

  @Override
  protected Optional<String> getUserId(Map<String, Object> map) {
    return Optional.ofNullable((String) map.get(JWT_SUB));
  }
  
  protected Optional<String> getClientId(JwtToken token) {
    if (token.getPayloadMap().containsKey(CLIENT_ID)) {
      return Optional.ofNullable((String) token.getPayloadMap().get(CLIENT_ID));
    }
    
    return Optional.empty();
  }
}
