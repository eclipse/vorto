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
package org.eclipse.vorto.repository.core.impl.validation;

import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;

public class UserHasAccessToNamespaceValidation implements IModelValidator {

  private DefaultUserAccountService userAccountService;
  private UserRepositoryRoleService userRepositoryRoleService;
  private UserNamespaceRoleService userNamespaceRoleService;

  public UserHasAccessToNamespaceValidation(DefaultUserAccountService userAccountService,
      UserRepositoryRoleService userRepositoryRoleService,
      UserNamespaceRoleService userNamespaceRoleService) {
    
    this.userAccountService = userAccountService;
    this.userRepositoryRoleService = userRepositoryRoleService;
    this.userNamespaceRoleService = userNamespaceRoleService;
  }

  @Override
  public void validate(ModelInfo modelResource, InvocationContext context)
      throws ValidationException {

    if (userRepositoryRoleService.isSysadmin(userAccountService.getUser(context.getUserContext().getUsername()))) {
      return;
    }
    
    User user = userAccountService.getUser(context.getUserContext().getUsername());
    try {
      if (userNamespaceRoleService.getNamespaces(user, user).stream().noneMatch(namespace -> namespace.owns(modelResource.getId()))) {
        throw new ValidationException("User " + user.getUsername() + " does not have access to target namespace "
            + modelResource.getId().getNamespace(), modelResource);
      }
    } catch (OperationForbiddenException | DoesNotExistException e) {
      throw new ValidationException("User " + user.getUsername() + " does not have access to target namespace "
          + modelResource.getId().getNamespace(), modelResource, e);
    }
  }

}
