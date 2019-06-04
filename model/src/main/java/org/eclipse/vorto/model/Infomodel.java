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

public class Infomodel extends AbstractModel {

  private List<ModelProperty> functionblocks = new ArrayList<ModelProperty>();


  public Infomodel(ModelId modelId) {
    super(modelId, ModelType.InformationModel);
  }

  protected Infomodel() {

  }
  
  public static InfomodelBuilder Builder(ModelId id) {
    return new InfomodelBuilder(id);
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
  
  
  public static class InfomodelBuilder extends Builder<Infomodel> {

    private InfomodelBuilder(ModelId id) {
      super(new Infomodel(id));
    }

    public InfomodelBuilder withProperty(ModelProperty property) {
      this.model.getFunctionblocks().add(property);
      return this;
    }
  }

}
