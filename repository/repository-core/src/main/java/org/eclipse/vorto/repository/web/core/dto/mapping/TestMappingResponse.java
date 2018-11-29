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
