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
package org.eclipse.vorto.mapping.targetplatform.awsiot;

import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.runtime.FunctionblockValue;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TwinPayloadFactoryTest {

  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
  
  @Test
  public void testCreateRequestSingleFbProperty() {
    FunctionblockModel fbm = FunctionblockModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Temperature:1.0.0"))
      .statusProperty(ModelProperty.createPrimitiveProperty("value", true, PrimitiveType.FLOAT)).build();
    
    Infomodel infomodel = Infomodel.Builder(ModelId.fromPrettyFormat("com.acme:TemperatureSensor:1.0.0"))
        .withProperty(ModelProperty.Builder("temperature", fbm).build()).build();
    
    FunctionblockValue fbValue = new FunctionblockValue(fbm);
    fbValue.withStatusProperty("value", 20.2);
    
    InfomodelValue infomodelValue = new InfomodelValue(infomodel);
    infomodelValue.withFunctionblock("temperature", fbValue);
    
    System.out.println(gson.toJson(TwinPayloadFactory.toShadowUpdateRequest(infomodelValue)));
  }
  
  @Test
  public void testCreateRequestSingleMultipleFbProperties() {
    FunctionblockModel fbm = FunctionblockModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Temperature:1.0.0"))
      .statusProperty(ModelProperty.createPrimitiveProperty("value", true, PrimitiveType.FLOAT)).build();
    
    Infomodel infomodel = Infomodel.Builder(ModelId.fromPrettyFormat("com.acme:TemperatureSensor:1.0.0"))
        .withProperty(ModelProperty.Builder("indoorTemperature", fbm).build())
        .withProperty(ModelProperty.Builder("outdoorTemperature", fbm).build())
        .build();
    
    FunctionblockValue indoorTemperature = new FunctionblockValue(fbm);
    indoorTemperature.withStatusProperty("value", 20.2);
    
    FunctionblockValue outdoorTemperature = new FunctionblockValue(fbm);
    outdoorTemperature.withStatusProperty("value", 9.2);
    
    InfomodelValue infomodelValue = new InfomodelValue(infomodel);
    infomodelValue.withFunctionblock("indoorTemperature", indoorTemperature);
    infomodelValue.withFunctionblock("outdoorTemperature", outdoorTemperature);
    
    System.out.println(gson.toJson(TwinPayloadFactory.toShadowUpdateRequest(infomodelValue)));
  }
}
