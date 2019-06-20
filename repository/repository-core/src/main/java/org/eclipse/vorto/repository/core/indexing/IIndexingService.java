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
package org.eclipse.vorto.repository.core.indexing;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;

public interface IIndexingService {
  
  /**
   * Re-indexes the given models 
   * 
   * @param models A map of tenantId to the models that tenant contains 
   * @return a result of how many tenants, and how many models per tenant were indexed
   */
  IndexingResult reindexAllModels(Map<String, List<ModelInfo>> models);
  
  /**
   * Indexes the given model with the given tenantId 
   * 
   * @param modelInfo the model to be indexed
   * @param tenantId the tenant that owns this model
   */
  void indexModel(ModelInfo modelInfo, String tenantId);
  
  /**
   * Updates the index for the given model
   * 
   * @param modelInfo the model to be updated
   */
  void updateIndex(ModelInfo modelInfo);
  
  /**
   * Deletes the index for the given model
   * 
   * @param modelId the modelId of the model to be deleted in the index
   */
  void deleteIndex(ModelId modelId);
  
  /**
   * Deletes all the index of the models owned by tenant
   *  
   * @param tenantId the owning tenant
   */
  void deleteIndexForTenant(String tenantId);
  
  /**
   * Searches the index given the owning tenants, and a search expression 
   * 
   * @param tenantIds the tenants whose models we should search. If tenantIds is empty, it searches all tenants. 
   *        If tenantIds is not empty but the collection inside is empty, it searches the public models of all tenants.
   *        If tenantIds is not empty and the collection inside is non-empty, it searches all models inside all tenants in tenantIds plus 
   *        all the public models of all tenants. 
   * @param expression The search expression
   * @return a list of models in the index
   */
  List<IndexedModelInfo> search(Optional<Collection<String>> tenantIds, String expression);
  
}
