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
package org.eclipse.vorto.repository.server.it;

import org.eclipse.vorto.model.*;
import org.eclipse.vorto.repository.client.IRepositoryClient;
import org.eclipse.vorto.repository.client.ModelContent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RepositoryJavaClientTest extends IntegrationTestBase {

  private IRepositoryClient repositoryClient = null;

  @Before
  @Override
  public void setUpTest() throws Exception {
    super.setUpTest();
    createNamespaceSuccessfully("com.test", userSysadmin);
    createModel(userSysadmin, "Zone.type", "com.test:Zone:1.0.0");
    setPublic("com.test:Zone:1.0.0");
    createModel(userSysadmin,"Color2.type", "com.test:Color:1.0.0");
    setPublic("com.test:Color:1.0.0");
    createModel(userSysadmin,"Fb_withDictionary.fbmodel", "com.test:InstalledSoftware:1.0.0");
    setPublic("com.test:InstalledSoftware:1.0.0");

    this.repositoryClient = IRepositoryClient.newBuilder()
        .setBaseUrl("http://localhost:" + port + "/infomodelrepository").build();
  }
  
  @Test
  public void testParseDictionary() {
    ModelContent content =
        repositoryClient.getContent(ModelId.fromPrettyFormat("com.test:InstalledSoftware:1.0.0"));
    Optional<ModelProperty> description = ((FunctionblockModel)content.getModels().get(content.getRoot())).getStatusProperties().stream().filter(p -> p.getName().equals("description")).findAny();
    assertTrue(description.isPresent());
    assertTrue(description.get().getType() instanceof DictionaryType);
  }

  @Test
  public void testSearchModel() {
    assertEquals(1, repositoryClient.search("Color").size());
  }

  @Test
  public void testGetContentOfEntityModel() {
    ModelContent content =
        repositoryClient.getContent(ModelId.fromPrettyFormat("com.test:Color:1.0.0"));
    assertEquals(1,
        ((EntityModel) content.getModels().get(content.getRoot())).getProperties().size());
    EnumAttributeProperty attribute =
        (EnumAttributeProperty) ((EntityModel) content.getModels().get(content.getRoot()))
            .getProperties().get(0).getAttributes().get(0);
    assertEquals("NORTH", attribute.getValue().getName());
  }

  @Test
  public void testGetContentOfEnumModel() {
    ModelContent content =
        repositoryClient.getContent(ModelId.fromPrettyFormat("com.test:Zone:1.0.0"));
    assertEquals(5, ((EnumModel) content.getModels().get(content.getRoot())).getLiterals().size());
  }

  @Test
  public void testGetAttachmentsOfModel() throws Exception {
    addAttachment("com.test:Color:1.0.0", userSysadmin, "test.json", MediaType.APPLICATION_JSON);
    assertEquals(1,
        repositoryClient.getAttachments(ModelId.fromPrettyFormat("com.test:Color:1.0.0")).size());
    assertEquals("test.json", repositoryClient
        .getAttachments(ModelId.fromPrettyFormat("com.test:Color:1.0.0")).get(0).getFilename());
  }

  @Test
  public void testDownloadAttachmentsOfModel() throws Exception {
    addAttachment("com.test:Color:1.0.0", userSysadmin, "test.json", MediaType.APPLICATION_JSON);
    assertTrue(
        repositoryClient.downloadAttachment((ModelId.fromPrettyFormat("com.test:Color:1.0.0")),
            "test.json").length > 0);
  }

}
