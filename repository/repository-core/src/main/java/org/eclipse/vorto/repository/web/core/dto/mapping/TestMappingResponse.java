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

import org.eclipse.vorto.model.runtime.ValidationReport;

public class TestMappingResponse {

  private String mappedOutput;

  private ValidationReport report;

  public String getMappedOutput() {
    return mappedOutput;
  }

  public void setMappedOutput(String mappedOutput) {
    this.mappedOutput = mappedOutput;
  }

  public ValidationReport getReport() {
    return report;
  }

  public void setReport(ValidationReport report) {
    this.report = report;
  }



}
