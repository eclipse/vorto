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
package org.eclipse.vorto.codegen.tests.mapping;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.junit.Before;

/**
 * @author sgp0247
 *
 */
public class MappingModelParser {
	private static final String EXAMPLES_DIRECTORY = "resources/org/eclipse/vorto/codegen/mapping/examples/dummydevice/";

	@Before
	public void setup() {
		MappingStandaloneSetup.doSetup();
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
