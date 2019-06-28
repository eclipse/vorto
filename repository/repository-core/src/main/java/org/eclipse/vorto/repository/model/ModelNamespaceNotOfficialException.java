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
package org.eclipse.vorto.repository.model;

import org.eclipse.vorto.model.ModelId;

public class ModelNamespaceNotOfficialException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = -2015063550749354134L;

  public ModelNamespaceNotOfficialException(ModelId model) {
    super("The model '" + model.getPrettyFormat() + "' does not have an official namespace.");
  }
}
