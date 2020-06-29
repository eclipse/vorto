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
package org.eclipse.vorto.repository.web.security;

import java.io.Serializable;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class HasPermissionEvaluator implements PermissionEvaluator {

  private IModelRepositoryFactory repositoryFactory;

  private NamespaceService namespaceService;

  private UserNamespaceRoleService userNamespaceRoleService;

  public HasPermissionEvaluator(
      @Autowired IModelRepositoryFactory repositoryFactory,
      @Autowired UserNamespaceRoleService userNamespaceRoleService,
      @Autowired NamespaceService namespaceService) {
    this.repositoryFactory = repositoryFactory;
    this.namespaceService = namespaceService;
    this.userNamespaceRoleService = userNamespaceRoleService;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object targetPermission) {
    final String username = authentication.getName();

    if (targetDomainObject instanceof ModelId) {
      if (targetPermission instanceof String) {
        try {
          ModelId modelId = (ModelId) targetDomainObject;

          String workspaceId = namespaceService
              .resolveWorkspaceIdForNamespace(modelId.getNamespace())
              .orElseThrow(() -> new ModelNotFoundException(
                  "Model '" + modelId.getPrettyFormat() + "' can't be found in any workspace."));

          String permission = (String) targetPermission;
          ModelInfo modelInfo = repositoryFactory.getRepository(workspaceId, authentication)
              .getById(modelId);
          if (modelInfo != null) {
            if ("model:delete".equalsIgnoreCase(permission)) {
              return modelInfo.getAuthor().equalsIgnoreCase(username);
            } else if ("model:get".equalsIgnoreCase(permission)) {
              return modelInfo.getState().equals(SimpleWorkflowModel.STATE_RELEASED.getName())
                  || modelInfo.getState().equals(SimpleWorkflowModel.STATE_DEPRECATED.getName())
                  || modelInfo.getAuthor().equals(username);
            } else if ("model:owner".equalsIgnoreCase(permission)) {
              return modelInfo.getAuthor().equals(username);
            }
          }
        } catch (NotAuthorizedException ex) {
          return false;
        }
      } else if (targetPermission instanceof Permission) {
        ModelId modelId = (ModelId) targetDomainObject;
        Permission permission = (Permission) targetPermission;
        String workspaceId = namespaceService.resolveWorkspaceIdForNamespace(modelId.getNamespace())
            .orElseThrow(() -> new ModelNotFoundException(
                "The workspace for '" + modelId.getPrettyFormat() + "' could not be found."));

        return repositoryFactory.getPolicyManager(workspaceId, authentication)
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

    if ("ModelId".equals(targetType)) {
      final ModelId modelId = ModelId.fromPrettyFormat((String) targetId);
      try {
        return userNamespaceRoleService
            .hasRole(authentication.getName(), modelId.getNamespace(), role);
      } catch (DoesNotExistException dnee) {
        return false;
      }
    }

    return false;
  }
}
