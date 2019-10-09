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
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.core.api.model.BuilderUtils;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.Generated;
import org.eclipse.vorto.repository.core.impl.parser.ParsingException;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class EclipseHonoJavaGeneratorTest extends AbstractGeneratorTest {

	ICodeGenerator eclipseHonoJavaGenerator = new EclipseHonoJavaGenerator();

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

		Generated generatedfile = zipFileReader(generationResult, "StatusPropertiesFunctionBlock",".java");
		File defaultFile = new File(getClass().getClassLoader()
				.getResource("defaultFileFormat/StatusPropertiesFunctionBlock.java").getFile());
		
		assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)), new String(generatedfile.getContent(),"utf-8"));
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

		Generated generatedfile = zipFileReader(generationResult, "ConfigPropertiesFunctionBlock",".java");
		File defaultFile = new File(getClass().getClassLoader()
				.getResource("defaultFileFormat/ConfigPropertiesFunctionBlock.java").getFile());
		assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)), new String(generatedfile.getContent(),"utf-8"));
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

		Generated generatedfile = zipFileReader(generationResult, "UnitEnum",".java");
		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/ValidEnum.java").getFile());
		assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)), new String(generatedfile.getContent(),"utf-8"));

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

		Generated generatedfile = zipFileReader(generationResult, "UnitEntity",".java");

		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/ValidEntity.java").getFile());
		assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)), new String(generatedfile.getContent(),"utf-8"));

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

		Generated generatedfile = zipFileReader(generationResult, "eventsAndOperationsFunctionBlock",".java");
		assertEquals(false, new String(generatedfile.getContent(),"utf-8").contains("testEvent"));
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

		Generated generatedfile = zipFileReader(generationResult, "eventsAndOperationsFunctionBlock",".java");
		assertEquals(false, new String(generatedfile.getContent(),"utf-8").contains("testOperation"));
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

		Generated generatedFileHonoDataService = zipFileReader(generationResult, "HonoDataService",".java");

		Generated generatedFileHonoMqttClient = zipFileReader(generationResult, "HonoMqttClient",".java");

		assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFileHonoDataService)), new String(generatedFileHonoDataService.getContent(),"utf-8"));
	    assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFileHonoMqttClient)), new String(generatedFileHonoMqttClient.getContent(),"utf-8"));

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

		Generated generatedfile = zipFileReader(generationResult, "MySensorApp",".java");

		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/MainApp.java").getFile());
	      assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)), new String(generatedfile.getContent(),"utf-8"));

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
