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
package org.eclipse.vorto.repository.tenant;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.domain.Tenant;

public interface ITenantService {

  /**
   * Check if a tenant with the tenantId exist
   * @param tenantId the tenantId to check
   * @return 
   */
  boolean tenantExist(String tenantId);

  /**
   * Returns whether an existing namespace conflicts with a current namespace
   * @param namespace to check
   * @return
   */
  boolean conflictsWithExistingNamespace(String namespace);
  
  /**
   * Create or update a tenant
   * 
   * @param tenantId the tenantId of this tenant
   * @param defaultNamespace the default namespace of the tenant
   * @param tenantAdmins a set of userIds who will be given tenant admin rights for this tenant
   * @param namespaces a set of namespaces to be given to the tenant
   * @param authenticationProvider the authentication provider of this tenant (currently unused)
   * @param authorizationProvider the authorization provider for this tenant (currently unused)
   * @param userContext the user who executed this call (he will become the tenant owner)
   * @return the tenant
   */
  Tenant createOrUpdateTenant(String tenantId, String defaultNamespace, Set<String> tenantAdmins,
      Optional<Set<String>> namespaces, Optional<String> authenticationProvider,
      Optional<String> authorizationProvider, IUserContext userContext);

  /**
   * Returns the tenant given the tenantId
   * 
   * @param tenantId
   * @return
   */
  Optional<Tenant> getTenant(String tenantId);
  
  /**
   * Returns the tenant who owns the namespace
   * @param namespace
   * @return
   */
  Optional<Tenant> getTenantFromNamespace(String namespace);
  
  /**
   * Get all the tenants
   * @return
   */
  Collection<Tenant> getTenants();

  /**
   * Update the namespace for the tenant
   * @param tenantId
   * @param namespaces
   * @return
   */
  boolean updateTenantNamespaces(String tenantId, Set<String> namespaces);

  /**
   * Add namespaces to tenant
   * 
   * @param tenantId
   * @param namespaces
   * @return
   */
  boolean addNamespacesToTenant(String tenantId, Set<String> namespaces);
  
  /**
   * Deletes the tenant
   * 
   * @param tenant
   * @param userContext
   * @return
   */
  boolean deleteTenant(Tenant tenant, IUserContext userContext);
}
