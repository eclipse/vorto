/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.tenant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.AuthenticationProvider;
import org.eclipse.vorto.repository.domain.AuthorizationProvider;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.tenant.repository.INamespaceRepository;
import org.eclipse.vorto.repository.tenant.repository.ITenantRepository;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import wiremock.com.google.common.collect.Sets;

public class TenantServiceTest {

  private ITenantRepository tenantRepo = Mockito.mock(ITenantRepository.class);
  private INamespaceRepository nsRepo = Mockito.mock(INamespaceRepository.class);
  private IUserAccountService accountService = Mockito.mock(IUserAccountService.class);

  @Test
  public void testPreconditions() {
    Mockito.when(accountService.exists("admin")).thenReturn(false);
    TenantService tenantService = getTenantService();

    try {
      tenantService.createOrUpdateTenant(null, "", Sets.newHashSet("admin"), Optional.empty(),
          Optional.empty(), Optional.empty(), UserContext.user("erle", "playground"));
      fail("Passing null as tenantId should throw an exception");
    } catch (IllegalArgumentException e) {
      // VALID : this is correct
    }

    try {
      tenantService.createOrUpdateTenant("", "", Sets.newHashSet("admin"), Optional.empty(),
          Optional.empty(), Optional.empty(), UserContext.user("erle", "playground"));
      fail("Passing blank as tenantId should throw an exception");
    } catch (IllegalArgumentException e) {
      // VALID : this is correct
    }

    try {
      tenantService.createOrUpdateTenant("    ", "", Sets.newHashSet("admin"), Optional.empty(),
          Optional.empty(), Optional.empty(), UserContext.user("erle", "playground"));
      fail("Passing whitespace as tenantId should throw an exception");
    } catch (IllegalArgumentException e) {
      // VALID : this is correct
    }

    try {
      tenantService.createOrUpdateTenant("myTenantId", null, Sets.newHashSet("admin"),
          Optional.empty(), Optional.empty(), Optional.empty(),
          UserContext.user("erle", "playground"));
      fail("Passing null as default namespace should throw an exception");
    } catch (IllegalArgumentException e) {
      // VALID : this is correct
    }

    try {
      tenantService.createOrUpdateTenant("myTenantId", "", Sets.newHashSet("admin"),
          Optional.empty(), Optional.empty(), Optional.empty(),
          UserContext.user("erle", "playground"));
      fail("Passing a blank default namespace should throw an exception");
    } catch (IllegalArgumentException e) {
      // VALID : this is correct
    }

    try {
      tenantService.createOrUpdateTenant("myTenantId", "    ", Sets.newHashSet("admin"),
          Optional.empty(), Optional.empty(), Optional.empty(),
          UserContext.user("erle", "playground"));
      fail("Passing a whitespace default namespace should throw an exception");
    } catch (IllegalArgumentException e) {
      // VALID : this is correct
    }

    try {
      tenantService.createOrUpdateTenant("myTenantId", "com.test", Sets.newHashSet("admin"),
          Optional.empty(), Optional.empty(), Optional.empty(),
          UserContext.user("erle", "playground"));
      fail("Passing a tenant admin that doesn't exist should throw an exception");
    } catch (TenantAdminDoesntExistException e) {
      // VALID : this is correct
    }
  }

  @Test
  public void testNewTenantOneNamespaceWithConflict() {
    Mockito.when(tenantRepo.findByTenantId("myTenantId")).thenReturn(null);
    Mockito.when(nsRepo.findAll())
        .thenReturn(Lists.newArrayList(Namespace.newNamespace("vorto.private.test")));
    Mockito.when(accountService.exists("admin")).thenReturn(true);
    Mockito.when(accountService.getUser("admin")).thenReturn(User.create("admin"));

    TenantService tenantService = getTenantService();

    try {
      tenantService.createOrUpdateTenant("myTenantId", "vorto.private.test", Sets.newHashSet("admin"),
          Optional.empty(), Optional.empty(), Optional.empty(), UserContext.user("admin", null));
      fail("Passing a namespace that already exist should throw an exception");
    } catch (NamespaceExistException e) {
      assertEquals(e.getMessage(), "Namespace 'vorto.private.test' already exist.");
    }
  }

  @Test
  public void testNewTenantOneNamespaceAllDefault() {
    Mockito.when(tenantRepo.findByTenantId("myTenantId")).thenReturn(null);
    Mockito.when(nsRepo.findAll())
        .thenReturn(Lists.newArrayList(Namespace.newNamespace("vorto.private.test3")));
    Mockito.when(accountService.exists("admin")).thenReturn(true);
    Mockito.when(accountService.getUser("admin")).thenReturn(User.create("admin"));

    TenantService tenantService = getTenantService();

    tenantService.setApplicationEventPublisher(Mockito.mock(ApplicationEventPublisher.class));

    tenantService.createOrUpdateTenant("myTenantId", "vorto.private.test", Sets.newHashSet("admin"),
        Optional.empty(), Optional.empty(), Optional.empty(), UserContext.user("admin", null));

    ArgumentCaptor<Tenant> argCaptor = ArgumentCaptor.forClass(Tenant.class);
    Mockito.verify(tenantRepo).save(argCaptor.capture());

    assertEquals(argCaptor.getValue().getTenantId(), "myTenantId");
    assertEquals(argCaptor.getValue().getDefaultNamespace(), "vorto.private.test");
    assertEquals(argCaptor.getValue().getAuthenticationProvider(), AuthenticationProvider.GITHUB);
    assertEquals(argCaptor.getValue().getAuthorizationProvider(), AuthorizationProvider.DB);
    assertEquals(argCaptor.getValue().getNamespaces().size(), 1);
    assertEquals(argCaptor.getValue().getNamespaces().toArray(new Namespace[1])[0].getName(),
        "vorto.private.test");
  }

  @Test
  public void testNewTenantTwoIdenticalNamespaceAllDefault() {
    Mockito.when(tenantRepo.findByTenantId("myTenantId")).thenReturn(null);
    Mockito.when(nsRepo.findAll())
        .thenReturn(Lists.newArrayList(Namespace.newNamespace("com.test3")));
    Mockito.when(accountService.exists("admin")).thenReturn(true);
    Mockito.when(accountService.getUser("admin")).thenReturn(User.create("admin"));

    TenantService tenantService = getTenantService();

    tenantService.setApplicationEventPublisher(Mockito.mock(ApplicationEventPublisher.class));

    tenantService.createOrUpdateTenant("myTenantId", "vorto.private.test", Sets.newHashSet("admin"),
        Optional.of(Sets.newHashSet("vorto.private.test")), Optional.empty(), Optional.empty(),
        UserContext.user("admin", null));

    ArgumentCaptor<Tenant> argCaptor2 = ArgumentCaptor.forClass(Tenant.class);
    Mockito.verify(tenantRepo).save(argCaptor2.capture());

    assertEquals(argCaptor2.getValue().getTenantId(), "myTenantId");
    assertEquals(argCaptor2.getValue().getDefaultNamespace(), "vorto.private.test");
    assertEquals(argCaptor2.getValue().getAuthenticationProvider(), AuthenticationProvider.GITHUB);
    assertEquals(argCaptor2.getValue().getAuthorizationProvider(), AuthorizationProvider.DB);
    assertEquals(argCaptor2.getValue().getNamespaces().size(), 1);
    assertTrue(argCaptor2.getValue().getNamespaces().stream()
        .anyMatch(ns -> ns.getName().equals("vorto.private.test")));
  }

  @Test
  public void testNewTenantMultipleNamespaceAllDefault() {
    Mockito.when(tenantRepo.findByTenantId("myTenantId")).thenReturn(null);
    Mockito.when(nsRepo.findAll())
        .thenReturn(Lists.newArrayList(Namespace.newNamespace("vorto.private.test3")));
    Mockito.when(accountService.exists("admin")).thenReturn(true);
    Mockito.when(accountService.getUser("admin")).thenReturn(User.create("admin"));

    TenantService tenantService = getTenantService();

    tenantService.setApplicationEventPublisher(Mockito.mock(ApplicationEventPublisher.class));

    tenantService.createOrUpdateTenant("myTenantId", "vorto.private.test", Sets.newHashSet("admin"),
        Optional.of(Sets.newHashSet("vorto.private.test1", "vorto.private.test2")), Optional.empty(), Optional.empty(),
        UserContext.user("admin", null));

    ArgumentCaptor<Tenant> argCaptor2 = ArgumentCaptor.forClass(Tenant.class);
    Mockito.verify(tenantRepo).save(argCaptor2.capture());

    assertEquals(argCaptor2.getValue().getTenantId(), "myTenantId");
    assertEquals(argCaptor2.getValue().getDefaultNamespace(), "vorto.private.test");
    assertEquals(argCaptor2.getValue().getAuthenticationProvider(), AuthenticationProvider.GITHUB);
    assertEquals(argCaptor2.getValue().getAuthorizationProvider(), AuthorizationProvider.DB);
    assertEquals(argCaptor2.getValue().getNamespaces().size(), 3);
    assertTrue(argCaptor2.getValue().getNamespaces().stream()
        .anyMatch(ns -> ns.getName().equals("vorto.private.test")));
    assertTrue(argCaptor2.getValue().getNamespaces().stream()
        .anyMatch(ns -> ns.getName().equals("vorto.private.test1")));
    assertTrue(argCaptor2.getValue().getNamespaces().stream()
        .anyMatch(ns -> ns.getName().equals("vorto.private.test2")));
  }

  @Test
  public void testUpdateTenantWithSupersetProblem() {
    Tenant myTenantId =
        Tenant.newTenant("myTenantId", "com.test", Sets.newHashSet("com.test1", "com.test2"));
    Mockito.when(tenantRepo.findByTenantId("myTenantId")).thenReturn(myTenantId);
    Mockito.when(nsRepo.findByName("com.test")).thenReturn(null);
    Mockito.when(accountService.exists("admin")).thenReturn(true);
    Mockito.when(accountService.getUser("admin"))
        .thenReturn(User.create("admin", myTenantId, Role.SYS_ADMIN));

    TenantService tenantService = getTenantService();

    try {
      tenantService.createOrUpdateTenant("myTenantId", "com.test", Sets.newHashSet("admin"),
          Optional.of(Sets.newHashSet("com.test1")), Optional.empty(), Optional.empty(),
          createUserContext("admin", myTenantId.getTenantId(), Role.rolePrefix + Role.SYS_ADMIN));
      fail(
          "Should throw an exception because passed namespace is not superset of current namespace");
    } catch (NewNamespacesNotSupersetException e) {
      // VALID: correct behavior!
    }
  }

  private IUserContext createUserContext(String username, String tenantId, String... roles) {
    Authentication auth = new TestingAuthenticationToken(username, username, roles);
    return UserContext.user(auth, tenantId);
  }

  @Test
  public void testUpdateTenantWithConflictProblem() {
    Tenant myTenantId =
        Tenant.newTenant("myTenantId", "com.test", Sets.newHashSet("com.test", "com.test1"));
    Mockito.when(tenantRepo.findByTenantId("myTenantId")).thenReturn(myTenantId);
    Mockito.when(nsRepo.findAll())
        .thenReturn(Lists.newArrayList(Namespace.newNamespace("com.test3")));
    Mockito.when(accountService.exists("admin")).thenReturn(true);
    Mockito.when(accountService.getUser("admin")).thenReturn(User.create("admin"));

    TenantService tenantService = getTenantService();

    try {
      tenantService.createOrUpdateTenant("myTenantId", "com.test", Sets.newHashSet("admin"),
          Optional.of(Sets.newHashSet("com.test", "com.test1", "com.test3")), Optional.empty(),
          Optional.empty(),
          createUserContext("admin", myTenantId.getTenantId(), Role.rolePrefix + Role.SYS_ADMIN));
      fail(
          "Should throw an exception because passed namespace is conflicting with other namespaces");
    } catch (NamespaceExistException e) {
      // VALID: correct behavior!
    }
  }

  @Test
  public void testUpdateTenant() {
    Tenant myTenantId =
        Tenant.newTenant("myTenantId", "com.test", Sets.newHashSet("com.test", "com.test1"));
    myTenantId.setAuthenticationProvider(AuthenticationProvider.BOSCH_ID);
    myTenantId.setAuthorizationProvider(AuthorizationProvider.DB);
    Mockito.when(tenantRepo.findByTenantId("myTenantId")).thenReturn(myTenantId);
    Mockito.when(nsRepo.findAll())
        .thenReturn(Lists.newArrayList(Namespace.newNamespace("com.test3")));
    Mockito.when(accountService.exists("admin")).thenReturn(true);
    Mockito.when(accountService.getUser("admin")).thenReturn(User.create("admin"));
    Mockito.when(accountService.getUser("admin")).thenReturn(User.create("admin"));

    TenantService tenantService = getTenantService();

    tenantService.setApplicationEventPublisher(Mockito.mock(ApplicationEventPublisher.class));

    tenantService.createOrUpdateTenant("myTenantId", "com.test1", Sets.newHashSet("admin"),
        Optional.of(Sets.newHashSet("com.test", "com.test1", "com.test2")), Optional.empty(),
        Optional.empty(),
        createUserContext("admin", myTenantId.getTenantId(), Role.rolePrefix + Role.SYS_ADMIN));

    ArgumentCaptor<Tenant> argCaptor2 = ArgumentCaptor.forClass(Tenant.class);
    Mockito.verify(tenantRepo).save(argCaptor2.capture());

    assertEquals(argCaptor2.getValue().getTenantId(), "myTenantId");
    assertEquals(argCaptor2.getValue().getDefaultNamespace(), "com.test1");
    assertEquals(argCaptor2.getValue().getAuthenticationProvider(),
        AuthenticationProvider.BOSCH_ID);
    assertEquals(argCaptor2.getValue().getAuthorizationProvider(), AuthorizationProvider.DB);
    assertEquals(argCaptor2.getValue().getNamespaces().size(), 3);
    assertTrue(argCaptor2.getValue().getNamespaces().stream()
        .anyMatch(ns -> ns.getName().equals("com.test")));
    assertTrue(argCaptor2.getValue().getNamespaces().stream()
        .anyMatch(ns -> ns.getName().equals("com.test1")));
    assertTrue(argCaptor2.getValue().getNamespaces().stream()
        .anyMatch(ns -> ns.getName().equals("com.test2")));
  }

  @Test
  public void testNamespaceValidity() {
    assertFalse(valid("com", "com.bosch.inst"));
    assertFalse(valid("com.bosch", "com.bosch.inst"));
    assertFalse(valid("com.bosch.inst", "com.bosch.inst"));
    assertFalse(valid("com.bosch.inst.vorto", "com.bosch.inst"));
    assertFalse(valid("com.bosch.inst.vorto.vorto", "com.bosch.inst"));
    assertTrue(valid("com.bosch.bt", "com.bosch.inst"));
    assertTrue(valid("com.bosch.inst1", "com.bosch.inst"));
    assertTrue(valid("com.bosch2.inst", "com.bosch.inst"));
    assertTrue(valid("com.bosch1", "com.bosch.inst"));
    assertTrue(valid("com2", "com.bosch.inst"));
    assertFalse(valid("com", "com.bosch"));
    assertFalse(valid("com.bosch", "com.bosch"));
    assertFalse(valid("com.bosch.inst", "com.bosch"));
    assertTrue(valid("com.bosch2", "com.bosch"));
  }

  private boolean valid(String str, String namespace) {
    return !Namespace.newNamespace(namespace).isInConflictWith(str);
  }
  
  private TenantService getTenantService() {
    return new TenantService(tenantRepo, nsRepo, accountService);
  }
}
