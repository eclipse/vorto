/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

import com.google.common.collect.Lists;
import org.eclipse.vorto.repository.UnitTestBase;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.tenant.TenantService;
import org.eclipse.vorto.repository.tenant.repository.ITenantRepository;
import org.eclipse.vorto.repository.tenant.repository.ITenantUserRepo;
import org.eclipse.vorto.repository.utils.TenantProvider;
import org.eclipse.vorto.repository.web.account.dto.TenantTechnicalUserDto;
import org.eclipse.vorto.repository.web.api.v1.dto.ICollaborator;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.impl.DefaultWorkflowService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UserAccountServiceTest extends UnitTestBase {

  @Mock
  private ITenantRepository tenantRepo;

  @Mock
  protected TenantService tenantService;

  private IWorkflowService workflow = null;

  protected Tenant playgroundTenant = TenantProvider.playgroundTenant();

  @Before
  @Override
  public void setup() throws Exception {
    super.setup();
    when(tenantService.getTenantFromNamespace(Matchers.anyString())).thenReturn(Optional.of(playgroundTenant));

    when(tenantService.getTenant("playground")).thenReturn(Optional.of(playgroundTenant));
    when(tenantService.getTenants()).thenReturn(Lists.newArrayList(playgroundTenant));
    when(tenantRepo.findByTenantId("playground")).thenReturn(playgroundTenant);
    when(tenantRepo.findAll()).thenReturn(Lists.newArrayList(playgroundTenant));

    accountService.setTenantUserRepo(Mockito.mock(ITenantUserRepo.class));
    accountService.setTenantRepo(tenantRepo);
    workflow = new DefaultWorkflowService(repositoryFactory, accountService, notificationService, namespaceService, userNamespaceRoleService, roleService);

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
    ICollaborator technicalUser = new TenantTechnicalUserDto();
    technicalUser.setAuthenticationProviderId("GITHUB");
    technicalUser.setSubject("subject");
    accountService.createTechnicalUserAndAddToTenant(
        "doesNotExist", "pepe",
        technicalUser,
        Role.USER
    );
  }

  /**
   * Tests that creating a technical user with no authentication provider fails.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testCreateTechnicalUserWithoutAuthenticationProvider() {
    ICollaborator technicalUser = new TenantTechnicalUserDto();
    technicalUser.setAuthenticationProviderId(null);
    technicalUser.setSubject("subject");
    // this implicitly creates the "playground" namespace
    User user = setupUserWithRoles("alex");
    accountService.createTechnicalUserAndAddToTenant(
        "playground", "pepe", technicalUser, Role.USER
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTechnicalUserWithoutSubject() {
    ICollaborator technicalUser = new TenantTechnicalUserDto();
    technicalUser.setAuthenticationProviderId("GITHUB");
    technicalUser.setSubject(null);
    User user = setupUserWithRoles("alex");
    accountService.createTechnicalUserAndAddToTenant(
        "playground", "pepe", technicalUser, Role.USER
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTechnicalUserWithSubjectTooShort() {
    ICollaborator technicalUser = new TenantTechnicalUserDto();
    technicalUser.setAuthenticationProviderId("GITHUB");
    technicalUser.setSubject("abc");
    User user = setupUserWithRoles("alex");
    accountService.createTechnicalUserAndAddToTenant(
        "playground", "pepe", technicalUser, Role.USER
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateTechnicalUserWithSubjectNotAlphanumeric() {
    ICollaborator technicalUser = new TenantTechnicalUserDto();
    technicalUser.setAuthenticationProviderId("GITHUB");
    technicalUser.setSubject("!§$%");
    User user = setupUserWithRoles("alex");
    accountService.createTechnicalUserAndAddToTenant(
        "playground", "pepe", technicalUser, Role.USER
    );
  }

  private User setupUserWithRoles(String username) {
    return User.create(username, "GITHUB", null, new Tenant("playground"), Role.SYS_ADMIN, Role.MODEL_CREATOR);
  }
}
