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
import static org.junit.Assert.fail;
import java.util.Optional;
import org.eclipse.vorto.model.AbstractModel;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Comment;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelDtoFactoryTest extends AbstractIntegrationTest {

  @Test
  public void testDtoCreation() {
    importModel("Color.type");
    ModelInfo original = repositoryFactory.getRepository(createUserContext("admin", "playground"))
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    
    ModelInfo dto = ModelDtoFactory.createDto(original);
    
    assertEquals(original.getCreationDate(), dto.getCreationDate());
    assertEquals(original.getId(), dto.getId());
    assertEquals(original.isHasImage(), dto.isHasImage());
    assertEquals(original.getAuthor(), dto.getAuthor());
  }
  
  @Test
  public void testCreateComment() {
    Comment comment = new Comment(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"), "erle", "test comment");
    
    Comment dto = ModelDtoFactory.createDto(comment, UserContext.user("anon", "playground"));
    
    assertEquals("erle", dto.getAuthor());
  }

  @Test
  public void testCreateModelId() {
    ModelId modelId = ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0");
    
    ModelId dto = ModelDtoFactory.createDto(modelId);
    
    assertEquals(modelId.getName(), dto.getName());
    assertEquals(modelId.getNamespace(), dto.getNamespace());
    assertEquals(modelId.getVersion(), dto.getVersion());
  }
  
  @Test
  public void testCreateResourcesForInformationModel() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    try {
      ModelInfo modelInfo = modelParserFactory.getParser("ColorLightIM.infomodel")
          .parse(new ClassPathResource("sample_models/ColorLightIM.infomodel").getInputStream());
      
      if (!(modelInfo instanceof ModelResource)) {
        fail("Assumption is wrong. Parser doesn't return a model resource");
      }
      
      ModelResource modelResource = (ModelResource) modelInfo;
      
      AbstractModel model = ModelDtoFactory.createResource(modelResource.getModel(), Optional.empty());
      
      assertEquals(Infomodel.class, model.getClass());
      assertEquals(modelInfo.getId(), model.getId());
      assertEquals(modelInfo.getDescription(), model.getDescription());
      assertEquals(modelInfo.getDisplayName(), model.getDisplayName());
      
    } catch(Exception e) {
      fail("Can't load test data");
    }
  }
  
  @Test
  public void testCreateResourcesForFunctionblockModel() {
    importModel("Color.type");
    try {
      ModelInfo modelInfo = modelParserFactory.getParser("Colorlight.fbmodel")
          .parse(new ClassPathResource("sample_models/Colorlight.fbmodel").getInputStream());
      
      if (!(modelInfo instanceof ModelResource)) {
        fail("Assumption is wrong. Parser doesn't return a model resource");
      }
      
      ModelResource modelResource = (ModelResource) modelInfo;
      
      AbstractModel model = ModelDtoFactory.createResource(modelResource.getModel(), Optional.empty());
      
      assertEquals(FunctionblockModel.class, model.getClass());
      assertEquals(modelInfo.getId(), model.getId());
      assertEquals(modelInfo.getDescription(), model.getDescription());
      assertEquals(modelInfo.getDisplayName(), model.getDisplayName());
      
    } catch(Exception e) {
      fail("Can't load test data");
    }
  }
  
  @Test
  public void testCreateResourcesForEntity() {
    try {
      ModelInfo modelInfo = modelParserFactory.getParser("Color.type")
          .parse(new ClassPathResource("sample_models/Color.type").getInputStream());
      
      if (!(modelInfo instanceof ModelResource)) {
        fail("Assumption is wrong. Parser doesn't return a model resource");
      }
      
      ModelResource modelResource = (ModelResource) modelInfo;
      AbstractModel model = ModelDtoFactory.createResource(modelResource.getModel(), Optional.empty());
      
      assertEquals(EntityModel.class, model.getClass());
      assertEquals(modelInfo.getId(), model.getId());
      assertEquals(modelInfo.getDescription(), model.getDescription());
      
    } catch(Exception e) {
      fail("Can't load test data");
    }
  }
  
  @Test
  public void testCreateResourcesForEnum() {
    try {
      ModelInfo modelInfo = modelParserFactory.getParser("ColorEnum.type")
          .parse(new ClassPathResource("sample_models/ColorEnum.type").getInputStream());
      
      if (!(modelInfo instanceof ModelResource)) {
        fail("Assumption is wrong. Parser doesn't return a model resource");
      }
      
      ModelResource modelResource = (ModelResource) modelInfo;
      
      AbstractModel model = ModelDtoFactory.createResource(modelResource.getModel(), Optional.empty());
      
      assertEquals(EnumModel.class, model.getClass());
      assertEquals(modelInfo.getId(), model.getId());
      assertEquals(modelInfo.getDescription(), model.getDescription());
      assertEquals(modelInfo.getDisplayName(), model.getDisplayName());
      
    } catch(Exception e) {
      fail("Can't load test data");
    }
  }
}
