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
package org.eclipse.vorto.repository.utils;

import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;

public class ModelUtils {

  public static ModelId toEMFModelId(org.eclipse.vorto.model.ModelId modelId, org.eclipse.vorto.model.ModelType type) {
    return new org.eclipse.vorto.core.api.model.model.ModelId(ModelType.valueOf(type.name()), modelId.getName(), modelId.getNamespace(), modelId.getVersion());
  }
  
  public static org.eclipse.vorto.model.ModelId fromEMFModelId(ModelId modelId) {
    return new org.eclipse.vorto.model.ModelId(modelId.getName(), modelId.getNamespace(), modelId.getVersion());
  }
}
