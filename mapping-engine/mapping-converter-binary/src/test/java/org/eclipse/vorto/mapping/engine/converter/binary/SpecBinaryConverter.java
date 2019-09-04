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
package org.eclipse.vorto.mapping.engine.converter.binary;

import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.service.mapping.spec.AbstractTestSpec;

public class SpecBinaryConverter extends AbstractTestSpec {

  @Override
  protected void createModel() {
    FunctionblockModel temperature = FunctionblockModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Temperature:1.0.0"))
      .statusProperty(ModelProperty.Builder("value", PrimitiveType.FLOAT)
            .withXPathStereotype("custom:convert(vorto_conversion1:byteArrayToInt(data,0,0,0,2))", "demo").build())
      .build();
    
    FunctionblockModel humidity = FunctionblockModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Humidity:1.0.0"))
        .statusProperty(ModelProperty.Builder("value", PrimitiveType.FLOAT)
            .withXPathStereotype("custom:convert(vorto_conversion1:byteArrayToInt(data,2,0,0,2))", "demo").build())
        .build();
    
 
    infomodel.getFunctionblocks().add(ModelProperty.Builder("temperature",temperature).build());
    infomodel.getFunctionblocks().add(ModelProperty.Builder("humidity",humidity).build());
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
    FunctionLibrary library = new FunctionLibrary();
    IScriptEvaluator evaluator = evalProvider.createEvaluator("custom");
    evaluator.addScriptFunction(new ScriptClassFunction("convert",
        "function convert(value) { return value*0.01; }"));
    library.addFunctions(evaluator.getFunctions());
    return library;
  }

}
