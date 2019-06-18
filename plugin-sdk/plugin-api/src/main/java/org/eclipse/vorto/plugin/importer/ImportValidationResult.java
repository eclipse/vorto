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
package org.eclipse.vorto.plugin.importer;

import org.eclipse.vorto.model.ModelId;

public class ImportValidationResult {
  
  private boolean valid;
  private String message;
  private ModelId modelId;
  
  public boolean isValid() {
    return valid;
  }
  public void setValid(boolean valid) {
    this.valid = valid;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public ModelId getModelId() {
    return modelId;
  }
  public void setModelId(ModelId modelId) {
    this.modelId = modelId;
  }
  
  
  
}
