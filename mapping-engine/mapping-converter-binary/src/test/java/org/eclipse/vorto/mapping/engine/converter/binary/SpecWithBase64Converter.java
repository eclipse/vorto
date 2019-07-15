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
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.service.mapping.spec.AbstractTestSpec;

public class SpecWithBase64Converter extends AbstractTestSpec {

  @Override
  protected void createFBSpec() {
    addFunctionblockProperty("button", createButtonFb());
  }

  private FunctionblockModel createButtonFb() {
    FunctionblockModel buttonModel = new FunctionblockModel(
        ModelId.fromPrettyFormat("demo.fb:PushButton:1.0.0"));
    ModelProperty digitalInputStateProperty = new ModelProperty();
    digitalInputStateProperty.setMandatory(true);
    digitalInputStateProperty.setName("digital_input_state");
    digitalInputStateProperty.setType(PrimitiveType.INT);

    digitalInputStateProperty.setTargetPlatformKey("iotbutton");

    digitalInputStateProperty
        .addStereotype(Stereotype.createWithXpath("vorto_base64:decodeString(/data)"));

    buttonModel.setStatusProperties(Arrays.asList(new ModelProperty[] {digitalInputStateProperty}));

    return buttonModel;
  }

  @Override
  public FunctionLibrary getScriptFunctions(IScriptEvalProvider evalProvider) {
    return new FunctionLibrary();
  }


}
