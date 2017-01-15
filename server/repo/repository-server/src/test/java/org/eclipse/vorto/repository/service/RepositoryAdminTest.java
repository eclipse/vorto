package org.eclipse.vorto.repository.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.internal.service.DefaultRepositoryManager;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class RepositoryAdminTest extends AbstractIntegrationTest {

	private DefaultRepositoryManager repositoryManager = null;
	
	
	@Override
	public void beforeEach() throws Exception {
		super.beforeEach();
		repositoryManager = new DefaultRepositoryManager();
		repositoryManager.setModelRepository(this.modelRepository);
		repositoryManager.setSession(jcrSession());
	}
	
	@Test
	public void testBackupFilesNoImages() throws Exception {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");	
		byte[] backedUpContent = repositoryManager.backup();
		assertNotNull(backedUpContent);
	}
	
	@Test
	public void testBackupFilesWithImage() throws Exception {
		checkinModel("Color.type");
		checkinModel("Colorlight.fbmodel");
		checkinModel("Switcher.fbmodel");
		checkinModel("HueLightStrips.infomodel");
		this.modelRepository.addModelImage(new ModelId("HueLightStrips","com.mycompany","1.0.0"), IOUtils.toByteArray(new ClassPathResource("sample_models/sample.png").getInputStream()));
		byte[] backedUpContent = repositoryManager.backup();
		assertNotNull(backedUpContent);
		assertTrue(new String(backedUpContent).contains(".png"));
	}

	
	@Test
	public void testRestoreBackup1() throws Exception {
		this.repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		assertEquals(4,this.modelRepository.search("*").size());
	}
	
	@Test
	public void testRestoreBackupExistingData() throws Exception {
		this.repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		assertEquals(4,this.modelRepository.search("*").size());
		this.repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		assertEquals(4,this.modelRepository.search("*").size());
		System.out.println(this.modelRepository.search("*").get(0).getId());
	}
	
	
	@Test
	public void testDeleteUnUsedType() {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		this.repositoryManager.removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
		assertEquals(0, modelRepository.search("*").size());
	}
	
	@Test
	public void testDeleteAndCheckinSameModel() {
		checkinModel("Color.type");
		assertEquals(1, modelRepository.search("*").size());
		this.repositoryManager.removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
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
			this.repositoryManager.removeModel(ModelId.fromReference("org.eclipse.vorto.examples.type.Color", "1.0.0"));
			fail("Expected exception");
		} catch (ModelReferentialIntegrityException ex) {
			assertEquals(1, ex.getReferencedBy().size());
		}
	}
	
}
