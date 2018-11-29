package org.eclipse.vorto.repository.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.junit.Before;
import org.junit.Test;

public class UserAccountServiceTest extends AbstractIntegrationTest {

  private IWorkflowService workflow = null;

  @Before
  public void setUp() {
    workflow =
        new DefaultWorkflowService(this.modelRepository, accountService, notificationService);

  }

  @Test
  public void testRemoveAccountWithModelsByUser() throws Exception {
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

  @Test
  public void testGetUserRoles() throws Exception {
    User user = setupUserWithRoles("alex");
    assertEquals(2, user.getRoles().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateUserAlreadyExists() throws Exception {
    User user = setupUserWithRoles("alex");
    when(userRepository.findByUsername("alex")).thenReturn(user);
    accountService.create(user.getUsername());
  }

  private User setupUserWithRoles(String username) {
    User user = new User();
    user.setUsername(username);
    user.addRoles(Role.ADMIN, Role.MODEL_CREATOR);
    return user;
  }
}
