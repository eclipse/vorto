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
package org.eclipse.vorto.model;

import java.util.HashMap;
import java.util.Map;

public class ModelContent {

  private ModelId root = null;

  private Map<ModelId, IModel> models = new HashMap<ModelId, IModel>();

  public ModelId getRoot() {
    return root;
  }

  public void setRoot(ModelId root) {
    this.root = root;
  }

  public Map<ModelId, IModel> getModels() {
    return models;
  }

  public void setModels(Map<ModelId, IModel> models) {
    this.models = models;
  }
  
  @Override
  public String toString() {
    return "ModelContent [root=" + root + ", models=" + models + "]";
  }

  public static Builder Builder(AbstractModel root) {
    return new Builder(root);
  }

  public static class Builder {
    private ModelContent content;
    
    public Builder(AbstractModel root) {
      this.content = new ModelContent();
      this.content.setRoot(root.getId());
      this.content.models.put(root.getId(), root);
    }
    
    public Builder withDependency(AbstractModel model) {
      this.content.models.put(model.getId(), model);
      return this;
    }
    
    public ModelContent build() {
      return this.content;
    }
  }

}
