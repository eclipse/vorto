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
package org.eclipse.vorto.codegen.hono.arduino;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.ICodeGenerator;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.junit.Test;

public class EclipseHonoArduinoGeneratorTest extends AbstractGeneratorTest {

	ICodeGenerator eclipseArduinoGenerator = new ArduinoCodeGenerator();

	/*
	 * -----Below test cases are specific to API related files-------
	 */

	/*
	 * Test case for checking whether the functionblock related file has the status
	 * properties both primitive and object with the setters and getters
	 * 
	 */
	@Test
	public void checkAPIStatusPropertiesInFunctionBlock() throws Exception {
		IGenerationResult generationResult = eclipseArduinoGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "StatusPropertiesFunctionBlock.cpp", ".cpp");

		File defaultFile = new File(getClass().getClassLoader()
				.getResource("defaultFileFormat/StatusPropertiesFunctionBlock.cpp").getFile());

		assertEquals(true, FileUtils.contentEquals(generatedfile, defaultFile));
		generatedfile.delete();

	}

	/*
	 * Test case for checking whether the functionblock related file has the config
	 * properties both primitive and object with the setters and getters
	 * 
	 */
	@Test
	public void checkAPIConfigPropertiesInFunctionBlock() throws Exception {
		IGenerationResult generationResult = eclipseArduinoGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "ConfigPropertiesFunctionBlock.cpp", ".cpp");

		File defaultFile = new File(getClass().getClassLoader()
				.getResource("defaultFileFormat/ConfigPropertiesFunctionBlock.cpp").getFile());

		assertEquals(true, FileUtils.contentEquals(generatedfile, defaultFile));
		generatedfile.delete();

	}

	/*
	 * Test case for checking whether the datatype related file has the entity and
	 * enum values specified in the input datatype file.
	 * 
	 */
	@Test
	public void checkAPIGenerationOfEntityModel() throws Exception {

		IGenerationResult generationResult = eclipseArduinoGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "UnitEntity.cpp", ".cpp");

		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/UnitEntity.cpp").getFile());

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
		IGenerationResult generationResult = eclipseArduinoGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "eventsAndOperationsFunctionBlock.cpp", ".cpp");

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
		IGenerationResult generationResult = eclipseArduinoGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "eventsAndOperationsFunctionBlock.cpp", ".cpp");

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
		IGenerationResult generationResult = eclipseArduinoGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedFile = zipFileReader(generationResult, "MySensorApp.ino", ".ino");

		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/MySensorApp.ino").getFile());

		assertEquals(true, FileUtils.contentEquals(generatedFile, defaultFile));

		generatedFile.delete();

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
		IGenerationResult generationResult = eclipseArduinoGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		File generatedfile = zipFileReader(generationResult, "MySensor.cpp", ".cpp");

		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/MySensor.cpp").getFile());

		assertEquals(true, FileUtils.contentEquals(generatedfile, defaultFile));
		generatedfile.delete();

	}

}
