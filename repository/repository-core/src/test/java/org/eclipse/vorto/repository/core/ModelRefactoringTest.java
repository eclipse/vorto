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
package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.tenant.NewNamespacesNotSupersetException;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelRefactoringTest extends AbstractIntegrationTest {

  
  @Test
  public void testRenameModelName() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId oldId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0");
    final ModelId newId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type.Colour:1.0.0");
    
    ModelInfo result = repositoryFactory.getRepository(createUserContext("admin")).rename(oldId,newId,createUserContext("admin"));
    assertEquals(newId,result.getId());
    assertNull(repositoryFactory.getRepository(createUserContext("admin")).getById(oldId));
    assertEquals(1,repositoryFactory.getRepository(createUserContext("admin")).getModelsReferencing(newId).size());
  }
  
  
  @Test
  public void testRenameModelNamespace() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId oldId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0");
    final ModelId newId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.Color:1.0.0");
    
    ModelInfo result = repositoryFactory.getRepository(createUserContext("admin")).rename(oldId,newId,createUserContext("admin"));
    assertEquals(newId,result.getId());
    assertNull(repositoryFactory.getRepository(createUserContext("admin")).getById(oldId));
  }
  
  @Test
  public void testRenameModelVersion() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId oldId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0");
    final ModelId newId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type.Color:2.0.0");
    
    ModelInfo result = repositoryFactory.getRepository(createUserContext("admin")).rename(oldId,newId,createUserContext("admin"));
    assertEquals(newId,result.getId());
    assertNull(repositoryFactory.getRepository(createUserContext("admin")).getById(oldId));
  }
  
  @Test (expected = NewNamespacesNotSupersetException.class)
  public void testRenameNamespaceUnownedNamespace() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId oldId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0");
    final ModelId newId = ModelId.fromPrettyFormat("org.apache.Color:1.0.0");
    
    repositoryFactory.getRepository(createUserContext("admin")).rename(oldId,newId,createUserContext("admin"));
    
  }
  
  @Test
  public void testRenameModelWithAttachment() throws Exception {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId oldId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0");
    final ModelId newId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type.Color:2.0.0");
    
    repositoryFactory.getRepository(createUserContext("admin")).attachFile(oldId,
        new FileContent("sample.png",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/sample.png").getInputStream())),
        createUserContext("admin"), Attachment.TAG_IMAGE);
    
    assertEquals(1,repositoryFactory.getRepository(createUserContext("admin")).getAttachments(oldId).size());
    
    ModelInfo result = repositoryFactory.getRepository(createUserContext("admin")).rename(oldId,newId,createUserContext("admin"));
    assertEquals(newId,result.getId());
    assertEquals(1,repositoryFactory.getRepository(createUserContext("admin")).getAttachments(newId).size());
    assertNull(repositoryFactory.getRepository(createUserContext("admin")).getById(oldId));
  }
  
  
  
  @Test (expected = ModelAlreadyExistsException.class)
  public void testRenameToIdThatAlreadyExist() {
    importModel("Color.type");
    importModel("Color5.type");
    importModel("Colorlight.fbmodel");
    
    final ModelId oldId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:Color:1.0.0");    
    final ModelId newId = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.Color:1.0.0");
    
    repositoryFactory.getRepository(createUserContext("admin")).rename(oldId,newId,createUserContext("admin"));
  }
}
