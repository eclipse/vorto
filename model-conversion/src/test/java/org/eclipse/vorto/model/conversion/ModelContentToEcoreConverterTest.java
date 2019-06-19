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
package org.eclipse.vorto.model.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Optional;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.model.ConstraintType;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelEvent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.Operation;
import org.eclipse.vorto.model.Param;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.ReturnType;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelContentToEcoreConverterTest {

  @Test
  public void testConvertDatatype() {
    EntityModel entityModel =
        EntityModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Location:1.0.0"))
            .property(ModelProperty.Builder("lng", PrimitiveType.FLOAT).withConstraint(ConstraintType.MIN, "0").build())
            .property(ModelProperty.Builder("lat", PrimitiveType.STRING).build())
            .description("Some description").displayname("Location").build();

    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();

    Entity model =
        (Entity) converter.convert(ModelContent.Builder(entityModel).build(), Optional.empty());
    assertEquals(entityModel.getId().getNamespace(), model.getNamespace());
    assertEquals(entityModel.getId().getName(), model.getName());
    assertEquals(entityModel.getId().getVersion(), model.getVersion());
    assertEquals(entityModel.getDescription(), model.getDescription());
    assertEquals(entityModel.getDisplayName(), model.getDisplayname());
    assertEquals(entityModel.getCategory(), model.getCategory());
    assertEquals(2, model.getProperties().size());

    Property property1 =
        model.getProperties().stream().filter(p -> p.getName().equals("lng")).findAny().get();
    assertNotNull(property1);
    assertEquals(org.eclipse.vorto.core.api.model.datatype.PrimitiveType.FLOAT,
        ((PrimitivePropertyType) property1.getType()).getType());
    assertEquals(1,property1.getConstraintRule().getConstraints().size());

    Property property2 =
        model.getProperties().stream().filter(p -> p.getName().equals("lat")).findAny().get();
    assertNotNull(property2);
    assertEquals(org.eclipse.vorto.core.api.model.datatype.PrimitiveType.STRING,
        ((PrimitivePropertyType) property2.getType()).getType());
  }

  @Test
  public void testConvertEnum() {
    EnumModel enumModel =
        EnumModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Unit:1.0.0"))
            .literal("celcius", null).literal("kg", null).build();

    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();

    org.eclipse.vorto.core.api.model.datatype.Enum model =
        (org.eclipse.vorto.core.api.model.datatype.Enum) converter
            .convert(ModelContent.Builder(enumModel).build(), Optional.empty());
    assertEquals(enumModel.getId().getNamespace(), model.getNamespace());
    assertEquals(enumModel.getId().getName(), model.getName());
    assertEquals(enumModel.getId().getVersion(), model.getVersion());
    assertEquals(enumModel.getDescription(), model.getDescription());
    assertEquals(enumModel.getDisplayName(), model.getDisplayname());
    assertEquals(enumModel.getCategory(), model.getCategory());
    assertEquals(2, model.getEnums().size());

  }

  @Test
  public void testEntityWithEnum() {
    EnumModel enumModel =
        EnumModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Unit:1.0.0"))
            .literal("celcius", null).literal("kg", null).build();

    EntityModel entityModel =
        EntityModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Location:1.0.0"))
            .property(ModelProperty.Builder("lng", PrimitiveType.FLOAT).build())
            .property(ModelProperty.Builder("lat", PrimitiveType.STRING).build())
            .property(ModelProperty.Builder("unit", enumModel.getId()).build())
            .reference(enumModel.getId()).build();

    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();

    Entity model = (Entity) converter.convert(
        ModelContent.Builder(entityModel).withDependency(enumModel).build(), Optional.empty());
    assertEquals(1, model.getReferences().size());
    assertEquals(3, model.getProperties().size());

    Property unitProperty =
        model.getProperties().stream().filter(p -> p.getName().equals("unit")).findAny().get();
    ObjectPropertyType type = (ObjectPropertyType) unitProperty.getType();
    Enum unit = (Enum) type.getType();
    assertNotNull(unit);
  }

  @Test
  public void testConvertFunctionblockBasic() {
    FunctionblockModel fbModel = FunctionblockModel
        .Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Sensor:1.0.0")).build();

    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();

    org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel model =
        (org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel) converter
            .convert(ModelContent.Builder(fbModel).build(), Optional.empty());
    assertEquals(fbModel.getId().getNamespace(), model.getNamespace());
    assertEquals(fbModel.getId().getName(), model.getName());
    assertEquals(fbModel.getId().getVersion(), model.getVersion());
    assertEquals(fbModel.getDescription(), model.getDescription());
    assertEquals(fbModel.getDisplayName(), model.getDisplayname());
    assertEquals(fbModel.getCategory(), model.getCategory());
  }

  @Test
  public void testConvertFunctionblockConfigStatusProperties() {
    FunctionblockModel fbModel =
        FunctionblockModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Sensor:1.0.0"))
            .statusProperty(ModelProperty.Builder("value", PrimitiveType.FLOAT).build())
            .configurationProperty(ModelProperty.Builder("enable", PrimitiveType.BOOLEAN).build())
            .build();

    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();

    org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel model =
        (org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel) converter
            .convert(ModelContent.Builder(fbModel).build(), Optional.empty());
    assertEquals(1, model.getFunctionblock().getStatus().getProperties().size());
    assertEquals(1, model.getFunctionblock().getConfiguration().getProperties().size());
  }

  @Test
  public void testConvertFunctionblockOperationWithPrimitiveTypes() {
    FunctionblockModel fbModel =
        FunctionblockModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Sensor:1.0.0"))
            .operation(Operation.Builder("op").description("some operation")
                .withParam(Param.Builder("p1", PrimitiveType.INT).build()).build())
            .operation(Operation.Builder("op2")
                .withResult(new ReturnType(PrimitiveType.BOOLEAN, false)).build())
            .build();

    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();

    org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel model =
        (org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel) converter
            .convert(ModelContent.Builder(fbModel).build(), Optional.empty());
    assertEquals(2, model.getFunctionblock().getOperations().size());

    org.eclipse.vorto.core.api.model.functionblock.Operation op1 = model.getFunctionblock()
        .getOperations().stream().filter(p -> p.getName().equals("op")).findAny().get();

    assertEquals("some operation", op1.getDescription());
    assertEquals(1, op1.getParams().size());
    assertEquals(org.eclipse.vorto.core.api.model.datatype.PrimitiveType.INT,
        ((PrimitiveParam) op1.getParams().get(0)).getType());

    org.eclipse.vorto.core.api.model.functionblock.Operation op2 = model.getFunctionblock()
        .getOperations().stream().filter(p -> p.getName().equals("op2")).findAny().get();

    assertEquals(org.eclipse.vorto.core.api.model.datatype.PrimitiveType.BOOLEAN,
        ((ReturnPrimitiveType) op2.getReturnType()).getReturnType());
  }

  @Test
  public void testConvertFunctionblockOperationWithObjectTypes() {
    EnumModel enumModel =
        EnumModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Unit:1.0.0"))
            .literal("celcius", null).literal("kg", null).build();

    FunctionblockModel fbModel = FunctionblockModel
        .Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Sensor:1.0.0"))
        .operation(Operation.Builder("op").withParam(Param.Builder("p1", enumModel.getId()).build())
            .build())
        .operation(
            Operation.Builder("op2").withResult(new ReturnType(enumModel.getId(), false)).build())
        .build();

    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();

    org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel model =
        (org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel) converter.convert(
            ModelContent.Builder(fbModel).withDependency(enumModel).build(), Optional.empty());
    assertEquals(2, model.getFunctionblock().getOperations().size());
    
    assertEquals(1,model.getReferences().size());

    org.eclipse.vorto.core.api.model.functionblock.Operation op1 = model.getFunctionblock()
        .getOperations().stream().filter(p -> p.getName().equals("op")).findAny().get();

    assertEquals(1, op1.getParams().size());
    assertEquals("Unit", ((Enum) ((RefParam) op1.getParams().get(0)).getType()).getName());
    assertEquals(2, ((Enum) ((RefParam) op1.getParams().get(0)).getType()).getEnums().size());

    org.eclipse.vorto.core.api.model.functionblock.Operation op2 = model.getFunctionblock()
        .getOperations().stream().filter(p -> p.getName().equals("op2")).findAny().get();

    assertEquals("Unit",
        ((Enum) (((ReturnObjectType) op2.getReturnType()).getReturnType())).getName());
    assertEquals(2,
        ((Enum) (((ReturnObjectType) op2.getReturnType()).getReturnType())).getEnums().size());
  }
  
  @Test
  public void testConvertFunctionblockWithEvents() {
    EnumModel enumModel =
        EnumModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Unit:1.0.0"))
            .literal("celcius", null).literal("kg", null).build();

    FunctionblockModel fbModel = FunctionblockModel
        .Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Sensor:1.0.0"))
         .event(ModelEvent.Builder("Exceeded").withProperty(ModelProperty.Builder("unit", enumModel.getId()).build()).build())
        .build();

    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();
    
    org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel model =
        (org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel) converter.convert(
            ModelContent.Builder(fbModel).withDependency(enumModel).build(), Optional.empty());
    
    assertEquals(1,model.getReferences().size());
    assertEquals(1,model.getFunctionblock().getEvents().size());
  }
  
  @Test
  public void testConvertInformationModel() throws Exception {
    FunctionblockModel fbModel1 = FunctionblockModel
        .Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Sensor:1.0.0"))
        .build();
    
    FunctionblockModel fbModel2 = FunctionblockModel
        .Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Temperature:1.0.0"))
        .build();
    
    Infomodel infomodel = Infomodel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:TestDevice:1.0.0"))
        .withProperty(ModelProperty.Builder("sensor", fbModel1.getId()).build())
        .withProperty(ModelProperty.Builder("temperature", fbModel2.getId()).build()).build();
    
    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();
    
    org.eclipse.vorto.core.api.model.informationmodel.InformationModel model =
        (org.eclipse.vorto.core.api.model.informationmodel.InformationModel) converter.convert(
            ModelContent.Builder(infomodel).withDependency(fbModel1).withDependency(fbModel2).build(), Optional.empty());
    
    assertEquals(infomodel.getId().getNamespace(), model.getNamespace());
    assertEquals(infomodel.getId().getName(), model.getName());
    assertEquals(infomodel.getId().getVersion(), model.getVersion());
    assertEquals(infomodel.getDescription(), model.getDescription());
    assertEquals(infomodel.getDisplayName(), model.getDisplayname());
    assertEquals(infomodel.getCategory(), model.getCategory());
    
    assertEquals(2,model.getReferences().size());
    
    FunctionblockProperty property = model.getProperties().stream().filter(p -> p.getName().equals("sensor")).findAny().get();
    org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel fb = (org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel) property.getType();
    assertEquals(fbModel1.getId().getName(),fb.getName());
    assertEquals(fbModel1.getId().getNamespace(),fb.getNamespace());
    assertEquals(fbModel1.getId().getVersion(),fb.getVersion());
    assertTrue(property.eContainer() instanceof InformationModel);
    
    System.out.println(new ObjectMapper().writeValueAsString(ModelContent.Builder(infomodel).withDependency(fbModel1).withDependency(fbModel2).build()));
    
  }

}
