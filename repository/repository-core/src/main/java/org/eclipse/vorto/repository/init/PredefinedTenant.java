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
package org.eclipse.vorto.repository.init;

import java.util.List;

public class PredefinedTenant {
  private String tenantId;
  private String defaultNamespace;
  private List<String> namespaces;
  private String authenticationProvider;
  private String authorizationProvider;

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getDefaultNamespace() {
    return defaultNamespace;
  }

  public void setDefaultNamespace(String defaultNamespace) {
    this.defaultNamespace = defaultNamespace;
  }

  public List<String> getNamespaces() {
    return namespaces;
  }

  public void setNamespaces(List<String> namespaces) {
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

  @Override
  public String toString() {
    return "PredefinedTenant [tenantId=" + tenantId + ", defaultNamespace=" + defaultNamespace
        + ", namespaces=" + namespaces + ", authenticationProvider=" + authenticationProvider
        + ", authorizationProvider=" + authorizationProvider + "]";
  }
}
