package org.eclipse.vorto.repository.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.junit.Before;
import org.junit.Test;

public class UserAccountServiceTest extends AbstractIntegrationTest  {

	private DefaultUserAccountService accountService;
	
	private IWorkflowService workflow = null;
	
	@Before
	public void setUp() {
		accountService = new DefaultUserAccountService();
		accountService.setModelRepository(modelRepository);
		accountService.setUserRepository(userRepository);
		workflow = new DefaultWorkflowService(this.modelRepository,userRepository);

	}
	
	@Test
	public void testRemoveAccountWithModelsByUser()  throws Exception {
		IUserContext alex = UserContext.user("alex");
		IUserContext admin = UserContext.user("admin");
		
		
		this.workflow.start(importModel("Color.type", alex).getId());
		this.workflow.start(importModel("Colorlight.fbmodel", alex).getId());
		importModel("Switcher.fbmodel", admin);
		importModel("ColorLightIM.infomodel", admin);
		importModel("HueLightStrips.infomodel", admin);
		
		when(userRepository.findByUsername("alex")).thenReturn(User.create("alex"));

		assertEquals(2, this.modelRepository.search("author:" + alex.getUsername()).size());
		accountService.delete("alex");		
		assertEquals(0, this.modelRepository.search("author:" + alex.getUsername()).size());
		assertEquals(2, this.modelRepository.search("author:anonymous").size());
	}
}
