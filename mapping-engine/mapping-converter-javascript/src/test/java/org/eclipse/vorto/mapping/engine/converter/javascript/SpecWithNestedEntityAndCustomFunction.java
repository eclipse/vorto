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
package org.eclipse.vorto.mapping.engine.converter.javascript;

import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.service.mapping.spec.AbstractTestSpec;

public class SpecWithNestedEntityAndCustomFunction extends AbstractTestSpec {

  @Override
  protected void createModel() {
    
    EntityModel countEntity = EntityModel.Builder(ModelId.fromPrettyFormat("demo:Count:1.0.0"))
         .property(ModelProperty.Builder("value", PrimitiveType.INT)
             .withXPathStereotype("button:convertClickType(clickType)","iotbutton")
             .build()).build();
    
    FunctionblockModel fbm = FunctionblockModel.Builder(ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"))
      .statusProperty(ModelProperty.Builder("count", countEntity).build()).build();
    
    infomodel.getFunctionblocks().add(ModelProperty.Builder("button",fbm).build());
    
    
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
    FunctionLibrary library = new FunctionLibrary();
    IScriptEvaluator evaluator = evalProvider.createEvaluator("button");
    evaluator.addScriptFunction(new ScriptClassFunction("convertClickType",
        "function convertClickType(clickType) {if (clickType === 'SINGLE') return 1; else if (clickType === 'DOUBLE') return 2; else return 99;}"));
    library.addFunctions(evaluator.getFunctions());

    return library;
  }


}
