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

public class UserHasAccessToNamespaceValidation implements IModelValidator {

  private IUserAccountService userRepository;
  
  public UserHasAccessToNamespaceValidation(IUserAccountService userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public void validate(ModelInfo modelResource, InvocationContext context)
      throws ValidationException {
    
    User user = userRepository.getUser(context.getUserContext().getUsername());
    if (user.getTenants().stream().noneMatch(tenant -> tenant.owns(modelResource.getId()))) {
      System.out.println("-erle- : here!!!");
      throw new ValidationException("User isn't allowed to import model [" + modelResource.getId().getPrettyFormat() + "].", modelResource);
    }
  }

}
