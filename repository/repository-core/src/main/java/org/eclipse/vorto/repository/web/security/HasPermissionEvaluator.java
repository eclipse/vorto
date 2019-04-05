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
package org.eclipse.vorto.repository.web.security;

import java.io.Serializable;
import java.util.Optional;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class HasPermissionEvaluator implements PermissionEvaluator {

  private IModelRepository repository;
  
  private ITenantService tenantService;

  public HasPermissionEvaluator(@Autowired IModelRepository repository, @Autowired ITenantService tenantService) {
    this.repository = repository;
    this.tenantService = tenantService;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    final String username = authentication.getName();
    final String hashedUsername = UserContext.getHash(username);

    if (targetDomainObject instanceof ModelId) {
      try {
      ModelInfo modelInfo = this.repository.getById((ModelId) targetDomainObject);
      if (modelInfo != null) {
        if ("model:delete".equalsIgnoreCase((String) permission)) {
          // TODO : Checking for hashedUsername is legacy and needs to be removed once full
          // migration has taken place
          return modelInfo.getAuthor()
              .equalsIgnoreCase(hashedUsername)
              || modelInfo.getAuthor().equalsIgnoreCase(username);
        } else if ("model:get".equalsIgnoreCase((String) permission)) {
          return modelInfo.getState().equals(SimpleWorkflowModel.STATE_RELEASED.getName())
              || modelInfo.getState().equals(SimpleWorkflowModel.STATE_DEPRECATED.getName()) ||
              // TODO : Checking for hashedUsername is legacy and needs to be removed once full
              // migration has taken place
              modelInfo.getAuthor().equals(hashedUsername)
              || modelInfo.getAuthor().equals(username);
        } else if ("model:owner".equalsIgnoreCase((String) permission)) {
          // TODO : Checking for hashedUsername is legacy and needs to be removed once full
          // migration has taken place
          return modelInfo.getAuthor().equals(hashedUsername)
              || modelInfo.getAuthor().equals(username);
        } 
      }
      } catch(NotAuthorizedException ex) {
        return false;
      }
    } else if (targetDomainObject instanceof String) {
      return username.equalsIgnoreCase((String) targetDomainObject);
    }
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    
    if (targetType.equals(Tenant.class.getName())) {
      final String username = authentication.getName();
      final String role = (String) permission;
      final String tenantId = (String) targetId;
      
      Optional<Tenant> _tenant = tenantService.getTenant(tenantId);
      if (_tenant.isPresent()) {
        Tenant tenant = _tenant.get();
        return tenant.getUsers().stream().anyMatch(tenantUser ->
          tenantUser.getUser().getUsername().equals(username) && 
            tenantUser.hasRole(Role.valueOf(role.replace("ROLE_", "")))
        );
      }
    }
    
    return false;
  }

}
