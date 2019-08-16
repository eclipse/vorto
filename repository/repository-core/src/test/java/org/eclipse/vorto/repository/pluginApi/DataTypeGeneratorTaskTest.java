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
package org.eclipse.vorto.repository.pluginApi;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
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

public class DataTypeGeneratorTaskTest {

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
						"sample_models/ColorLightIM.infomodel"), ModelType.InformationModel)
				.addFile(getClass().getClassLoader()
						.getResourceAsStream("dsls/com.example.aws_AWSButtonMapping_1_0_0.mapping"), ModelType.Mapping)
				.addFile(getClass().getClassLoader()
						.getResourceAsStream("dsls/com.example.aws_Button1Mapping_1_0_0.mapping"), ModelType.Mapping)
				.addFile(getClass().getClassLoader()
						.getResourceAsStream("dsls/com.example.aws_Button2Mapping_1_0_0.mapping"), ModelType.Mapping)
				.addFile(getClass().getClassLoader().getResourceAsStream(
						"dsls/com.ipso.smartobjects_Push_button_0_0_1.fbmodel"), ModelType.Functionblock)
				.read();
		InformationModel ctx = (InformationModel) workspace.get().stream().filter(p -> p instanceof InformationModel)
				.findAny().get();
		GenerationResultZip zipOutputter = new GenerationResultZip(ctx,"eclipseditto" );
		InvocationContext context = new InvocationContext(mappingModels, configProperties);
		DatatypeGeneratorTask datatypeGeneratorTask = new DatatypeGeneratorTask(entityTemplate, enumTemplate);
		datatypeGeneratorTask.generate(ctx, context, zipOutputter);
	}

}
