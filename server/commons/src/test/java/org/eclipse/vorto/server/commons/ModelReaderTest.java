/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.server.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.server.commons.reader.IModelWorkspace;
import org.junit.Test;

public class ModelReaderTest {

	@Test
	public void testReadFromFile() {
		IModelWorkspace workspace = IModelWorkspace.newReader()
		.addFile(getClass().getClassLoader().getResourceAsStream("dsls/com.example_AWSIoTButton_1_0_0.infomodel"),ModelType.InformationModel)
		.addFile(getClass().getClassLoader().getResourceAsStream("dsls/com.example.aws_AWSButtonMapping_1_0_0.mapping"),ModelType.Mapping)
		.addFile(getClass().getClassLoader().getResourceAsStream("dsls/com.example.aws_Button1Mapping_1_0_0.mapping"),ModelType.Mapping)
		.addFile(getClass().getClassLoader().getResourceAsStream("dsls/com.example.aws_Button2Mapping_1_0_0.mapping"),ModelType.Mapping)
		.addFile(getClass().getClassLoader().getResourceAsStream("dsls/com.ipso.smartobjects_Push_button_0_0_1.fbmodel"),ModelType.Functionblock).read();
		
		InformationModel model = (InformationModel)workspace.get().stream().filter(p -> p instanceof InformationModel).findAny().get();
		assertNotNull(model);
		assertEquals("AWSIoTButton",model.getName());
		
		assertEquals("AWSButtonMapping",workspace.get().stream().filter(p -> p.getName().equals("AWSButtonMapping")).findAny().get().getName());
	}
	
	@Test
	public void testReadInfomodelFromZipFile() throws Exception {
		IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("models.zip"))).read();
		Model model = workspace.get().stream().filter(p -> p.getName().equals("TI_SensorTag_CC2650")).findAny().get();
		assertNotNull(model);
		assertTrue(model instanceof InformationModel);
	}
	
	@Test
	public void testReadFunctionblockModelsFromZipFile() {
		IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("models.zip"))).read();
		assertEquals(2,workspace.get().stream().filter(p -> p instanceof FunctionblockModel).collect(Collectors.toList()).size());
	}
	
	@Test
	public void testMappingFromZipFile() {
		IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("mappings.zip"))).read();
		Model model = workspace.get().stream().filter(p -> p instanceof MappingModel).findAny().get();
		assertNotNull(model);
		assertTrue(model instanceof MappingModel);
		assertEquals("Accelerometer_Mapping",model.getName());
	}
	
	@Test
	public void testReadMultipleZipFiles() {
		IModelWorkspace workspace = IModelWorkspace.newReader()
				.addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("mappings.zip")))
				.addZip(new ZipInputStream(getClass().getClassLoader().getResourceAsStream("models.zip")))
				.read();
		
		assertEquals(10,workspace.get().size());
	}
}
