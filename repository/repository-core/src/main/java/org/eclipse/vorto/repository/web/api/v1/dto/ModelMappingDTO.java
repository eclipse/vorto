/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.web.api.v1.dto;

import org.eclipse.vorto.repository.core.ModelInfo;

/**
 * Represents model information for the front-end, with a minimal subset of the details contained in
 * {@link ModelInfoDto}.<br/>
 * Loaded as element of the mappings for the main model by the UI.
 */
public class ModelMappingDTO extends ModelMinimalInfoDTO {

  private String targetPlatform;

  public static ModelMinimalInfoDTO fromModelInfo(String parentTargetPlatform, ModelInfo source) {
    return new ModelMappingDTO()
        .withTargetPlatform(parentTargetPlatform)
        .withID(source.getId().getPrettyFormat())
        .withState(source.getState());
  }

  public ModelMinimalInfoDTO withTargetPlatform(String targetPlatform) {
    this.targetPlatform = targetPlatform;
    return this;
  }

  public String getTargetPlatform() {
    return targetPlatform;
  }

  public void setTargetPlatform(String targetPlatform) {
    this.targetPlatform = targetPlatform;
  }

}
