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
package org.eclipse.vorto.repository.workflow;

import org.eclipse.vorto.repository.core.ModelInfo;

public class WorkflowException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private ModelInfo model;

  public WorkflowException(ModelInfo model, String msg) {
    super(msg);
    this.model = model;
  }

  public ModelInfo getModel() {
    return model;
  }
}
