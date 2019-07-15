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
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
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

  private IModelRepositoryFactory repositoryFactory;
  
  private IUserAccountService accountService;

  private ITenantService tenantService;

  public HasPermissionEvaluator(@Autowired IModelRepositoryFactory repositoryFactory,
      @Autowired ITenantService tenantService, @Autowired IUserAccountService userAccountService) {
    this.repositoryFactory = repositoryFactory;
    this.tenantService = tenantService;
    this.accountService = userAccountService;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object targetPermission) {
    final String username = authentication.getName();

    if (targetDomainObject instanceof ModelId) {
      if (targetPermission instanceof String) {
        try {
          ModelId modelId = (ModelId) targetDomainObject;
          
          String tenant = tenantService.getTenantFromNamespace(modelId.getNamespace())
              .map(_tenant -> _tenant.getTenantId())
              .orElseThrow(() -> new ModelNotFoundException("Model '" + modelId.getPrettyFormat() + " can't be found in any tenant"));
              
          String permission = (String) targetPermission;
          ModelInfo modelInfo = repositoryFactory.getRepository(tenant, authentication)
              .getById(modelId);
          if (modelInfo != null) {
            if ("model:delete".equalsIgnoreCase((String) permission)) {
              return modelInfo.getAuthor().equalsIgnoreCase(username);
            } else if ("model:get".equalsIgnoreCase((String) permission)) {
              return modelInfo.getState().equals(SimpleWorkflowModel.STATE_RELEASED.getName())
                  || modelInfo.getState().equals(SimpleWorkflowModel.STATE_DEPRECATED.getName())
                  || modelInfo.getAuthor().equals(username);
            } else if ("model:owner".equalsIgnoreCase((String) permission)) {
              return modelInfo.getAuthor().equals(username);
            }
          }
        } catch (NotAuthorizedException ex) {
          return false;
        }
      } else if (targetPermission instanceof Permission) {
        ModelId modelId = (ModelId) targetDomainObject;
        Permission permission = (Permission) targetPermission;
        
        Tenant tenant = tenantService.getTenantFromNamespace(modelId.getNamespace()).orElseThrow(
            () -> new ModelNotFoundException("The tenant for '" + modelId.getPrettyFormat() + "' could not be found."));
        
        return repositoryFactory.getPolicyManager(tenant.getTenantId(), authentication)
            .hasPermission(modelId, permission);
      }
    } else if (targetDomainObject instanceof String) {
      return username.equalsIgnoreCase((String) targetDomainObject);
    }
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {

    final String role = (String) permission;
    
    if (targetType.equals(Tenant.class.getName())) {
      final String tenantId = (String) targetId;
      return accountService.hasRole(tenantId, authentication, role);
    } else if ("ModelId".equals(targetType)) {                                               
      final ModelId modelId = ModelId.fromPrettyFormat((String) targetId);                   
      Optional<Tenant> tenant = tenantService.getTenantFromNamespace(modelId.getNamespace());
      if (tenant.isPresent()) {                                                              
        return accountService.hasRole(tenant.get().getTenantId(), authentication, role);     
      } 
    }
    
    return false;  
  }
}
