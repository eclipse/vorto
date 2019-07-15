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

import java.util.Optional;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IModelRetrievalService;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.impl.InvocationContext;
import org.eclipse.vorto.repository.tenant.ITenantService;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class DuplicateModelValidation implements IModelValidator {

  private IModelRepositoryFactory modelRepoFactory;
  
  private ITenantService tenantService;

  public DuplicateModelValidation(IModelRepositoryFactory modelRepoFactory, 
      ITenantService tenantService) {
    this.modelRepoFactory = modelRepoFactory;
    this.tenantService = tenantService;
  }

  @Override
  public void validate(ModelInfo modelResource, InvocationContext context)
      throws ValidationException {
    Optional<String> tenant = tenantService.getTenantFromNamespace(modelResource.getId().getNamespace())
        .map(tn -> tn.getTenantId());
    
    if (tenant.isPresent()) {
      IModelPolicyManager policyManager = modelRepoFactory.getPolicyManager(tenant.get(), context.getUserContext().getAuthentication());
      IModelRetrievalService modelRetrievalService =
          modelRepoFactory.getModelRetrievalService(context.getUserContext().getAuthentication());
      if (modelRetrievalService.getModel(modelResource.getId()).isPresent() && (!context.getUserContext().isSysAdmin()
          && !policyManager.hasPermission(modelResource.getId(), Permission.MODIFY))) {
        throw new ValidationException("Model already exists", modelResource);
      }
    }
  }
}
