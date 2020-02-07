/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.plugin;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.plugin.utils.Utils;
import org.junit.Test;

public class UtilsTest extends AbstractGeneratorTest {

  /*
   * Check if InformationModel is returned for inputs InformationModel and FunctionBlock
   */
  @Test
  public void wrapInformationModelOrFunctionBlock() throws Exception {
    /*
     * Information Model passed
     */
    InformationModel infoModel = Utils.toInformationModel(informationModelProvider());
    assertEquals(true, infoModel instanceof InformationModel);

    /*
     * FunctionBlock passed
     */
    infoModel = Utils.toInformationModel(statusPropertyProvider());
    assertEquals(true, infoModel instanceof InformationModel);

  }

  /*
   * Check the referenced types of FunctionBlock
   */
  @Test
  public void checkReferencedTypesFunctionBlock() throws Exception {
    List entityList = null;
    /*
     * Status property function block
     */
    entityList = Utils.getReferencedTypes(statusPropertyProvider().getFunctionblock());
    assertEquals(4, entityList.size());
    /*
     * Config property function block
     */
    entityList = Utils.getReferencedTypes(configPropertyProvider().getFunctionblock());
    assertEquals(4, entityList.size());
    /*
     * Events in  function block
     */
    entityList = Utils.getReferencedTypes(eventFunctionBlockProvider().getFunctionblock());
    assertEquals(0, entityList.size());
  }

  /*
   * Check the referenced entities of FunctionBlock
   */
  @Test
  public void checkReferencedEntityFunctioBlock() throws Exception {
    List entityList = Utils.getReferencedEntities(statusPropertyProvider().getFunctionblock());
    assertEquals(1, entityList.size());
  }

  /*
   * Check the referenced enums of FunctionBlock
   */
  @Test
  public void checkReferencedEnumFunctioBlock() throws Exception {
    List enumList = Utils.getReferencedEnums(statusPropertyProvider().getFunctionblock());
    assertEquals(1, enumList.size());
  }

  /*
   * Check the referenced types
   */
  @Test
  public void checkReferencedTypes() throws Exception {
    List enumList = Utils.getReferencedTypes(entityProvider());
    assertEquals(3, enumList.size());
  }



  public FunctionblockModel eventFunctionBlockProvider() {
    Event eventBlock = BuilderUtils.newEvent("testEvent").build();

    FunctionblockModel eventsAndOperationsFunctionBlock = BuilderUtils
        .newFunctionblock(
            new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Functionblock,
                "eventsAndOperationsFunctionBlock", "org.eclipse.vorto", "1.0.0"))
        .withEvent(eventBlock).withOperation("testOperation", null, null, true, null).build();

    return eventsAndOperationsFunctionBlock;

  }

  public Entity entityProvider() {
    Enum _enum =
        BuilderUtils.newEnum(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Datatype,
            "UnitEnum", "org.eclipse.vorto", "1.0.0")).withLiterals("KG", "G").build();

    Entity _entity = BuilderUtils
        .newEntity(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Datatype,
            "UnitEntity", "org.eclipse.vorto", "1.0.0"))
        .withProperty("value", PrimitiveType.FLOAT).withProperty("unitEnum", _enum).build();
    return _entity;
  }

  public FunctionblockModel statusPropertyProvider() {

    FunctionblockModel statusPropertiesFunctionBlock = BuilderUtils
        .newFunctionblock(
            new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Functionblock,
                "StatusPropertiesFunctionBlock", "org.eclipse.vorto", "1.0.0"))
        .withStatusProperty("statusValue", entityProvider())
        .withStatusProperty("statusBoolean", PrimitiveType.BOOLEAN).build();

    return statusPropertiesFunctionBlock;
  }

  public FunctionblockModel configPropertyProvider() {

    FunctionblockModel statusPropertiesFunctionBlock = BuilderUtils
        .newFunctionblock(
            new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Functionblock,
                "StatusPropertiesFunctionBlock", "org.eclipse.vorto", "1.0.0"))
        .withConfiguration("configValue", entityProvider())
        .withConfiguration("configBoolean", PrimitiveType.BOOLEAN).build();

    return statusPropertiesFunctionBlock;
  }

  public InformationModel informationModelProvider() {
    InformationModel infomodel = BuilderUtils
        .newInformationModel(
            new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.InformationModel,
                "MySensor", "org.eclipse.vorto", "1.0.0"))
        .withFunctionBlock(statusPropertyProvider(), "statusPropertiesFunctionBlock", "", false)
        .build();
    return infomodel;
  }
}
