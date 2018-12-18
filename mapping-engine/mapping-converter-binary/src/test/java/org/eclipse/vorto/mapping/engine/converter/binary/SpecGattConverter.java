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

import java.util.Arrays;
import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.service.mapping.spec.AbstractTestSpec;

public class SpecGattConverter extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"), ModelType.Functionblock);
    ModelProperty digitalInputStateProperty = new ModelProperty();
    digitalInputStateProperty.setMandatory(true);
    digitalInputStateProperty.setName("sensor_value");
    digitalInputStateProperty.setType(PrimitiveType.STRING);

    digitalInputStateProperty.setTargetPlatformKey("iotbutton");

    digitalInputStateProperty.addStereotype(Stereotype.createWithXpath(
        "button:convertSensorValue(vorto_conversion1:byteArrayToInt(characteristics[@uuid='23-D1-13-EF-5F-78-23-15-DE-EF-12-12-0D-F0-00-00']/data, 3, 0, 0, 3))"));

    buttonModel.setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputStateProperty}));

    addFunctionblockProperty("button", buttonModel);
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
    FunctionLibrary library = new FunctionLibrary();
    IScriptEvaluator evaluator = evalProvider.createEvaluator("button");
    evaluator.addScriptFunction(new ScriptClassFunction("convertSensorValue",
        "function convertSensorValue(value) { return value*0.01; }"));
    library.addFunctions(evaluator.getFunctions());
    return library;
  }

}
