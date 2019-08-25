/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.service.mapping.spec;

import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;

public abstract class AbstractTestSpec implements IMappingSpecification {

  protected Infomodel infomodel =
      new Infomodel(ModelId.fromPrettyFormat("devices:AWSIoTButton:1.0.0"));
  
  
  public AbstractTestSpec() {
    createModel();
  }

  @Override
  public Infomodel getInfoModel() {
    return infomodel;
  }

  protected abstract void createModel();
  
  @Override
  public FunctionblockModel getFunctionBlock(String name) {
    return (FunctionblockModel)this.infomodel.getFunctionblocks().stream().filter(p -> p.getName().equals(name)).findAny().get().getType();
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider provider) {
    return new FunctionLibrary();
  }
}
