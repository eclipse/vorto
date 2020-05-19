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

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.internal.JwtToken;
import org.eclipse.vorto.repository.oauth.internal.MalformedElement;
import org.eclipse.vorto.repository.oauth.internal.PublicKeyHelper;
import org.eclipse.vorto.repository.oauth.internal.Resource;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.*;
import java.util.function.Supplier;

@Component
public class BoschIoTSuiteOAuthProviderV2 extends AbstractOAuthProvider {

  private static final String RS256_ALG = "RS256";

  private static final String CLIENT_ID = "client_id";
  
  private String hydraJwtIssuer;

  @Autowired
  public BoschIoTSuiteOAuthProviderV2(
          @Value("${oauth2.verification.hydra.issuer: #{null}}") String hydraJwtIssuer,
          @Value("${oauth2.verification.hydra.publicKeyUri: #{null}}") String hydraPublicKeyUri,
          @Autowired IUserAccountService userAccountService,
          @Autowired UserNamespaceRoleService userNamespaceRoleService) {
    super(PublicKeyHelper.supplier(new RestTemplate(), hydraPublicKeyUri), userAccountService, userNamespaceRoleService);
    this.hydraJwtIssuer = hydraJwtIssuer;
  }
  
  public BoschIoTSuiteOAuthProviderV2(Supplier<Map<String, PublicKey>> publicKeySupplier, 
      IUserAccountService userAccountService, UserNamespaceRoleService userNamespaceRoleService) {
    super(publicKeySupplier, userAccountService, userNamespaceRoleService);
  }
  
  @Override
  public String getId() {
    return "BOSCH-IOT-SUITE-AUTH";
  }

  @Override
  public boolean canHandle(Authentication auth) {
    return false;
  }

  @Override
  public String getIssuer() {
    return hydraJwtIssuer; 
  }
  
  @Override
  public OAuth2Authentication createAuthentication(HttpServletRequest httpRequest, JwtToken jwtToken) {
    return getTechnicalUser(jwtToken)
            .map(technicalUser -> createAuthentication(technicalUser.getUsername(), technicalUser.getUsername(),
                    technicalUser.getUsername(), null, getRolesForRequest(technicalUser, httpRequest)))
            .orElseThrow(() -> new MalformedElement("clientId in jwtToken isn't a registered technical user"));
  }

  private Set<IRole> getRolesForRequest(User user, HttpServletRequest httpRequest) {
    Optional<Resource> resource = resource(httpRequest);
    if (resource.isPresent()) {
      Optional<Namespace> ns = namespaceApplicableToResource(user, resource.get());
      if (ns.isPresent()) {
        try {
          return new HashSet<>(userNamespaceRoleService.getRoles(user, ns.get()));
        } catch (DoesNotExistException e) {
          // ignore this exception and continue
        }
      }
    }
    return userNamespaceRoleService.getRolesOnAllNamespaces(user);
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

    return getTechnicalUser(jwtToken)
            .map(user -> true)
            .orElseThrow(() -> new MalformedElement("clientId in jwtToken isn't a registered technical user"));
  }

  private Optional<User> getTechnicalUser(JwtToken jwtToken) {
    String technicalUserId = getClientId(jwtToken)
            .orElseThrow(() -> new MalformedElement("jwtToken doesn't have clientId"));
    return Optional.ofNullable(userAccountService.getUser(technicalUserId));
  }

  private boolean verifyAlgorithm(JwtToken jwtToken) {
    return jwtToken.getHeaderMap().get("alg").equals(RS256_ALG);
  }

  private Optional<Namespace> namespaceApplicableToResource(User user, Resource resource) {
    Collection<Tenant> tenants = userAccountService.getTenants(user);
    for (Tenant tenant : tenants) {
      for (Namespace ns : tenant.getNamespaces()) {
        if (scopeApplies(ns, resource)) {
          return Optional.of(ns);
        }
      }
    }
    return Optional.empty();
  }

  private boolean scopeApplies(Namespace namespace, Resource resource) {
    if (resource.getType() == Resource.Type.ModelId) {
      return namespace.owns(ModelId.fromPrettyFormat(resource.getName()));
    } else {
      return namespace.owns(resource.getName());
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

  @Override
  public String getLabel() {
    return "Bosch IoT Suite OAuth";
  }
}
