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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.ModelType;
import org.eclipse.vorto.repository.model.UploadModelResult;
import org.eclipse.vorto.repository.model.User;
import org.eclipse.vorto.repository.service.IModelRepository.ContentType;
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
	public void tesUploadValidModel() throws IOException {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream()), "Color.type");
		assertEquals(true, uploadResult.isValid());
		assertNull(uploadResult.getErrorMessage());
		assertNotNull(uploadResult.getHandleId());
		ModelResource resource = uploadResult.getModelResource();
		assertEquals("org.eclipse.vorto.examples.type", resource.getId().getNamespace());
		assertEquals("Color", resource.getId().getName());
		assertEquals("1.0.0", resource.getId().getVersion());
		assertEquals(ModelType.Datatype, resource.getModelType());
		assertEquals(0, resource.getReferences().size());
		assertEquals("Color", resource.getDisplayName());
		assertNull(resource.getDescription());
		assertEquals(0, modelRepository.search("*").size());
	}

	@Test
	public void testCheckinValidModel() throws Exception {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream()), "Color.type");
		assertEquals(true, uploadResult.isValid());
		assertEquals(0, modelRepository.search("*").size());

		User user1 = new User();
		user1.setUsername("alex");
		user1.setHasWatchOnRepository(true);

		User user2 = new User();
		user2.setUsername("andi");
		user2.setHasWatchOnRepository(false);

		Collection<User> recipients = new ArrayList<User>();
		recipients.add(user1);
		recipients.add(user2);

		when(userRepository.findAll()).thenReturn(recipients);

		verify(notificationService);

		modelRepository.checkin(uploadResult.getHandleId(), user1.getUsername());

		Thread.sleep(1000);
		assertEquals(1, modelRepository.search("*").size());
	}

	@Test
	public void testCheckinInvalidModel() throws Exception {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Colorlight.fbmodel").getInputStream()),
				"Colorlight.fbmodel");
		assertEquals(false, uploadResult.isValid());
		assertNotNull(uploadResult.getErrorMessage());
	}

	@Test
	public void testGetModelById() throws Exception {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		ModelResource result = modelRepository
				.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		assertNotNull(result);
	}

	@Test
	public void testGetDSLContentForModel() throws Exception {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		byte[] content = modelRepository.getModelContent(
				ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"), ContentType.DSL).getContent();
		String actualContent = new String(content, "UTF-8");
		String expectedContent = IOUtils.toString(new ClassPathResource("sample_models/Color.type").getInputStream());
		assertEquals(expectedContent, actualContent);
	}

	@Test
	public void testGetXMIContentForModel() throws Exception {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		byte[] content = modelRepository.getModelContent(
				ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"), ContentType.XMI).getContent();
		String actualContent = new String(content, "UTF-8");
		String expectedContent = IOUtils.toString(new ClassPathResource("sample_models/Color.xmi").getInputStream());
		assertEquals(expectedContent, actualContent);
	}

	@Test
	public void testGetReferencesFromModel() throws Exception {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		assertEquals(2, modelRepository.search("*").size());
		ModelResource result = modelRepository
				.getById(ModelId.fromReference("org.eclipse.vorto.examples.fb.ColorLight", "1.0.0"));
		assertEquals(1, result.getReferences().size());
		assertEquals("org.eclipse.vorto.examples.type.Color:1.0.0", result.getReferences().get(0).getPrettyFormat());
	}

	@Test
	public void testGetReferencedBy() throws Exception {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		assertEquals(2, modelRepository.search("*").size());
		ModelResource result = modelRepository
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
	public void tesUploadMapping() throws IOException {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream()), "Color.type");
		assertEquals(true, uploadResult.isValid());
		assertNull(uploadResult.getErrorMessage());
		assertNotNull(uploadResult.getHandleId());
		ModelResource resource = uploadResult.getModelResource();
		assertEquals("org.eclipse.vorto.examples.type", resource.getId().getNamespace());
		assertEquals("Color", resource.getId().getName());
		assertEquals("1.0.0", resource.getId().getVersion());
		assertEquals(ModelType.Datatype, resource.getModelType());
		assertEquals(0, resource.getReferences().size());
		assertEquals("Color", resource.getDisplayName());
		assertNull(resource.getDescription());
		assertEquals(0, modelRepository.search("*").size());
	}

	@Test
	public void testCheckinValidMapping() throws Exception {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.type").getInputStream()), "Color.type");
		assertEquals(true, uploadResult.isValid());
		assertEquals(0, modelRepository.search("*").size());

		User user = new User();
		user.setUsername("alex");
		user.setHasWatchOnRepository(true);

		Collection<User> users = new ArrayList<User>();
		users.add(user);

		when(userRepository.findAll()).thenReturn(users);

		modelRepository.checkin(uploadResult.getHandleId(), "alex");
		Thread.sleep(2000); // hack coz it might take awhile until index is
							// updated to do a search
		assertEquals(1, modelRepository.search("*").size());

		uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/sample.mapping").getInputStream()),
				"sample.mapping");
		assertEquals(true, uploadResult.isValid());
		modelRepository.checkin(uploadResult.getHandleId(), "alex");
		assertEquals(1, modelRepository.search("-Mapping").size());
	}

	@Test
	public void testGetMappingsOfEntityForTargetPlatform() throws Exception {
		checkinModel("Color.type");
		checkinModel("sample.mapping");
		Thread.sleep(2000);
		assertEquals(1, modelRepository.getMappingModelsForTargetPlatform(
				ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"), "ios").size());
	}

	@Test
	public void testUsedByMappingOfEntity() throws Exception {
		checkinModel("Color.type");
		checkinModel("sample.mapping");
		Thread.sleep(2000);
		assertEquals(1, modelRepository.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"))
				.getReferencedBy().size());
		assertEquals("org.eclipse.vorto.examples.type.Color_ios:1.0.0",
				modelRepository.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"))
						.getReferencedBy().get(0).getPrettyFormat());
	}

	@Test
	public void testGetIndirectMappingsOfInformationModel() throws Exception {
		checkinModel("Color.type");
		checkinModel("sample.mapping");
		checkinModel("Colorlight.fbmodel");
		checkinModel("ColorLightIM.infomodel");
		Thread.sleep(2000);
		assertEquals(1, modelRepository
				.getMappingModelsForTargetPlatform(ModelId.fromReference("com.mycompany.ColorLightIM", "1.0.0"), "ios")
				.size());
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
	public void testUploadCorruptModelMissingVersion() throws Exception {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Corrupt-model_missingVersion.type").getInputStream()),
				"sample_models/Corrupt-model_missingVersion.type");
		assertEquals(false,uploadResult.isValid());
		assertNotNull(uploadResult.getErrorMessage());
	}
	
	@Test
	public void testUploadCorruptModelVersion() throws Exception {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Corrupt-model_namespace.type").getInputStream()),
				"sample_models/Corrupt-model_namespace.type");
		assertEquals(false,uploadResult.isValid());
		assertNotNull(uploadResult.getErrorMessage());
	}
	
	@Test (expected = FileNotFoundException.class)
	public void testUploadInvalidFileName() throws Exception {
		modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color.typ").getInputStream()),
				"sample_models/Bogus.type");
	}
	
	@Test
	public void testUploadModelThatCompliesToOlderVersionOfMetaModel() throws Exception {
		UploadModelResult uploadResult = modelRepository.upload(
				IOUtils.toByteArray(new ClassPathResource("sample_models/Corrupt-model_olderVersionOfMetaModel.fbmodel").getInputStream()),
				"sample_models/Corrupt-model_olderVersionOfMetaModel.fbmodel");
		assertEquals(false,uploadResult.isValid());
		assertNotNull(uploadResult.getErrorMessage());
	}
}
