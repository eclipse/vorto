/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
