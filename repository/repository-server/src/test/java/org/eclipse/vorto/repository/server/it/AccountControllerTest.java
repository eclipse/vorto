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
package org.eclipse.vorto.repository.server.it;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.services.RoleService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class AccountControllerTest extends IntegrationTestBase {


  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private RoleService roleService;

  private String testMail = "test@mail.de";

  @Test
  public void getUser() throws Exception {
    this.repositoryServer.perform(get("/rest/accounts/" + USER_MODEL_CREATOR_NAME).with(userSysadmin))
        .andExpect(status().isOk());
    this.repositoryServer.perform(get("/rest/accounts/doesNotExist").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void searchExistingUserStartingWith() throws Exception {
    MvcResult result = this.repositoryServer.perform(get("/rest/accounts/search/" + "user").with(userSysadmin))
        .andExpect(status().isOk())
        .andReturn();
    // quick, but dirty
    String expectedUsernameKV = "\"userId\":\"" + USER_MODEL_CREATOR_NAME + "\"";
    assertTrue(result.getResponse().getContentAsString().contains(expectedUsernameKV));
  }

  @Test
  public void searchExistingUserContaining() throws Exception {
    MvcResult result = this.repositoryServer.perform(get("/rest/accounts/search/rModel").with(userSysadmin))
        .andExpect(status().isOk())
        .andReturn();
    // quick, but dirty
    String expectedUsernameKV = "\"userId\":\"" + USER_MODEL_CREATOR_NAME + "\"";
    assertTrue(result.getResponse().getContentAsString().contains(expectedUsernameKV));
  }

  @Test
  public void searchNonExistingUser() throws Exception {
    MvcResult result = this.repositoryServer.perform(get("/rest/accounts/search/blah").with(userSysadmin))
        .andExpect(status().isOk())
        .andReturn();
    assertEquals("[]", result.getResponse().getContentAsString());
  }
  
  @Test
  public void upgradeUserAccount() throws Exception {
    this.repositoryServer.perform(post("/rest/accounts/" + USER_MODEL_CREATOR_NAME + "/updateTask").with(userSysadmin))
        .andExpect(status().isCreated());
    this.repositoryServer.perform(post("/rest/accounts/doesnotexist/updateTask").with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void updateAccount() throws Exception {
    this.repositoryServer
        .perform(put("/rest/accounts/" + USER_MODEL_CREATOR_NAME).content(testMail).with(userSysadmin))
        .andExpect(status().isOk());
    assert (this.accountService.getUser(USER_MODEL_CREATOR_NAME).getEmailAddress().equals(testMail));
    this.repositoryServer
        .perform(put("/rest/accounts/doesnotexist").content(testMail).with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  @Test
  public void deleteUserAccount() throws Exception {
    this.repositoryServer.perform(get("/rest/accounts/" + USER_MODEL_VIEWER_NAME).with(userSysadmin))
        .andExpect(status().isOk());

    this.repositoryServer
        .perform(delete("/rest/accounts/" + USER_MODEL_VIEWER_NAME).content(testMail).with(userSysadmin))
        .andExpect(status().isNoContent());

    this.repositoryServer.perform(get("/rest/accounts/" + USER_MODEL_VIEWER_NAME).with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  protected IRole modelViewerRole() {
    return roleService.findAnyByName("model_viewer")
        .orElseThrow(() -> new IllegalStateException("Model viewer role not found."));
  }

  /*
   * Test case to check if user account deletion is successful for Sys Admin role
   */
  @Test
  public void deleteUserAccountSysAdmin() throws Exception {
    this.repositoryServer.perform(get("/rest/accounts/" + USER_SYSADMIN_NAME_2).with(userSysadmin))
        .andExpect(status().isOk());

    this.repositoryServer
        .perform(delete("/rest/accounts/" + USER_SYSADMIN_NAME_2).content(testMail).with(userSysadmin))
        .andExpect(status().isNoContent());

    this.repositoryServer.perform(get("/rest/accounts/" + USER_SYSADMIN_NAME_2).with(userSysadmin))
        .andExpect(status().isNotFound());
  }

  /*
   * Test case to check whether empty tenant list is returned if user does not exist in user
   * repository
   */
  @Test
  public void getTenantsOfUser() throws Exception {
    assert (this.accountService.getTenantsOfUser("doesNotExist").isEmpty());
  }


}
