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

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.ModelId;

public class ModelContent {

  private ModelId root = null;

  private Map<ModelId, AbstractModel> models = new HashMap<ModelId, AbstractModel>();

  public ModelId getRoot() {
    return root;
  }

  public void setRoot(ModelId root) {
    this.root = root;
  }

  public Map<ModelId, AbstractModel> getModels() {
    return models;
  }

  public void setModels(Map<ModelId, AbstractModel> models) {
    this.models = models;
  }

  @Override
  public String toString() {
    return "ModelContent [root=" + root + ", models=" + models + "]";
  }


}
