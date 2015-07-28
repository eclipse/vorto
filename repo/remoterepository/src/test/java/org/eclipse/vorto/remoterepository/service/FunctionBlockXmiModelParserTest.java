/*******************************************************************************
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


package org.eclipse.vorto.remoterepository.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.vorto.remoterepository.internal.converter.XpathConverterService;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.service.converter.IModelConverterService;
import org.junit.Test;

public class FunctionBlockXmiModelParserTest {

	IModelConverterService service = new XpathConverterService();
	
	@Test
	public void getFbModelFromXmi() throws IOException{
		Path modelPath = Paths
				.get("src/test/resources/modelexamples/org.eclipse.vorto/ColorLight/1.0.0/ColorLight.fbmodel_xmi");
		byte[] modelArray = Files.readAllBytes(modelPath);
		
		ModelContent modelContent = new ModelContent(ModelType.FUNCTIONBLOCK, modelArray);
		ModelView modelView = service.convert(modelContent);
		assertEquals("org.eclipse.vorto", modelView.getModelId().getNamespace());
		assertEquals("ColorLight", modelView.getModelId().getName());
		assertEquals("1.0.0", modelView.getModelId().getVersion());
		assertEquals(ModelType.FUNCTIONBLOCK, modelView.getModelId().getModelType());
		assertEquals("A light makes the environment bright and colorful", modelView.getDescription());
		assertTrue(modelView.getReferenceModels().size()==1);
		assertEquals(modelView.getReferenceModels().get(0).getNamespace(),"org.eclipse.vorto.Color");
	}
}
