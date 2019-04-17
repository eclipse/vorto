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
package org.eclipse.vorto.repository.core;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.eclipse.vorto.model.ModelId;

/**
 * This service is for retrieving models across tenants 
 * 
 * @author ERM1SGP
 *
 */
public interface IModelRetrievalService {
  /**
   * Returns the model, along with the tenant
   * 
   * @param modelId
   * @return
   */
  Optional<Map.Entry<String, ModelInfo>> getModel(ModelId modelId);
  
  /**
   * Returns the content of the model, along with the tenant
   * 
   * @param modelId
   * @return
   */
  Optional<Map.Entry<String, FileContent>> getContent(ModelId modelId);
  
  /**
   * Return the EMF resource of the requested modelId from tenantId
   * @param tenantId
   * @param modelId
   * @return
   */
  Optional<ModelResource> getEMFResource(String tenantId, ModelId modelId);
  
  /**
   * Get all models across all repositories who are referencing modelId
   * @param modelId the modelId that is being referenced
   * @return a map entry with the key being the tenantId and the value being the 
   * list of models referencing modelId
   */
  Map<String, List<ModelInfo>> getModelsReferencing(ModelId modelId);
}
