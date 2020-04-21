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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.codegen.ditto.EclipseDittoGenerator;
import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.Generated;
import org.eclipse.vorto.plugin.utils.Utils;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EclipseDittoGeneratorTest extends AbstractGeneratorTest {
  public static final String folderPath = "dsls/";
  private static final Map<String, ModelType> FILENAME_TO_TYPE_MAP = new HashMap<>();



  static {
    FILENAME_TO_TYPE_MAP.put("infomodel", ModelType.InformationModel);
    FILENAME_TO_TYPE_MAP.put("functionblock", ModelType.Functionblock);
    FILENAME_TO_TYPE_MAP.put("fbmodel", ModelType.Functionblock);
    FILENAME_TO_TYPE_MAP.put("type", ModelType.Datatype);
  }


  @BeforeClass
  public static void initParser() {
    ModelWorkspaceReader.init();
  }

  public InformationModel modelProvider(String... filenames) {
    ModelWorkspaceReader mwr = IModelWorkspace.newReader();
    for(String filename : filenames) {
      mwr.addFile(getClass().getClassLoader().getResourceAsStream(folderPath + filename),
              FILENAME_TO_TYPE_MAP.get(FilenameUtils.getExtension(filename)));
    }
    IModelWorkspace workspace = mwr.read();
    InformationModel model = (InformationModel) workspace.get().stream()
            .filter(p -> p instanceof InformationModel).findAny().get();
    return model;
  }


  public InformationModel inheritanceFunctionBlockProvider(String rootFunctionBlockName, String... functionBlocks) {
    ModelWorkspaceReader mwr = IModelWorkspace.newReader();

    for (String parentFb : functionBlocks) {
      mwr.addFile(getClass().getClassLoader().getResourceAsStream(folderPath + parentFb),
              ModelType.Functionblock);
    }
    IModelWorkspace workspace = mwr.read();
    FunctionblockModel fb = (FunctionblockModel) workspace.get().stream()
            .filter(p -> p.getName().equals(rootFunctionBlockName)).findAny().get();
    return Utils.toInformationModel(fb);
  }

  ICodeGenerator eclipseDittoGenerator = new EclipseDittoGenerator();

  /*
   * Test case for checking whether the multiple keyword generates corresponding array type in json
   *
   */
  @Test
  public void checkMultipleKeywordInFunctionBlock() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("target", "jsonSchema");
    IGenerationResult generationResult = eclipseDittoGenerator.generate(
        modelProvider("MultiplTypeIm.infomodel", "MultipleTypeFb.functionblock"),
        InvocationContext.simpleInvocationContext(params));

    Generated generatedfile = zipFileReader(generationResult, "properties-status.schema", ".json");

    File defaultFile = new File(getClass().getClassLoader()
        .getResource("defaultFileFormat/properties-status.schema.json").toURI());

    assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)),
        new String(generatedfile.getContent(), "utf-8"));

  }

  /*
   * Test case for checking whether the config properties are reflected in json
   */
  @Test
  public void checkAPIConfigPropertiesInFunctionBlock() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("target", "jsonSchema");
    IGenerationResult generationResult = eclipseDittoGenerator.generate(
        modelProvider("MultiplTypeIm.infomodel", "MultipleTypeFb.functionblock"),
        InvocationContext.simpleInvocationContext(params));

    Generated generatedfile = zipFileReader(generationResult, "properties-configuration.schema",
        ".json");

    File defaultFile = new File(getClass().getClassLoader()
        .getResource("defaultFileFormat/properties-configuration.schema.json").toURI());

    assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)),
        new String(generatedfile.getContent(), "utf-8"));

  }

  @Test
  public void checkThingStructure() throws Exception {
    Map<String, String> config = new HashMap<>();
    config.put("target", "thingJson");
    IGenerationResult generationResult = eclipseDittoGenerator.generate(
        modelProvider("MultiplTypeIm.infomodel", "MultipleTypeFb.functionblock"),
        InvocationContext.simpleInvocationContext(config));
    assertEquals("application/json", generationResult.getMediatype());
  }

  /**
   * Tests function block inheritance over two levels.
   *
   * (please be aware that the inherited properties are missing in the
   * expected result because they are added by the flattening
   * mechanism which takes place on repository side. Its not relevant for
   * this test.)
   *
   * @throws Exception
   */
  @Test
  public void testFunctionBlockInheritance() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("target", "thingJson");
    IGenerationResult generationResult = eclipseDittoGenerator.generate(
            inheritanceFunctionBlockProvider("SubTestFb", "ParentFbToBeExtended.functionblock", "SubTestFb.functionblock", "TestFb.functionblock"),
            InvocationContext.simpleInvocationContext(params));
    assertEquals("application/json", generationResult.getMediatype());

    String expectedResult = "{\n" +
            "  \"definition\": \"vortotest:SubTestFbIM:1.0.0\",\n" +
            "  \"attributes\": {\n" +
            " \t\"modelDisplayName\": \"SubTestFb\"\n" +
            " \t },\n" +
            " \t\"features\": {\n" +
            " \t\"subtestfb\" : {\n" +
            " \t\"definition\": [\n" +
            " \t\t\"vortotest:SubTestFb:1.0.0\",\n" +
            " \t\t\"vortotest:TestFb:1.0.0\",\n" +
            " \t\t\"vortotest:ParentFbToBeExtended:1.0.0\"\n" +
            " \t],\n" +
            " \t\"properties\": {\n" +
            " \t\t\"status\": {\n" +
            " \t\t\t\"myFloatInSubTestFb\" : 0.0\n" +
            " \t\t},\n" +
            "\t\"configuration\": {\n" +
            "\t\t\"temperatureSub\" : 0.0\n" +
            "}\n" +
            " \t}\n" +
            " \t}\n" +
            "}\n" +
            "}";
    JSONAssert.assertEquals(expectedResult, new String(generationResult.getContent()), true);
  }

  /**
   * Tests function block inheritance with multiple function blocks.
   * (please be aware that the inherited properties are missing in the
   * expected result because they are added by the flattening
   * mechanism which takes place on repository side. Its not relevant for
   * this test.)
   *
   * @throws Exception
   */
  @Test
  public void testFunctionBlockInheritanceMultipleFunctionBlocks() throws Exception {
    Map<String, String> params = new HashMap<>();
    params.put("target", "thingJson");
    IGenerationResult generationResult = eclipseDittoGenerator.generate(
            modelProvider("Battery.fbmodel", "Location.fbmodel", "Percentage.type", "RaspberryPi.infomodel", "SensorValue.type","Temperature.fbmodel", "Voltage.fbmodel"),
            InvocationContext.simpleInvocationContext(params));
    assertEquals("application/json", generationResult.getMediatype());

    String expectedResult = "{\n" +
            "  \"definition\": \"org.eclipse.vorto.tutorials:RaspberryPi:1.0.0\",\n" +
            "  \"attributes\": {\n" +
            " \t\"modelDisplayName\": \"RaspberryPi\"\n" +
            " \t },\n" +
            " \t\"features\": {\n" +
            " \t\"location\" : {\n" +
            " \t\"definition\": [\n" +
            " \t \t\t\"org.eclipse.vorto:Location:1.0.0\"\n" +
            " \t],\n" +
            " \t\"properties\": {\n" +
            " \t\t\"status\": {\n" +
            " \t\t\t\"latitude\" : 0.0,\n" +
            " \t\t\t\"longitude\" : 0.0\n" +
            " \t\t}\n" +
            " \t}\n" +
            " \t},\n" +
            " \t\"battery\" : {\n" +
            " \t\"definition\": [\n" +
            " \t \t\t\"org.eclipse.vorto:Battery:1.0.0\",\n" +
            " \t \t\t\"org.eclipse.vorto:Voltage:1.0.0\"\n" +
            " \t],\n" +
            " \t\"properties\": {\n" +
            " \t\t\"status\": {\n" +
            " \t\t\t\"remainingCapacity\" : {\n" +
            " \t\t\t\t\"value\" : 0.0\n" +
            " \t\t\t}\n" +
            " \t\t},\n" +
            "\t\"configuration\": {\n" +
            "\t\t\"remainingCapacityAmpHour\" : 0.0\n" +
            "}\n" +
            " \t}\n" +
            " \t},\n" +
            " \t\"cpuTemperature\" : {\n" +
            " \t\"definition\": [\n" +
            " \t \t\t\"org.eclipse.vorto:Temperature:1.0.0\"\n" +
            " \t],\n" +
            " \t\"properties\": {\n" +
            " \t\t\"status\": {\n" +
            " \t\t\t\"value\" : {\n" +
            " \t\t\t\t\"currentMeasured\" : 0.0,\n" +
            " \t\t\t\t\"minMeasured\" : 0.0,\n" +
            " \t\t\t\t\"maxMeasured\" : 0.0\n" +
            " \t\t\t}\n" +
            " \t\t}\n" +
            " \t}\n" +
            " \t}\n" +
            "}\n" +
            "}";

    JSONAssert.assertEquals(expectedResult, new String(generationResult.getContent()), true);
  }

  @Test
  public void testNonInheritanceFunctionBlock() throws GeneratorException, JSONException {

    BuilderUtils.EnumBuilder enumBuilder = BuilderUtils.newEnum(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Datatype, "Units", "org.eclipse.vorto.types", "1.0.0"));
    enumBuilder.withLiterals("F", "C");

    FunctionblockModel fbm = BuilderUtils.newFunctionblock(
            new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.Functionblock, "Temperature", "org.eclipse.vorto", "1.0.0")).withStatusProperty("unit", enumBuilder.build()).build();

    BuilderUtils.InformationModelBuilder informationModelBuilder = BuilderUtils.newInformationModel(
            new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.InformationModel, "RPi", "org.eclipse.vorto", "1.0.0"));
    informationModelBuilder.withFunctionBlock(fbm, "cpuTemperature", null, false);

    IGenerationResult generationResult = eclipseDittoGenerator.generate(informationModelBuilder.build(), InvocationContext.simpleInvocationContext());

    String expectedResult = "{\n" +
            "  \"definition\": \"org.eclipse.vorto:RPi:1.0.0\",\n" +
            "  \"attributes\": {\n" +
            " \t\"modelDisplayName\": \"RPi\"\n" +
            " \t },\n" +
            " \t\"features\": {\n" +
            " \t\"cpuTemperature\" : {\n" +
            " \t\"definition\": [\n" +
            " \t\t\"org.eclipse.vorto:Temperature:1.0.0\"\n" +
            " \t],\n" +
            " \t\"properties\": {\n" +
            " \t\t\"status\": {\n" +
            " \t\t\t\"unit\" : \"F\"\n" +
            " \t\t}\n" +
            " \t}\n" +
            " \t}\n" +
            "}\n" +
            "}";

    JSONAssert.assertEquals(expectedResult, new String(generationResult.getContent()), true);
  }

}
