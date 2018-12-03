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
