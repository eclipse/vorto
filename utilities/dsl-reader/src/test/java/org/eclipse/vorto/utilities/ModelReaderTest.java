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
package org.eclipse.vorto.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;
import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelReaderTest {

  @BeforeClass
  public static void initParser() {
    ModelWorkspaceReader.init();
  }

  @Test
  public void testReadFromFile() {
    IModelWorkspace workspace =
        IModelWorkspace.newReader()
            .addFile(getClass().getClassLoader().getResourceAsStream(
                "dsls/com.example_AWSIoTButton_1_0_0.infomodel"), ModelType.InformationModel)
            .addFile(getClass().getClassLoader().getResourceAsStream(
                "dsls/com.example.aws_AWSButtonMapping_1_0_0.mapping"), ModelType.Mapping)
            .addFile(getClass().getClassLoader().getResourceAsStream(
                "dsls/com.example.aws_Button1Mapping_1_0_0.mapping"), ModelType.Mapping)
            .addFile(getClass().getClassLoader().getResourceAsStream(
                "dsls/com.example.aws_Button2Mapping_1_0_0.mapping"), ModelType.Mapping)
            .addFile(
                getClass().getClassLoader()
                    .getResourceAsStream("dsls/com.ipso.smartobjects_Push_button_0_0_1.fbmodel"),
                ModelType.Functionblock)
            .read();

    InformationModel model = (InformationModel) workspace.get().stream()
        .filter(p -> p instanceof InformationModel).findAny().get();
    assertNotNull(model);
    assertEquals("AWSIoTButton", model.getName());

    assertEquals("AWSButtonMapping", workspace.get().stream()
        .filter(p -> p.getName().equals("AWSButtonMapping")).findAny().get().getName());
  }

  @Test
  public void testReadFromFile_Encoding() {
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/Color_encoding.type"),
            ModelType.Datatype)
        .read();

    Entity model =
        (Entity) workspace.get().stream().filter(p -> p instanceof Entity).findAny().get();
    assertNotNull(model);
    assertEquals("Überraschung", model.getProperties().get(0).getDescription());
  }

  @Test
  public void testReadInfomodelFromZipFile() throws Exception {
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("models.zip")))
        .read();
    Model model = workspace.get().stream().filter(p -> p.getName().equals("TI_SensorTag_CC2650"))
        .findAny().get();
    assertNotNull(model);
    assertTrue(model instanceof InformationModel);
  }

  @Test
  public void testReadFunctionblockModelsFromZipFile() {
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("models.zip")))
        .read();
    assertEquals(2, workspace.get().stream().filter(p -> p instanceof FunctionblockModel)
        .collect(Collectors.toList()).size());
  }

  @Test
  public void testMappingFromZipFile() {
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("mappings.zip")))
        .read();
    Model model = workspace.get().stream().filter(p -> p instanceof MappingModel).findAny().get();
    assertNotNull(model);
    assertTrue(model instanceof MappingModel);
    assertEquals("Accelerometer_Mapping", model.getName());
  }

  @Test
  public void testReadMultipleZipFiles() {
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("mappings.zip")))
        .addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("models.zip")))
        .read();

    assertEquals(10, workspace.get().size());
  }

  @Test
  public void testFlatInheritanceFB() {
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/SomeFb.fbmodel"),
            ModelType.Functionblock)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/SuperFb.fbmodel"),
            ModelType.Functionblock)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/SuperSuperFb.fbmodel"),
            ModelType.Functionblock)
        .read();

    FunctionblockModel fbm =
        ModelConversionUtils.convertToFlatHierarchy((FunctionblockModel) workspace.get().get(0));
    assertEquals("SomeFb", fbm.getName());
    assertEquals(4, fbm.getFunctionblock().getStatus().getProperties().size());
    assertEquals(3, fbm.getFunctionblock().getConfiguration().getProperties().size());
    assertEquals(2, fbm.getFunctionblock().getOperations().size());
  }

  @Test
  public void testFlatInheritanceIM() {
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/TestModel.infomodel"),
            ModelType.InformationModel)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/SomeFb.fbmodel"),
            ModelType.Functionblock)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/SuperFb.fbmodel"),
            ModelType.Functionblock)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/SuperSuperFb.fbmodel"),
            ModelType.Functionblock)
        .read();

    InformationModel infomodel =
        ModelConversionUtils.convertToFlatHierarchy((InformationModel) workspace.get().get(0));
    assertEquals("TestModel", infomodel.getName());

    assertEquals(4, infomodel.getProperties().get(0).getType().getFunctionblock().getStatus()
        .getProperties().size());
    assertEquals(3, infomodel.getProperties().get(0).getType().getFunctionblock().getConfiguration()
        .getProperties().size());
    assertEquals(2,
        infomodel.getProperties().get(0).getType().getFunctionblock().getOperations().size());

    Property statusProperty =
        infomodel.getProperties().get(0).getType().getFunctionblock().getStatus().getProperties()
            .stream().filter(p -> p.getName().equals("statusProp")).findFirst().get();

    assertEquals(2, statusProperty.getConstraintRule().getConstraints().size());
  }

  @Test
  public void testFlatInheritanceFBwithDatatypes() {
    IModelWorkspace workspace = IModelWorkspace.newReader()
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/Brightness.type"),
            ModelType.Datatype)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/Light.type"),
            ModelType.Datatype)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/ColorLight.type"),
            ModelType.Datatype)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/TestModel.infomodel"),
            ModelType.InformationModel)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/SomeFb.fbmodel"),
            ModelType.Functionblock)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/SuperFb.fbmodel"),
            ModelType.Functionblock)
        .addFile(getClass().getClassLoader().getResourceAsStream("dsls/SuperSuperFb.fbmodel"),
            ModelType.Functionblock)
        .read();

    InformationModel infomodel =
        ModelConversionUtils.convertToFlatHierarchy((InformationModel) workspace.get().get(3));
    assertEquals("TestModel", infomodel.getName());

    Property colorLightProperty =
        infomodel.getProperties().get(0).getType().getFunctionblock().getStatus().getProperties()
            .stream().filter(p -> p.getName().equals("light")).findFirst().get();
    assertNotNull(colorLightProperty);

    ObjectPropertyType type = (ObjectPropertyType) colorLightProperty.getType();
    Entity colorLight = (Entity) type.getType();
    assertEquals(2, colorLight.getProperties().size());
    assertEquals(1, colorLight.getReferences().size());

    assertEquals(1, infomodel.getProperties().get(0).getType().getReferences().size());
    assertEquals("iot.ColorLight",
        infomodel.getProperties().get(0).getType().getReferences().get(0).getImportedNamespace());
  }

}
