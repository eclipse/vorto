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
package org.eclipse.vorto.model.conversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Optional;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;
import org.junit.Test;

public class ModelContentToEcoreConverterTest {

  @Test
  public void testConvertDatatype() {
    EntityModel entityModel = EntityModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Location:1.0.0"))
        .property(ModelProperty.Builder("lng", PrimitiveType.FLOAT).build())
        .property(ModelProperty.Builder("lat", PrimitiveType.STRING).build())
        .description("Some description")
        .displayname("Location")
        .build();
    
    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();
    
    Entity model = (Entity)converter.convert(ModelContent.Builder(entityModel).build(),Optional.empty());
    assertEquals(entityModel.getId().getNamespace(),model.getNamespace());
    assertEquals(entityModel.getId().getName(),model.getName());
    assertEquals(entityModel.getId().getVersion(),model.getVersion());
    assertEquals(entityModel.getDescription(),model.getDescription());
    assertEquals(entityModel.getDisplayName(),model.getDisplayname());
    assertEquals(entityModel.getCategory(),model.getCategory());
    assertEquals(2,model.getProperties().size());
    
    Property property1 = model.getProperties().stream().filter(p -> p.getName().equals("lng")).findAny().get();
    assertNotNull(property1);
    assertEquals(org.eclipse.vorto.core.api.model.datatype.PrimitiveType.FLOAT,((PrimitivePropertyType)property1.getType()).getType());

    Property property2 = model.getProperties().stream().filter(p -> p.getName().equals("lat")).findAny().get();
    assertNotNull(property2);
    assertEquals(org.eclipse.vorto.core.api.model.datatype.PrimitiveType.STRING,((PrimitivePropertyType)property2.getType()).getType());
  }
  
  @Test
  public void testConvertEnum() {
    EnumModel enumModel = EnumModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Unit:1.0.0"))
      .literal("celcius", null)
      .literal("kg",null).build();
    
    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();
    
    org.eclipse.vorto.core.api.model.datatype.Enum model = (org.eclipse.vorto.core.api.model.datatype.Enum)converter.convert(ModelContent.Builder(enumModel).build(),Optional.empty());
    assertEquals(enumModel.getId().getNamespace(),model.getNamespace());
    assertEquals(enumModel.getId().getName(),model.getName());
    assertEquals(enumModel.getId().getVersion(),model.getVersion());
    assertEquals(enumModel.getDescription(),model.getDescription());
    assertEquals(enumModel.getDisplayName(),model.getDisplayname());
    assertEquals(enumModel.getCategory(),model.getCategory());
    assertEquals(2,model.getEnums().size());

  }
  
  @Test
  public void testEntityWithEnum() {
    EnumModel enumModel = EnumModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Unit:1.0.0"))
        .literal("celcius", null)
        .literal("kg",null).build();
    
    EntityModel entityModel = EntityModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Location:1.0.0"))
        .property(ModelProperty.Builder("lng", PrimitiveType.FLOAT).build())
        .property(ModelProperty.Builder("lat", PrimitiveType.STRING).build())
        .property(ModelProperty.Builder("unit", enumModel.getId()).build())
        .reference(enumModel.getId())
        .build();
    
    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();

    Entity model = (Entity)converter.convert(ModelContent.Builder(entityModel).withDependency(enumModel).build(),Optional.empty());
    assertEquals(1,model.getReferences().size());
    assertEquals(3,model.getProperties().size());
    
    Property unitProperty = model.getProperties().stream().filter(p -> p.getName().equals("unit")).findAny().get();
    ObjectPropertyType type = (ObjectPropertyType)unitProperty.getType();
    Enum unit = (Enum)type.getType();
    assertNotNull(unit);
  }
  
  @Test
  public void testConvertFunctionblock() {
    FunctionblockModel fbModel = FunctionblockModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Sensor:1.0.0"))
        .statusProperty(ModelProperty.Builder("value", PrimitiveType.FLOAT).build())
        .configurationProperty(ModelProperty.Builder("enable", PrimitiveType.BOOLEAN).build())
        .build();
    
    ModelContentToEcoreConverter converter = new ModelContentToEcoreConverter();
    
    org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel model = (org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel)converter.convert(ModelContent.Builder(fbModel).build(),Optional.empty());
    assertEquals(fbModel.getId().getNamespace(),model.getNamespace());
    assertEquals(fbModel.getId().getName(),model.getName());
    assertEquals(fbModel.getId().getVersion(),model.getVersion());
    assertEquals(fbModel.getDescription(),model.getDescription());
    assertEquals(fbModel.getDisplayName(),model.getDisplayname());
    assertEquals(fbModel.getCategory(),model.getCategory());
    assertEquals(1,model.getFunctionblock().getStatus().getProperties().size());
    assertEquals(1,model.getFunctionblock().getConfiguration().getProperties().size());
  }
}
