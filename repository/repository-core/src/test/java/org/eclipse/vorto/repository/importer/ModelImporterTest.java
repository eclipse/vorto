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
package org.eclipse.vorto.repository.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelImporterTest extends AbstractIntegrationTest {

  @Test
  public void testImportFileWithNonMatchingFileName() {
    IUserContext alex = createUserContext("alex", "playground");
    importModel("Color2.type", alex);
    ModelInfo modelInfo = repositoryFactory.getRepository(alex).search("").get(0);
    assertEquals("Color.type",
        repositoryFactory.getRepository(alex).getById(modelInfo.getId()).getFileName());
  }

  @Test
  public void testUploadSameModelTwiceByAuthorWhichIsInDraftState() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    ModelInfo info = importModel("Color.type", alex);
    this.workflow.start(info.getId(), alex);
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("Color.type",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())),
        alex);

    assertTrue(uploadResult.hasWarnings());
    assertTrue(uploadResult.isValid());
    assertTrue(uploadResult.getReport().get(0).isValid());
    assertEquals(ValidationReport.WARNING_MODEL_ALREADY_EXISTS,
        uploadResult.getReport().get(0).getMessage());
  }

  @Test
  public void testUploadSameModelTwiceByAuthorAlreadyReleased() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    ModelInfo info = importModel("Color.type", alex);
    this.workflow.start(info.getId(), alex);
    setReleaseState(info);
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("Color.type",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())),
        alex);

    assertFalse(uploadResult.hasWarnings());
    assertFalse(uploadResult.isValid());
    assertFalse(uploadResult.getReport().get(0).isValid());
    assertEquals(ValidationReport.ERROR_MODEL_ALREADY_RELEASED,
        uploadResult.getReport().get(0).getMessage());
  }

  @Test
  public void testUploadSameModelTwiceByDifferentUser() throws Exception {
    importModel("Color.type", createUserContext("alex", "playground"));
    UploadModelResult uploadResult =
        this.importer.upload(
            FileUpload.create("Color.type",
                IOUtils.toByteArray(
                    new ClassPathResource("sample_models/Color2.type").getInputStream())),
            createUserContext("erle", "playground"));

    assertFalse(uploadResult.hasWarnings());
    assertFalse(uploadResult.isValid());
    assertFalse(uploadResult.getReport().get(0).isValid());
    assertEquals(ValidationReport.ERROR_MODEL_ALREADY_EXISTS,
        uploadResult.getReport().get(0).getMessage());
  }

  @Test
  public void testUploadSameModelByAdminDraftState() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    ModelInfo info = importModel("Color.type", alex);
    this.workflow.start(info.getId(), alex);
    IUserContext admin = createUserContext("admin", "playground");

    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("Color.type",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())),
        admin);

    this.importer.doImport(uploadResult.getHandleId(), admin);
    ModelFileContent content = repositoryFactory.getRepository(admin)
        .getModelContent(uploadResult.getReports().get(0).getModel().getId(), true);

    assertTrue(uploadResult.hasWarnings());
    assertTrue(new String(content.getContent(), "utf-8").contains("mandatory b as int"));
    assertTrue(uploadResult.isValid());
    assertTrue(uploadResult.getReport().get(0).isValid());
    assertEquals(ValidationReport.WARNING_MODEL_ALREADY_EXISTS,
        uploadResult.getReport().get(0).getMessage());

    ;
  }

  @Test
  public void testUploadSameModelByAdminReleasedState() throws Exception {
    ModelInfo info = importModel("Color.type", createUserContext("alex", "playground"));
    this.workflow.start(info.getId(), createUserContext("alex", "playground"));
    setReleaseState(info);
    IUserContext admin = createUserContext("admin", "playground");

    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("Color.type",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())),
        admin);

    assertFalse(uploadResult.hasWarnings());
    assertFalse(uploadResult.isValid());
    assertFalse(uploadResult.getReport().get(0).isValid());
    assertEquals(ValidationReport.ERROR_MODEL_ALREADY_RELEASED,
        uploadResult.getReport().get(0).getMessage());

  }

  @Test
  public void testOverwriteInvalidModelByAdmin() throws Exception {
    importModel("Color.type", createUserContext("alex", "playground"));
    UploadModelResult uploadResult =
        this.importer.upload(
            FileUpload.create("Color.type",
                IOUtils.toByteArray(
                    new ClassPathResource("sample_models/Color3.type").getInputStream())),
            createUserContext("admin", "playground"));

    assertFalse(uploadResult.hasWarnings());
    assertFalse(uploadResult.isValid());
    assertFalse(uploadResult.getReport().get(0).isValid());
    assertNotNull(uploadResult.getReport().get(0).getMessage());
    assertEquals(MessageSeverity.ERROR, uploadResult.getReport().get(0).getMessage().getSeverity());
  }

  @Test
  public void testUploadFileMissingReference() throws Exception {
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("ColorLightIM.infomodel",
            IOUtils.toByteArray(
                new ClassPathResource("sample_models/ColorLightIM.infomodel").getInputStream())),
        createUserContext("admin", "playground"));
    assertFalse(uploadResult.isValid());
    assertEquals(1, uploadResult.getReports().get(0).getUnresolvedReferences().size());
  }

  @Test
  public void tesUploadValidModel() throws IOException {
    IUserContext admin = createUserContext("admin", "playground");
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("Color.type",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream())),
        admin);
    assertEquals(true, uploadResult.isValid());
    assertEquals(MessageSeverity.INFO, uploadResult.getReports().get(0).getMessage().getSeverity());
    assertNotNull(uploadResult.getHandleId());
    ModelInfo resource = uploadResult.getReports().get(0).getModel();
    assertEquals("org.eclipse.vorto.examples.type", resource.getId().getNamespace());
    assertEquals("Color", resource.getId().getName());
    assertEquals("1.0.0", resource.getId().getVersion());
    assertEquals(ModelType.Datatype, resource.getType());
    assertEquals(0, resource.getReferences().size());
    assertEquals("Color", resource.getDisplayName());
    assertNull(resource.getDescription());
    assertEquals(0, repositoryFactory.getRepository(admin).search("*").size());
  }

  @Test
  public void testCheckinValidModel() throws Exception {
    IUserContext admin = createUserContext("admin", "playground");
    UploadModelResult uploadResult =
        this.importer.upload(
            FileUpload.create("Color.type",
                IOUtils.toByteArray(
                    new ClassPathResource("sample_models/Color.type").getInputStream())),
            admin);
    assertEquals(true, uploadResult.isValid());
    assertEquals(0, repositoryFactory.getRepository(admin).search("*").size());

    List<ModelInfo> modelInfos = importer.doImport(uploadResult.getHandleId(),
        createUserContext("alex", "playground"));
    modelInfos.forEach(resource -> assertEquals(false, resource.getImported()));

    Thread.sleep(1000);
    assertEquals(1, repositoryFactory.getRepository(admin).search("*").size());
  }

  @Test
  public void testCheckinInvalidModel() throws Exception {
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("Colorlight.fbmodel",
            IOUtils.toByteArray(
                new ClassPathResource("sample_models/Colorlight.fbmodel").getInputStream())),
        createUserContext("admin", "playground"));
    assertEquals(false, uploadResult.isValid());
    assertNotNull(uploadResult.getReports().get(0).getMessage());
  }

  @Test
  public void testUploadCorruptModelMissingVersion() throws Exception {
    UploadModelResult uploadResult =
        this.importer.upload(FileUpload.create("sample_models/Corrupt-model_missingVersion.type",
            IOUtils.toByteArray(
                new ClassPathResource("sample_models/Corrupt-model_missingVersion.type")
                    .getInputStream())),
            createUserContext("admin", "playground"));
    assertEquals(false, uploadResult.isValid());
    assertNotNull(uploadResult.getReports().get(0).getMessage());
  }

  @Test
  public void testUploadCorruptModelVersion() throws Exception {
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("sample_models/Corrupt-model_namespace.type", IOUtils.toByteArray(
            new ClassPathResource("sample_models/Corrupt-model_namespace.type").getInputStream())),
        createUserContext("admin", "playground"));
    assertEquals(false, uploadResult.isValid());
    assertNotNull(uploadResult.getReports().get(0).getMessage());
  }

  @Test(expected = FileNotFoundException.class)
  public void testUploadInvalidFileName() throws Exception {
    this.importer
        .upload(
            FileUpload.create("sample_models/Bogus.type",
                IOUtils.toByteArray(
                    new ClassPathResource("sample_models/Color.typ").getInputStream())),
            createUserContext("admin", "playground"));
  }

  @Test
  public void testUploadModelThatCompliesToOlderVersionOfMetaModel() throws Exception {
    UploadModelResult uploadResult =
        this.importer.upload(FileUpload.create("Corrupt-model_olderVersionOfMetaModel.fbmodel",
            IOUtils.toByteArray(
                new ClassPathResource("sample_models/Corrupt-model_olderVersionOfMetaModel.fbmodel")
                    .getInputStream())),
            createUserContext("alex", "playground"));
    assertEquals(false, uploadResult.isValid());
    assertNotNull(uploadResult.getReports().get(0).getMessage());
  }
}
