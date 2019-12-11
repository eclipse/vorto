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

import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;

public class TenantDto {
  private String tenantId;
  private Collection<String> admins;
  private String authorizationProvider;
  private String defaultNamespace;
  private Collection<String> namespaces;

  public static TenantDto fromTenant(Tenant tenant) {
    TenantDto dto = new TenantDto();
    dto.setTenantId(tenant.getTenantId());
    dto.setAdmins(tenant.getTenantAdmins().stream().map(User::getUsername)
        .collect(Collectors.toList()));
    dto.setAuthorizationProvider(tenant.getAuthorizationProvider().toString());
    dto.setDefaultNamespace(tenant.getDefaultNamespace());
    dto.setNamespaces(
        tenant.getNamespaces().stream().map(Namespace::getName).collect(Collectors.toList()));
    return dto;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public Collection<String> getAdmins() {
    return admins;
  }

  public void setAdmins(Collection<String> admins) {
    this.admins = admins;
  }

  public String getAuthorizationProvider() {
    return authorizationProvider;
  }

  public void setAuthorizationProvider(String authorizationProvider) {
    this.authorizationProvider = authorizationProvider;
  }

  public String getDefaultNamespace() {
    return defaultNamespace;
  }

  public void setDefaultNamespace(String defaultNamespace) {
    this.defaultNamespace = defaultNamespace;
  }

  public Collection<String> getNamespaces() {
    return namespaces;
  }

  public void setNamespaces(Collection<String> namespaces) {
    this.namespaces = namespaces;
  }
}
