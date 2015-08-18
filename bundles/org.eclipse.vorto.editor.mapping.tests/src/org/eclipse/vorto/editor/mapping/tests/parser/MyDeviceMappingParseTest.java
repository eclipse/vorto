/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.editor.mapping.tests.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.vorto.core.api.model.mapping.DataTypeMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.junit.Before;
import org.junit.Test;

public class MyDeviceMappingParseTest {
	private static final String EXAMPLES_DIRECTORY = "resources/com/mycompany/map/";

	@Before
	public void setup() {
		MappingStandaloneSetup.doSetup();
	}
	
	@Test
	public void parseMappingBasicAttributes() throws IOException {
		MappingModel mappingModel = createMappingModel("AllInOne.mapping");
		assertEquals("Name is wrong", "AllInOne",
				mappingModel.getName());

		EList<DataTypeMapping> dataRules = mappingModel.getDataTypeMappings();
		EList<FunctionBlockMapping> fbRules = mappingModel.getFunctionBlockMappings();
		EList<InfoModelMappingRule> infoModelMappingRule = mappingModel.getInfoModelMappingRules();
		assertEquals(2, dataRules.size());
		assertEquals(1, fbRules.size());
		assertEquals(1, infoModelMappingRule.size());
	}
	
	@Test
	public void parseMappingWithDataTypeAttribute() throws IOException {
		MappingModel mappingModel = createMappingModel("DataType_Attribute.mapping");

		EList<DataTypeMapping> rules = mappingModel.getDataTypeMappings();
		assertEquals(1, rules.size());
		assertEquals(3, rules.get(0).getDatatypeMappingRules().size());
	}
	
	@Test
	public void parseMappingWithDataType_Nested() throws IOException {
		MappingModel mappingModel = createMappingModel("DataType_Nested.mapping");

		EList<DataTypeMapping> rules = mappingModel.getDataTypeMappings();
		assertEquals(1, rules.size());
		assertEquals(1, rules.get(0).getDatatypeMappingRules().size());
	}
	
	@Test
	public void parseMappingWithDataType_Ref() throws IOException {
		MappingModel mappingModel = createMappingModel("DataType_Ref.mapping");

		EList<DataTypeMapping> rules = mappingModel.getDataTypeMappings();
		assertEquals(2, rules.size());
		assertEquals(1, rules.get(0).getDatatypeMappingRules().size());
	}
	
	@Test
	public void parseMappingWithFunctionBlock_Attribute() throws IOException {
		MappingModel mappingModel = createMappingModel("FunctionBlock_Attribute.mapping");

		EList<FunctionBlockMapping> rules = mappingModel.getFunctionBlockMappings();
		assertEquals(1, rules.size());
		assertEquals(6, rules.get(0).getFunctionBlockMappingRules().size());
	}
	
	@Test
	public void parseMappingWithFunctionBlock_Property() throws IOException {
		MappingModel mappingModel = createMappingModel("FunctionBlock_Property.mapping");

		EList<FunctionBlockMapping> rules = mappingModel.getFunctionBlockMappings();
		assertEquals(1, rules.size());
		assertEquals(6, rules.get(0).getFunctionBlockMappingRules().size());
	}
	
	@Test
	public void parseMappingWithFunctionBlock_Ref() throws IOException {
		MappingModel mappingModel = createMappingModel("FunctionBlock_Ref.mapping");

		EList<FunctionBlockMapping> rules = mappingModel.getFunctionBlockMappings();
		assertEquals(1, rules.size());
		assertEquals(1, rules.get(0).getFunctionBlockMappingRules().size());
	}
	
	@Test
	public void parseMappingWithInfomodel_Attribute() throws IOException {
		MappingModel mappingModel = createMappingModel("Infomodel_Attribute.mapping");

		EList<InfoModelMappingRule> rules = mappingModel.getInfoModelMappingRules();
		assertEquals(6, rules.size());
	}
	
	@Test
	public void parseMappingWithInfomodel_FBRef() throws IOException {
		MappingModel mappingModel = createMappingModel("Infomodel_FBRef.mapping");

		EList<InfoModelMappingRule> rules = mappingModel.getInfoModelMappingRules();
		assertEquals(6, rules.size());
	}
	
	@Test
	public void parseMappingWithInfomodel_FunctionBlock() throws IOException {
		MappingModel mappingModel = createMappingModel("Infomodel_FunctionBlock.mapping");

		EList<InfoModelMappingRule> rules = mappingModel.getInfoModelMappingRules();
		assertEquals(1, rules.size());
	}
	
	/*@Test
	public void testParsingInfoModelMapping() throws IOException {
		MappingModel mappingModel = createMappingModel("DummyDeviceMapping.mapping");
		assertEquals("Name is wrong", "DummyDeviceMapping",
				mappingModel.getName());

		EList<InfoModelMappingRule> rules = mappingModel.getInfoModelMappingRules();
		assertEquals(11, rules.size());
	}

	@Test
	public void testParsingFunctionBlockModelMapping() throws IOException {
		MappingModel mappingModel = createMappingModel("DummyFunctionBlockMapping.mapping");
		assertEquals("Name is wrong", "DummyFunctionBlockMapping",
				mappingModel.getName());
		EList<FunctionBlockMappingRule> rules = mappingModel.getFunctionBlockMappingRules();
		assertEquals(8, rules.size());
	}
	
	@Test
	public void testParsingDataTypeModelMapping() throws IOException {
		MappingModel mappingModel = createMappingModel("DummyDataTypeMapping.mapping");
		assertEquals("Name is wrong", "DummyDataTypeMapping",
				mappingModel.getName());

		EList<DataTypeMappingRule> rules = mappingModel.getDataTypeMappingRules();
		assertEquals(6, rules.size());
	}
	
	@Test
	public void testParsingAllInOneModelMapping() throws IOException {
		MappingModel mappingModel = createMappingModel("MyPlatformMapping.mapping");
		assertEquals("Name is wrong", "MyPlatformMapping",
				mappingModel.getName());

		assertEquals(11, mappingModel.getInfoModelMappingRules().size());
		assertEquals(8, mappingModel.getFunctionBlockMappingRules().size());
		assertEquals(6, mappingModel.getDataTypeMappingRules().size());
	}
	*/
	private MappingModel createMappingModel(String mappingFileName) throws IOException{
		ResourceSet rset = new ResourceSetImpl();
		rset.getPackageRegistry().put(MappingPackage.eNS_URI,
				MappingPackage.eINSTANCE);
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());
		String exampleSmarthomeMappingFile = EXAMPLES_DIRECTORY
				+ mappingFileName;
		Resource resource = rset.getResource(
				URI.createFileURI(exampleSmarthomeMappingFile), true);
		resource.load(null);
		MappingModel mappingModel = (MappingModel) resource.getContents()
				.get(0);
		return mappingModel;
	}
}