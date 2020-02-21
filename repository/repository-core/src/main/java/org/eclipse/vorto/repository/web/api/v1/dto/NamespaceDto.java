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
package org.eclipse.vorto.repository.web.api.v1.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.TenantHasNoNamespaceException;

public class NamespaceDto {
  private String name;
  private Collection<Collaborator> collaborators;
  private Collection<Collaborator> admins;

  public static NamespaceDto fromTenant(Tenant tenant) {
    return new NamespaceDto(getNamespaceName(tenant), getCollaborators(tenant), getAdmins(tenant));
  }

  /**
   * Converts the tenant admins user list to collaborators
   * @param tenant
   * @return
   */
  private static Collection<Collaborator> getAdmins(Tenant tenant) {
    return tenant.getTenantAdmins().stream().map(Collaborator::fromUser).collect(Collectors.toList());
  }

  private static Collection<Collaborator> getCollaborators(Tenant tenant) {
    return tenant.getUsers().stream().map(Collaborator::fromTenantUser).collect(Collectors.toList());
  }

  private static String getNamespaceName(Tenant tenant) {
    if (tenant.getNamespaces().size() <= 0) {
      throw new TenantHasNoNamespaceException(tenant.getTenantId());
    }
    
    // we are expected to only have 1 namespace per tenant
    return tenant.getNamespaces().iterator().next().getName();
  }
  
  public NamespaceDto(String name, Collection<Collaborator> collaborators, Collection<Collaborator> admins) {
    this.name = name;
    this.collaborators = collaborators;
    this.admins = admins;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<Collaborator> getCollaborators() {
    return collaborators;
  }

  public void setCollaborators(Collection<Collaborator> collaborators) {
    this.collaborators = collaborators;
  }

  public Collection<Collaborator> getAdmins() {
    return this.admins;
  }

  public void setAdmins(Collection<Collaborator> admins) {
    this.admins = admins;
  }
}
