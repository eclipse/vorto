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
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.domain.Role;
import org.springframework.security.core.Authentication;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class DuplicateModelValidation implements IModelValidator {

  private IModelRepositoryFactory modelRepoFactory;

  private IUserAccountService userRepository;

  public DuplicateModelValidation(IModelRepositoryFactory modelRepoFactory,
      IUserAccountService userRepo) {
    this.modelRepoFactory = modelRepoFactory;
    this.userRepository = userRepo;
  }

  @Override
  public void validate(ModelInfo modelResource, InvocationContext context)
      throws ValidationException {
    IModelPolicyManager policyManager = modelRepoFactory.getPolicyManager(context.getUserContext());
    IModelRetrievalService modelRetrievalService =
        modelRepoFactory.getModelRetrievalService(context.getUserContext());
    if (modelRetrievalService.getModel(modelResource.getId()).isPresent() && (!isAdmin(context)
        && !policyManager.hasPermission(modelResource.getId(), Permission.MODIFY))) {
      throw new ValidationException("Model already exists", modelResource);
    }
  }

  private boolean isAdmin(InvocationContext context) {
    assert (context != null);
    assert (context.getUserContext() != null);
    assert (context.getUserContext().getUsername() != null);
    assert (userRepository != null);

    Authentication authentication = context.getUserContext().getAuthentication();

    return authentication.getAuthorities().stream()
        .anyMatch(ga -> ga.getAuthority().equals(Role.rolePrefix + Role.SYS_ADMIN));
  }
}
