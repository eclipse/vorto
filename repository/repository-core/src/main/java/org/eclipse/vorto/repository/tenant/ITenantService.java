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

// TODO : Document interface
public interface ITenantService {

  boolean tenantExist(String tenantId);

  boolean namespaceExist(String namespace);
  
  Tenant createOrUpdateTenant(String tenantId, String defaultNamespace, Set<String> tenantAdmins,
      Optional<Set<String>> namespaces, Optional<String> authenticationProvider,
      Optional<String> authorizationProvider, IUserContext userContext);

  Optional<Tenant> getTenant(String tenantId);
  
  Optional<Tenant> getTenantFromNamespace(String namespace);
  
  Collection<Tenant> getTenants();

  boolean updateTenantNamespaces(String tenantId, Set<String> namespaces);

  boolean addNamespacesToTenant(String tenantId, Set<String> namespaces);
  
  boolean deleteTenant(String tenantId, IUserContext userContext);
}
