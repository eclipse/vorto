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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
 
import java.io.IOException;
import java.util.List;

import org.eclipse.vorto.repository.internal.service.utils.BulkUploadHelper;
import org.eclipse.vorto.repository.model.UploadModelResult;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class BulkUploadTest extends AbstractIntegrationTest  {

	private BulkUploadHelper bulkUploadHelper;
	
	@Override
	public void beforeEach() throws Exception {
		super.beforeEach();
		bulkUploadHelper = new BulkUploadHelper(this.modelRepository);
	}
	
	@Test
	public void testUploadValidModels() throws IOException {
		String fileName = "sample_models/valid-models.zip";
		List<UploadModelResult> uploadResults = bulkUploadHelper.uploadMultiple(fromClasspath(fileName));
		assertEquals(3, uploadResults.size());
		verifyAllModelsAreValid(uploadResults);
	}

	@Test
	public void testUploadOneMissingModels() throws IOException {
		String fileName = "sample_models/missing-models.zip";
		List<UploadModelResult> uploadResults = bulkUploadHelper.uploadMultiple(fromClasspath(fileName));
		assertEquals(2, uploadResults.size());
		verifyOneModelAreMissing(uploadResults);
	}
	
	@Test
	public void testUploadInvalidModels() throws IOException {
		String fileName = "sample_models/invalid-models.zip";
		List<UploadModelResult> result = bulkUploadHelper.uploadMultiple(fromClasspath(fileName));
		assertEquals(2,result.size());
		assertFalse(result.get(0).isValid());
		assertFalse(result.get(1).isValid()); 
	}
	
	@Test
	public void testUploadDifferentModelTypesWithSameId() throws Exception {
		String fileName = "sample_models/modelsWithSameId.zip";
		List<UploadModelResult> result = bulkUploadHelper.uploadMultiple(fromClasspath(fileName));
		assertEquals(2,result.size());
		assertFalse(result.get(1).isValid()); 	
	}
	
	@Test
	public void testUploadModelWithInvalidGrammar() throws Exception {
		String fileName = "sample_models/modelsWithWrongGrammar.zip";
		List<UploadModelResult> result = bulkUploadHelper.uploadMultiple(fromClasspath(fileName));
		assertEquals(2,result.size());
		assertFalse(result.get(0).isValid());
		assertFalse(result.get(1).isValid()); 
		assertEquals("org.eclipse.vorto.examples",result.get(0).getModelResource().getId().getNamespace());
		assertEquals("Accelerometer",result.get(0).getModelResource().getId().getName());
		assertEquals("0.0.1",result.get(0).getModelResource().getId().getVersion());
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
	
	private String fromClasspath(String fileName) throws IOException {
		return new ClassPathResource(fileName).getFile().getAbsolutePath();
	}

}
