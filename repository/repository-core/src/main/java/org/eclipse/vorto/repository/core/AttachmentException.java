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

public class AttachmentException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private ModelId modelId;

  public AttachmentException(ModelId modelId, String message) {
    super(message);
    this.modelId = modelId;
  }

  public AttachmentException(ModelId modelId, Throwable throwable) {
    super(throwable);
    this.modelId = modelId;
  }

  public ModelId getModelId() {
    return modelId;
  }

}
