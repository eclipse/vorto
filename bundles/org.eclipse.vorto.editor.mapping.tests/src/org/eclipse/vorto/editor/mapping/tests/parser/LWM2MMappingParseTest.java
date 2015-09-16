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
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.junit.Before;
import org.junit.Test;

public class LWM2MMappingParseTest {
	private static final String EXAMPLES_DIRECTORY = "resources/org/eclipse/vorto/editor/mapping/tests/parser/examples/lwm2m/shared_models/";

	@Before
	public void setup() {
		MappingStandaloneSetup.doSetup();
	}

	@Test
	public void parseEntityMapping() throws IOException {
		MappingModel mappingModel = this.createMappingModel("ExampleEntity.mapping");

		EList<MappingRule> rules = mappingModel.getRules();
		assertEquals(4, rules.size());

	}

	@Test
	public void parseEnumMapping() throws IOException {
		MappingModel mappingModel = this.createMappingModel("ExampleEnum.mapping");

		EList<MappingRule> rules = mappingModel.getRules();
		assertEquals(4, rules.size());

	}
	
	@Test
	public void parseFunctionBlockMapping() throws IOException {
		MappingModel mappingModel = createMappingModel("ExampleFunctionBlock.mapping");

		EList<MappingRule> rules = mappingModel.getRules();
		assertEquals(3, rules.size());
	}

	@Test
	public void parseInformationModelMapping() throws IOException {
		MappingModel mappingModel = createMappingModel("ExampleInfoModel.mapping");

		EList<MappingRule> rules = mappingModel.getRules();
		assertEquals(1, rules.size());
	}

	private MappingModel createMappingModel(String mappingFileName) throws IOException {
		ResourceSet rset = new ResourceSetImpl();
		rset.getPackageRegistry().put(MappingPackage.eNS_URI, MappingPackage.eINSTANCE);
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

		Resource resource = rset.getResource(URI.createFileURI(EXAMPLES_DIRECTORY + mappingFileName), true);
		resource.load(null);
		MappingModel mappingModel = (MappingModel) resource.getContents().get(0);
		return mappingModel;
	}
}
