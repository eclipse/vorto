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
import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EntitySourceElement;
import org.eclipse.vorto.core.api.model.mapping.EntityTargetElement;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.junit.Before;
import org.junit.Test;

public class MappingModelSyntaxTest {
	private static final String EXAMPLES_DIRECTORY = "resources/com/mycompany/map/";

	@Before
	public void setup() {
		MappingStandaloneSetup.doSetup();
	}
	
	
	@Test
	public void parseMappingWithEntityAttribute() throws IOException {
		MappingModel mappingModel = createMappingModel("Entity_Attribute.mapping");

		EList<EntityMappingRule> rules = ((EntityMapping)mappingModel.getMapping()).getEntityMappingRules();
		assertEquals(3, rules.size());
	}
	
	@Test
	public void parseMappingWithEntityProperty() throws IOException {
		MappingModel mappingModel = createMappingModel("Entity_Property.mapping");

		EList<EntityMappingRule> rules = ((EntityMapping)mappingModel.getMapping()).getEntityMappingRules();
		EntityMappingRule rule = rules.get(0);
		EntitySourceElement entitySourceElement = rule.getEntitySourceElement().get(0);
		EntityTargetElement entityTargetElement = rule.getTargetElement();
		assertEquals(1, rules.size());
	}
	
	@Test
	public void parseMappingWithEntityReference() throws IOException {
		MappingModel mappingModel = createMappingModel("Entity_Ref.mapping");

		EList<EntityMappingRule> rules = ((EntityMapping)mappingModel.getMapping()).getEntityMappingRules();
		assertEquals(1, rules.size());
	}
	
	@Test
	public void parseMappingWithEnumAttribute() throws IOException {
		MappingModel mappingModel = createMappingModel("Enum_Attribute.mapping");

		EList<EnumMappingRule> rules = ((EnumMapping)mappingModel.getMapping()).getEnumMappingRules();
		assertEquals(3, rules.size());
	}
	
	@Test
	public void parseMappingWithEnumProperty() throws IOException {
		MappingModel mappingModel = createMappingModel("Enum_Property.mapping");

		EList<EnumMappingRule> rules = ((EnumMapping)mappingModel.getMapping()).getEnumMappingRules();
		assertEquals(2, rules.size());
	}
	
	@Test
	public void parseMappingWithFunctionBlockAttribute() throws IOException {
		MappingModel mappingModel = createMappingModel("FunctionBlock_Attribute.mapping");

		EList<FunctionBlockMappingRule> rules = ((FunctionBlockMapping)mappingModel.getMapping()).getFunctionBlockMappingRules();
		assertEquals(6, rules.size());
	}
	
	@Test
	public void parseMappingWithFunctionBlockProperty() throws IOException {
		MappingModel mappingModel = createMappingModel("FunctionBlock_Property.mapping");

		EList<FunctionBlockMappingRule> rules = ((FunctionBlockMapping)mappingModel.getMapping()).getFunctionBlockMappingRules();
		assertEquals(6, rules.size());
	}
	
	@Test
	public void parseMappingWithFunctionBlockReference() throws IOException {
		MappingModel mappingModel = createMappingModel("FunctionBlock_Ref.mapping");

		EList<FunctionBlockMappingRule> rules = ((FunctionBlockMapping)mappingModel.getMapping()).getFunctionBlockMappingRules();
		assertEquals(2, rules.size());
	}
	
	@Test
	public void parseMappingWithInfoModelAttribute() throws IOException {
		MappingModel mappingModel = createMappingModel("Infomodel_Attribute.mapping");

		EList<InfoModelMappingRule> rules = ((InfoModelMapping)mappingModel.getMapping()).getInfoModelMappingRules();
		assertEquals(6, rules.size());
	}
	
	@Test
	public void parseMappingWithInfomodelReference() throws IOException {
		MappingModel mappingModel = createMappingModel("Infomodel_FBRef.mapping");

		EList<InfoModelMappingRule> rules = ((InfoModelMapping)mappingModel.getMapping()).getInfoModelMappingRules();
		assertEquals(1, rules.size());
	}

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