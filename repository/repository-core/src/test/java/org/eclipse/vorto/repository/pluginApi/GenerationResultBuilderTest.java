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
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.plugin.generator.*;
import org.eclipse.vorto.plugin.generator.utils.Generated;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultBuilder;
import org.eclipse.vorto.plugin.generator.utils.GenerationResultZip;

public class GenerationResultBuilderTest {
	

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
	  Generated generated = new Generated("test","test","test");
	  IModelWorkspace workspace =
		        IModelWorkspace.newReader()
		            .addFile(getClass().getClassLoader().getResourceAsStream(
		            		 "dsls/com.example_AWSIoTButton_1_0_0.infomodel"), ModelType.InformationModel)
		            .addFile(getClass().getClassLoader().getResourceAsStream(
		                "dsls/com.example.aws_AWSButtonMapping_1_0_0.mapping"), ModelType.Mapping)
		            .addFile(getClass().getClassLoader().getResourceAsStream(
		                "dsls/com.example.aws_Button1Mapping_1_0_0.mapping"), ModelType.Mapping)
		            .addFile(getClass().getClassLoader().getResourceAsStream(
		                "dsls/com.example.aws_Button2Mapping_1_0_0.mapping"), ModelType.Mapping)
		            .addFile(
		                getClass().getClassLoader()
		                    .getResourceAsStream("dsls/com.ipso.smartobjects_Push_button_0_0_1.fbmodel"),
		                ModelType.Functionblock)
		            .read();
	  InformationModel model = (InformationModel) workspace.get().stream()
		        .filter(p -> p instanceof InformationModel).findAny().get();
	  GenerationResultZip output = new GenerationResultZip(model, "eclipsehono");
	  output.write(generated);
	  GenerationResultBuilder result = GenerationResultBuilder.from(output);
	  result.append(output);
  }

 
}
