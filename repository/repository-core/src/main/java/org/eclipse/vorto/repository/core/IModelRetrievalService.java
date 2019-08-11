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
import org.eclipse.vorto.model.ModelId;

/**
 * This service is for retrieving models across tenants 
 * 
 *
 */
public interface IModelRetrievalService {
  
  /**
   * Get all models across all repositories who are referencing modelId
   * @param modelId the modelId that is being referenced
   * @return a map entry with the key being the tenantId and the value being the 
   * list of models referencing modelId
   */
  Map<String, List<ModelInfo>> getModelsReferencing(ModelId modelId);
}
