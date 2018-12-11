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
package org.eclipse.vorto.repository.core;

import org.eclipse.vorto.model.ModelId;

public class Diagnostic {
  private ModelId modelId;
  private String nodeId;
  private String diagnosticMessage;

  public Diagnostic(ModelId modelId, String nodeId, String diagnosticMessage) {
    this.modelId = modelId;
    this.nodeId = nodeId;
    this.diagnosticMessage = diagnosticMessage;
  }

  public Diagnostic(ModelId modelId, String diagnosticMessage) {
    this.modelId = modelId;
    this.diagnosticMessage = diagnosticMessage;
  }

  public ModelId getModelId() {
    return modelId;
  }

  public void setModelId(ModelId modelId) {
    this.modelId = modelId;
  }

  public String getDiagnosticMessage() {
    return diagnosticMessage;
  }

  public void setDiagnosticMessage(String diagnosticMessage) {
    this.diagnosticMessage = diagnosticMessage;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  @Override
  public String toString() {
    return "Diagnostic [modelId=" + modelId + ", nodeId=" + nodeId + ", diagnosticMessage="
        + diagnosticMessage + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((diagnosticMessage == null) ? 0 : diagnosticMessage.hashCode());
    result = prime * result + ((modelId == null) ? 0 : modelId.hashCode());
    result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
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
    Diagnostic other = (Diagnostic) obj;

    return checkStringProperty(diagnosticMessage, other.diagnosticMessage)
        && checkStringProperty(nodeId, other.nodeId) && checkModelId(modelId, other.modelId);
  }

  private boolean checkStringProperty(String myString, String otherString) {
    if (myString == null) {
      if (otherString != null)
        return false;
    } else if (!myString.equals(otherString))
      return false;
    return true;
  }

  private boolean checkModelId(ModelId mine, ModelId other) {
    if (mine == null) {
      if (other != null)
        return false;
    } else if (!mine.equals(other))
      return false;
    return true;
  }
}
