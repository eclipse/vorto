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
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelImporterTest extends AbstractIntegrationTest {

  @Test
  public void testUploadZipFile() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("models.zip",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/models.zip").getInputStream())),
        Context.create(alex,Optional.of("org.eclipse.vorto")));
    
    assertEquals(true,uploadResult.isValid());
  }
  
  @Test
  public void testUploadAndImportZipWithSameNameDifferentVersion() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("models.zip",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/models_same_name.zip").getInputStream())),
        Context.create(alex,Optional.of("org.eclipse.vorto")));
    
    assertTrue(uploadResult.isValid());
    
    List<ModelInfo> imported = this.importer.doImport(uploadResult.getHandleId(),Context.create(alex,Optional.of("org.eclipse.vorto")));
    assertEquals(2,imported.size());
  }
  
  @Test
  public void testUploadFileWithoutVortolang() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("ColorEnum.type",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/model_without_vortolang.type").getInputStream())),
        Context.create(alex,Optional.empty()));
    
    assertEquals(true,uploadResult.isValid());
    
    List<ModelInfo> imported = this.importer.doImport(uploadResult.getHandleId(), Context.create(alex,Optional.empty()));
    
    IModelRepository repository = repositoryFactory.getRepository(alex);
    String content = new String(repository.getFileContent(imported.get(0).getId(), Optional.empty()).get().getContent(),"utf-8");
    System.out.println(content);
    assertTrue(content.contains("vortolang 1.0"));
  }
  
  @Test
  public void testUploadFileWithUnownedNamespace() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("Color.type",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/vortoprivateColor.type").getInputStream())),
        Context.create(alex,Optional.empty()));
    
    assertEquals(false,uploadResult.isValid());
    assertEquals(MessageSeverity.ERROR,uploadResult.getReport().get(0).getMessage().getSeverity());

    assertEquals("1) You do not own the target namespace 'vorto.private.alex'.",uploadResult.getReport().get(0).getMessage().getMessage());
  }
  
  @Test
  public void testUploadFileWithOwnedTargetNamespace() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("Color.type",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/vortoprivateColor.type").getInputStream())),
        Context.create(alex,Optional.of("org.eclipse.vorto")));
    
    assertEquals(true,uploadResult.isValid());
  }
  
  @Test
  public void testImportModelWithConversionNamespace() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("Color.type",
            IOUtils
                .toByteArray(new ClassPathResource("sample_models/vortoprivateColor.type").getInputStream())),
        Context.create(alex,Optional.of("org.eclipse.vorto")));
    
    assertEquals(true,uploadResult.isValid());
    
    List<ModelInfo> imported = this.importer.doImport(uploadResult.getHandleId(), Context.create(alex,Optional.of("org.eclipse.vorto")));
    assertEquals(1,imported.size());
    assertEquals("org.eclipse.vorto",imported.get(0).getId().getNamespace());
  }
  
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
        Context.create(alex,Optional.empty()));

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
        Context.create(alex,Optional.empty()));

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
            Context.create(createUserContext("erle", "playground"), Optional.empty()));

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
        Context.create(admin,Optional.empty()));

    this.importer.doImport(uploadResult.getHandleId(), Context.create(admin,Optional.empty()));
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
        Context.create(admin,Optional.empty()));

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
            Context.create(createUserContext("admin", "playground"),Optional.empty()));

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
        Context.create(createUserContext("admin", "playground"),Optional.empty()));
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
        Context.create(admin,Optional.empty()));
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
            Context.create(admin,Optional.empty()));
    assertEquals(true, uploadResult.isValid());
    assertEquals(0, repositoryFactory.getRepository(admin).search("*").size());

    List<ModelInfo> modelInfos = importer.doImport(uploadResult.getHandleId(),
        Context.create(createUserContext("alex", "playground"),Optional.empty()));
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
        Context.create(createUserContext("admin", "playground"),Optional.empty()));
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
            Context.create(createUserContext("admin", "playground"),Optional.empty()));
    assertEquals(false, uploadResult.isValid());
    assertNotNull(uploadResult.getReports().get(0).getMessage());
  }

  @Test
  public void testUploadCorruptModelVersion() throws Exception {
    UploadModelResult uploadResult = this.importer.upload(
        FileUpload.create("sample_models/Corrupt-model_namespace.type", IOUtils.toByteArray(
            new ClassPathResource("sample_models/Corrupt-model_namespace.type").getInputStream())),
        Context.create(createUserContext("admin", "playground"),Optional.empty()));
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
            Context.create(createUserContext("admin", "playground"),Optional.empty()));
  }

  @Test
  public void testUploadModelThatCompliesToOlderVersionOfMetaModel() throws Exception {
    UploadModelResult uploadResult =
        this.importer.upload(FileUpload.create("Corrupt-model_olderVersionOfMetaModel.fbmodel",
            IOUtils.toByteArray(
                new ClassPathResource("sample_models/Corrupt-model_olderVersionOfMetaModel.fbmodel")
                    .getInputStream())),
            Context.create(createUserContext("alex", "playground"),Optional.empty()));
    assertEquals(false, uploadResult.isValid());
    assertNotNull(uploadResult.getReports().get(0).getMessage());
  }
}
