/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.importer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class UploadModelResult {
  private String handleId = null;
  private boolean isValid = false;
  private boolean hasWarnings = false;

  private List<ValidationReport> reports = new ArrayList<ValidationReport>();

  public UploadModelResult(String handleId, List<ValidationReport> reports) {
    super();
    this.handleId = handleId;
    this.reports.addAll(reports);
    this.isValid = reports.stream().filter(report -> !report.isValid()).count() == 0;
    this.hasWarnings = reports.stream()
        .filter(report -> report.getMessage().getSeverity() == MessageSeverity.WARNING).count() > 0;

  }

  protected UploadModelResult() {

  }

  public String getHandleId() {
    return handleId;
  }

  public List<ValidationReport> getReport() {
    return reports;
  }

  public boolean isValid() {
    return isValid;
  }

  public void setValid(boolean isValid) {
    this.isValid = isValid;
  }

  public List<ValidationReport> getReports() {
    return reports;
  }

  public void setReports(List<ValidationReport> reports) {
    this.reports = reports;
  }

  public void setHandleId(String handleId) {
    this.handleId = handleId;
  }

  public boolean hasWarnings() {
    return hasWarnings;
  }

  public void setHasWarnings(boolean hasWarnings) {
    this.hasWarnings = hasWarnings;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((handleId == null) ? 0 : handleId.hashCode());
    result = prime * result + (hasWarnings ? 1231 : 1237);
    result = prime * result + (isValid ? 1231 : 1237);
    result = prime * result + ((reports == null) ? 0 : reports.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UploadModelResult other = (UploadModelResult) obj;
    if (handleId == null) {
      if (other.handleId != null)
        return false;
    } else if (!handleId.equals(other.handleId))
      return false;
    if (hasWarnings != other.hasWarnings)
      return false;
    if (isValid != other.isValid)
      return false;
    if (reports == null) {
      if (other.reports != null)
        return false;
    } else if (!reports.equals(other.reports))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "UploadModelResult [handleId=" + handleId + ", isValid=" + isValid + ", hasWarnings="
        + hasWarnings + ", reports=" + reports + "]";
  }
}
