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

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.model.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelService implements IModelService {

  private ModelVisibilityService modelVisibilityService;
  
  public ModelService(@Autowired ModelVisibilityService modelVisibilityService) {
    this.modelVisibilityService = modelVisibilityService;
  }

  @Override
  public void makeModelPublic(IUserContext userContext, ModelId modelId) {
    modelVisibilityService.makeModelPublic(userContext, modelId);
  }

}
