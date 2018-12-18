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
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class HasPermissionEvaluator implements PermissionEvaluator {

  @Autowired
  private IModelRepository repository;

  public HasPermissionEvaluator(IModelRepository repository) {
    this.repository = repository;
  }

  public HasPermissionEvaluator() {}

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    final String callerId = authentication.getName();

    if (targetDomainObject instanceof ModelId) {
      try {
      ModelInfo modelInfo = this.repository.getById((ModelId) targetDomainObject);
      if (modelInfo != null) {
        if ("model:delete".equalsIgnoreCase((String) permission)) {
          // TODO : Checking for hashedUsername is legacy and needs to be removed once full
          // migration has taken place
          return modelInfo.getAuthor()
              .equalsIgnoreCase(UserContext.user(callerId).getHashedUsername())
              || modelInfo.getAuthor().equalsIgnoreCase(UserContext.user(callerId).getUsername());
        } else if ("model:get".equalsIgnoreCase((String) permission)) {
          IUserContext user = UserContext.user(authentication.getName());
          return modelInfo.getState().equals(SimpleWorkflowModel.STATE_RELEASED.getName())
              || modelInfo.getState().equals(SimpleWorkflowModel.STATE_DEPRECATED.getName()) ||
              // TODO : Checking for hashedUsername is legacy and needs to be removed once full
              // migration has taken place
              modelInfo.getAuthor().equals(user.getHashedUsername())
              || modelInfo.getAuthor().equals(user.getUsername());
        } else if ("model:owner".equalsIgnoreCase((String) permission)) {
          IUserContext user = UserContext.user(authentication.getName());
          // TODO : Checking for hashedUsername is legacy and needs to be removed once full
          // migration has taken place
          return modelInfo.getAuthor().equals(user.getHashedUsername())
              || modelInfo.getAuthor().equals(user.getUsername());
        }

      }
      } catch(NotAuthorizedException ex) {
        return false;
      }
    } else if (targetDomainObject instanceof String) {
      return callerId.equalsIgnoreCase((String) targetDomainObject);
    }
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    return false;
  }

}
