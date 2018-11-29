/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelRepositoryTest extends AbstractIntegrationTest {

  @Test
  public void testQueryWithEmptyExpression() {
    assertEquals(0, modelRepository.search("").size());
  }

  @Test
  public void testGetModelById() throws Exception {
    importModel("Color.type");
    assertEquals(1, modelRepository.search("*").size());
    ModelInfo result = modelRepository
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals("Color.type", result.getFileName());
    assertNotNull(result);
  }

  @Test
  public void testGetDSLEncoding() throws Exception {
    importModel("Color_encoding.type");
    ModelFileContent fileContent = modelRepository
        .getModelContent(ModelId.fromReference("org.eclipse.vorto.examples.type.Farbe", "1.0.0"));
    String actualContent = new String(fileContent.getContent(), "UTF-8");
    String expectedContent = IOUtils
        .toString(new ClassPathResource("sample_models/Color_encoding.type").getInputStream());
    assertEquals(expectedContent, actualContent);
    assertNotNull(fileContent.getModel());
    assertEquals("Überraschung",
        ((Entity) fileContent.getModel()).getProperties().get(0).getDescription());
    assertEquals("Farbe", fileContent.getModel().getName());
    assertEquals("Farbe.type", fileContent.getFileName());
  }

  @Test
  public void testGetDSLContentForModelId() throws Exception {
    importModel("Color.type");
    ModelFileContent fileContent = modelRepository
        .getModelContent(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    String actualContent = new String(fileContent.getContent(), "UTF-8");
    String expectedContent =
        IOUtils.toString(new ClassPathResource("sample_models/Color.type").getInputStream());
    assertEquals(expectedContent, actualContent);
    assertNotNull(fileContent.getModel());
    assertEquals("Color.type", fileContent.getFileName());
  }

  @Test
  public void testGetDSLContentForModelByFileName() throws Exception {
    importModel("Color.type");
    ModelInfo model = modelRepository
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));

    Optional<FileContent> fileContent =
        modelRepository.getFileContent(model.getId(), Optional.of(model.getFileName()));
    String expectedContent =
        IOUtils.toString(new ClassPathResource("sample_models/Color.type").getInputStream());
    assertEquals(expectedContent, new String(fileContent.get().getContent(), "utf-8"));
    assertEquals("Color.type", fileContent.get().getFileName());
  }

  @Test
  public void testGetDSLContentForModelWithoutFileName() throws Exception {
    importModel("Color.type");
    ModelInfo model = modelRepository
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));

    Optional<FileContent> fileContent =
        modelRepository.getFileContent(model.getId(), Optional.empty());
    String expectedContent =
        IOUtils.toString(new ClassPathResource("sample_models/Color.type").getInputStream());
    assertEquals(expectedContent, new String(fileContent.get().getContent(), "utf-8"));
    assertEquals("Color.type", fileContent.get().getFileName());
  }

  @Test
  public void testGetReferencesFromModel() throws Exception {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    assertEquals(2, modelRepository.search("*").size());
    ModelInfo result = modelRepository
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.fb.ColorLight", "1.0.0"));
    assertEquals(1, result.getReferences().size());
    assertEquals("org.eclipse.vorto.examples.type:Color:1.0.0",
        result.getReferences().get(0).getPrettyFormat());
  }

  @Test
  public void testGetReferencedBy() throws Exception {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    assertEquals(2, modelRepository.search("*").size());
    ModelInfo result = modelRepository
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals(1, result.getReferencedBy().size());
    assertEquals("org.eclipse.vorto.examples.fb:ColorLight:1.0.0",
        result.getReferencedBy().get(0).getPrettyFormat());
  }

  @Test
  public void testSearchAllModels() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(4, modelRepository.search("*").size());
  }

  @Test
  public void testSearchModelWithCriteria1() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(2, modelRepository.search("color").size());
  }


  @Test
  public void testGetModelWithNoImage() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(false, this.modelRepository
        .getById(new ModelId("HueLightStrips", "com.mycompany", "1.0.0")).isHasImage());
  }

  @Test
  public void testGetModelWithImage() throws Exception {
    IUserContext alex = UserContext.user("alex");

    final ModelId modelId = new ModelId("HueLightStrips", "com.mycompany", "1.0.0");
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    this.modelRepository.attachFile(modelId,
        new FileContent("sample.png",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/sample.png").getInputStream())),
        alex, Attachment.TAG_IMAGE);
    assertEquals(true, this.modelRepository.getById(modelId).isHasImage());
  }

  @Test
  public void testSearchModelWithFilters1() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(1, modelRepository.search("name:Color").size());
    assertEquals(3, modelRepository.search("name:Color name:Switcher name:HueLightStrips").size());
  }

  @Test
  public void testSearchModelWithFilters2() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(1, modelRepository.search("name:Color version:1.0.0   ").size());
    assertEquals(0, modelRepository.search("name:Color version:1.0.1").size());
  }

  @Test
  public void testSearchModelWithFilters3() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("ColorLightIM.infomodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(1, modelRepository.search("namespace:org.eclipse.vorto.examples.fb").size());
    assertEquals(1, modelRepository.search("namespace:com.mycompany.fb").size());
    assertEquals(2, modelRepository.search("namespace:com.mycompany   version:1.0.0").size());

  }

  @Test
  public void testSearchModelWithFilters4() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("ColorLightIM.infomodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(0, modelRepository.search("name:Switcher InformationModel").size());
    assertEquals(1, modelRepository.search("name:Switcher Functionblock").size());
    assertEquals(2, modelRepository.search("Functionblock").size());
  }

  @Test
  public void testSearchModelsByCreator() {
    IUserContext alex = UserContext.user("alex");
    importModel("Color.type", alex);
    importModel("Colorlight.fbmodel", alex);
    importModel("Switcher.fbmodel", UserContext.user("admin"));
    importModel("ColorLightIM.infomodel", UserContext.user("admin"));
    importModel("HueLightStrips.infomodel", UserContext.user("admin"));

    assertEquals(2, modelRepository.search("author:" + alex.getUsername()).size());
  }

  @Test
  public void testSaveInvalidModelWithError() {
    try {
      IUserContext erle = UserContext.user("erle");
      modelRepository.save(ModelId.fromPrettyFormat("com.ipso.smartobjects:Accelerometer:0.0.3"),
          IOUtils.toByteArray(new ClassPathResource("sample_models/Accelerometer-invalid.fbmodel")
              .getInputStream()),
          "Accelerometer-invalid.fbmodel", erle);
      fail("This test should be getting a validation exception");
    } catch (ValidationException e) {
      assertTrue(e.getMessage().contains("Constraint cannot apply on this property's datatype"));
    } catch (IOException e) {
      fail("Not able to load test file");
    }
  }

  @Test
  public void testDeleteUnUsedType() {
    importModel("Color.type");
    assertEquals(1, modelRepository.search("*").size());
    this.modelRepository
        .removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals(0, modelRepository.search("*").size());
  }

  @Test
  public void testDeleteAndCheckinSameModel() {
    importModel("Color.type");
    assertEquals(1, modelRepository.search("*").size());
    this.modelRepository
        .removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals(0, modelRepository.search("*").size());
    importModel("Color.type");
    assertEquals(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"),
        modelRepository.search("*").get(0).getId());
  }

  @Test
  public void testDeleteUsedType() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    assertEquals(2, modelRepository.search("*").size());
    try {
      this.modelRepository
          .removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
      fail("Expected exception");
    } catch (ModelReferentialIntegrityException ex) {
      assertEquals(1, ex.getReferencedBy().size());
    }
  }

  @Test
  public void testAuthorSearch() {
    IUserContext erle = UserContext.user("erle");
    IUserContext admin = UserContext.user("admin");
    importModel("Color.type", erle);
    importModel("Colorlight.fbmodel", erle);
    importModel("Switcher.fbmodel", admin);

    assertEquals(2,
        modelRepository.search("author:" + UserContext.user("erle").getUsername()).size());
    assertEquals(1,
        modelRepository.search("author:" + UserContext.user("admin").getUsername()).size());
  }

  @Test
  public void testCreateNewMajorVersionOfExistingModel() {
    IUserContext alex = UserContext.user("alex");
    importModel("Color.type");
    ModelInfo model = importModel("Colorlight.fbmodel", alex);
    final String newVersion = "2.0.0";
    ModelResource resource = this.modelRepository.createVersion(model.getId(), newVersion, alex);
    assertNotNull(resource);
    assertEquals(ModelId.newVersion(model.getId(), newVersion), resource.getId());
    assertNotNull(resource.getModel());
  }

  @Test(expected = ModelAlreadyExistsException.class)
  public void testCreateModelWithVersionConflict() {
    IUserContext alex = UserContext.user("alex");
    importModel("Color.type");
    ModelInfo model = importModel("Colorlight.fbmodel", alex);
    final String newVersion = "1.0.0";
    this.modelRepository.createVersion(model.getId(), newVersion, alex);
  }

  @Test(expected = ModelNotFoundException.class)
  public void testCreateModelWithVersionNotFound() {
    IUserContext alex = UserContext.user("alex");
    importModel("Color.type");
    importModel("Colorlight.fbmodel", alex);
    final String newVersion = "1.0.0";
    this.modelRepository.createVersion(new ModelId("Some", "demo", "1.0.0"), newVersion, alex);
  }
}
