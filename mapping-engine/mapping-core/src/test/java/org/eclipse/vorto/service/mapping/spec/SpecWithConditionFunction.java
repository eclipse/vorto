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

import java.util.Arrays;

import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;

public class SpecWithConditionFunction extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"));
    
    buttonModel.setTargetPlatformKey("iotbutton");
    buttonModel.addStereotype(Stereotype.createCondition("not empty(array[0])"));
    
    ModelProperty digitalInputStateProperty = new ModelProperty();
    digitalInputStateProperty.setMandatory(true);
    digitalInputStateProperty.setName("sensor_value");
    digitalInputStateProperty.setType(PrimitiveType.FLOAT);

    digitalInputStateProperty.setTargetPlatformKey("iotbutton");
    digitalInputStateProperty
        .addStereotype(Stereotype.createWithXpath("array[1]"));

    buttonModel.setStatusProperties(
        Arrays.asList(new ModelProperty[] {digitalInputStateProperty}));

    addFunctionblockProperty("button", buttonModel);
  }

}
