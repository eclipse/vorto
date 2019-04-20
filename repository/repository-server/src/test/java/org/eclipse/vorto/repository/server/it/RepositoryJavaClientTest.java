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
package org.eclipse.vorto.repository.server.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumAttributeProperty;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.client.IRepositoryClient;
import org.eclipse.vorto.repository.client.ModelContent;
import org.junit.After;
import org.junit.Test;
import org.springframework.http.MediaType;

public class RepositoryJavaClientTest extends AbstractIntegrationTest  {

  private IRepositoryClient repositoryClient = null;
  
  @Override
  protected void setUpTest() throws Exception {
	createModel("Zone.type", "com.test.Zone:1.0.0");
	releaseModel("com.test:Zone:1.0.0");
    createModel("Color.type", "org.eclipse.vorto.examples.type:Color:1.0.0");
    releaseModel("org.eclipse.vorto.examples.type:Color:1.0.0");
    
    this.repositoryClient = IRepositoryClient.newBuilder().setBaseUrl("http://localhost:" + port+"/infomodelrepository").build();
  }
  
  @After
  public void cleanup() throws Exception {
    deleteModel("org.eclipse.vorto.examples.type:Color:1.0.0");
    deleteModel("com.test:Zone:1.0.0");
  }
  
  @Test
  public void testSearchModel() {
    assertEquals(1,repositoryClient.search("Color").size());
  }
  
  @Test
  public void testGetContentOfEntityModel() {
    ModelContent content = repositoryClient.getContent(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0"));
    assertEquals(1,((EntityModel)content.getModels().get(content.getRoot())).getProperties().size());
    EnumAttributeProperty attribute =  (EnumAttributeProperty)((EntityModel)content.getModels().get(content.getRoot())).getProperties().get(0).getAttributes().get(0);
    assertEquals("NORTH",attribute.getValue().getName());
  }
  
  @Test
  public void testGetContentOfEnumModel() {
    ModelContent content = repositoryClient.getContent(ModelId.fromPrettyFormat("com.test:Zone:1.0.0"));
    assertEquals(5,((EnumModel)content.getModels().get(content.getRoot())).getLiterals().size());
  }
  
  @Test
  public void testGetAttachmentsOfModel() throws Exception {
    addAttachment("org.eclipse.vorto.examples.type:Color:1.0.0", userAdmin, "test.json", MediaType.APPLICATION_JSON);
    assertEquals(1,repositoryClient.getAttachments(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0")).size());
    assertEquals("test.json",repositoryClient.getAttachments(ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0")).get(0).getFilename());
  }
  
  @Test
  public void testDownloadAttachmentsOfModel() throws Exception {
    addAttachment("org.eclipse.vorto.examples.type:Color:1.0.0", userAdmin, "test.json", MediaType.APPLICATION_JSON);
    assertTrue(repositoryClient.downloadAttachment((ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0")),"test.json").length > 0);
  }

}
