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
