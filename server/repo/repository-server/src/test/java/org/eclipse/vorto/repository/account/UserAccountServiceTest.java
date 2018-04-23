package org.eclipse.vorto.repository.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.account.impl.User;
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
		checkinModel("Color.type","alex");
		checkinModel("Colorlight.fbmodel","alex");
		checkinModel("Switcher.fbmodel","admin");
		checkinModel("ColorLightIM.infomodel","admin");
		checkinModel("HueLightStrips.infomodel","admin");
		
		when(userRepository.findByUsername("alex")).thenReturn(User.create("alex"));

		assertEquals(2,this.modelRepository.search("author:alex").size());
		accountService.delete("alex");		
		assertEquals(0,this.modelRepository.search("author:alex").size());
		assertEquals(2,this.modelRepository.search("author:anonymous").size());
	}
}
