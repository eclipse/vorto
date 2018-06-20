package org.eclipse.vorto.repository.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class ModelImporterTest extends AbstractIntegrationTest {

	@Test
	public void testUploadSameModelTwiceByAuthor() throws Exception {
		IUserContext alex = UserContext.user("alex");
		importModel("Color.type", alex);
		UploadModelResult uploadResult =  this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())), alex);
		assertTrue(uploadResult.getReport().isValid());
	}
	
	@Test
	public void testUploadSameModelTwiceByDifferent() throws Exception {
		importModel("Color.type", UserContext.user("alex"));
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())), UserContext.user("stefan"));
		assertFalse(uploadResult.getReport().isValid());
	}
	
	@Test
	public void testUploadSameModelByAdmin() throws Exception {
		IUserContext admin = UserContext.user("admin");
		importModel("Color.type", UserContext.user("alex"));
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color2.type").getInputStream())), admin);
		assertTrue(uploadResult.getReport().isValid());
		
		this.importer.doImport(uploadResult.getHandleId(), admin);
		ModelFileContent content = modelRepository.getModelContent(uploadResult.getReport().getModel().getId());
		assertTrue(new String(content.getContent(),"utf-8").contains("mandatory b as int"));
	}
	
	@Test
	public void testOverwriteInvalidModelByAdmin() throws Exception {
		importModel("Color.type", UserContext.user("alex"));
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("Color.type",
				IOUtils.toByteArray(new ClassPathResource("sample_models/Color3.type").getInputStream())), UserContext.user("admin"));
		assertFalse(uploadResult.getReport().isValid());
	}
	
	@Test
	public void testUploadFileMissingReference() throws Exception {
		UploadModelResult uploadResult = this.importer.upload(FileUpload.create("ColorLightIM.infomodel",
				IOUtils.toByteArray(new ClassPathResource("sample_models/ColorLightIM.infomodel").getInputStream())), UserContext.user("admin"));
		assertFalse(uploadResult.getReport().isValid());
		assertEquals(1,uploadResult.getReport().getUnresolvedReferences().size());
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

}
