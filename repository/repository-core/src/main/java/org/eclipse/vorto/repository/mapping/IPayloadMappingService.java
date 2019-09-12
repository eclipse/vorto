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
package org.eclipse.vorto.repository.mapping;

import java.util.Optional;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;

public interface IPayloadMappingService {

  /**
   * Gets an existing mapping specification for the given modelId or creates one if not existing
   * @param modelId
   * @return
   */
  IMappingSpecification getOrCreateSpecification(ModelId modelId);
  
  /**
   * 
   * @param specification
   * @param user
   */
  void saveSpecification(IMappingSpecification specification, IUserContext user);
  
  /**
   * 
   * @param specification
   * @param testData
   * @return
   */
  InfomodelValue runTest(IMappingSpecification specification, Object testData);
  
  /**
   * 
   * @param modelId
   * @return
   */
  Optional<ModelInfo> resolveMappingIdForModelId(ModelId modelId);
  
  /**
   * 
   * @param modelId
   * @return
   */
  boolean exists(ModelId modelId);
}

