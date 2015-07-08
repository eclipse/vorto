/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
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
import org.eclipse.vorto.core.api.model.mapping.Rule;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.junit.Before;
import org.junit.Test;

public class MappingParserTest {

	private static final String EXAMPLES_DIRECTORY = "src/org/eclipse/vorto/editor/mapping/tests/parser/examples/";

	@Before
	public void setup() {
		MappingStandaloneSetup.doSetup();
	}

	@Test
	public void testParsingSampleSmarthomeMapping() throws IOException {

		ResourceSet rset = new ResourceSetImpl();
		rset.getPackageRegistry().put(MappingPackage.eNS_URI,
				MappingPackage.eINSTANCE);
		rset.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());
		String exampleSmarthomeMappingFile = EXAMPLES_DIRECTORY
				+ "smarthome.mapping";
		Resource resource = rset.getResource(
				URI.createFileURI(exampleSmarthomeMappingFile), true);
		resource.load(null);
		MappingModel smarthomeModel = (MappingModel) resource.getContents()
				.get(0);
		verifyMappingModel(smarthomeModel);
	}

	private void verifyMappingModel(MappingModel smarthomeModel) {

		assertEquals("Target Element is wrong", "smarthome",
				smarthomeModel.getTarget());
		assertEquals("Mapping rules count is wrong", 2, smarthomeModel
				.getRules().size());
		EList<Rule> rules = smarthomeModel.getRules();
		assertEquals(2, rules.size());

	}
}
