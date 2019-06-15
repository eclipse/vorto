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
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.core.impl.utils.BulkUploadHelper;
import org.eclipse.vorto.repository.importer.impl.VortoModelImporter;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelBulkImportTest extends AbstractIntegrationTest {

  private BulkUploadHelper bulkUploadHelper;

  @Override
  public void beforeEach() throws Exception {
    super.beforeEach();
    bulkUploadHelper = new BulkUploadHelper(repositoryFactory, this.accountService, this.tenantService);
  }

  @Test
  public void testUploadValidModels() throws IOException {
    String fileName = "sample_models/valid-models.zip";
    List<ValidationReport> uploadResults = bulkUploadHelper
        .uploadMultiple(loadContentForFile(fileName), fileName, createUserContext("admin", "playground"));
    assertEquals(3, uploadResults.size());
    verifyAllModelsAreValid(uploadResults);
  }

  @Test
  public void testUploadValidModelWithAlienFile() throws IOException {
    String fileName = "sample_models/valid-models-with-alien-file.zip";
    List<ValidationReport> uploadResults = bulkUploadHelper
        .uploadMultiple(loadContentForFile(fileName), fileName, createUserContext("admin", "playground"));
    assertEquals(3, uploadResults.size());
    verifyAllModelsAreValid(uploadResults);
  }

  @Test
  public void testUploadOneMissingModels() throws IOException {
    String fileName = "sample_models/missing-models.zip";
    List<ValidationReport> uploadResults = bulkUploadHelper
        .uploadMultiple(loadContentForFile(fileName), fileName, createUserContext("admin", "playground"));
    assertEquals(2, uploadResults.size());
    ValidationReport report = uploadResults.stream()
        .filter(r -> r.getModel().getId().getName().equals("ColorLightIM")).findFirst().get();
    assertEquals(1, report.getUnresolvedReferences().size());
    verifyOneModelAreMissing(uploadResults);
  }

  @Test
  public void testUploadInvalidModels() throws IOException {
    String fileName = "sample_models/invalid-models.zip";
    List<ValidationReport> result = bulkUploadHelper.uploadMultiple(loadContentForFile(fileName),
        fileName, createUserContext("admin", "playground"));
    assertEquals(2, result.size());
    assertFalse(result.get(0).isValid());
    assertFalse(result.get(1).isValid());
    assertNotNull(result.get(0).getModel());
    assertNotNull(result.get(1).getModel());
  }

  @Test
  public void testUploadDifferentModelTypesWithSameId() throws Exception {
    String fileName = "sample_models/modelsWithSameId.zip";
    List<ValidationReport> result = bulkUploadHelper.uploadMultiple(loadContentForFile(fileName),
        fileName, createUserContext("admin", "playground"));
    assertEquals(2, result.size());
    assertFalse(result.get(1).isValid());
  }

  @Test
  public void testUploadModelWithInvalidGrammar() throws Exception {
    String fileName = "sample_models/modelsWithWrongGrammar.zip";
    List<ValidationReport> result = bulkUploadHelper.uploadMultiple(loadContentForFile(fileName),
        fileName, createUserContext("admin", "playground"));
    assertEquals(2, result.size());
    assertFalse(result.get(0).isValid());
    assertFalse(result.get(1).isValid());

  }

  @Test
  public void testUploadZipContainingNonVortoFiles() throws Exception {
    IUserContext alex = createUserContext("alex", "playground");
    VortoModelImporter vortoImporter = new VortoModelImporter();
    vortoImporter.setModelRepoFactory(repositoryFactory);
    vortoImporter.setModelParserFactory(modelParserFactory);
    
    // TODO : Fix!
    //vortoImporter.setTenantUserService(tenantUserService);
    
    vortoImporter.setUploadStorage(new InMemoryTemporaryStorage());
    vortoImporter.setUserRepository(accountService);

    UploadModelResult uploadResult =
        vortoImporter.upload(
            FileUpload.create("sample_models/lwm2m/lwm2m.zip",
                IOUtils.toByteArray(
                    new ClassPathResource("sample_models/lwm2m/lwm2m.zip").getInputStream())),
            Optional.empty(),
            alex);

    assertEquals(false, uploadResult.isValid());
    assertEquals(1, uploadResult.getReport().size());
    assertEquals(MessageSeverity.ERROR, uploadResult.getReport().get(0).getMessage().getSeverity());
    assertNotNull(uploadResult.getReport().get(0).getMessage().getMessage());
    System.out.println(uploadResult.getReport().get(0).getMessage().getMessage());
  }

  private void verifyOneModelAreMissing(List<ValidationReport> uploadResults) {
    assertEquals(false, uploadResults.stream().allMatch(result -> result.isValid()));
    assertEquals(uploadResults.size(),
        uploadResults.stream().filter(result -> result.getMessage() != null).count());
  }

  private void verifyAllModelsAreValid(List<ValidationReport> uploadResults) {
    assertEquals(true, uploadResults.stream().allMatch(result -> result.isValid()));
  }

  private byte[] loadContentForFile(String fileName) throws IOException {
    return IOUtils.toByteArray(new ClassPathResource(fileName).getInputStream());
  }

}
