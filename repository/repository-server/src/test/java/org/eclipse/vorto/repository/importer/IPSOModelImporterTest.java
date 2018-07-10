package org.eclipse.vorto.repository.importer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.InMemoryTemporaryStorage;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.importer.ipso.ModelImporterIPSO;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class IPSOModelImporterTest extends AbstractIntegrationTest {

	private ModelImporterIPSO ipsoImporter = null;
	
	public void beforeEach() throws Exception {
		super.beforeEach();
		this.ipsoImporter = new ModelImporterIPSO();
		this.ipsoImporter.setModelRepository(modelRepository);
		this.ipsoImporter.setUploadStorage(new InMemoryTemporaryStorage());
		this.ipsoImporter.setUserRepository(userRepository);
	}
	
	@Test
	public void testUploadNonExistingIPSOModel() throws Exception {
		IUserContext alex = UserContext.user("alex");
		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create("sample_models/lwm2m/3310.xml",
						IOUtils.toByteArray(new ClassPathResource("sample_models/lwm2m/3310.xml").getInputStream())),
				alex);
		assertTrue(uploadResult.isValid());
	}
	
	@Test
	public void testUploadExistingIPSOModelBySameUser() throws Exception {
		IUserContext alex = UserContext.user("alex");
		List<ModelInfo> models = importIPSO("3310.xml", alex);
		workflow.start(models.get(0).getId());
		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create("sample_models/lwm2m/3310.xml",
						IOUtils.toByteArray(new ClassPathResource("sample_models/lwm2m/3310.xml").getInputStream())),
				alex);
		assertTrue(uploadResult.isValid());
	}
	
	@Test
	public void testUploadExistingIPSOModelByDifferentUser() throws Exception {
		IUserContext alex = UserContext.user("alex");
		importIPSO("3310.xml", alex);
		
		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create("sample_models/lwm2m/3310.xml",
						IOUtils.toByteArray(new ClassPathResource("sample_models/lwm2m/3310.xml").getInputStream())),
				UserContext.user("stefan"));
		assertFalse(uploadResult.isValid());
	}
	
	@Test
	public void testUploadExistingIPSOModelByAdmin() throws Exception {
		IUserContext alex = UserContext.user("alex");
		List<ModelInfo> models = importIPSO("3310.xml", alex);
		workflow.start(models.get(0).getId());
		
		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create("sample_models/lwm2m/3310.xml",
						IOUtils.toByteArray(new ClassPathResource("sample_models/lwm2m/3310.xml").getInputStream())),
				UserContext.user("admin"));
		assertTrue(uploadResult.isValid());
	}

	private List<ModelInfo> importIPSO(String modelName, IUserContext user) throws Exception {
		UploadModelResult uploadResult = this.ipsoImporter.upload(
				FileUpload.create(modelName,
						IOUtils.toByteArray(new ClassPathResource("sample_models/lwm2m/" + modelName).getInputStream())),
				user);
		return this.ipsoImporter.doImport(uploadResult.getHandleId(), user);
	}

}
