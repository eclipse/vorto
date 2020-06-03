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
package org.eclipse.vorto.repository.web;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.server.it.IntegrationTestBase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AccountControllerTest extends IntegrationTestBase {


  private static final String GITHUB = "GITHUB";

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private ApplicationContext context;

//  @Override
//  protected void setUpTest() throws Exception {
//    accountService = context.getBean(IUserAccountService.class);
//  }

  private String testUser = "testUser";
  private String testUser2 = "testUser2";
  private String testMail = "test@mail.de";

  @Test
  public void getUser() throws Exception {
    if (accountService.getUser(testUser) == null) {
      accountService.createOrUpdate(testUser, GITHUB, null, "playground", Role.USER);
    }
    this.repositoryServer.perform(get("/rest/accounts/" + testUser).with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer.perform(get("/rest/accounts/doesNotExist").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void searchExistingUserStartingWith() throws Exception {
    if (accountService.getUser(testUser) == null) {
      accountService.createOrUpdate(testUser, GITHUB, null, "playground", Role.USER);
    }
    MvcResult result = this.repositoryServer.perform(get("/rest/accounts/search/test").with(userSysadmin))
        .andExpect(status().isOk())
        .andReturn();
    // quick, but dirty
    String expectedUsernameKV = "\"userId\":\"testUser\"";
    assertTrue(result.getResponse().getContentAsString().contains(expectedUsernameKV));
  }

  @Test
  public void searchExistingUserContaining() throws Exception {
    if (accountService.getUser(testUser) == null) {
      accountService.createOrUpdate(testUser, GITHUB, null, "playground", Role.USER);
    }
    MvcResult result = this.repositoryServer.perform(get("/rest/accounts/search/stuse").with(userSysadmin))
        .andExpect(status().isOk())
        .andReturn();
    // quick, but dirty
    String expectedUsernameKV = "\"userId\":\"testUser\"";
    String debug = result.getResponse().getContentAsString();
    assertTrue(result.getResponse().getContentAsString().contains(expectedUsernameKV));
  }

  @Test
  public void searchNonExistingUser() throws Exception {
    if (accountService.getUser(testUser) == null) {
      accountService.createOrUpdate(testUser, GITHUB, null, "playground", Role.USER);
    }
    MvcResult result = this.repositoryServer.perform(get("/rest/accounts/search/blah").with(userSysadmin))
        .andExpect(status().isOk())
        .andReturn();
    // quick, but dirty
    String expectedUsernameKV = "\"userId\":\"testUser\"";
    assertEquals(result.getResponse().getContentAsString(), "[]");
  }
  
  @Test
  public void upgradeUserAccount() throws Exception {
    if (accountService.getUser(testUser) == null) {
      accountService.createOrUpdate(testUser, GITHUB, null, "playground", Role.USER);
    }
    this.repositoryServer.perform(post("/rest/accounts/testUser/updateTask").with(userSysadmin))
        .andExpect(status().isCreated());
    this.repositoryServer.perform(post("/rest/accounts/doesnotexist/updateTask").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateAccount() throws Exception {
    if (accountService.getUser(testUser) == null) {
      accountService.createOrUpdate(testUser, GITHUB, null, "playground", Role.USER);
    }
    this.repositoryServer
        .perform(put("/rest/accounts/" + testUser).content(testMail).with(userSysadmin))
        .andExpect(status().isOk());
    assert (this.accountService.getUser(testUser).getEmailAddress().equals(testMail));
    this.repositoryServer
        .perform(put("/rest/accounts/doesnotexist").content(testMail).with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void deleteUserAccount() throws Exception {
    if (accountService.getUser(testUser) == null) {
      accountService.createOrUpdate(testUser, GITHUB, null, "playground", Role.USER);
    }

    this.repositoryServer.perform(get("/rest/accounts/" + testUser).with(userSysadmin))
        .andExpect(status().isOk());

    this.repositoryServer
        .perform(delete("/rest/accounts/" + testUser).content(testMail).with(userSysadmin))
        .andExpect(status().isNoContent());

    this.repositoryServer.perform(get("/rest/accounts/" + testUser).with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  /*
   * Test case to check if user account deletion is successful for Sys Admin role
   */
  @Test
  public void deleteUserAccountSysAdmin() throws Exception {
    if (accountService.getUser(testUser) == null) {
      accountService.createOrUpdate(testUser, GITHUB, null, "playground", Role.SYS_ADMIN);
    }

    this.repositoryServer.perform(get("/rest/accounts/" + testUser).with(userSysadmin))
        .andExpect(status().isOk());

    this.repositoryServer
        .perform(delete("/rest/accounts/" + testUser).content(testMail).with(userSysadmin))
        .andExpect(status().isNoContent());

    this.repositoryServer.perform(get("/rest/accounts/" + testUser).with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void deleteUserFromTenant() throws Exception {
    if (accountService.getUser(testUser) == null) {
      accountService.createOrUpdate(testUser, GITHUB, null, "playground", Role.USER);
    }

    this.repositoryServer.perform(get("/rest/tenants/playground/users/" + testUser).with(userSysadmin))
        .andExpect(status().isOk());

    this.repositoryServer
        .perform(
            delete("/rest/tenants/playground/users/" + testUser).content(testMail).with(userSysadmin))
        .andExpect(status().isOk());

    this.repositoryServer.perform(get("/rest/tenants/playground/users/" + testUser).with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  /*
   * Test case to check whether empty tenant list is returned if user does not exist in user
   * repository
   */
  @Test
  public void getTenantsOfUser() throws Exception {
    assert (this.accountService.getTenantsOfUser(testUser2).isEmpty());
  }


}
