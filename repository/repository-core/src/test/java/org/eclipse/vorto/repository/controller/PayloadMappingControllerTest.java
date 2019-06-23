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
package org.eclipse.vorto.repository.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.mapping.engine.model.spec.MappingSpecification;
import org.eclipse.vorto.model.IPropertyAttribute;
import org.eclipse.vorto.model.IReferenceType;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.plugin.generator.adapter.ObjectMapperFactory;
import org.eclipse.vorto.plugin.generator.adapter.ObjectMapperFactory.ModelReferenceDeserializer;
import org.eclipse.vorto.plugin.generator.adapter.ObjectMapperFactory.PropertyAttributeDeserializer;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.web.api.v1.ModelController;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.repository.web.core.PayloadMappingController;
import org.eclipse.vorto.repository.web.core.dto.mapping.TestMappingRequest;
import org.eclipse.vorto.repository.web.core.dto.mapping.TestMappingResponse;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

public class PayloadMappingControllerTest {
	
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @BeforeClass
  public static void init() {
    ModelWorkspaceReader.init();
  }
  
 

  @Test
  public void testCreateMappingSpecificationWithExistingMapping() throws Exception {
    IModelRepositoryFactory factory = Mockito.mock(IModelRepositoryFactory.class);
    IModelRepository repo = Mockito.mock(IModelRepository.class);
    ITenantService iTenantService = Mockito.mock(ITenantService.class);
    Tenant tenant =  new Tenant();
    tenant.setTenantId("A");
    
    when(repo.getMappingModelsForTargetPlatform(Matchers.any(), Matchers.any(),Matchers.any()))
        .thenReturn(Arrays.asList(new ModelInfo()));
    when(factory.getRepository(Matchers.anyString(), Matchers.any())).thenReturn(repo);
    //when(factory.getRepository(Matchers.anyObject())).thenReturn(repo);

    when(iTenantService.getTenantFromNamespace(Matchers.any()))
    .thenReturn(Optional.of(tenant));
    
    PayloadMappingController controller = new PayloadMappingController();
    controller.setModelRepositoryFactory(factory);
    controller.setTenantService(iTenantService);
    

    thrown.expect(ModelAlreadyExistsException.class);
    controller.createMappingSpecification("com.test:Device:1.0.0", "test");
  }

  @Test
  public void testCreateMappingSpecificationWithNoExistingMapping() throws Exception {
    IModelRepositoryFactory factory = Mockito.mock(IModelRepositoryFactory.class);
    IModelRepository repo = Mockito.mock(IModelRepository.class);
    ITenantService iTenantService = Mockito.mock(ITenantService.class);
    Tenant tenant =  new Tenant();
    tenant.setTenantId("A");
    
    ModelController modelController = Mockito.mock(ModelController.class);
    IWorkflowService workflowService = Mockito.mock(IWorkflowService.class);

    when(repo.getMappingModelsForTargetPlatform(Matchers.any(), Matchers.eq("test"),Matchers.any()))
        .thenReturn(Collections.emptyList());

    when(repo.save(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any()))
        .thenReturn(null);

    when(modelController.getModelContent(Matchers.eq("com.mycompany:ColorLightIM:1.0.0")))
        .thenReturn(getModelContent());

    when(workflowService.start(Matchers.any(), Matchers.any())).thenReturn(null);
    
    when(factory.getRepository(Matchers.any(), Matchers.any())).thenReturn(repo);
    
    when(factory.getRepository(Matchers.anyString())).thenReturn(repo);
    
    when(factory.getRepository(Matchers.any(IUserContext.class))).thenReturn(repo);
    
    when(iTenantService.getTenantFromNamespace(Matchers.any()))
    .thenReturn(Optional.of(tenant));

    PayloadMappingController controller = new PayloadMappingController();
    controller.setModelRepositoryFactory(factory);
    controller.setModelController(modelController);
    controller.setUserContextFn((tenantId) -> UserContext.user("erle", tenantId));
    controller.setWorkflowService(workflowService);
    controller.setTenantService(iTenantService);
    
    Map<String, Object> returnValue =
        controller.createMappingSpecification("com.mycompany:ColorLightIM:1.0.0", "test");

    assertTrue(returnValue.get("mappingId") != null);
    assertTrue(returnValue.get("spec") != null);
  }

  private ModelContent getModelContent() throws IOException {
    ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(IOUtils.toByteArray(this
        .getClass().getClassLoader().getResource("sample_models/valid-models.zip").openStream())));

    IModelWorkspace workspace = new ModelWorkspaceReader().addZip(zis).read();

    ModelContent result = new ModelContent();
    result.setRoot(ModelId.fromPrettyFormat("com.mycompany:ColorLightIM:1.0.0"));

    workspace.get().stream().forEach(model -> {
      result.getModels().put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
          ModelDtoFactory.createResource(model, Optional.empty()));
    });

    return result;
  }

  @Test
  public void testGetMappingSpecs() {
    ModelController modelController = Mockito.mock(ModelController.class);
    ITenantService iTenantService = Mockito.mock(ITenantService.class);
    Tenant tenant =  new Tenant();
    tenant.setTenantId("A");
    try {
      when(modelController.getModelContentForTargetPlatform(
          Matchers.eq("com.mycompany:ColorLightIM:1.0.0"), Matchers.any()))
              .thenReturn(modelContentForSample1());

      when(modelController
          .getModelContent(Matchers.eq("org.eclipse.vorto.examples.fb:ColorLight:1.0.0")))
              .thenReturn(modelContentForSample2());
      
      

      PayloadMappingController controller = new PayloadMappingController();
      controller.setModelController(modelController);
      controller.setTenantService(iTenantService);

      IMappingSpecification mappingSpec =
          controller.getMappingSpecification("com.mycompany:ColorLightIM:1.0.0", "test");

      assertTrue(mappingSpec != null);

    } catch (IOException e) {
      fail("Unable to load test resources");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Test fired an exception");
    }
  }

  private ModelContent modelContentForSample1() throws IOException {
    Map<String, ModelType> sample1 = new HashMap<>();
    sample1.put("sample_models/ColorLightIM.infomodel", ModelType.InformationModel);
    sample1.put("sample_models/Colorlight.fbmodel", ModelType.Functionblock);
    sample1.put("sample_models/Color.type", ModelType.Datatype);
    return getModelContentForFile("com.mycompany:ColorLightIM:1.0.0", sample1);
  }

  private ModelContent modelContentForSample2() throws IOException {
    Map<String, ModelType> sample1 = new HashMap<>();
    sample1.put("sample_models/Colorlight.fbmodel", ModelType.Functionblock);
    sample1.put("sample_models/Color.type", ModelType.Datatype);
    return getModelContentForFile("org.eclipse.vorto.examples.fb:ColorLight:1.0.0", sample1);
  }

  private ModelContent getModelContentForFile(String modelId, Map<String, ModelType> files)
      throws IOException {
    ModelWorkspaceReader reader = new ModelWorkspaceReader();

    for (Map.Entry<String, ModelType> entry : files.entrySet()) {
      InputStream is = this.getClass().getClassLoader().getResource(entry.getKey()).openStream();
      reader.addFile(is, entry.getValue());
    }

    ModelContent result = new ModelContent();
    result.setRoot(ModelId.fromPrettyFormat(modelId));

    reader.read().get().stream().forEach(model -> {
      result.getModels().put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()),
          ModelDtoFactory.createResource(model, Optional.empty()));
    });

    return result;
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
  
  @Test
  public void testSaveMappingSpecification() throws Exception {
    
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(IPropertyAttribute.class, new PropertyAttributeDeserializer());
    module.addDeserializer(IReferenceType.class, new ModelReferenceDeserializer());
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.registerModule(module);
    
    MappingSpecification spec = mapper.readValue(new ClassPathResource("mappingRequest2.json").getInputStream(), MappingSpecification.class);  
    assertNotNull(spec);
    assertNotNull(spec.getFunctionBlock("connectivity"));
    System.out.println(spec);
  }
  
  @Test
  public void testDeserializeModelContentContainingMapping() throws Exception {
    ModelContent content = ObjectMapperFactory.getInstance().readValue(new ClassPathResource("modelcontent_lwm2m.json").getInputStream(), ModelContent.class);  
    assertNotNull(content);
    assertNotNull(content.getModels().get(content.getRoot()));
    System.out.println(content);

    
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
}
