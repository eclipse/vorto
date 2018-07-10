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

import java.util.Collection;
import java.util.List;

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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore //FIXME: The test cases should run against a test system that this test suite sets up in the beginning.
public class ModelRepositoryClientTest {

	private static final String PUBLIC_ECLIPSE_REPO_URL = "http://localhost:8080/infomodelrepository";
	
	private IModelRepository modelRepo;
	private IMapping mapping;
	
	@Before
	public void setUp() {
		RepositoryClientBuilder builder = RepositoryClientBuilder.newBuilder().setBaseUrl(PUBLIC_ECLIPSE_REPO_URL);
		
		if (System.getProperty("http.proxyHost") != null) {
			builder.setProxyHost(System.getProperty("http.proxyHost"));
			builder.setProxyPort(Integer.valueOf(System.getProperty("http.proxyPort")));
			System.out.println("Using proxy -> " + System.getProperty("http.proxyHost") + ":" + Integer.valueOf(System.getProperty("http.proxyPort")));
		}
		
		modelRepo = builder.buildModelRepositoryClient();
		mapping = builder.buildIMappingClient();
	}

	@Test
	public void testSearchModels() throws Exception {
		Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().freeText("B").build()).get();
		assertTrue(models.size() > 0);
	}
	
	@Test
	public void testSearchModelBySpecificName() throws Exception {
		Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().name("Bus").build()).get();
		assertTrue(models.size() > 0);
	}
	
	@Test
	public void testSearchModelBySpecificType() throws Exception {
		Collection<ModelInfo> models = modelRepo.search(new ModelQueryBuilder().type(ModelType.InformationModel).name("Bus").build()).get();
		assertTrue(models.size() > 0);
	}
	
	@Test
	public void testGetContentOfSpecificModel() throws Exception {
		Infomodel model = modelRepo.getContent(ModelId.fromPrettyFormat("com.bosch.Watch:1.0.0"), Infomodel.class).get();
		assertNotNull(model);
		assertTrue(model.getFunctionblocks().size() > 0);
	}
	
	@Test
	public void testMandatoryFields() throws Exception {
		FunctionblockModel model = modelRepo.getContent(ModelId.fromPrettyFormat("com.ipso.smartobjects.Gyrometer:1.1.0"), FunctionblockModel.class).get();
		assertNotNull(model);
		assertEquals("xValue",model.getStatusProperties().get(0).getName());
		assertEquals(PrimitiveType.FLOAT,model.getStatusProperties().get(0).getType());
		assertEquals(true,model.getStatusProperties().get(0).isMandatory());
		assertEquals(false,model.getStatusProperties().get(0).isMultiple());
	}
	
	@Test
	public void testGetModelForTargetPlatform() throws Exception {
		FunctionblockModel accelerometer = modelRepo.getContent(ModelId.fromPrettyFormat("com.ipso.smartobjects.Gyrometer:1.1.0"),FunctionblockModel.class,"lwm2m").get();
		assertNotNull(accelerometer);
		assertEquals("lwm2m",accelerometer.getTargetPlatformKey());
		assertTrue(accelerometer.getStereotype("Object").isPresent());
		assertEquals(6,accelerometer.getStereotype("Object").get().getAttributes().size());
	}
	
	@Test  //FIXME : Repository REST URL Changes might have an impact on this test case. REST Endpoints should be tested along with repository
	public void testGetModelForNotAvailableTargetPlatform() throws Exception {
		FunctionblockModel accelerometer = modelRepo.getContent(ModelId.fromPrettyFormat("com.ipso.smartobjects.Gyrometer:1.1.0"),FunctionblockModel.class,"omadm").get();
		assertNotNull(accelerometer);
	}
		
	@Test
	public void testQueryModelPropertyByMappedAttribute() throws Exception {
		FunctionblockModel accelerometer = modelRepo.getContent(ModelId.fromPrettyFormat("com.ipso.smartobjects.Gyrometer:1.1.0"),FunctionblockModel.class,"lwm2m").get();
		
		List<ModelProperty> properties = mapping.newPropertyQuery(accelerometer).stereotype("Resource").attribute("ID", "5702").list();
		assertEquals(1,properties.size());
		assertEquals("xValue",properties.get(0).getName());
	}
	
}
