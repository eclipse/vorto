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

import java.util.Optional;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.utils.PreConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.base.Strings;

@Component
public class TenantUserService implements ITenantUserService {

  private ITenantService tenantService;

  private IUserAccountService userAccountService;

  public TenantUserService(@Autowired ITenantService tenantService,
      @Autowired IUserAccountService userAccountService) {
    this.tenantService = tenantService;
    this.userAccountService = userAccountService;
  }

  @Override
  public void addRolesToUser(String tenantId, String userId, Role... roles) {
    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      throw new IllegalArgumentException("tenantId should not be null or blank");
    }

    if (Strings.nullToEmpty(userId).trim().isEmpty()) {
      throw new IllegalArgumentException("userId should not be null or blank");
    }

    if (roles == null || roles.length < 1) {
      throw new IllegalArgumentException("Roles should not be empty.");
    }

    Tenant tenant = tenantService.getTenant(tenantId)
        .orElseThrow(() -> TenantDoesntExistException.missingTenant(tenantId));

    User user = userAccountService.getUser(userId);
    if (user != null) {
      user.addRoles(tenant, roles);
      userAccountService.saveUser(user);
    } else {
      throw new UserDoesntExistException(userId);
    }
  }
  
  public Optional<Tenant> getTenantOfUserAndNamespace(String userId, String namespace) {
    PreConditions.notNullOrEmpty(userId, "userId should not be null or blank");
    PreConditions.notNullOrEmpty(namespace, "namespace should not be null or blank");
    
    User user = userAccountService.getUser(userId);
    return user.getTenants().stream().filter(tenant -> tenant.owns(namespace)).findFirst();
  }

}
