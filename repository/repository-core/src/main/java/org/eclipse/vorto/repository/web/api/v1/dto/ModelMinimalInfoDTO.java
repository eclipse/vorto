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
 * Represents the core of minimal information used by the front-end to display model data, e.g. for
 * mappings (see {@link ModelMappingDTO}) or for references (see {@link ModelReferenceDTO}).
 */
public class ModelMinimalInfoDTO {

  String id;
  String state;

  public static ModelMinimalInfoDTO fromModelInfo(ModelInfo source) {
    return new ModelMinimalInfoDTO()
        .withID(source.getId().getPrettyFormat())
        .withState(source.getState());
  }

  public ModelMinimalInfoDTO withID(String id) {
    this.id = id;
    return this;
  }

  public ModelMinimalInfoDTO withState(String state) {
    this.state = state;
    return this;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
