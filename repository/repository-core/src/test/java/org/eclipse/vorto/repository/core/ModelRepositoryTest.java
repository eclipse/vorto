/**
 * Copyright (c) 2018, 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ModelRepositoryTest extends AbstractIntegrationTest {

  @Test
  public void testGetModelById() throws Exception {
    importModel("Color.type");
    assertEquals(1, repositoryFactory.getRepository(createUserContext("admin")).search("*").size());
    ModelInfo result = repositoryFactory.getRepository(createUserContext("admin"))
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals("Color.type", result.getFileName());
    assertNotNull(result);
  }

  @Test
  public void testGetDSLEncoding() throws Exception {
    importModel("Color_encoding.type");

    ModelFileContent fileContent =
        repositoryFactory.getRepository(createUserContext("admin")).getModelContent(
            ModelId.fromReference("org.eclipse.vorto.examples.type.Farbe", "1.0.0"), false);

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

    ModelFileContent fileContent =
        repositoryFactory.getRepository(createUserContext("admin")).getModelContent(
            ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"), false);

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
    ModelInfo model = repositoryFactory.getRepository(createUserContext("admin"))
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));

    Optional<FileContent> fileContent = repositoryFactory.getRepository(createUserContext("admin"))
        .getFileContent(model.getId(), Optional.of(model.getFileName()));
    String expectedContent =
        IOUtils.toString(new ClassPathResource("sample_models/Color.type").getInputStream());
    assertEquals(expectedContent, new String(fileContent.get().getContent(), "utf-8"));
    assertEquals("Color.type", fileContent.get().getFileName());
  }

  @Test
  public void testGetDSLContentForModelWithoutFileName() throws Exception {
    importModel("Color.type");
    ModelInfo model = repositoryFactory.getRepository(createUserContext("admin"))
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));

    Optional<FileContent> fileContent = repositoryFactory.getRepository(createUserContext("admin"))
        .getFileContent(model.getId(), Optional.empty());
    String expectedContent =
        IOUtils.toString(new ClassPathResource("sample_models/Color.type").getInputStream());
    assertEquals(expectedContent, new String(fileContent.get().getContent(), "utf-8"));
    assertEquals("Color.type", fileContent.get().getFileName());
  }

  @Test
  public void testGetReferencesFromModel() throws Exception {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    assertEquals(2, repositoryFactory.getRepository(createUserContext("admin")).search("*").size());
    ModelInfo result = repositoryFactory.getRepository(createUserContext("admin"))
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.fb.ColorLight", "1.0.0"));
    assertEquals(1, result.getReferences().size());
    assertEquals("org.eclipse.vorto.examples.type:Color:1.0.0",
        result.getReferences().get(0).getPrettyFormat());
  }

  @Test
  public void testGetReferencedBy() throws Exception {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    assertEquals(2, repositoryFactory.getRepository(createUserContext("admin")).search("*").size());
    ModelInfo result = repositoryFactory.getRepository(createUserContext("admin"))
        .getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals(1, result.getReferencedBy().size());
    assertEquals("org.eclipse.vorto.examples.fb:ColorLight:1.0.0",
        result.getReferencedBy().get(0).getPrettyFormat());
  }

  @Test
  public void testGetModelWithNoImage() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    assertEquals(false, this.repositoryFactory.getRepository(createUserContext("admin"))
        .getById(new ModelId("HueLightStrips", "com.mycompany", "1.0.0")).isHasImage());
  }

  @Test
  public void testGetModelWithImage() throws Exception {
    IUserContext alex = UserContext.user("alex", "playground");

    final ModelId modelId = new ModelId("HueLightStrips", "com.mycompany", "1.0.0");
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    importModel("Switcher.fbmodel");
    importModel("HueLightStrips.infomodel");
    this.repositoryFactory.getRepository(createUserContext("admin")).attachFile(modelId,
        new FileContent("sample.png",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/sample.png").getInputStream())),
        alex, Attachment.TAG_IMAGE);
    assertEquals(true, this.repositoryFactory.getRepository(createUserContext("admin"))
        .getById(modelId).isHasImage());
  }

  @Test
  public void testSaveInvalidModelWithError() {
    try {
      IUserContext erle = createUserContext("erle", "playground");
      repositoryFactory.getRepository(createUserContext("admin")).save(
          ModelId.fromPrettyFormat("com.ipso.smartobjects:Accelerometer:0.0.3"),
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
    assertEquals(1, repositoryFactory.getRepository(createUserContext("admin")).search("*").size());
    this.repositoryFactory.getRepository(createUserContext("admin"))
        .removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals(0, repositoryFactory.getRepository(createUserContext("admin")).search("*").size());
  }

  @Test
  public void testDeleteAndCheckinSameModel() {
    importModel("Color.type");
    assertEquals(1, repositoryFactory.getRepository(createUserContext("admin")).search("*").size());
    this.repositoryFactory.getRepository(createUserContext("admin"))
        .removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals(0, repositoryFactory.getRepository(createUserContext("admin")).search("*").size());
    importModel("Color.type");
    assertEquals(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"),
        repositoryFactory.getRepository(createUserContext("admin")).search("*").get(0).getId());
  }

  @Test
  public void testDeleteUsedType() {
    importModel("Color.type");
    importModel("Colorlight.fbmodel");
    assertEquals(2, repositoryFactory.getRepository(createUserContext("admin")).search("*").size());
    try {
      this.repositoryFactory.getRepository(createUserContext("admin"))
          .removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
      fail("Expected exception");
    } catch (ModelReferentialIntegrityException ex) {
      assertEquals(1, ex.getReferencedBy().size());
    }
  }

  @Test
  public void testCreateNewMajorVersionOfExistingModel() {
    IUserContext alex = createUserContext("alex", "playground");
    importModel("Color.type");
    ModelInfo model = importModel("Colorlight.fbmodel", alex);
    final String newVersion = "2.0.0";
    ModelResource resource = this.repositoryFactory.getRepository(createUserContext("admin"))
        .createVersion(model.getId(), newVersion, alex);
    assertNotNull(resource);
    assertEquals(ModelId.newVersion(model.getId(), newVersion), resource.getId());
    assertNotNull(resource.getModel());
  }

  @Test(expected = ModelAlreadyExistsException.class)
  public void testCreateModelWithVersionConflict() {
    IUserContext alex = createUserContext("alex", "playground");
    importModel("Color.type");
    ModelInfo model = importModel("Colorlight.fbmodel", alex);
    final String newVersion = "1.0.0";
    this.repositoryFactory.getRepository(createUserContext("admin")).createVersion(model.getId(),
        newVersion, alex);
  }

  @Test(expected = ModelNotFoundException.class)
  public void testCreateModelWithVersionNotFound() {
    IUserContext alex = createUserContext("alex", "playground");
    importModel("Color.type");
    importModel("Colorlight.fbmodel", alex);
    final String newVersion = "1.0.0";
    this.repositoryFactory.getRepository(createUserContext("admin"))
        .createVersion(new ModelId("Some", "demo", "1.0.0"), newVersion, alex);
  }

  @Test
  public void testGetLatestModelVersionId() throws Exception {
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
    ModelId id = this.repositoryFactory.getRepository(createUserContext("admin"))
        .getLatestModelVersionId(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertEquals("org.eclipse.vorto.examples.type:Color:1.0.1", id.getPrettyFormat());
  }

  @Test
  public void testGetLatestModelVersionIdNoReleasedVersion() throws Exception {
    importModel("Color.type");
    importModel("Color6.type");
    importModel("sample.mapping");
    ModelId id = this.repositoryFactory.getRepository(createUserContext("admin"))
        .getLatestModelVersionId(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
    assertNull(id);
  }
}
