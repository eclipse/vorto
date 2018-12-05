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
package org.eclipse.vorto.repository.core.impl.utils;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.model.ModelId;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelReferencesHelper {
  private List<ModelId> references = null;

  public ModelReferencesHelper(List<ModelId> references) {
    this.references = references;
  }

  public ModelReferencesHelper() {
    this.references = new ArrayList<ModelId>();
  }

  public String[] getReferencesInPrettyFormat() {
    List<String> prettyFormatReferences = new ArrayList<String>();
    for (ModelId id : references) {
      prettyFormatReferences.add(id.getPrettyFormat());
    }
    return prettyFormatReferences.toArray(new String[prettyFormatReferences.size()]);
  }

  public void addModelReference(String prettyFormat) {
    this.references.add(ModelId.fromPrettyFormat(prettyFormat));
  }
  
  public void addModelReference(ModelReference reference) {
    ModelId modelId = ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion());
    references.add(modelId);
  }

  public List<ModelId> getReferences() {
    return references;
  }

  public boolean hasReferences() {
    return !this.references.isEmpty();
  }

}
