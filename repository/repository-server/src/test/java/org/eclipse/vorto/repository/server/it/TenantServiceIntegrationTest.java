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
package org.eclipse.vorto.repository.server.it;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import org.eclipse.vorto.repository.web.tenant.dto.CreateTenantRequest;
import org.junit.Test;
import com.google.common.collect.Sets;
import com.google.gson.Gson;

public class TenantServiceIntegrationTest extends AbstractIntegrationTest {

  @Override
  protected void setUpTest() throws Exception {

  }

  @Test
  public void testCreateTenant() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant").content(createTenantRequest)
        .contentType("application/json").with(userCreator)).andExpect(status().isOk());

    repositoryServer.perform(get("/rest/tenants/myTenant").with(userCreator)).andDo(result -> {
      String reply = result.getResponse().getContentAsString();
      System.out.println(reply);
      assertTrue(reply.contains("\"tenantId\":\"myTenant\""));
    }).andExpect(status().isOk());
  }
  
  @Test
  public void testUpdateTenant() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant1").content(tenantRequest("vorto.private.tre", "user2"))
        .contentType("application/json").with(userStandard)).andExpect(status().isOk());
    
    repositoryServer.perform(get("/rest/tenants/myTenant1").with(userAdmin)).andExpect(status().isOk());
    
    repositoryServer.perform(put("/rest/tenants/myTenant1").content(tenantRequest("vorto.private.tre", "user2", "user1"))
        .contentType("application/json").with(userStandard)).andExpect(status().isOk());
    
    repositoryServer.perform(put("/rest/tenants/myTenant1").content(tenantRequest("vorto.private.tre", "user3"))
        .contentType("application/json").with(userCreator)).andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateTenantWithBadRequest() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant").content(createTenantRequest)
        .contentType("application/json").with(userCreator)).andExpect(status().isOk());
    
    for(int i=0; i < badTenantRequests.size(); i++) {
      String badTenantRequest = badTenantRequests.get(i);
      repositoryServer.perform(put("/rest/tenants/tenantWithBadRequest" + i).content(badTenantRequest)
          .contentType("application/json").with(userCreator)).andDo(result -> {
            System.out.println("Request: " + badTenantRequest + " Reply:" + result.getResponse().getStatus());
          }).andExpect(status().isBadRequest());
    }
  }

  @Test
  public void testCreateTenantWithNamespaceConflict() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant").content(createTenantRequest)
        .contentType("application/json").with(userCreator)).andExpect(status().isOk());
    
    for(int i=0; i < conflictingTenantRequests.size(); i++) {
      String conflictingTenantRequest = conflictingTenantRequests.get(i);
      repositoryServer.perform(put("/rest/tenants/tenantWithConflict" + i).content(conflictingTenantRequest)
          .contentType("application/json").with(userCreator)).andDo(result -> {
            System.out.println("Request: " + conflictingTenantRequest + " Reply:" + result.getResponse().getStatus());
          }).andExpect(status().isConflict());
    }
    
    for(int i=0; i < nonConflictingTenantRequests.size(); i++) {
      String nonConflictingTenantRequest = nonConflictingTenantRequests.get(i);
      repositoryServer.perform(put("/rest/tenants/tenantWithNoConflict" + i).content(nonConflictingTenantRequest)
          .contentType("application/json").with(userCreator)).andDo(result -> {
            System.out.println("Request: " + nonConflictingTenantRequest + " Reply:" + result.getResponse().getStatus());
          }).andExpect(status().isOk());
    }
  }
  
  @Test
  public void testAdminCanRequestNonPrivateNamespace() throws Exception {
    String officialNs = new Gson().toJson(newTenantRequest("com.myofficial.namespace"));
    repositoryServer.perform(put("/rest/tenants/adminTenant").content(officialNs)
        .contentType("application/json").with(userAdmin)).andDo(result -> {
          System.out.println("Request: " + officialNs + " Reply:" + result.getResponse().getStatus());
        }).andExpect(status().isOk());
  }

  @Test
  public void testTenantNotFound() throws Exception {
    repositoryServer.perform(get("/rest/tenants/myTenant2").with(userCreator))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteTenant() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant").content(createTenantRequest)
        .contentType("application/json").with(userCreator)).andExpect(status().isOk());

    repositoryServer.perform(get("/rest/tenants/myTenant").with(userCreator))
        .andExpect(status().isOk());

    repositoryServer.perform(delete("/rest/tenants/myTenant").with(userCreator))
        .andExpect(status().isOk());

    repositoryServer.perform(get("/rest/tenants/myTenant").with(userCreator))
        .andExpect(status().isNotFound());
  }

  private String createTenantRequest = tenantRequest("vorto.private.abc.xyz");

  private List<String> badTenantRequests = 
      Arrays.asList(new Gson().toJson(newTenantRequest("com2")),
          new Gson().toJson(newTenantRequest("vorto.private")),
          new Gson().toJson(newTenantRequest("vorto")),
          new Gson().toJson(newTenantRequest("vorto.private.")));
  
  private List<String> nonConflictingTenantRequests =
      Arrays.asList(new Gson().toJson(newTenantRequest("vorto.private.xyz.ecm")),
          new Gson().toJson(newTenantRequest("vorto.private.abc1")),
          new Gson().toJson(newTenantRequest("vorto.private.abc.xyz1")));

  private List<String> conflictingTenantRequests =
      Arrays.asList(new Gson().toJson(newTenantRequest("vorto.private.abc")),
          new Gson().toJson(newTenantRequest("vorto.private.abc.xyz")),
          new Gson().toJson(newTenantRequest("vorto.private.abc.xyz.abc")),
          new Gson().toJson(newTenantRequest("vorto.private.abc.xyz.abc.ecm")));

  private String tenantRequest(String ns) {
    return new Gson().toJson(newTenantRequest(ns));
  }
  
  private String tenantRequest(String ns, String ... users) {
    return new Gson().toJson(newTenantRequest(ns, users));
  }
  
  private CreateTenantRequest newTenantRequest(String ns) {
    return newTenantRequest(ns, "user3");
  }
  
  private CreateTenantRequest newTenantRequest(String ns, String ... users) {
    CreateTenantRequest createRequest = new CreateTenantRequest();
    createRequest.setAuthenticationProvider("GITHUB");
    createRequest.setAuthorizationProvider("DB");
    createRequest.setDefaultNamespace(ns);
    createRequest.setNamespaces(Sets.newHashSet(ns));
    createRequest.setTenantAdmins(Sets.newHashSet(users));

    return createRequest;
  }
}
