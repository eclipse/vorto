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
