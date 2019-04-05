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
package org.eclipse.vorto.repository.importer.ipso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.MessageSeverity;
import org.eclipse.vorto.repository.importer.ModelImporterException;
import org.eclipse.vorto.repository.importer.UploadModelResult;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class IPSOModelImporterTest extends AbstractIntegrationTest {

	private ModelImporterIPSO ipsoImporter = null;

	public void beforeEach() throws Exception {
		super.beforeEach();
		this.ipsoImporter = new ModelImporterIPSO();
		this.ipsoImporter.setModelRepoFactory(repositoryFactory);
		this.ipsoImporter.setTenantUserService(tenantUserService);
		this.ipsoImporter.setUploadStorage(new InMemoryTemporaryStorage());
		this.ipsoImporter.setUserRepository(accountService);
		this.ipsoImporter.setModelParserFactory(modelParserFactory);
	}
	
	@Test (expected=ModelImporterException.class) 
	public void testPreventXXEAttack() throws Exception {
		IUserContext alex = createUserContext("alex", "playground");
		importIPSO("3310_xxe.xml", alex);
	}

	@Test
	public void testUploadNonExistingIPSOModel() throws Exception {
		IUserContext alex = createUserContext("alex", "playground");
		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create("sample_models/lwm2m/3310.xml",
						IOUtils.toByteArray(new ClassPathResource("sample_models/lwm2m/3310.xml").getInputStream())),
				alex);

		assertTrue(uploadResult.isValid());
	}

	@Test
	public void testImportModel() throws Exception {
		IUserContext alex = createUserContext("alex", "playground");
		List<ModelInfo> models = importIPSO("3310.xml", alex);
		assertTrue(models.get(0).getImported());
		assertTrue(repositoryFactory.getRepository(alex).getById(models.get(0).getId()).getImported());
	}
	
	@Test
	public void testUploadExistingIPSOModelBySameUser() throws Exception {
		IUserContext alex = createUserContext("alex", "playground");
		List<ModelInfo> models = importIPSO("3310.xml", alex);
		workflow.start(models.get(0).getId(),alex);
		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create("sample_models/lwm2m/3310.xml",
						IOUtils.toByteArray(new ClassPathResource("sample_models/lwm2m/3310.xml").getInputStream())),
				alex);

		assertTrue(uploadResult.isValid());
		assertTrue(uploadResult.hasWarnings());
		assertEquals(ValidationReport.WARNING_MODEL_ALREADY_EXISTS, uploadResult.getReport().get(0).getMessage());
		assertTrue(uploadResult.getReport().get(0).isValid());
	}

	@Test
	public void testUploadExistingIPSOModelByDifferentUser() throws Exception {
		IUserContext alex = createUserContext("alex", "playground");
		importIPSO("3310.xml", alex);

		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create("sample_models/lwm2m/3310.xml",
						IOUtils.toByteArray(new ClassPathResource("sample_models/lwm2m/3310.xml").getInputStream())),
				createUserContext("creator", "playground"));

		assertFalse(uploadResult.isValid());
		assertFalse(uploadResult.hasWarnings());
		assertFalse(uploadResult.isValid());
		assertEquals(ValidationReport.ERROR_MODEL_ALREADY_EXISTS, uploadResult.getReport().get(0).getMessage());
	}

	@Test
	public void testUploadExistingIPSOModelInDraftStateByAdmin() throws Exception {
		IUserContext alex = createUserContext("alex", "playground");
		List<ModelInfo> models = importIPSO("3310.xml", alex);
		workflow.start(models.get(0).getId(),alex);

		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create("sample_models/lwm2m/3310.xml",
						IOUtils.toByteArray(new ClassPathResource("sample_models/lwm2m/3310.xml").getInputStream())),
				createUserContext("admin", "playground"));

		assertTrue(uploadResult.hasWarnings());
		assertTrue(uploadResult.isValid());
		assertEquals(ValidationReport.WARNING_MODEL_ALREADY_EXISTS, uploadResult.getReport().get(0).getMessage());
		assertTrue(uploadResult.getReport().get(0).isValid());
	}

	private List<ModelInfo> importIPSO(String modelName, IUserContext user) throws Exception {
		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload
						.create(modelName,
								IOUtils.toByteArray(
										new ClassPathResource("sample_models/lwm2m/" + modelName).getInputStream())),
				user);
		return this.ipsoImporter.doImport(uploadResult.getHandleId(), user);
	}

	@Test
	public void testUploadZipContainingNonIPSOFiles() throws Exception {
		IUserContext alex = createUserContext("alex", "playground");
		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create("sample_models/valid-models.zip",
						IOUtils.toByteArray(new ClassPathResource("sample_models/valid-models.zip").getInputStream())),alex);
		assertEquals(false, uploadResult.isValid());
		assertEquals(1, uploadResult.getReport().size());
		assertEquals(MessageSeverity.ERROR, uploadResult.getReport().get(0).getMessage().getSeverity());
		assertNotNull(uploadResult.getReport().get(0).getMessage().getMessage());
		System.out.println(uploadResult.getReport().get(0).getMessage().getMessage());
	}
	
}
