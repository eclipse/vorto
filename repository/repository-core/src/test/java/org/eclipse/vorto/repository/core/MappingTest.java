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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.IReferenceType;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.UploadModelResult;
import org.eclipse.vorto.repository.web.core.PayloadMappingController;
import org.eclipse.vorto.repository.web.core.dto.mapping.TestMappingRequest;
import org.eclipse.vorto.repository.web.core.dto.mapping.TestMappingResponse;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class MappingTest extends AbstractIntegrationTest {


  @Test
  public void tesUploadMapping() throws IOException {
    UploadModelResult uploadResult =
        this.importer.upload(
            FileUpload.create("Color.type",
                IOUtils.toByteArray(
                    new ClassPathResource("sample_models/Color.type").getInputStream())),
            UserContext.user("admin"));
    assertEquals(true, uploadResult.isValid());
    assertNotNull(uploadResult.getHandleId());
    ModelInfo resource = uploadResult.getReports().get(0).getModel();
    assertEquals("org.eclipse.vorto.examples.type", resource.getId().getNamespace());
    assertEquals("Color", resource.getId().getName());
    assertEquals("1.0.0", resource.getId().getVersion());
    assertEquals(ModelType.Datatype, resource.getType());
    assertEquals(0, resource.getReferences().size());
    assertEquals("Color", resource.getDisplayName());
    assertNull(resource.getDescription());
    assertEquals(0, modelRepository.search("*").size());
  }


  @Test
  public void testCheckinValidMapping() throws Exception {
    UploadModelResult uploadResult =
        this.importer.upload(
            FileUpload.create("Color.type",
                IOUtils.toByteArray(
                    new ClassPathResource("sample_models/Color.type").getInputStream())),
            UserContext.user("admin"));
    assertEquals(true, uploadResult.isValid());
    assertEquals(0, modelRepository.search("*").size());

    User user = new User();
    user.setUsername("alex");

    Collection<User> users = new ArrayList<User>();
    users.add(user);

    when(userRepository.findAll()).thenReturn(users);

    this.importer.doImport(uploadResult.getHandleId(), UserContext.user("alex"));
    Thread.sleep(2000); // hack coz it might take awhile until index is
                        // updated to do a search
    assertEquals(1, modelRepository.search("*").size());

    uploadResult = this.importer.upload(
        FileUpload.create("sample.mapping",
            IOUtils.toByteArray(
                new ClassPathResource("sample_models/sample.mapping").getInputStream())),
        UserContext.user("admin"));
    assertEquals(true, uploadResult.getReports().get(0).isValid());
    this.importer.doImport(uploadResult.getHandleId(), UserContext.user("alex"));
    assertEquals(1, modelRepository.search("-Mapping").size());
  }

  @Test
  public void testGetMappingsOfEntityForTargetPlatform() throws Exception {
    importModel("Color.type");
    importModel("sample.mapping");
    Thread.sleep(2000);
    assertEquals(1,
        modelRepository
            .getMappingModelsForTargetPlatform(
                ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"), "ios")
            .size());
  }

  @Test
  public void testUsedByMappingOfEntity() throws Exception {
    importModel("Color.type");
    importModel("sample.mapping");
    Thread.sleep(2000);
    assertEquals(1,
        modelRepository
            .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"))
            .getReferencedBy().size());
    assertEquals("org.eclipse.vorto.examples.type:Color_ios:1.0.0",
        modelRepository
            .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"))
            .getReferencedBy().get(0).getPrettyFormat());

    assertEquals(1,
        modelRepository
            .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"))
            .getPlatformMappings().size());
  }

  @Test
  public void testGetPlatformMappingsOfEntity() throws Exception {
    importModel("Color.type");
    importModel("sample.mapping");
    Thread.sleep(2000);
    ModelInfo colorInfo = modelRepository
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals(1, colorInfo.getPlatformMappings().size());
    assertEquals("ios", colorInfo.getPlatformMappings().keySet().iterator().next());
  }

  private Gson gsonWithDeserializer() {
    return new GsonBuilder()
        .registerTypeAdapter(IReferenceType.class, new JsonDeserializer<IReferenceType>() {
          @Override
          public IReferenceType deserialize(JsonElement json, Type arg1,
              JsonDeserializationContext arg2) throws JsonParseException {
            if (json.isJsonObject()) {
              final JsonObject jsonObject = json.getAsJsonObject();
              return new ModelId(jsonObject.get("name").getAsString(),
                  jsonObject.get("namespace").getAsString(),
                  jsonObject.get("version").getAsString());
            } else if (json.isJsonPrimitive()) {
              return PrimitiveType.valueOf(json.getAsString());
            }

            return null;
          }
        }).create();
  }

  @Test
  public void testMappingEngineJsonIntegerProblem() {
    try {
      TestMappingRequest mappingRequest = gsonWithDeserializer().fromJson(
          new InputStreamReader(new ClassPathResource("mappingRequest.json").getInputStream()),
          TestMappingRequest.class);
      TestMappingResponse response = new PayloadMappingController().testMapping(mappingRequest);
      response.getReport().getItems().forEach(item -> {
        assertFalse(item.getMessage().matches("Field intfb/status/\\S+ must be of type 'Integer'"));
      });
    } catch (JsonSyntaxException | JsonIOException | IOException e) {
      fail("Can't load test file.");
    } catch (Exception e) {
      fail("Got exception." + e.getMessage());
    }
  }
}
