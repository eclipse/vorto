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
package org.eclipse.vorto.repository.api.content;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;


@Deprecated
public class EntityModel extends AbstractModel {

  private List<ModelProperty> properties = new ArrayList<ModelProperty>();

  public EntityModel(ModelId modelId, ModelType modelType) {
    super(modelId, modelType);
  }

  protected EntityModel() {

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


}
