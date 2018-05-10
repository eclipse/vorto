package org.eclipse.vorto.repository.upgrade;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.backup.impl.DefaultModelBackupService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.upgrade.impl.GdprUpgradeTask;
import org.eclipse.vorto.repository.upgrade.impl.GdprUpgradeTaskCondition;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class GdprUpgradeTaskTest extends AbstractIntegrationTest {
	
	private DefaultModelBackupService repositoryManager = null;
	private GdprUpgradeTask task = null; 
	
	@Override
	public void beforeEach() throws Exception {
		super.beforeEach();
		repositoryManager = new DefaultModelBackupService();
		repositoryManager.setModelRepository(this.modelRepository);
		repositoryManager.setSession(jcrSession());
		
		repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		
		task = new GdprUpgradeTask(this.modelRepository);
		task.setUpgradeTaskCondition(new GdprUpgradeTaskCondition() {
			public boolean shouldExecuteTask() {
				return true;
			}
		});
	}
	
	@Test
	public void testUpgradeTask() {
		IUserContext alex = UserContext.user("alex");
		
		List<ModelInfo> models = modelRepository.search(null);
		
		assertEquals(4, models.size());
		
		for(ModelInfo model : models) {
			assertEquals(alex.getUsername(), model.getAuthor());
		}
		
		// This should hashed the pre-existing authors
		task.doUpgrade();
		
		models = modelRepository.search(null);
		
		for(ModelInfo model : models) {
			assertEquals(alex.getHashedUsername(), model.getAuthor());
		}

		// This shouldn't have any effect since the authors are already hashed
		task.doUpgrade();
		
		for(ModelInfo model : models) {
			assertEquals(alex.getHashedUsername(), model.getAuthor());
		}
	}
	
}
