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
package org.eclipse.vorto.repository.web.core.dto.mapping;

import org.eclipse.vorto.mapping.engine.model.spec.MappingSpecification;

public class TestMappingRequest {

  private String sourceJson;
  private MappingSpecification specification;

  public String getSourceJson() {
    return sourceJson;
  }

  public void setSourceJson(String sourceJson) {
    this.sourceJson = sourceJson;
  }

  public MappingSpecification getSpecification() {
    return specification;
  }

  public void setSpecification(MappingSpecification specification) {
    this.specification = specification;
  }

}
