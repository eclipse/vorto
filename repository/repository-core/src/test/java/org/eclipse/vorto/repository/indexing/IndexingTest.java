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
package org.eclipse.vorto.repository.indexing;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.UnitTestBase;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.Assert.*;

public class IndexingTest extends UnitTestBase {

  private static final ModelId MODEL_ID = ModelId.fromPrettyFormat("org.eclipse.vorto.examples.type:ColorEnum:1.0.0");
  
  @Test
  public void savingAModelShouldIndexIt() {
    IUserContext creator = createUserContext("creator", "playground");
    
    try {
      repositoryFactory.getRepository(creator).save(MODEL_ID,
          IOUtils.toByteArray(new ClassPathResource("sample_models/ColorEnum.type")
              .getInputStream()),
          "ColorEnum.type", creator);
      
      ArgumentCaptor<ModelInfo> captor = ArgumentCaptor.forClass(ModelInfo.class);
      Mockito.verify(indexingService, Mockito.times(1)).indexModel(captor.capture(), Mockito.endsWith("playground"));
      
      ModelInfo savedModel = captor.getValue();
      assertEquals(savedModel.getId().getNamespace(), "org.eclipse.vorto.examples.type");
      assertEquals(savedModel.getId().getName(), "ColorEnum");
      assertEquals(savedModel.getId().getVersion(), "1.0.0");
      assertEquals(savedModel.getType(), ModelType.Datatype);
      assertEquals(savedModel.getAuthor(), "creator");
      assertNull(savedModel.getState());
      
    } catch (IOException e) {
      fail();
    }
  }
 
  @Test
  public void updatingAModelShouldUpdateTheIndex() {
    IUserContext creator = createUserContext("creator", "playground");
    
    importModel("creator", "ColorEnum.type");
    
    ModelInfo modelInfo = repositoryFactory.getRepository(creator).getById(MODEL_ID); 
    
    modelInfo.setAuthor("newauthor");
    
    repositoryFactory.getRepository(creator).updateMeta(modelInfo);
    
    ArgumentCaptor<ModelInfo> captor = ArgumentCaptor.forClass(ModelInfo.class);
    Mockito.verify(indexingService, Mockito.times(1)).updateIndex(captor.capture());
    
    ModelInfo savedModel = captor.getValue();
    assertEquals(savedModel.getAuthor(), "newauthor");
  }
  
  @Test
  public void updatingAModelStateShouldUpdateTheIndex() {
    IUserContext creator = createUserContext("creator", "playground");
    
    importModel("creator", "ColorEnum.type");
    
    repositoryFactory.getRepository(creator).updateState(MODEL_ID, ModelState.Draft.getName());
    
    ArgumentCaptor<ModelInfo> captor = ArgumentCaptor.forClass(ModelInfo.class);
    Mockito.verify(indexingService, Mockito.times(1)).updateIndex(captor.capture());
    
    ModelInfo savedModel = captor.getValue();
    assertEquals(savedModel.getState(), ModelState.Draft.getName());
  }
  
  @Test
  public void deletingAModelShouldUpdateTheIndex() {
    IUserContext creator = createUserContext("creator", "playground");
    
    importModel("creator", "ColorEnum.type");
    
    repositoryFactory.getRepository(creator).removeModel(MODEL_ID);
    
    ArgumentCaptor<ModelId> captor = ArgumentCaptor.forClass(ModelId.class);
    Mockito.verify(indexingService, Mockito.times(1)).deleteIndex(captor.capture());
    
    ModelId modelId = captor.getValue();
    
    assertEquals(modelId.getNamespace(), "org.eclipse.vorto.examples.type");
    assertEquals(modelId.getName(), "ColorEnum");
    assertEquals(modelId.getVersion(), "1.0.0");
  }
}
