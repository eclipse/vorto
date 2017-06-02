/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.internal.service.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.internal.service.utils.BulkUploadHelper;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class BulkUploadTest extends AbstractIntegrationTest  {

	private BulkUploadHelper bulkUploadHelper;
	
	@Override
	public void beforeEach() throws Exception {
		super.beforeEach();
		bulkUploadHelper = new BulkUploadHelper(this.modelRepository, new InMemoryTemporaryStorage());
	}
	
	@Test
	public void testUploadValidModels() throws IOException {
		String fileName = "sample_models/valid-models.zip";
		List<UploadModelResult> uploadResults = bulkUploadHelper.uploadMultiple(loadContentForFile(fileName),fileName);
		assertEquals(3, uploadResults.size());
		verifyAllModelsAreValid(uploadResults);
	}

	@Test
	public void testUploadOneMissingModels() throws IOException {
		String fileName = "sample_models/missing-models.zip";
		List<UploadModelResult> uploadResults = bulkUploadHelper.uploadMultiple(loadContentForFile(fileName),fileName);
		assertEquals(2, uploadResults.size());
		verifyOneModelAreMissing(uploadResults);
	}
	
	@Test
	public void testUploadInvalidModels() throws IOException {
		String fileName = "sample_models/invalid-models.zip";
		List<UploadModelResult> result = bulkUploadHelper.uploadMultiple(loadContentForFile(fileName),fileName);
		assertEquals(2,result.size());
		assertFalse(result.get(0).isValid());
		assertFalse(result.get(1).isValid()); 
	}
	
	@Test
	public void testUploadDifferentModelTypesWithSameId() throws Exception {
		String fileName = "sample_models/modelsWithSameId.zip";
		List<UploadModelResult> result = bulkUploadHelper.uploadMultiple(loadContentForFile(fileName),fileName);
		assertEquals(2,result.size());
		assertFalse(result.get(1).isValid()); 	
	}
	
	@Test
	public void testUploadModelWithInvalidGrammar() throws Exception {
		String fileName = "sample_models/modelsWithWrongGrammar.zip";
		List<UploadModelResult> result = bulkUploadHelper.uploadMultiple(loadContentForFile(fileName),fileName);
		assertEquals(2,result.size());
		assertFalse(result.get(0).isValid());
		assertFalse(result.get(1).isValid()); 
		assertEquals("org.eclipse.vorto.examples",result.get(0).getModelResource().getId().getNamespace());
		assertEquals("Accelerometer",result.get(0).getModelResource().getId().getName());
		assertEquals("0.0.1",result.get(0).getModelResource().getId().getVersion());
	}
	/*
	 * Steps: Upload an information model to the vorto repository
	 * Expected Result: The formating of the DSL should be readable.
	 */
	@Ignore
	@Test
	public void toDotestUploadValidModelsCheckFormat() throws Exception {
		
		fail("To check formatting of the uploaded models Model preview");
		
	}
	/*
	 * Steps: Upload a model of acceptable file size
	 * Expected Result: Model should be uploaded successfully
	 */
	@Ignore
	@Test
	public void toDotestUploadValidModelsAcceptableFileSize() throws Exception {
		
		fail("To check the acceptable file size of uploaded models missing");
		
	}
	/*
	 * Steps: Upload a model file of unacceptable file size
	 * Expected Result: Model should not be allowed to be uploaded. An error message should be displayed that the uploaded file size is unacceptable.
	 */
	@Ignore
	@Test
	public void toDotestUploadValidModelsInvalidFileSize() throws Exception {
		
		fail("Test cases for checking the invalid filesize of uploaded models missing");
		
	}
	/*
	 * Steps: Upload a virus infected file (test file from EICAR http://www.eicar.org/86-0-Intended-use.html)
	 * Expected Result: Uploading of infected file should not be allowed.
	 */
	@Ignore
	@Test
	public void toDotestUploadingVirusInfectedFile() throws Exception {
		fail("To test uploading of Virus infected file ");
		
	}
	
	
	private void verifyOneModelAreMissing(List<UploadModelResult> uploadResults) {
		assertEquals(false, uploadResults.stream().allMatch(result -> result.isValid()));
		assertEquals((uploadResults.size() - 1), uploadResults.stream().filter(result -> result.getErrorMessage() == null).count());
		assertEquals(1, uploadResults.stream().filter(result -> result.getErrorMessage() !=null).count());
	}

	private void verifyAllModelsAreValid(List<UploadModelResult> uploadResults) {
		assertEquals(true, uploadResults.stream().allMatch(result -> result.isValid()));
		assertTrue(uploadResults.stream().allMatch(result -> result.getErrorMessage() == null));
		assertTrue(uploadResults.stream().allMatch(result -> result.getHandleId() != null));
	}
	
	private byte[] loadContentForFile(String fileName) throws IOException {
		return IOUtils.toByteArray(new ClassPathResource(fileName).getInputStream());
	}

}
