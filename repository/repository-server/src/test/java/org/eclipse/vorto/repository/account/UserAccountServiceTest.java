package org.eclipse.vorto.repository.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.junit.Before;
import org.junit.Test;

public class UserAccountServiceTest extends AbstractIntegrationTest  {

	private DefaultUserAccountService accountService;
	
	@Before
	public void setUp() {
		accountService = new DefaultUserAccountService();
		accountService.setModelRepository(modelRepository);
		accountService.setUserRepository(userRepository);
	}
	
	@Test
	public void testRemoveAccountWithModelsByUser() {
		IUserContext alex = UserContext.user("alex");
		IUserContext admin = UserContext.user("admin");
		
		importModel("Color.type", alex);
		importModel("Colorlight.fbmodel", alex);
		importModel("Switcher.fbmodel", admin);
		importModel("ColorLightIM.infomodel", admin);
		importModel("HueLightStrips.infomodel", admin);
		
		when(userRepository.findByUsername("alex")).thenReturn(User.create("alex"));

		assertEquals(2,this.modelRepository.search("author:" + alex.getHashedUsername()).size());
		accountService.delete("alex");		
		assertEquals(2,this.modelRepository.search("author:" + alex.getHashedUsername()).size());
	}
}
