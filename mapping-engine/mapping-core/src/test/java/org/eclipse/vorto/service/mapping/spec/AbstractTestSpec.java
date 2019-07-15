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
package org.eclipse.vorto.service.mapping.spec;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;

public abstract class AbstractTestSpec implements IMappingSpecification {

  private static Map<String, FunctionblockModel> FBS = new HashMap<String, FunctionblockModel>(2);

  private Infomodel infomodel = new Infomodel(
      ModelId.fromPrettyFormat("devices:AWSIoTButton:1.0.0"));

  public AbstractTestSpec() {
    createFBSpec();
  }

  @Override
  public Infomodel getInfoModel() {
    return infomodel;
  }

  protected abstract void createFBSpec();

  protected void addFunctionblockProperty(final String name, final FunctionblockModel fbm) {
    FBS.put(name, fbm);
    ModelProperty prop = new ModelProperty();
    prop.setName(name);
    prop.setType(fbm.getId());
    infomodel.getFunctionblocks().add(prop);
  }

  @Override
  public FunctionblockModel getFunctionBlock(String name) {
    return FBS.get(name);
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider provider) {
    return new FunctionLibrary();
  }
}
