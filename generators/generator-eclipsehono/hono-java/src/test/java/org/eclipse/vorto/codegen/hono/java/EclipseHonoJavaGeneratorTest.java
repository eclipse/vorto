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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.generator.*;
import org.eclipse.vorto.plugin.generator.utils.DatatypeGeneratorTask;
import org.eclipse.vorto.plugin.generator.utils.Generated;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultBuilder;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip;
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate;
import org.eclipse.vorto.plugin.generator.utils.IGeneratedWriter;


public class EclipseHonoJavaGeneratorTest {

	@Mock
	protected IFileTemplate<Entity> entityTemplate = Mockito.mock(IFileTemplate.class);

	@Mock
	protected IFileTemplate<org.eclipse.vorto.core.api.model.datatype.Enum> enumTemplate = Mockito
			.mock(IFileTemplate.class);

	@Before
	public void beforeEach() throws Exception {
	}

	@Before
	public void setUp() {

	}

	@BeforeClass
	public static void initParser() {
		ModelWorkspaceReader.init();
	}

	@Test
	public void testAppend() throws Exception {
		List<MappingModel> mappingModels = new ArrayList<>();
		Map<String, String> configProperties = new HashMap<>();
		IModelWorkspace workspace = IModelWorkspace.newReader()
				.addFile(getClass().getClassLoader().getResourceAsStream(
						"dsls/SuperInfomodel.infomodel"), ModelType.InformationModel)
				.addFile(getClass().getClassLoader().getResourceAsStream(
						"dsls/SuperSuperFb.fbmodel"), ModelType.Functionblock)
				.addFile(getClass().getClassLoader().getResourceAsStream(
						"dsls/ColorLight.type"), ModelType.Datatype)
				.addFile(getClass().getClassLoader().getResourceAsStream(
						"dsls/Light.type"), ModelType.Datatype)
				.addFile(getClass().getClassLoader().getResourceAsStream(
						"dsls/Brightness.type"), ModelType.Datatype)
				.addFile(getClass().getClassLoader().getResourceAsStream(
						"dsls/Coffee_Enum.type"), ModelType.Datatype)
				.read();
		InformationModel model = (InformationModel) workspace.get().stream().filter(p -> p instanceof InformationModel)
				.findAny().get();
		InvocationContext context = new InvocationContext(mappingModels, configProperties);
		
		EclipseHonoJavaGenerator eclipseHonoJavaGenerator = new EclipseHonoJavaGenerator();
		eclipseHonoJavaGenerator.generate(model, context);
		
	}

}
