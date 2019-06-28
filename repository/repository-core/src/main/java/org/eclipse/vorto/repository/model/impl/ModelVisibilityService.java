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
package org.eclipse.vorto.repository.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.core.TenantNotFoundException;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.model.ModelNamespaceNotOfficialException;
import org.eclipse.vorto.repository.model.ModelNotReleasedException;
import org.eclipse.vorto.repository.model.RepositoryAccessException;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Component
public class ModelVisibilityService {

  private static Logger logger = Logger.getLogger(ModelVisibilityService.class);
  
  private IModelRepositoryFactory repositoryFactory;
  
  private ITenantService tenantService;
  
  public ModelVisibilityService(@Autowired IModelRepositoryFactory repositoryFactory, 
      @Autowired ITenantService tenantService) {
    this.repositoryFactory = repositoryFactory;
    this.tenantService = tenantService;
  }

  public void makeModelPublic(IUserContext user, ModelId modelId) {
    List<ModelId> accumulator = new ArrayList<>();
    
    LoadingCache<String, IModelRepository> modelRepoCache = CacheBuilder.newBuilder()
        .build(modelRepoCacheLoader(user));
    
    LoadingCache<String, IModelPolicyManager> policyMgrCache = CacheBuilder.newBuilder()
        .build(policyMgrCacheLoader(user));

    LoadingCache<String, String> tenantCache = CacheBuilder.newBuilder()
        .build(tenantNamespaceCacheLoader());
    
    try {
      
      makeModelPublicRecursively(user, modelId, accumulator, modelRepoCache, policyMgrCache, tenantCache);
      
    } catch(ModelNamespaceNotOfficialException | ModelNotReleasedException e) {
      revertToPrivate(user, accumulator, modelRepoCache, policyMgrCache, tenantCache);
      throw e;
    } catch (ExecutionException e) {
      revertToPrivate(user, accumulator, modelRepoCache, policyMgrCache, tenantCache);
      throw new RepositoryAccessException("Errors were thrown while accessing the repository.", e);
    } 
  }

  private void makeModelPublicRecursively(
      IUserContext user, 
      ModelId modelId, 
      List<ModelId> modelsMadePublicAcc, 
      LoadingCache<String, IModelRepository> modelRepoCache,
      LoadingCache<String, IModelPolicyManager> policyMgrCache,
      LoadingCache<String, String> tenantCache) 
          throws ExecutionException {
    
    if (hasPrivateNamespace(modelId)) {
      throw new ModelNamespaceNotOfficialException(modelId);
    }
      
    String tenant = tenantCache.get(modelId.getNamespace());
    
    IModelRepository repository = modelRepoCache.get(tenant);
    ModelInfo modelInfo = repository.getById(modelId);
    
    if (!modelInfo.isReleased()) {
      throw new ModelNotReleasedException(modelId);
    }
    
    if (isPrivate(modelInfo)) {
      // Add public visibility property
      repository.updateVisibility(modelId, IModelRepository.VISIBILITY_PUBLIC);

      // Add corresponding policy to model
      IModelPolicyManager policyMgr = policyMgrCache.get(tenant);
      policyMgr.addPolicyEntry(modelId, 
          PolicyEntry.of(IModelPolicyManager.ANONYMOUS_ACCESS_POLICY, PrincipalType.User, Permission.READ));
      
      modelsMadePublicAcc.add(modelId);
      
      // Make references public
      for(ModelId referencedModelId : modelInfo.getReferences()) {
        makeModelPublicRecursively(user, 
            referencedModelId, 
            modelsMadePublicAcc, 
            modelRepoCache, 
            policyMgrCache, 
            tenantCache);
      }
    }
  }

  private boolean isPrivate(ModelInfo modelInfo) {
    return IModelRepository.VISIBILITY_PRIVATE.equals(modelInfo.getVisibility());
  }
  
  private boolean hasPrivateNamespace(ModelId modelId) {
    return modelId.getNamespace().startsWith(Namespace.PRIVATE_NAMESPACE_PREFIX);
  }
  
  private CacheLoader<String, IModelRepository> modelRepoCacheLoader(IUserContext user) {
    return new CacheLoader<String, IModelRepository>() {
      public IModelRepository load(String tenantId) {
        return repositoryFactory.getRepository(tenantId, user.getAuthentication());
      }
    };
  }

  private CacheLoader<String, IModelPolicyManager> policyMgrCacheLoader(IUserContext user) {
    return new CacheLoader<String, IModelPolicyManager>() {
      public IModelPolicyManager load(String tenantId) {
        return repositoryFactory.getPolicyManager(tenantId, user.getAuthentication());
      }
    };
  }
  
  private CacheLoader<String, String> tenantNamespaceCacheLoader() {
    return new CacheLoader<String, String>() {
      public String load(String namespace) {
        return tenantService.getTenantFromNamespace(namespace)
            .orElseThrow(() -> new TenantNotFoundException(namespace))
            .getTenantId();
      }
    };
  }
  
  private void revertToPrivate(
      IUserContext user, 
      List<ModelId> accumulator,
      LoadingCache<String, IModelRepository> modelRepoCache,
      LoadingCache<String, IModelPolicyManager> policyMgrCache,
      LoadingCache<String, String> tenantCache) {
    
    try {
      for(ModelId modelId : accumulator) {
        String tenantId = tenantCache.get(modelId.getNamespace());
        
        // changed back visibility property
        modelRepoCache.get(tenantId).updateVisibility(modelId, IModelRepository.VISIBILITY_PRIVATE);
        
        // remove added policy
        Collection<PolicyEntry> policies = policyMgrCache.get(tenantId).getPolicyEntries(modelId);
        for (PolicyEntry policy : policies) {
          if (policy.getPrincipalId().equals(IModelPolicyManager.ANONYMOUS_ACCESS_POLICY)) {
            policyMgrCache.get(tenantId).removePolicyEntry(modelId, policy);
            break;
          }
        }
      }
    } catch (ExecutionException e) {
      logger.error("Error while trying to revert the following models [" + 
          String.join(", ", toString(accumulator))+ "] to private.", e);
    }
  }

  private List<String> toString(List<ModelId> accumulator) {
    return accumulator.stream().map(modelId -> modelId.getPrettyFormat()).collect(Collectors.toList());
  }
}
