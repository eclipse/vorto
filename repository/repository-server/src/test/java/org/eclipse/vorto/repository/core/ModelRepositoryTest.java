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
package org.eclipse.vorto.repository.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.importer.FileUpload;
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
	public void testUploadSameModelTwiceByAuthor() throws Exception {
		IUserContext alex = UserContext.user("alex");
		checkinModel("Color.type", alex);
		UploadModelResult uploadResult =  this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())), alex);
		assertTrue(uploadResult.getReport().isValid());
	}
	
	@Test
	public void testUploadSameModelTwiceByDifferent() throws Exception {
		checkinModel("Color.type", UserContext.user("alex"));
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())), UserContext.user("stefan"));
		assertFalse(uploadResult.getReport().isValid());
	}
	
	@Test
	public void testUploadSameModelByAdmin() throws Exception {
		IUserContext admin = UserContext.user("admin");
		checkinModel("Color.type", UserContext.user("alex"));
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())), admin);
		assertTrue(uploadResult.getReport().isValid());
		
		this.importer.doImport(uploadResult.getHandleId(), admin);
		ModelFileContent content = modelRepository.getModelContent(uploadResult.getReport().getModel().getId());
		assertTrue(new String(content.getContent(),"utf-8").contains("mandatory b as int"));
	}
	
	
	@Test
	public void testOverwriteInvalidModelByAdmin() throws Exception {
		checkinModel("Color.type", UserContext.user("alex"));
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color3.type").getInputStream())), UserContext.user("admin"));
		assertFalse(uploadResult.getReport().isValid());
	}
	
	@Test
	public void tesUploadValidModel() throws IOException {
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream())), UserContext.user("admin"));
		assertEquals(true, uploadResult.getReport().isValid());
		assertNull(uploadResult.getReport().getErrorMessage());
		assertNotNull(uploadResult.getHandleId());
		ModelInfo resource = uploadResult.getReport().getModel();
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
	public void testCheckinValidModel() throws Exception {
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream())), UserContext.user("admin"));
		assertEquals(true, uploadResult.getReport().isValid());
		assertEquals(0, modelRepository.search("*").size());

		User user1 = new User();
		user1.setUsername("alex");

		User user2 = new User();
		user2.setUsername("andi");

		Collection<User> recipients = new ArrayList<User>();
		recipients.add(user1);
		recipients.add(user2);

		when(userRepository.findAll()).thenReturn(recipients);

		this.importer.doImport(uploadResult.getHandleId(), UserContext.user(user1.getUsername()));

		Thread.sleep(1000);
		assertEquals(1, modelRepository.search("*").size());
	}

	@Test
	public void testCheckinInvalidModel() throws Exception {
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Colorlight.fbmodel",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Colorlight.fbmodel").getInputStream())), UserContext.user("admin"));
		assertEquals(false, uploadResult.getReport().isValid());
		assertNotNull(uploadResult.getReport().getErrorMessage());
	}

	@Test
	public void testGetModelById() throws Exception {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		ModelInfo result = modelRepository
				.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		assertNotNull(result);
	}

	@Test
	public void testGetDSLContentForModel() throws Exception {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		ModelFileContent fileContent = modelRepository.getModelContent(
				ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		String actualContent = new String(fileContent.getContent(), "UTF-8");
		String expectedContent = IOUtils.toString(new ClassPathResource("sample_models/Color.type").getInputStream());
		assertEquals(expectedContent, actualContent);
		assertEquals("Color.type",fileContent.getFileName());
	}

	@Test
	public void testGetReferencesFromModel() throws Exception {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		assertEquals(2, modelRepository.search("*").size());
		ModelInfo result = modelRepository
				.getById(ModelId.fromReference("org.eclipse.vorto.examples.fb.ColorLight", "1.0.0"));
		assertEquals(1, result.getReferences().size());
		assertEquals("org.eclipse.vorto.examples.type.Color:1.0.0", result.getReferences().get(0).getPrettyFormat());
	}

	@Test
	public void testGetReferencedBy() throws Exception {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		assertEquals(2, modelRepository.search("*").size());
		ModelInfo result = modelRepository
				.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		assertEquals(1, result.getReferencedBy().size());
		assertEquals("org.eclipse.vorto.examples.fb.ColorLight:1.0.0",
				result.getReferencedBy().get(0).getPrettyFormat());
	}

	@Test
	public void testSearchAllModels() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		assertEquals(4, modelRepository.search("*").size());
	}
	
	@Test
	public void testSearchModelWithCriteria1() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		assertEquals(2, modelRepository.search("color").size());
	}
	

	@Test
	public void testGetModelWithNoImage() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		assertEquals(false,
				this.modelRepository.getById(new ModelId("HueLightStrips", "com.mycompany", "1.0.0")).isHasImage());
	}

	@Test
	public void testGetModelWithImage() throws Exception {
		final ModelId modelId = new ModelId("HueLightStrips", "com.mycompany", "1.0.0");
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		this.modelRepository.addModelImage(modelId,
				IOUtils.toByteArray(new ClassPathResource("sample_models/sample.png").getInputStream()));
		assertEquals(true, this.modelRepository.getById(modelId).isHasImage());
	}
	
	@Test
	public void testRemoveModelImage() throws Exception {
		final ModelId modelId = new ModelId("HueLightStrips", "com.mycompany", "1.0.0");
		byte[] modelContent = IOUtils.toByteArray(new ClassPathResource("sample_models/sample.png").getInputStream());
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		this.modelRepository.addModelImage(modelId, modelContent);
		assertTrue(this.modelRepository.getModelImage(modelId).length > 0);
		
		this.modelRepository.removeModelImage(modelId);
		assertFalse(this.modelRepository.getById(modelId).isHasImage());
	}

	@Test
	public void testGetModelImage() throws Exception {
		final ModelId modelId = new ModelId("HueLightStrips", "com.mycompany", "1.0.0");
		byte[] modelContent = IOUtils.toByteArray(new ClassPathResource("sample_models/sample.png").getInputStream());
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		this.modelRepository.addModelImage(modelId, modelContent);
		assertTrue(this.modelRepository.getModelImage(modelId).length > 0);
	}

	@Test
	public void testOverrideImage() throws Exception {
		final ModelId modelId = new ModelId("HueLightStrips", "com.mycompany", "1.0.0");
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		byte[] modelContent = IOUtils.toByteArray(new ClassPathResource("sample_models/sample.png").getInputStream());
		this.modelRepository.addModelImage(modelId, modelContent);
		this.modelRepository.addModelImage(modelId, modelContent);
	}

	@Test
	public void testSearchModelWithFilters1() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		assertEquals(1, modelRepository.search("name:Color").size());
		assertEquals(3, modelRepository.search("name:Color name:Switcher name:HueLightStrips").size());
	}

	@Test
	public void testSearchModelWithFilters2() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		assertEquals(1, modelRepository.search("name:Color version:1.0.0   ").size());
		assertEquals(0, modelRepository.search("name:Color version:1.0.1").size());
	}

	@Test
	public void testSearchModelWithFilters3() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("ColorLightIM.infomodel");
		checkinModel("HueLightStrips.infomodel");
		assertEquals(1, modelRepository.search("namespace:org.eclipse.vorto.examples.fb").size());
		assertEquals(1, modelRepository.search("namespace:com.mycompany.fb").size());
		assertEquals(2, modelRepository.search("namespace:com.mycompany   version:1.0.0").size());

	}

	@Test
	public void testSearchModelWithFilters4() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("ColorLightIM.infomodel");
		checkinModel("HueLightStrips.infomodel");
		assertEquals(0, modelRepository.search("name:Switcher InformationModel").size());
		assertEquals(1, modelRepository.search("name:Switcher Functionblock").size());
		assertEquals(2, modelRepository.search("Functionblock").size());
	}

	@Test
	public void testSearchModelsByCreator() {
		IUserContext alex = UserContext.user("alex");
		checkinModel("Color.type", alex);
		checkinModel("Colorlight.fbmodel", alex);
		checkinModel("Switcher.fbmodel", UserContext.user("admin"));
		checkinModel("ColorLightIM.infomodel", UserContext.user("admin"));
		checkinModel("HueLightStrips.infomodel", UserContext.user("admin"));
		
		assertEquals(2, modelRepository.search("author:" + alex.getHashedUsername()).size());
	}
	
	@Test
	public void testUploadCorruptModelMissingVersion() throws Exception {
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("sample_models/Corrupt-model_missingVersion.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Corrupt-model_missingVersion.type").getInputStream())), UserContext.user("admin"));
		assertEquals(false,uploadResult.getReport().isValid());
		assertNotNull(uploadResult.getReport().getErrorMessage());
	}
	
	@Test
	public void testUploadCorruptModelVersion() throws Exception {
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("sample_models/Corrupt-model_namespace.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Corrupt-model_namespace.type").getInputStream())), UserContext.user("admin"));
		assertEquals(false,uploadResult.getReport().isValid());
		assertNotNull(uploadResult.getReport().getErrorMessage());
	}
	
	@Test (expected = FileNotFoundException.class)
	public void testUploadInvalidFileName() throws Exception {
		this.importer.upload(FileUpload.create("sample_models/Bogus.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.typ").getInputStream())), UserContext.user("admin"));
	}
	
	@Test
	public void testUploadModelThatCompliesToOlderVersionOfMetaModel() throws Exception {
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Corrupt-model_olderVersionOfMetaModel.fbmodel",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Corrupt-model_olderVersionOfMetaModel.fbmodel").getInputStream())), UserContext.user("admin"));
		assertEquals(false,uploadResult.getReport().isValid());
		assertNotNull(uploadResult.getReport().getErrorMessage());
	}
	
	@Test
	public void testDeleteUnUsedType() {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		this.modelRepository.removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		assertEquals(0, modelRepository.search("*").size());
	}
	
	@Test
	public void testDeleteAndCheckinSameModel() {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		this.modelRepository.removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		assertEquals(0, modelRepository.search("*").size());
		checkinModel("Color.type");
		assertEquals(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"),modelRepository.search("*").get(0).getId());
	}

	@Test
	public void testDeleteUsedType() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		assertEquals(2, modelRepository.search("*").size());
		try {
			this.modelRepository.removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
			fail("Expected exception");
		} catch (ModelReferentialIntegrityException ex) {
			assertEquals(1, ex.getReferencedBy().size());
		}
	}
	
	@Test
	public void testAuthorSearch() {
		IUserContext erle = UserContext.user("erle");
		IUserContext admin = UserContext.user("admin");
		checkinModel("Color.type", erle);
		checkinModel("Colorlight.fbmodel", erle);
		checkinModel("Switcher.fbmodel", admin);
		
		assertEquals(2, modelRepository.search("author:" + UserContext.user("erle").getHashedUsername()).size());
		assertEquals(1, modelRepository.search("author:" + UserContext.user("admin").getHashedUsername()).size());
	}

}
