/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.model.IBulkOperationsService;
import org.eclipse.vorto.repository.model.ModelNamespaceNotOfficialException;
import org.eclipse.vorto.repository.model.ModelNotReleasedException;
import org.eclipse.vorto.repository.model.RepositoryAccessException;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DefaultBulkOperationsService implements IBulkOperationsService {

  private static Logger logger = Logger.getLogger(DefaultBulkOperationsService.class);

  private IModelRepositoryFactory repositoryFactory;

  public DefaultBulkOperationsService(@Autowired IModelRepositoryFactory repositoryFactory,
      @Autowired ITenantService tenantService) {
    this.repositoryFactory = repositoryFactory;
  }

  public List<ModelId> makeModelPublic(ModelId modelId) {
    List<ModelId> accumulator = new ArrayList<>();

    try {

      makeModelPublicRecursively(modelId, accumulator);

    } catch (ModelNamespaceNotOfficialException | ModelNotReleasedException e) {
      revertToPrivate(accumulator);
      throw e;
    } catch (ExecutionException e) {
      revertToPrivate(accumulator);
      throw new RepositoryAccessException("Errors were thrown while accessing the repository.", e);
    }
    
    return accumulator;
  }

  private void makeModelPublicRecursively(ModelId modelId,
      List<ModelId> modelsMadePublicAcc) throws ExecutionException {

    if (hasPrivateNamespace(modelId)) {
      throw new ModelNamespaceNotOfficialException(modelId);
    }

    IModelRepository repository = repositoryFactory.getRepositoryByModel(modelId);
    ModelInfo modelInfo = repository.getById(modelId);

    if (!modelInfo.isReleased()) {
      throw new ModelNotReleasedException(modelId);
    }

    if (isPrivate(modelInfo)) {
      logger.info("Changing visibility of model " + modelId.getPrettyFormat() + " to public.");
      // Add public visibility property
      repository.updateVisibility(modelId, IModelRepository.VISIBILITY_PUBLIC);

      // Add corresponding policy to model
      IModelPolicyManager policyMgr = this.repositoryFactory
          .getPolicyManager(repository.getTenantId(), getAuthenticationToken());
      policyMgr.addPolicyEntry(modelId, PolicyEntry.of(IModelPolicyManager.ANONYMOUS_ACCESS_POLICY,
          PrincipalType.User, Permission.READ));

      modelsMadePublicAcc.add(modelId);

      // Make references public
      for (ModelId referencedModelId : modelInfo.getReferences()) {
        makeModelPublicRecursively(referencedModelId, modelsMadePublicAcc);
      }
    }
  }

  private boolean isPrivate(ModelInfo modelInfo) {
    return IModelRepository.VISIBILITY_PRIVATE.equals(modelInfo.getVisibility());
  }

  private boolean hasPrivateNamespace(ModelId modelId) {
    return modelId.getNamespace().startsWith(Namespace.PRIVATE_NAMESPACE_PREFIX);
  }

  private void revertToPrivate(List<ModelId> accumulator) {

    for (ModelId modelId : accumulator) {
      IModelRepository repo = this.repositoryFactory.getRepositoryByModel(modelId);
      String tenantId = repo.getTenantId();

      // changed back visibility property
      repo.updateVisibility(modelId, IModelRepository.VISIBILITY_PRIVATE);

      // remove added policy
      IModelPolicyManager policyManager =
          this.repositoryFactory.getPolicyManager(tenantId, getAuthenticationToken());
      Collection<PolicyEntry> policies = policyManager.getPolicyEntries(modelId);
      for (PolicyEntry policy : policies) {
        if (policy.getPrincipalId().equals(IModelPolicyManager.ANONYMOUS_ACCESS_POLICY)) {
          policyManager.removePolicyEntry(modelId, policy);
          break;
        }
      }
    }
  }

  protected Authentication getAuthenticationToken() {
    return SecurityContextHolder.getContext().getAuthentication();
  }
}
