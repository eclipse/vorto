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
package org.eclipse.vorto.repository.core.impl.validation;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.tenant.ITenantService;

public class UserHasAccessToNamespaceValidation implements IModelValidator {

  private IUserAccountService userRepository;
  private ITenantService tenantService;
  
  public UserHasAccessToNamespaceValidation(IUserAccountService userRepository, 
      ITenantService tenantService) {
    this.userRepository = userRepository;
    this.tenantService = tenantService;
  }

  @Override
  public void validate(ModelInfo modelResource, InvocationContext context)
      throws ValidationException {
    
    if (context.getUserContext().isSysAdmin()) {
      if (!tenantService.getTenantFromNamespace(modelResource.getId().getNamespace()).isPresent()) {
        throw new ValidationException("The target namespace '" + 
            modelResource.getId().getNamespace() + "' is currently not owned by anybody.", modelResource);
      }
      return;
    }
    
    User user = userRepository.getUser(context.getUserContext().getUsername());
    if (user.getTenants().stream().noneMatch(tenant -> tenant.owns(modelResource.getId()))) {
      throw new ValidationException("You do not own the target namespace '" + 
          modelResource.getId().getNamespace() + "'.", modelResource);
    }
  }

}
