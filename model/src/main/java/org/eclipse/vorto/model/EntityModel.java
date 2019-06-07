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

import java.util.ArrayList;
import java.util.List;

public class EntityModel extends AbstractModel {

  private List<ModelProperty> properties = new ArrayList<ModelProperty>();

  public EntityModel(ModelId modelId) {
    super(modelId, ModelType.Datatype);
  }

  protected EntityModel() {

  }
  
  public static EntityBuilder Builder(ModelId id) {
    return new EntityBuilder(id);
  }

  public List<ModelProperty> getProperties() {
    return properties;
  }

  public void setProperties(List<ModelProperty> properties) {
    this.properties = properties;
  }

  @Override
  public String toString() {
    return "EntityModelDto [properties=" + properties + "]";
  }


  public static class EntityBuilder extends Builder<EntityModel> {
    
    private EntityBuilder(ModelId id) {
      super(new EntityModel(id));
    }
    
    public EntityBuilder property(ModelProperty property) {
      this.model.properties.add(property);
      return this;
    }
  }
}
