package org.eclipse.vorto.repository.upgrade;

import static org.junit.Assert.assertEquals;

import java.util.List;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.backup.impl.DefaultModelBackupService;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.upgrade.impl.WorkflowUpgradeTask;
import org.eclipse.vorto.repository.utils.DummySecurityCredentials;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

@Ignore
public class WorkflowUpgradeTaskTest extends AbstractIntegrationTest {
	
	private DefaultModelBackupService repositoryManager = null;
	private WorkflowUpgradeTask task = null; 
	
	@Override
	public void beforeEach() throws Exception {
		super.beforeEach();
		repositoryManager = new DefaultModelBackupService() {
		  
		  @Override
          public Session getSession() {
              try {
                  return repository.login(new DummySecurityCredentials("admin", "ROLE_ADMIN"));
              } catch (RepositoryException e) {
                  throw new RuntimeException(e);
              }
          }   
		};
		repositoryManager.setModelRepository(this.modelRepository);
		
		repositoryManager.restore(IOUtils.toByteArray(new ClassPathResource("sample_models/backup1.xml").getInputStream()));
		
		task = new WorkflowUpgradeTask(this.modelRepository, new DefaultWorkflowService(modelRepository, accountService,null));
		task.setUpgradeTaskCondition(new IUpgradeTaskCondition() {
			
			@Override
			public boolean shouldExecuteTask() {
				return true;
			}
		});
	}
	
	@Test
	public void testUpgradeFull() {		
		List<ModelInfo> models = modelRepository.search("state:Draft");
		
		assertEquals(0, models.size());
		
		task.doUpgrade();
		
		models = modelRepository.search("state:Draft");
		
		assertEquals(4, models.size());
	}
	
	@Test
	public void testUpgradePartialModels() {		
		List<ModelInfo> models = modelRepository.search("*");
		
		ModelInfo model = models.get(0);
		model.setState("Released");
		this.modelRepository.updateMeta(model);
		
		task.doUpgrade();
				
		assertEquals(1, modelRepository.search("state:Released").size());
		assertEquals(model.getId(), modelRepository.search("state:Released").get(0).getId());
		assertEquals(3, modelRepository.search("state:Draft").size());
	}
	
}
