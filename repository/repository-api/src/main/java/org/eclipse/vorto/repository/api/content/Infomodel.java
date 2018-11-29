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
package org.eclipse.vorto.repository.api.content;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;

@Deprecated
public class Infomodel extends AbstractModel {

  private List<ModelProperty> functionblocks = new ArrayList<ModelProperty>();


  public Infomodel(ModelId modelId, ModelType modelType) {
    super(modelId, modelType);
  }

  protected Infomodel() {

  }

  public List<ModelProperty> getFunctionblocks() {
    return functionblocks;
  }

  public void setFunctionblocks(List<ModelProperty> functionblocks) {
    this.functionblocks = functionblocks;
  }

  @Override
  public String toString() {
    return "InfomodelDto [functionblocks=" + functionblocks + "]";
  }

}
