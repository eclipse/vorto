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
package org.eclipse.vorto.repository.search.impl;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.search.IIndexingService;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.search.IndexingResult;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Simple search which merely delegates the search to the model repository
 *
 */
public class SimpleSearchService implements ISearchService, IIndexingService {

  private ITenantService tenantService;

  private IModelRepositoryFactory repositoryFactory;

  public SimpleSearchService(ITenantService tenantService,
      IModelRepositoryFactory repositoryFactory) {
    this.tenantService = tenantService;
    this.repositoryFactory = repositoryFactory;
  }

  @Override
  public List<ModelInfo> search(String expression, IUserContext userContext) {
    return search(expression, userContext.getAuthentication());
  }
  
  /**
   * Searches all tenants existing in the system. Use modeshape ACLs to get result back which matches user rights.
   */
  @Override
  public List<ModelInfo> search(String expression) {
    return search(expression, SecurityContextHolder.getContext().getAuthentication());
  }
  
  private List<ModelInfo> search(String expression, Authentication authentication) {
    List<ModelInfo> result = new ArrayList<>();
    
    tenantService.getTenants().forEach(tenant -> {
      IModelRepository repository = this.repositoryFactory.getRepository(tenant.getTenantId(), 
          authentication);
      result.addAll(repository.search(expression));
    });

    return result;
  }

  @Override
  public IndexingResult reindexAllModels() {
    return new IndexingResult();
  }

  @Override
  public void indexModel(ModelInfo modelInfo, String tenantId) {
    // NOOP
  }

  @Override
  public void updateIndex(ModelInfo modelInfo) {
    // NOOP 
  }

  @Override
  public void deleteIndex(ModelId modelId) {
    // NOOP
  }

  @Override
  public void deleteIndexForTenant(String tenantId) {
    // NOOP
  }

}
