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
package org.eclipse.vorto.model.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.vorto.model.ModelProperty;

public class ValidationReport {

  private List<ValidationReportItem> invalidProperties =
      new ArrayList<ValidationReport.ValidationReportItem>();

  public void addItem(ModelProperty meta, String message) {
    this.invalidProperties.add(new ValidationReportItem(message, meta));
  }

  public void addReport(ValidationReport report) {
    invalidProperties.addAll(report.getItems());
  }

  public List<ValidationReportItem> getItems() {
    return Collections.unmodifiableList(invalidProperties);
  }

  public boolean isValid() {
    return invalidProperties.isEmpty();
  }


  public static class ValidationReportItem {
    private String message;
    private ModelProperty meta;

    public ValidationReportItem(String msg, ModelProperty meta) {
      this.message = msg;
      this.meta = meta;
    }

    @SuppressWarnings("unused")
    private ValidationReportItem() {

    }

    public String getMessage() {
      return this.message;
    }

    public ModelProperty getType() {
      return this.meta;
    }
  }
}
