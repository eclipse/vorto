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
package org.eclipse.vorto.codegen.hono.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.util.FunctionblockAdapterFactory;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.utils.Utils;
import org.eclipse.vorto.repository.core.impl.parser.ParsingException;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class EclipseHonoJavaGeneratorTest extends AbstractGeneratorTest {

	ICodeGenerator eclipseHonoJavaGenerator = new EclipseHonoJavaGenerator();

	@BeforeClass
	public static void initParser() {
		ModelWorkspaceReader.init();
	}

	/*
	 * -----Below test cases are specific to API related files-------
	 */

	/*
	 * Test case for checking whether the functionblock related java file has the
	 * status properties both primitive and object with the setters and getters
	 * 
	 */
	@Test
	public void checkAPIStatusPropertiesInFunctionBlock() throws Exception {
		IGenerationResult generationResult = eclipseHonoJavaGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "StatusPropertiesFunctionBlock");
		File defaultFile = new File(getClass().getClassLoader()
				.getResource("defaultFileFormat/StatusPropertiesFunctionBlock.java").getFile());
		assertEquals(true, FileUtils.contentEquals(generatedfile, defaultFile));
		generatedfile.delete();
	}

	/*
	 * Test case for checking whether the functionblock related java file has the
	 * config properties both primitive and object with the setters and getters
	 * 
	 */
	@Test
	public void checkAPIConfigPropertiesInFunctionBlock() throws Exception {
		IGenerationResult generationResult = eclipseHonoJavaGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "ConfigPropertiesFunctionBlock");
		File defaultFile = new File(getClass().getClassLoader()
				.getResource("defaultFileFormat/ConfigPropertiesFunctionBlock.java").getFile());
		assertEquals(true, FileUtils.contentEquals(generatedfile, defaultFile));
		generatedfile.delete();
	}

	/*
	 * Test case for checking whether the datatype related java file has the enum
	 * values specified in the input datatype file.
	 * 
	 */
	@Test
	public void checkAPIGenerationOfEnumModel() throws Exception {
		IGenerationResult generationResult = eclipseHonoJavaGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "UnitEnum");
		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/ValidEnum.java").getFile());
		assertEquals(true, FileUtils.contentEquals(generatedfile, defaultFile));
		generatedfile.delete();

	}

	/*
	 * Test case for checking whether the datatype related java file has the entity
	 * values specified in the input datatype file.
	 * 
	 */
	@Test
	public void checkAPIGenerationOfEntityModel() throws Exception {

		IGenerationResult generationResult = eclipseHonoJavaGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "UnitEntity");

		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/ValidEntity.java").getFile());
		assertEquals(true, FileUtils.contentEquals(generatedfile, defaultFile));
		generatedfile.delete();

	}

	/*
	 * Test case for checking whether the events specified in the functionblock is
	 * not reflected in the generated files. This is because we currently do not
	 * support events.
	 * 
	 */
	@Test
	public void testAPIFunctionBlockWithEvents() throws Exception {
		IGenerationResult generationResult = eclipseHonoJavaGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "eventsAndOperationsFunctionBlock");
		assertEquals(false, FileUtils.readFileToString(generatedfile).contains("testEvent"));
		generatedfile.delete();
	}

	/*
	 * Test case for checking whether the operations specified in the functionblock
	 * is not reflected in the generated files. This is because we currently do not
	 * support events.
	 * 
	 */
	@Test
	public void testAPIFunctionBlockWithOperations() throws Exception {
		IGenerationResult generationResult = eclipseHonoJavaGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "eventsAndOperationsFunctionBlock");
		assertEquals(false, FileUtils.readFileToString(generatedfile).contains("testOperation"));
		generatedfile.delete();
	}

	/*
	 * -----Below test cases are specific to Connector related files-------
	 */

	/*
	 * Test case for checking whether the MQTT client generated contains the models
	 * which are listed in the information model
	 * 
	 */
	@Test
	public void testConnectorGeneratedMQTTClient() throws GeneratorException, IOException {
		IGenerationResult generationResult = eclipseHonoJavaGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File defaultFileHonoDataService = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/HonoDataService_MQTT.java").getFile());

		File defaultFileHonoMqttClient = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/HonoMqttClient.java").getFile());

		File generatedFileHonoDataService = zipFileReader(generationResult, "HonoDataService");

		File generatedFileHonoMqttClient = zipFileReader(generationResult, "HonoMqttClient");

		assertEquals(true, FileUtils.contentEquals(generatedFileHonoDataService, defaultFileHonoDataService));
		assertEquals(true, FileUtils.contentEquals(generatedFileHonoMqttClient, defaultFileHonoMqttClient));

		generatedFileHonoDataService.delete();
		generatedFileHonoMqttClient.delete();

	}

	/*
	 * -----Below test cases are specific to Client App related files-------
	 */

	/*
	 * Test case for checking whether the client App generated contains the models
	 * which are listed in the information model
	 * 
	 */
	@Test
	public void testClientAppGeneratedModels() throws GeneratorException, IOException {
		IGenerationResult generationResult = eclipseHonoJavaGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "MySensorApp");

		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/MainApp.java").getFile());
		assertEquals(true, FileUtils.contentEquals(generatedfile, defaultFile));
		generatedfile.delete();
	}

	/*
	 * Test case for checking infomodel with empty namespace
	 */
	@Ignore // Issue created https://github.com/eclipse/vorto/issues/1885
	@Test(expected = ParsingException.class)
	public void checkAPIEmptyNamespaceInfomodelHono() throws Exception {
		InformationModel emptyNameSpaceInfomodel = BuilderUtils
				.newInformationModel(new ModelId(org.eclipse.vorto.core.api.model.model.ModelType.InformationModel,
						"emptyNameSpaceInfomodel", "", "1.0.0"))
				.build();
		IGenerationResult generationResult = eclipseHonoJavaGenerator.generate(emptyNameSpaceInfomodel,
				InvocationContext.simpleInvocationContext());
	}

}
