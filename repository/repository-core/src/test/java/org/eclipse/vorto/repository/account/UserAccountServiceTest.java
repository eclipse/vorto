/**
 * Copyright (c) 2018, 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.vorto.repository.AbstractIntegrationTest;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.web.account.dto.TenantTechnicalUserDto;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.junit.Before;
import org.junit.Test;

public class UserAccountServiceTest extends AbstractIntegrationTest {

  private IWorkflowService workflow = null;

  @Before
  public void setUp() {
    workflow = new DefaultWorkflowService(repositoryFactory, accountService, notificationService);

  }

  @Test
  public void testRemoveAccountWithModelsByUser() throws Exception {
    IUserContext alex = createUserContext("alex");
    IUserContext admin = createUserContext("admin");

    this.workflow.start(importModel("Color.type", alex).getId(), alex);
    this.workflow.start(importModel("Colorlight.fbmodel", alex).getId(), alex);
    importModel("Switcher.fbmodel", admin);
    importModel("ColorLightIM.infomodel", admin);
    importModel("HueLightStrips.infomodel", admin);

    assertEquals(2, getModelRepository(alex).search("author:" + alex.getUsername()).size());
    accountService.delete("alex");
    assertEquals(0, getModelRepository(alex).search("author:" + alex.getUsername()).size());
    assertEquals(2, getModelRepository(alex).search("author:anonymous").size());
  }

  @Test
  public void testGetUserRoles() throws Exception {
    User user = setupUserWithRoles("alex");
    assertEquals(2, user.getRoles("playground").size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateUserAlreadyExists() throws Exception {
    User user = setupUserWithRoles("alex");
    when(userRepository.findByUsername("alex")).thenReturn(user);
    accountService.create(user.getUsername(), "GITHUB", null);
  }

  /**
   * Tests that creating a technical user and adding to a non-existing namespace fails.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateTechnicalUserAndAddToNonExistingNamespace() {
    accountService.createTechnicalUserAndAddToTenant(
        "doesNotExist", "pepe",
        new TenantTechnicalUserDto().setAuthenticationProviderId("GITHUB").setSubject("subject"),
        Role.USER
    );
  }

  /**
   * Tests that creating a technical user with no authentication provider fails.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateTechnicalUserWithoutAuthenticationProvider() {
    // this implicitly creates the "playground" namespace
    User user = setupUserWithRoles("alex");
    accountService.createTechnicalUserAndAddToTenant(
        "playground", "pepe", new TenantTechnicalUserDto().setSubject("subject").setAuthenticationProviderId(null), Role.USER
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTechnicalUserWithoutSubject() {
    User user = setupUserWithRoles("alex");
    accountService.createTechnicalUserAndAddToTenant(
        "playground", "pepe", new TenantTechnicalUserDto().setSubject(null).setAuthenticationProviderId("GITHUB"), Role.USER
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTechnicalUserWithSubjectTooShort() {
    User user = setupUserWithRoles("alex");
    accountService.createTechnicalUserAndAddToTenant(
        "playground", "pepe", new TenantTechnicalUserDto().setSubject("abc").setAuthenticationProviderId("GITHUB"), Role.USER
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTechnicalUserWithSubjectNotAlphanumeric() {
    User user = setupUserWithRoles("alex");
    accountService.createTechnicalUserAndAddToTenant(
        "playground", "pepe", new TenantTechnicalUserDto().setSubject("!§$%").setAuthenticationProviderId("GITHUB"), Role.USER
    );
  }

  private User setupUserWithRoles(String username) {
    return User.create(username, "GITHUB", null, new Tenant("playground"), Role.SYS_ADMIN, Role.MODEL_CREATOR);
  }
}
