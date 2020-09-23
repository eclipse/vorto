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
 * Loaded as element of the references for the main model by the UI.
 */
public class ModelReferenceDTO extends ModelMinimalInfoDTO {

  private String type;
  private boolean hasAccess;

  /**
   * Returns an "inaccessible" reference DTO, which will only display the model's prettified ID in
   * the front-end.
   *
   * @param id
   * @return
   */
  public static ModelMinimalInfoDTO inaccessibleModelReference(String id) {
    return new ModelReferenceDTO()
        .withID(id);
  }

  public static ModelMinimalInfoDTO fromModelInfo(ModelInfo modelInfo) {
    return new ModelReferenceDTO()
        .withType(modelInfo.getModelType())
        .withAccess(true)
        .withID(modelInfo.getId().getPrettyFormat())
        .withState(modelInfo.getState());
  }

  public ModelReferenceDTO withType(String type) {
    this.type = type;
    return this;
  }

  public ModelReferenceDTO withAccess(boolean hasAccess) {
    this.hasAccess = hasAccess;
    return this;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isHasAccess() {
    return hasAccess;
  }

  public void setHasAccess(boolean hasAccess) {
    this.hasAccess = hasAccess;
  }
}
