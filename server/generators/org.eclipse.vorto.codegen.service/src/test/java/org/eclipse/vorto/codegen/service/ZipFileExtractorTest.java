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
package org.eclipse.vorto.codegen.service;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.service.generator.web.utils.MappingZipFileExtractor;
import org.eclipse.vorto.service.generator.web.utils.ModelZipFileExtractor;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class ZipFileExtractorTest {
	
	@Test
	public void testGetInformationModelFromZipFile() throws Exception {
		ModelZipFileExtractor extractor = new ModelZipFileExtractor(IOUtils.toByteArray(new ClassPathResource("models.zip").getInputStream()));
		InformationModel model = extractor.extract(new ModelId(ModelType.InformationModel,"TI_SensorTag_CC2650","examples.informationmodels.sensors","1.0.0"));
		assertNotNull(model);
	}
	
	
	@Test
	public void testCreateMappingContextFromZipFile() throws Exception {
		MappingZipFileExtractor mappingFileExtractor = new MappingZipFileExtractor(IOUtils.toByteArray(new ClassPathResource("mappings.zip").getInputStream()));
		assertNotNull(mappingFileExtractor.extract());
	}
	
}
