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
package org.eclipse.vorto.repository.plugin.generator.impl;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.*;
import org.eclipse.vorto.repository.plugin.generator.GeneratedOutput;
import org.eclipse.vorto.repository.plugin.generator.GeneratorPluginConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author kolotu
 */
@RunWith(MockitoJUnitRunner.class)
public class GeneratedOutputAttachmentHandlerTest {

  @Mock
  private IModelRepositoryFactory modelRepositoryFactory;

  @Mock
  private IModelRepository repository;

  @InjectMocks
  private GeneratedOutputAttachmentHandler sut;

  @Test
  public void getGeneratedOutputFromAttachment() {
    // setup
    String filename = "thiswasgenerated.json";
    byte[] content = {0x01};
    List<Attachment> attList = new ArrayList<>();
    ModelId id = new ModelId();
    id.setName("TestInfoModel");
    attList.add(Attachment.newInstance(id, filename));
    when(repository.getAttachmentsByTags(any(), any())).thenReturn(attList);
    ModelInfo modelInfo = new ModelInfo();
    modelInfo.setId(id);
    Map<String, String> requestParams = new HashMap<>();
    GeneratorPluginConfiguration plugin = GeneratorPluginConfiguration
        .of("eclipseditto", "v2", "http://localhost:8888", "1.0.0");
    when(repository.getAttachmentContent(any(), any()))
        .thenReturn(Optional.of(new FileContent(filename, content)));

    // execute
    Optional<GeneratedOutput> result = sut
        .getGeneratedOutputFromAttachment(modelInfo, requestParams, plugin, repository);

    // verify
    assertTrue(result.isPresent());
    assertEquals(content, result.get().getContent());
    assertEquals(filename, result.get().getFileName());
    assertEquals(1, result.get().getSize());
  }

  @Test
  public void attachGeneratedOutput() {
    // setup
    String filename = "thiswasgenerated.json";
    byte[] content = {0x01};
    List<Attachment> attList = new ArrayList<>();
    ModelId id = new ModelId();
    id.setName("TestInfoModel");
    attList.add(Attachment.newInstance(id, filename));
    when(repository.getAttachmentsByTags(any(), any())).thenReturn(attList);
    ModelInfo modelInfo = new ModelInfo();
    modelInfo.setId(id);
    Map<String, String> requestParams = new HashMap<>();
    GeneratorPluginConfiguration plugin = GeneratorPluginConfiguration
        .of("eclipseditto", "v2", "http://localhost:8888", "1.0.0");
    when(repository.getAttachmentContent(any(), any()))
        .thenReturn(Optional.of(new FileContent(filename, content)));
    when(modelRepositoryFactory.getRepositoryByModel(any(), any())).thenReturn(repository);

    IUserContext userContext = new IUserContext() {
      @Override
      public Authentication getAuthentication() {
        return null;
      }

      @Override
      public String getUsername() {
        return "testuser";
      }

      @Override
      public String getWorkspaceId() {
        return null;
      }

      @Override
      public String getHashedUsername() {
        return null;
      }

      @Override
      public boolean isAnonymous() {
        return false;
      }

      @Override
      public boolean isSysAdmin() {
        return false;
      }
    };
    GeneratedOutput generatedOutput = sut
        .getGeneratedOutputFromAttachment(modelInfo, requestParams, plugin, repository).get();

    // execute
    GeneratedOutput result = sut.attachGeneratedOutput(userContext, modelInfo.getId(),
        "eclipseditto", requestParams, generatedOutput,
        plugin);

    // verify
    assertNotNull(result);
    assertEquals(1, result.getSize());
    assertEquals("generated_eclipseditto_1.0.0_TestInfoModel.json", result.getFileName());
    assertEquals(content, result.getContent());
  }

  @Test
  public void tagsForRequest() {
    // setup
    GeneratorPluginConfiguration plugin = GeneratorPluginConfiguration
        .of("eclipseditto", "v2", "http://localhost:8888", "1.0.0");
    Map<String, String> requestParams = new HashMap<>();
    requestParams.put("target", "thingJson");
    requestParams.put("anotherParam", "test");

    // execute
    Tag[] result = GeneratedOutputAttachmentHandler.tagsForRequest(plugin, requestParams);

    // verify
    assertNotNull(result);
    assertTrue(Arrays.asList(result).contains(new Tag("test")));
    assertTrue(Arrays.asList(result).contains(new Tag("thingJson")));
    assertTrue(Arrays.asList(result).contains(new Tag("generated")));
    assertTrue(Arrays.asList(result).contains(new Tag("eclipseditto_1.0.0")));
  }
}
