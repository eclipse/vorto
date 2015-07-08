/*******************************************************************************
/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/

package org.eclipse.vorto.remoterepository.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.eclipse.vorto.remoterepository.internal.dao.FilesystemModelDAO;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelFactory;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationcontext.xml" })
public class ModelDAOTest {

	@Autowired
	FilesystemModelDAO dao;

	@Before
	public void init() {
		Path modelPath = Paths.get("src/test/resources/modelexamples");

		dao.setRepositoryBaseDirectory(modelPath.toString());
	}

	@Test
	public void getModel() {
		ModelId modelId = ModelFactory.newModelId(ModelType.INFORMATIONMODEL,
				"org.eclipse.vorto", "1.0.0", "PhilipsHue");
		ModelContent content = dao.getModelById(modelId);
		assertNotNull(content);
	}

	@Test
	public void getAllInformationModelContent() {
		Collection<ModelContent> models = dao
				.getAllModels(ModelType.INFORMATIONMODEL);
		assertEquals(1, models.size());
	}

	@Test
	public void getAllFunctionBlockModelsContent() {
		Collection<ModelContent> models = dao
				.getAllModels(ModelType.FUNCTIONBLOCK);
		assertEquals(1, models.size());
	}
}
