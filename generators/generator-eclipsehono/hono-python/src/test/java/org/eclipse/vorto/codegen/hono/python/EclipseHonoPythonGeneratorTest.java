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
package org.eclipse.vorto.codegen.hono.python;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.plugin.AbstractGeneratorTest;
import org.eclipse.vorto.plugin.generator.GeneratorException;
import org.eclipse.vorto.plugin.generator.IGenerationResult;
import org.eclipse.vorto.plugin.generator.InvocationContext;
import org.eclipse.vorto.plugin.generator.utils.Generated;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class EclipseHonoPythonGeneratorTest extends AbstractGeneratorTest {

	PythonGenerator pythonGenerator = new PythonGenerator();
	

	/*
	 * -----Below test cases are specific to API related files-------
	 */

	/*
	 * Test case for checking whether the functionblock related python file has the
	 * status properties both primitive and object with the setters and getters
	 * 
	 */
	@Test
	public void checkAPIStatusPropertiesInFunctionBlock() throws Exception {
		IGenerationResult generationResult = pythonGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		Generated generatedfile = zipFileReader(generationResult, "StatusPropertiesFunctionBlock",".py");
		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/StatusPropertiesFunctionBlock.py").getFile());
	    assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)), new String(generatedfile.getContent(),"utf-8"));

	}

	/*
	 * Test case for checking whether the functionblock related python file has the
	 * config properties both primitive and object with the setters and getters
	 * 
	 */
	@Test
	public void checkAPIConfigPropertiesInFunctionBlock() throws Exception {
		IGenerationResult generationResult = pythonGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		Generated generatedfile = zipFileReader(generationResult, "ConfigPropertiesFunctionBlock",".py");
		File defaultFile = new File(getClass().getClassLoader()
				.getResource("defaultFileFormat/ConfigPropertiesFunctionBlock.py").getFile());
	    assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)), new String(generatedfile.getContent(),"utf-8"));

	}


	/*
	 * Test case for checking whether the entity/enum
	 * values specified in the input datatype file are available.
	 * 
	 */
	@Test
	public void checkAPIGenerationOfEntityModel() throws Exception {

		IGenerationResult generationResult = pythonGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		Generated generatedfile = zipFileReader(generationResult, "MySensorApp",".py");
		assertEquals(true, new String(generatedfile.getContent(),"utf-8").contains("unitEnum"));
	}

	/*
	 * Test case for checking whether the events specified in the functionblock is
	 * not reflected in the generated files. This is because we currently do not
	 * support events.
	 * 
	 */
	@Test
	public void testAPIFunctionBlockWithEvents() throws Exception {
		IGenerationResult generationResult = pythonGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		Generated generatedfile = zipFileReader(generationResult, "eventsAndOperationsFunctionBlock",".py");
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
		IGenerationResult generationResult = pythonGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		Generated generatedfile = zipFileReader(generationResult, "eventsAndOperationsFunctionBlock",".py");
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
		
		IGenerationResult generationResult = pythonGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());

		Generated generatedFileHonoMqttClient = zipFileReader(generationResult, "DittoSerializer",".py");
		File defaultFileHonoMqttClient = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/DittoSerializer.py").getFile());
		
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
		IGenerationResult generationResult = pythonGenerator.generate(modelProvider(),
				InvocationContext.simpleInvocationContext());
		Generated generatedfile = zipFileReader(generationResult, "MySensorApp",".py");
		
		File defaultFile = new File(
				getClass().getClassLoader().getResource("defaultFileFormat/MainApp.py").getFile());
	    assertEquals(IOUtils.toString(FileUtils.openInputStream(defaultFile)), new String(generatedfile.getContent(),"utf-8"));

	}



}
