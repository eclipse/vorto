package org.eclipse.vorto.repository.backup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.backup.impl.DefaultModelBackupService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
		this.repositoryManager
				.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		assertEquals(4, this.modelRepository.search("*").size());
	}

	@Test
	public void testRestoreBackupExistingData() throws Exception {
		this.repositoryManager
				.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		assertEquals(4, this.modelRepository.search("*").size());
		this.repositoryManager
				.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		assertEquals(4, this.modelRepository.search("*").size());
	}

	@Test
	public void testRestoreCorruptBackup() throws Exception {
		this.repositoryManager.restore(
				IOUtils.toByteArray(new ClassPathResource("sample_models/vortobackup_valid.xml").getInputStream()));
		assertEquals(5, this.modelRepository.search("*").size());

		try {
			this.repositoryManager.restore(IOUtils
					.toByteArray(new ClassPathResource("sample_models/vortobackup_corrupt.xml").getInputStream()));
		} catch (Exception e) {
			// TODO: handle exception
		}

		assertEquals(5, this.modelRepository.search("*").size());
		System.out.println(this.modelRepository.search("*").get(0).getId());
	}

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testExceptionWhenImportingCorruptBackup() throws Exception {
		thrown.expect(Exception.class);
		thrown.expectMessage(this.repositoryManager.EXCEPTION_MESSAGE_RESTORABLE_CONTENT);
		this.repositoryManager.restore(
				IOUtils.toByteArray(new ClassPathResource("sample_models/vortobackup_corrupt.xml").getInputStream()));
	}

}
