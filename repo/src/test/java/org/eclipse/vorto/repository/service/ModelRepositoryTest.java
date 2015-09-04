/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.repository.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.internal.service.JcrModelRepository;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.ModelType;
import org.eclipse.vorto.repository.model.UploadModelResult;
import org.eclipse.vorto.repository.service.IModelRepository.ContentType;
import org.junit.Test;
import org.modeshape.test.ModeShapeSingleUseTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;

public class ModelRepositoryTest extends ModeShapeSingleUseTest  {

	private JcrModelRepository modelRepository;
	
	public void beforeEach() throws Exception {
		super.beforeEach();
		startRepositoryWithConfiguration(new ClassPathResource("vorto-repository.json").getInputStream());
		modelRepository = new JcrModelRepository();
		modelRepository.setSession(jcrSession());
		modelRepository.createValidators();


	}

	@Test
	public void testQueryWithEmptyExpression() {
		assertEquals(0, modelRepository.search("").size());
	}

	@Test
	public void tesUploadValidModel() throws IOException {
		UploadModelResult uploadResult = modelRepository.upload(IOUtils
				.toByteArray(new ClassPathResource("sample_models/Color.type")
						.getInputStream()), "Color.type");
		assertEquals(true, uploadResult.isValid());
		assertNull(uploadResult.getErrorMessage());
		assertNotNull(uploadResult.getHandleId());
		ModelResource resource = uploadResult.getModelResource();
		assertEquals("org.eclipse.vorto.examples.type", resource.getId()
				.getNamespace());
		assertEquals("Color", resource.getId().getName());
		assertEquals("1.0.0", resource.getId().getVersion());
		assertEquals(ModelType.Datatype, resource.getModelType());
		assertEquals(0, resource.getReferences().size());
		assertEquals("Color", resource.getDisplayName());
		assertNull(resource.getDescription());
		assertEquals(0, modelRepository.search("*").size());
	}

//	@Test
//	public void testCheckinValidModel() throws Exception {
//		UploadModelResult uploadResult = modelRepository.upload(IOUtils
//				.toByteArray(new ClassPathResource("sample_models/Color.type")
//						.getInputStream()), "Color.type");
//		assertEquals(true, uploadResult.isValid());
//		assertEquals(0, modelRepository.search("*").size());
//		modelRepository.checkin(uploadResult.getHandleId());
//		assertEquals(1, modelRepository.search("*").size());
//	}
	
	@Test
	public void testCheckinInvalidModel() throws Exception {
		UploadModelResult uploadResult = modelRepository.upload(IOUtils
				.toByteArray(new ClassPathResource("sample_models/Colorlight.fbmodel")
						.getInputStream()), "Colorlight.fbmodel");
		assertEquals(false, uploadResult.isValid());
		assertNotNull(uploadResult.getErrorMessage());
	}
	
	@Test
	public void testGetModelById() throws Exception {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		ModelResource result = modelRepository.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		assertNotNull(result);
	}
	
	@Test
	public void testGetDSLContentForModel() throws Exception {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		byte[] content = modelRepository.getModelContent(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"), ContentType.DSL);
		String actualContent = new String(content,"UTF-8");
		String expectedContent = IOUtils.toString(new ClassPathResource("sample_models/Color.type").getInputStream());
		assertEquals(expectedContent,actualContent);
	}
	
	@Test
	public void testGetXMIContentForModel() throws Exception {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		byte[] content = modelRepository.getModelContent(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"), ContentType.XMI);
		String actualContent = new String(content,"UTF-8");
		String expectedContent = IOUtils.toString(new ClassPathResource("sample_models/Color.xmi").getInputStream());
		assertEquals(expectedContent,actualContent);
	}
	
	@Test
	public void testGetReferencesFromModel() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		assertEquals(2,modelRepository.search("*").size());
		ModelResource result = modelRepository.getById(ModelId.fromReference("org.eclipse.vorto.examples.fb.ColorLight", "1.0.0"));
		assertEquals(1,result.getReferences().size());
		assertEquals("org.eclipse.vorto.examples.type.Color:1.0.0",result.getReferences().get(0).getPrettyFormat());
	}
	
//	@Test
//	public void testGetReferencedBy() {
//		checkinModel("Color.type");
//		checkinModel("Colorlight.fbmodel");
//		assertEquals(2,modelRepository.search("*").size());
//		ModelResource result = modelRepository.getById(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
//		assertEquals(1,result.getReferencedBy().size());
//		assertEquals("org.eclipse.vorto.examples.fb.ColorLight:1.0.0",result.getReferencedBy().get(0).getPrettyFormat());
//	}
	
	@Test
	public void testDeleteUnUsedType() {
		checkinModel("Color.type");
		assertEquals(1,modelRepository.search("*").size());
		modelRepository.removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		assertEquals(0,modelRepository.search("*").size());
	}
	
	@Test
	public void testDeleteUsedType() {
		checkinModel("Color.type");
		checkinModel("ColorLight.fbmodel");
		assertEquals(2,modelRepository.search("*").size());
		try {
			modelRepository.removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
			fail("Expected exception");
		} catch(ModelReferentialIntegrityException ex) {
			assertEquals(1,ex.getReferencedBy().size());
		}
	}
	
	@Test
	public void testSearchAllModels() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		assertEquals(4,modelRepository.search("*").size());
	}
	
	@Test
	public void testSearchModelWithCriteria1() {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		assertEquals(2,modelRepository.search("color").size());
	}
		
	private void checkinModel(String modelName) {
		try {
		UploadModelResult uploadResult = modelRepository.upload(IOUtils
				.toByteArray(new ClassPathResource("sample_models/"+modelName)
						.getInputStream()), modelName);
		Assert.isTrue(uploadResult.isValid(), uploadResult.getErrorMessage());
		modelRepository.checkin(uploadResult.getHandleId());
		modelRepository.search("*");
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
