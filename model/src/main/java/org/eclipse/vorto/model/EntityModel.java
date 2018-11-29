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
package org.eclipse.vorto.model;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;

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
