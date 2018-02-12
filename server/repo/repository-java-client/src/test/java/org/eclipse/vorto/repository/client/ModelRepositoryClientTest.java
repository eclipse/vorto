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

package org.eclipse.vorto.repository.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.repository.api.IModelPublisher;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelQueryBuilder;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.api.mapping.IMapping;
import org.eclipse.vorto.repository.api.upload.ModelPublishException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class ModelRepositoryClientTest {

	private static final String PUBLIC_ECLIPSE_REPO_URL = "http://vorto.eclipse.org";
//	private static final String PUBLIC_ECLIPSE_REPO_URL = "http://localhost:8080/infomodelrepository";
	
	private IModelRepository modelRepo;
	private IMapping mapping;
	private IModelPublisher publisher;
	
	@Before
	public void setUp() {
		modelRepo = RepositoryClientBuilder.newBuilder().setBaseUrl(PUBLIC_ECLIPSE_REPO_URL).buildModelRepositoryClient();
		mapping = RepositoryClientBuilder.newBuilder().setBaseUrl(PUBLIC_ECLIPSE_REPO_URL).buildIMappingClient();
		publisher = RepositoryClientBuilder.newBuilder().setBaseUrl(PUBLIC_ECLIPSE_REPO_URL).buildModelPublishClient();
	}

	@Test
	public void testSearchModels() throws Exception {
		Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().freeText("Incline").build()).get();
		assertTrue(models.size() > 0);
	}
	
	@Test
	public void testSearchModelBySpecificName() throws Exception {
		Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().name("XDK").build()).get();
		assertTrue(models.size() > 0);
	}
	
	@Test
	public void testSearchModelBySpecificType() throws Exception {
		Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().type(ModelType.InformationModel).name("XDK").build()).get();
		assertTrue(models.size() > 0);
	}
	
	@Test
	public void testGetContentOfSpecificModel() throws Exception {
		Infomodel xdkModel = modelRepo.getContent(ModelId.fromPrettyFormat("com.bosch.devices.XDK:1.0.0"), Infomodel.class).get();
		assertNotNull(xdkModel);
		assertTrue(xdkModel.getFunctionblocks().size() > 0);
	}
	
	@Test
	public void testMandatoryFields() throws Exception {
		FunctionblockModel model = modelRepo.getContent(ModelId.fromPrettyFormat("devices.fb.DistanceSensor:1.0.0"), FunctionblockModel.class).get();
		assertNotNull(model);
		assertEquals("distance",model.getStatusProperties().get(0).getName());
		assertEquals(PrimitiveType.DOUBLE,model.getStatusProperties().get(0).getType());
		assertEquals(true,model.getStatusProperties().get(0).isMandatory());
		assertEquals(false,model.getStatusProperties().get(0).isMultiple());
		
		assertEquals("sensor_units",model.getStatusProperties().get(1).getName());
		assertEquals(false,model.getStatusProperties().get(1).isMandatory());
	}
	
	@Test
	public void testGetModelInfoById() throws Exception {
		ModelInfo xdkModel = modelRepo.getById(ModelId.fromPrettyFormat("com.bosch.devices.XDK:1.0.0")).get();
		assertNotNull(xdkModel);
		assertEquals(ModelId.fromPrettyFormat("com.bosch.devices.XDK:1.0.0"),xdkModel.getId());
	}
	
	@Test @Ignore
	public void testGetModelForTargetPlatform() throws Exception {
		FunctionblockModel accelerometer = modelRepo.getContent(ModelId.fromPrettyFormat("com.ipso.smartobjects.Accelerometer:0.0.1"),FunctionblockModel.class,"lwm2m").get();
		assertNotNull(accelerometer);
		assertEquals("lwm2m",accelerometer.getTargetPlatformKey());
		assertTrue(accelerometer.getStereotype("Object").isPresent());
		assertEquals(7,accelerometer.getStereotype("Object").get().getAttributes().size());
	}
	
	@Test  @Ignore //FIXME : Repository REST URL Changes might have an impact on this test case. REST Endpoints should be tested along with repository
	public void testGetModelForNotAvailableTargetPlatform() throws Exception {
		FunctionblockModel accelerometer = modelRepo.getContent(ModelId.fromPrettyFormat("com.ipso.smartobjects.Accelerometer:0.0.1"),FunctionblockModel.class,"omadm").get();
		assertNotNull(accelerometer);
	}
		
	@Test  @Ignore
	public void testQueryModelPropertyByMappedAttribute() throws Exception {
		FunctionblockModel accelerometer = modelRepo.getContent(ModelId.fromPrettyFormat("com.ipso.smartobjects.Accelerometer:0.0.1"),FunctionblockModel.class,"lwm2m").get();
		
		List<ModelProperty> properties = mapping.newPropertyQuery(accelerometer).stereotype("Resource").attribute("ID", "5702").list();
		assertEquals(1,properties.size());
		assertEquals("x_value",properties.get(0).getName());
	}
	
	@Test
	@Ignore
	public void testPublishModel() throws Exception {
		File uploadableFile = new File("src/test/resources/Color3.type");
		ModelId uploadedModelId = publisher.publish(ModelType.Datatype, FileUtils.readFileToString(uploadableFile));
		assertEquals(new ModelId("Color","demo","1.0.0"),uploadedModelId);
	}
	
	@Test (expected=ModelPublishException.class)
	@Ignore
	public void testPublishInvalidModel() throws Exception {
		File uploadableFile = new File("src/test/resources/Color3_invalid.type");
		publisher.publish(ModelType.Datatype, FileUtils.readFileToString(uploadableFile));
	}
}
