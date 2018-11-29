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

public class EnumModel extends AbstractModel {

  private List<EnumLiteral> literals = new ArrayList<EnumLiteral>();

  public EnumModel(ModelId modelId, ModelType modelType) {
    super(modelId, modelType);
  }

  protected EnumModel() {

  }

  public List<EnumLiteral> getLiterals() {
    return literals;
  }

  public void setLiterals(List<EnumLiteral> literals) {
    this.literals = literals;
  }

  @Override
  public ModelType getType() {
    return ModelType.Datatype;
  }

  @Override
  public String toString() {
    return "EnumModel [literals=" + literals + ", id=" + id + ", type=" + type + ", displayName="
        + displayName + ", description=" + description + ", references=" + references + "]";
  }


}
