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
package org.eclipse.vorto.repository.web.tenant.dto;

import java.util.Set;
import java.util.stream.Collectors;

public class CreateTenantRequest {
  private String authenticationProvider;
  private String authorizationProvider;
  private String defaultNamespace;
  private Set<String> tenantAdmins;
  private Set<String> namespaces;

  public String getDefaultNamespace() {
    return defaultNamespace != null ? defaultNamespace.toLowerCase() : defaultNamespace;
  }

  public void setDefaultNamespace(String defaultNamespace) {
    this.defaultNamespace = defaultNamespace;
  }

  public Set<String> getTenantAdmins() {
    return tenantAdmins;
  }

  public void setTenantAdmins(Set<String> tenantAdmins) {
    this.tenantAdmins = tenantAdmins;
  }

  public Set<String> getNamespaces() {
    return namespaces != null ? namespaces.stream().map(n -> n.toLowerCase()).collect(Collectors.toSet()) : namespaces;
  }

  public void setNamespaces(Set<String> namespaces) {
    this.namespaces = namespaces;
  }

  public String getAuthenticationProvider() {
    return authenticationProvider;
  }

  public void setAuthenticationProvider(String authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
  }

  public String getAuthorizationProvider() {
    return authorizationProvider;
  }

  public void setAuthorizationProvider(String authorizationProvider) {
    this.authorizationProvider = authorizationProvider;
  }
}
