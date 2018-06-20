package org.eclipse.vorto.repository.backup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.backup.impl.DefaultModelBackupService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class RepositoryAdminTest extends AbstractIntegrationTest {

	private DefaultModelBackupService repositoryManager = null;
	
	
	@Override
	public void beforeEach() throws Exception {
		super.beforeEach();
		repositoryManager = new DefaultModelBackupService();
		repositoryManager.setModelRepository(this.modelRepository);
		repositoryManager.setSession(jcrSession());
	}
	
	@Test
	public void testBackupFilesNoImages() throws Exception {
		importModel("Color.type");
		importModel("Colorlight.fbmodel");
		importModel("Switcher.fbmodel");
		importModel("HueLightStrips.infomodel");	
		byte[] backedUpContent = repositoryManager.backup();
		assertNotNull(backedUpContent);
	}
	
	@Test
	public void testRestoreBackup1() throws Exception {
		this.repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		assertEquals(4,this.modelRepository.search("*").size());
	}
	
	@Test
	@Ignore
	public void testRestoreBackupExistingData() throws Exception {
		this.repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		assertEquals(4,this.modelRepository.search("*").size());
		this.repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		assertEquals(4,this.modelRepository.search("*").size());
		System.out.println(this.modelRepository.search("*").get(0).getId());
	}
	
}
