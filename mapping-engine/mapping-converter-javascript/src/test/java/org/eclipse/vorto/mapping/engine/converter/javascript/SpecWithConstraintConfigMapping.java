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

import java.util.Arrays;
import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;
import org.eclipse.vorto.model.Constraint;
import org.eclipse.vorto.model.ConstraintType;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.service.mapping.spec.AbstractTestSpec;

public class SpecWithConstraintConfigMapping extends AbstractTestSpec {

  @Override
  protected void createModel() {
    FunctionblockModel buttonModel = createButtonFb();
    infomodel.getFunctionblocks().add(ModelProperty.Builder("button",buttonModel).build());
    
    FunctionblockModel voltageModel = createVoltageFb();
    
    infomodel.getFunctionblocks().add(ModelProperty.Builder("voltage",voltageModel).build());
  }

  private FunctionblockModel createButtonFb() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"));
    ModelProperty digitalInputStateProperty = new ModelProperty();
    digitalInputStateProperty.setMandatory(true);
    digitalInputStateProperty.setName("digital_input_state");
    digitalInputStateProperty.setType(PrimitiveType.BOOLEAN);

    digitalInputStateProperty.setTargetPlatformKey("iotbutton");

    digitalInputStateProperty
        .addStereotype(Stereotype.createWithXpath("boolean:toBoolean(\"true\")"));

    ModelProperty digitalInputCount = new ModelProperty();
    digitalInputCount.setMandatory(true);
    digitalInputCount.setName("digital_input_count");
    digitalInputCount.setType(PrimitiveType.INT);

    digitalInputCount.setTargetPlatformKey("iotbutton");
    digitalInputCount
        .addStereotype(Stereotype.createWithXpath("button:convertClickType(clickType)"));

    buttonModel.setConfigurationProperties(
        Arrays.asList(new ModelProperty[] {digitalInputStateProperty, digitalInputCount}));

    return buttonModel;
  }

  private FunctionblockModel createVoltageFb() {
    FunctionblockModel voltageModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:Voltage:1.0.0"));
    ModelProperty sensorValueProperty = new ModelProperty();
    sensorValueProperty.setMandatory(true);
    sensorValueProperty.setName("sensor_value");
    sensorValueProperty.setType(PrimitiveType.FLOAT);
    Constraint constraint = new Constraint(ConstraintType.MAX, "5000");
    sensorValueProperty.setConstraints(Arrays.asList(constraint));

    sensorValueProperty.setTargetPlatformKey("iotbutton");

    sensorValueProperty.addStereotype(Stereotype.createWithXpath(
        "number:toFloat(string:substring(batteryVoltage,0,string:length(batteryVoltage)-2))"));

    ModelProperty sensorUnitsProperty = new ModelProperty();
    sensorUnitsProperty.setMandatory(false);
    sensorUnitsProperty.setName("sensor_units");
    sensorUnitsProperty.setType(PrimitiveType.STRING);

    sensorUnitsProperty.setTargetPlatformKey("iotbutton");
    sensorUnitsProperty.addStereotype(Stereotype
        .createWithXpath("string:substring(batteryVoltage,string:length(batteryVoltage)-2)"));
    voltageModel.setConfigurationProperties(
        Arrays.asList(new ModelProperty[] {sensorValueProperty, sensorUnitsProperty}));

    return voltageModel;
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
