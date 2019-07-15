/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.server.it.AbstractIntegrationTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class AccountControllerTest extends AbstractIntegrationTest {

    private IUserAccountService accountService;

    @Autowired private ApplicationContext context;

    @Override protected void setUpTest() throws Exception {
        accountService = context.getBean(IUserAccountService.class);
    }

    private String testUser = "testUser";
    private String testMail = "test@mail.de";

    @Test public void getUser() throws Exception {
        if(accountService.getUser(testUser) == null){
            accountService.create(testUser, "playground", Role.USER);
        }
        this.repositoryServer.perform(get("/rest/accounts/"+testUser).with(userAdmin))
            .andExpect(status().isOk());
        this.repositoryServer.perform(get("/rest/accounts/doesNotExist").with(userAdmin))
            .andExpect(status().isNotFound());
    }

    @Test public void createUserAccount() throws Exception {
        //when(accountService.create(testUser)).thenReturn(User.create(testUser));
        //ToDo fix payload
        //this.repositoryServer.perform(
        //    post("/rest/default/accounts").contentType(MediaType.APPLICATION_JSON)
        //        .content("{\"username\": \"testUser\"}").with(userAdmin))
        //    .andExpect(status().isCreated());
    }

    @Test public void upgradeUserAccount() throws Exception{
        if(accountService.getUser(testUser) == null){
            accountService.create(testUser, "playground", Role.USER);
        }
        this.repositoryServer.perform(post("/rest/accounts/testUser/updateTask").with(userAdmin))
            .andExpect(status().isCreated());
        this.repositoryServer.perform(post("/rest/accounts/doesnotexist/updateTask").with(userAdmin))
            .andExpect(status().isNotFound());
    }

    @Test public void updateAccount() throws Exception{
        if(accountService.getUser(testUser) == null){
            accountService.create(testUser, "playground", Role.USER);
        }
        this.repositoryServer.perform(
            put("/rest/accounts/"+testUser)
                .content(testMail).with(userAdmin))
            .andExpect(status().isOk());
        assert(this.accountService.getUser(testUser).getEmailAddress().equals(testMail));
        this.repositoryServer.perform(
            put("/rest/accounts/doesnotexist")
                .content(testMail).with(userAdmin))
            .andExpect(status().isNotFound());
    }

    @Test public void deleteUserAccount() throws Exception {
        if(accountService.getUser(testUser) == null){
            accountService.create(testUser, "playground", Role.USER);
        }
        
        this.repositoryServer.perform(get("/rest/accounts/"+testUser).with(userAdmin))
          .andExpect(status().isOk());
        
        this.repositoryServer.perform(
            delete("/rest/accounts/"+testUser)
                .content(testMail).with(userAdmin))
            .andExpect(status().isNoContent());
        
        this.repositoryServer.perform(get("/rest/accounts/"+testUser).with(userAdmin))
          .andExpect(status().isNotFound());
    }

    @Test public void deleteUserFromTenant() throws Exception {
      if(accountService.getUser(testUser) == null){
          accountService.create(testUser, "playground", Role.USER);
      }
      
      this.repositoryServer.perform(get("/rest/tenants/playground/users/"+testUser).with(userAdmin))
      .andExpect(status().isOk());
      
      this.repositoryServer.perform(
          delete("/rest/tenants/playground/users/"+testUser)
              .content(testMail).with(userAdmin))
          .andExpect(status().isOk());
      
      this.repositoryServer.perform(get("/rest/tenants/playground/users/"+testUser).with(userAdmin))
        .andExpect(status().isNotFound());
    }
}
