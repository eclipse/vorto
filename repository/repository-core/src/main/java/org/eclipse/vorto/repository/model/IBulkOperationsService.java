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
package org.eclipse.vorto.repository.model;

import java.util.List;
import org.eclipse.vorto.model.ModelId;

public interface IBulkOperationsService {
  
  /**
   * Sets the visibility of the given model to Public. It also sets all the references of the modelId to Public 
   * 
   * @pre The give model and all its (transitive) references are in state 'Released"
   * 
   * @post The given model and all its (transitive) references have visibility "Public"
   * 
   * @param modelId the model to make public
   * 
   * @return a list of resolved modelIds that have been made public
   */
  List<ModelId> makeModelPublic(ModelId modelId);
  
  
}
