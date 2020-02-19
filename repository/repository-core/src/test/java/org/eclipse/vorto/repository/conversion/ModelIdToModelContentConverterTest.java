/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.repository.conversion;

import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ModelIdToModelContentConverterTest extends AbstractIntegrationTest {

  @Test
  public void testConvertFbWithMultipleProperty() {
    importModel("FbWithMultipleProperty.fbmodel");
    
    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.repositoryFactory);
    
    ModelContent content = converter.convert(ModelId.fromPrettyFormat("org.eclipse.vorto:TestFb:1.0.0"), Optional.empty());
    FunctionblockModel fbm = (FunctionblockModel) content.getModels().get(content.getRoot());
    assertTrue(fbm.getStatusProperty("foos").get().isMultiple());

    
  }
  
  @Test
  public void testConvertWithTargetPlatform() throws Exception {
    importModel("Color.type");
    importModel("sample.mapping");

    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.repositoryFactory);

    ModelContent content = converter.convert(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"), Optional.of("ios"));
    assertNotNull(content);
    assertEquals(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"),content.getModels().get(content.getRoot()).getId());
    assertEquals("colortype",((EntityModel)content.getModels().get(content.getRoot())).getStereotypes().get(0).getName());
  }

  @Test
  public void testConvertWithoutTargetPlatform() throws Exception {
    importModel("Color.type");
    importModel("sample.mapping");

    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.repositoryFactory);

    ModelContent content = converter.convert(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"), Optional.empty());
    assertEquals(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"),content.getModels().get(content.getRoot()).getId());
    assertEquals(0,((EntityModel)content.getModels().get(content.getRoot())).getStereotypes().size());
  }

  @Test
  public void testConvertWithoutTargetPlatformLatestTag() throws Exception {
    setupTestDataForLatestTag();

    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.repositoryFactory);
    ModelContent content = converter.convert(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:latest"), Optional.empty());

    assertEquals(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.1"),content.getModels().get(content.getRoot()).getId());
    assertEquals(0,((EntityModel)content.getModels().get(content.getRoot())).getStereotypes().size());
  }

  private void setupTestDataForLatestTag() throws WorkflowException {
    IUserContext user = createUserContext("alex", "playground");
    ModelInfo color = importModel("Color.type");
    ModelInfo color6 = importModel("Color6.type");
    importModel("Color7.type");
    importModel("sample.mapping");
    color.setState(ModelState.Released.getName());
    color6.setState(ModelState.Released.getName());
    this.workflow.start(color.getId(), user);
    this.workflow.start(color6.getId(), user);
    setReleaseState(color);
    setReleaseState(color6);
  }
}
