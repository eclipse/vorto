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

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.model.ModelId;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelReferentialIntegrityException extends ModelRepositoryException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private List<ModelId> referencedBy = new ArrayList<>();

  public ModelReferentialIntegrityException(String msg, List<ModelId> referencedBy) {
    super(msg);
    this.referencedBy = referencedBy;
  }

  public List<ModelId> getReferencedBy() {
    return referencedBy;
  }

}
