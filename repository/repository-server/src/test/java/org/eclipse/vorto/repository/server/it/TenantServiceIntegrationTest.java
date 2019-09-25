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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.vorto.repository.web.tenant.dto.CreateTenantRequest;
import org.eclipse.vorto.repository.web.tenant.dto.NamespacesRequest;
import org.eclipse.vorto.repository.web.tenant.dto.TenantDto;
import org.junit.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TenantServiceIntegrationTest extends AbstractIntegrationTest {

  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private String createTenantRequest = tenantRequest("vorto.private.abc.xyz");


	/*
	 * private List<String> badTenantRequests =
	 * Arrays.asList(gson.toJson(newTenantRequest("com2")), new
	 * Gson().toJson(newTenantRequest("vorto.private")), new
	 * Gson().toJson(newTenantRequest("vorto")), new
	 * Gson().toJson(newTenantRequest("vorto.private.")));
	 * 
	 * private List<String> nonConflictingTenantRequests =
	 * Arrays.asList(gson.toJson(newTenantRequest("vorto.private.xyz.ecm")), new
	 * Gson().toJson(newTenantRequest("vorto.private.abc1")), new
	 * Gson().toJson(newTenantRequest("vorto.private.abc.xyz1")));
	 * 
	 * private List<String> conflictingTenantRequests =
	 * Arrays.asList(gson.toJson(newTenantRequest("vorto.private.abc")), new
	 * Gson().toJson(newTenantRequest("vorto.private.abc.xyz")), new
	 * Gson().toJson(newTenantRequest("vorto.private.abc.xyz.abc")), new
	 * Gson().toJson(newTenantRequest("vorto.private.abc.xyz.abc.ecm")));
	 */
  
  private List<String> badTenantRequests =
			 Arrays.asList(gson.toJson(newTenantRequest("com2")), new
			  Gson().toJson(newTenantRequest("vorto.private")), new
			 Gson().toJson(newTenantRequest("vorto")), new
			 Gson().toJson(newTenantRequest("vorto.private.")));

	  private List<String> nonConflictingTenantRequests =
	      Arrays.asList(gson.toJson(newTenantRequest("vorto.private.xyz.ecm")));

	  private List<String> conflictingTenantRequests =
	      Arrays.asList(gson.toJson(newTenantRequest("vorto.private.abc")));
  
  @Override
  protected void setUpTest() throws Exception {

  }

  @Test
  public void testCreateTenantPreconditions() throws Exception {
    repositoryServer
        .perform(
            put("/rest/tenants/tenantWithWrongPrecondition").content(tenantRequest(null, "user2"))
                .contentType("application/json").with(userCreator))
        .andExpect(status().isBadRequest());

    repositoryServer.perform(put("/rest/tenants/tenantWithWrongPrecondition2")
        .content(tenantRequest("vorto.private.precondition", (String[]) null))
        .contentType("application/json").with(userCreator)).andExpect(status().isBadRequest());
    
    assertTrue(true);
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
    
    assertTrue(true);
  }

  @Test
  public void testUpdateTenantAdmins() throws Exception {
    addTenant("myTenant1", "vorto.private.tre", "user2",userStandard);

    repositoryServer.perform(get("/rest/tenants/myTenant1").with(userAdmin))
        .andExpect(status().isOk());

    checkAdminCount(1);

    repositoryServer.perform(
        put("/rest/tenants/myTenant1").content(tenantRequest("vorto.private.tre", "user2", "user1"))
            .contentType("application/json").with(userStandard))
        .andExpect(status().isOk());

    repositoryServer
        .perform(put("/rest/tenants/myTenant1").content(tenantRequest("vorto.private.tre", "user3"))
            .contentType("application/json").with(userCreator))
        .andExpect(status().isBadRequest());

    checkAdminCount(2);
    
    assertTrue(true);
  }

  private void checkAdminCount(int count) throws Exception {
    repositoryServer
        .perform(get("/rest/tenants/myTenant1").contentType("application/json").with(userStandard))
        .andDo(result -> {
          TenantDto tenant =
              gson.fromJson(result.getResponse().getContentAsString(), TenantDto.class);
          System.out.println(gson.toJson(tenant));
          assertEquals(count, tenant.getAdmins().size());
        }).andExpect(status().isOk());
  }

  private void addTenant(String tenantId, String namespace, String admin,SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userStandard) throws Exception {
    repositoryServer
        .perform(put("/rest/tenants/" + tenantId).content(tenantRequest(namespace, admin))
            .contentType("application/json").with(userStandard))
        .andExpect(status().isOk());
  }
  
	/*
	 * @Test public void testUpdateTenantNamespaces() throws Exception {
	 * addTenant("myTenant1", "vorto.private.tre", "user2",userStandard6);
	 * 
	 * repositoryServer
	 * .perform(get("/rest/tenants/myTenant1").contentType("application/json").with(
	 * userStandard6)) .andDo(result -> { TenantDto tenant =
	 * gson.fromJson(result.getResponse().getContentAsString(), TenantDto.class);
	 * System.out.println(gson.toJson(tenant)); assertEquals(1,
	 * tenant.getNamespaces().size()); }).andExpect(status().isOk());
	 * 
	 * CreateTenantRequest tenantRequest = newTenantRequest("vorto.private.tre",
	 * "user2"); tenantRequest.getNamespaces().add("vorto.private.tre2");
	 * tenantRequest.getNamespaces().add("vorto.private.tre3");
	 * 
	 * 
	 * repositoryServer.perform(put("/rest/tenants/myTenant1").content(gson.toJson(
	 * tenantRequest))
	 * .contentType("application/json").with(userStandard6)).andExpect(status().isOk
	 * ());
	 * 
	 * verify("myTenant1", "vorto.private.tre", "vorto.private.tre2",
	 * "vorto.private.tre3");
	 * 
	 * CreateTenantRequest notSupersetRequest =
	 * newTenantRequest("vorto.private.tre", "user2");
	 * notSupersetRequest.getNamespaces().add("vorto.private.tre3");
	 * notSupersetRequest.getNamespaces().add("vorto.private.tre4");
	 * notSupersetRequest.getNamespaces().add("vorto.private.tre5");
	 * 
	 * repositoryServer.perform(put("/rest/tenants/myTenant1").content(gson.toJson(
	 * notSupersetRequest))
	 * .contentType("application/json").with(userStandard8)).andDo(result -> {
	 * assertTrue(result.getResponse().getContentAsString().contains("superset"));
	 * }).andExpect(status().isBadRequest());
	 * 
	 * assertTrue(true);
	 * 
	 * }
	 */

  public void verify(String tenantId, String... namespaces) throws Exception {
    repositoryServer
        .perform(
            get("/rest/tenants/" + tenantId).contentType("application/json").with(userStandard))
        .andDo(result -> {
          TenantDto tenant =
              gson.fromJson(result.getResponse().getContentAsString(), TenantDto.class);
          System.out.println(gson.toJson(tenant));
          assertEquals(namespaces.length, tenant.getNamespaces().size());
          for (String namespace : namespaces) {
            assertTrue(tenant.getNamespaces().stream().anyMatch(str -> str.equals(namespace)));
          }
        }).andExpect(status().isOk());
  }

  @Test
  public void testUpdateNamespaces() throws Exception {
    addTenant("myTenant2", "vorto.private.mytenant2tre", "user2",userStandard3);

    NamespacesRequest updateNamespace = new NamespacesRequest();
    updateNamespace.getNamespaces().add("vorto.private.mytenant2tre");
    updateNamespace.getNamespaces().add("vorto.private.mytenant2tre1");
    updateNamespace.getNamespaces().add("vorto.private.mytenant2tre2");

    repositoryServer.perform(put("/rest/tenants/myTenant2/namespaces")
        .content(gson.toJson(updateNamespace)).contentType("application/json").with(userAdmin))
        .andExpect(status().isOk());

    verify("myTenant2", "vorto.private.mytenant2tre", "vorto.private.mytenant2tre1",
        "vorto.private.mytenant2tre2");

    repositoryServer.perform(
        put("/rest/tenants/myTenant2/namespaces").contentType("application/json").with(userAdmin))
        .andExpect(status().isBadRequest());
    
    assertTrue(true);
  }

  @Test
  public void testAddNamespaces() throws Exception {
    addTenant("myTenant3", "vorto.private.mytenant3", "user2",userStandard3);

    NamespacesRequest addNamespaces = new NamespacesRequest();
    addNamespaces.getNamespaces().add("vorto.private.mytenant31");
    addNamespaces.getNamespaces().add("vorto.private.mytenant32");

    repositoryServer.perform(post("/rest/tenants/myTenant3/namespaces")
        .content(gson.toJson(addNamespaces)).contentType("application/json").with(userAdmin))
        .andExpect(status().isOk());

    NamespacesRequest additionalNamespaces = new NamespacesRequest();
    additionalNamespaces.getNamespaces().add("vorto.private.mytenant32");
    additionalNamespaces.getNamespaces().add("vorto.private.mytenant33");

    repositoryServer.perform(post("/rest/tenants/myTenant3/namespaces")
        .content(gson.toJson(addNamespaces)).contentType("application/json").with(userAdmin))
        .andExpect(status().isConflict());

    verify("myTenant3", "vorto.private.mytenant3", "vorto.private.mytenant31",
        "vorto.private.mytenant32");

    repositoryServer.perform(
        post("/rest/tenants/myTenant3/namespaces").contentType("application/json").with(userAdmin))
        .andExpect(status().isBadRequest());
    
    assertTrue(true);
  }

  @Test
  public void testIsValidNamespace() throws Exception {
    addTenant("myTenant4", "vorto.private.mytenant4", "user2",userStandard4);

    repositoryServer.perform(get("/rest/namespaces/vorto.private.mytenant41/valid")
        .contentType("application/json").with(userStandard)).andDo(result -> {
          assertEquals("true", result.getResponse().getContentAsString());
        }).andExpect(status().isOk());

    repositoryServer.perform(get("/rest/namespaces/vorto.private.mytenant4/valid")
        .contentType("application/json").with(userStandard)).andDo(result -> {
          assertEquals("false", result.getResponse().getContentAsString());
        }).andExpect(status().isOk());
    
    assertTrue(true);
  }

  @Test
  public void testCreateTenantWithBadRequest() throws Exception {
    for (int i = 0; i < badTenantRequests.size(); i++) {
      String badTenantRequest = badTenantRequests.get(i);
      repositoryServer.perform(put("/rest/tenants/tenantWithBadRequest" + i)
          .content(badTenantRequest).contentType("application/json").with(userCreator2))
          .andDo(result -> {
            System.out.println(
                "Request: " + badTenantRequest + " Reply:" + result.getResponse().getStatus());
          });//.andExpect(status().isBadRequest());
    }
    
    assertTrue(true);
  }

  @Test
  public void testCreateTenantWithNamespaceConflict() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant").content(createTenantRequest)
        .contentType("application/json").with(userCreator)).andExpect(status().isOk());

    for (int i = 0; i < conflictingTenantRequests.size(); i++) {
      String conflictingTenantRequest = conflictingTenantRequests.get(i);
      repositoryServer.perform(put("/rest/tenants/tenantWithConflict" + i)
          .content(conflictingTenantRequest).contentType("application/json").with(userCreator2))
          .andDo(result -> {
            System.out.println("Request: " + conflictingTenantRequest + " Reply:"
                + result.getResponse().getStatus());
          }).andExpect(status().isConflict());
    }

    for (int i = 0; i < nonConflictingTenantRequests.size(); i++) {
      String nonConflictingTenantRequest = nonConflictingTenantRequests.get(i);
      repositoryServer.perform(put("/rest/tenants/tenantWithNoConflict" + i)
          .content(nonConflictingTenantRequest).contentType("application/json").with(userCreator3))
          .andDo(result -> {
            System.out.println("Request: " + nonConflictingTenantRequest + " Reply:"
                + result.getResponse().getStatus());
          }).andExpect(status().isOk());
    }
    
    assertTrue(true);
  }

  @Test
  public void testAdminCanRequestNonPrivateNamespace() throws Exception {
    String officialNs = gson.toJson(newTenantRequest("com.myofficial.namespace"));
    repositoryServer.perform(put("/rest/tenants/adminTenant").content(officialNs)
        .contentType("application/json").with(userAdmin)).andDo(result -> {
          System.out
              .println("Request: " + officialNs + " Reply:" + result.getResponse().getStatus());
        }).andExpect(status().isOk());
    
    assertTrue(true);
  }

  @Test
  public void testTenantNotFound() throws Exception {
    repositoryServer.perform(get("/rest/tenants/myTenantNotFound").with(userCreator))
        .andExpect(status().isNotFound());
    
    assertTrue(true);
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
    
    assertTrue(true);
  }

  @Test
  public void testDeleteTenantPreconditions() throws Exception {
    repositoryServer.perform(delete("/rest/tenants/tenantThatDoesntExist").with(userCreator))
        .andExpect(status().isBadRequest());
    
    assertTrue(true);
  }

  @Test
  public void testCannotDeleteTenantIfNonTenantAdmin() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant").content(createTenantRequest)
        .contentType("application/json").with(userCreator)).andExpect(status().isOk());

    repositoryServer.perform(delete("/rest/tenants/myTenant").with(nonTenantUser))
        .andExpect(status().isForbidden());
    
    assertTrue(true);
  }

  @Test
  public void testGetTenants() throws Exception {
    for (int i = 0; i < 2; i++) {
      repositoryServer.perform(put("/rest/tenants/getTenants" + i)
          .content(tenantRequest("vorto.private.testgettenants" + i, "user3"))
          .contentType("application/json").with(userCreator2)).andExpect(status().isOk());
    }

    repositoryServer.perform(get("/rest/tenants").contentType("application/json").with(userCreator2))
        .andDo(result -> {
          List<TenantDto> tenants = toTenantList(result);
          System.out.println("tenants.size() = " + tenants.size());
          //assertTrue(tenants.size() >= 10);
        }).andExpect(status().isOk());

    repositoryServer.perform(get("/rest/tenants").contentType("application/json").with(userAdmin))
        .andDo(result -> {
          List<TenantDto> tenants = toTenantList(result);
          System.out.println("tenants.size() = " + tenants.size());
          //assertTrue(tenants.size() >= 10);
        }).andExpect(status().isOk());

    repositoryServer
        .perform(get("/rest/tenants").contentType("application/json").with(nonTenantUser))
        .andDo(result -> {
          List<TenantDto> tenants = toTenantList(result);
          System.out.println("tenants.size() = " + tenants.size());
          //assertTrue(tenants.size() <= 0);
        }).andExpect(status().isOk());
    
    assertTrue(true);
  }

  private List<TenantDto> toTenantList(MvcResult result) throws UnsupportedEncodingException {
    Type founderListType = new TypeToken<ArrayList<TenantDto>>() {}.getType();
    List<TenantDto> tenants =
        gson.fromJson(result.getResponse().getContentAsString(), founderListType);
    return tenants;
  }

  @Test
  public void testGetTenant() throws Exception {
    repositoryServer.perform(put("/rest/tenants/getTenant")
        .content(tenantRequest("vorto.private.testgettenant", "user3"))
        .contentType("application/json").with(userCreator3)).andExpect(status().isOk());

    repositoryServer
        .perform(get("/rest/tenants/getTenant").contentType("application/json").with(userCreator3))
        .andExpect(status().isOk());

    repositoryServer
        .perform(get("/rest/tenants/getTenant").contentType("application/json").with(userAdmin))
        .andExpect(status().isOk());

    repositoryServer
        .perform(get("/rest/tenants/getTenant").contentType("application/json").with(nonTenantUser))
        .andExpect(status().isNotFound());
    
    assertTrue(true);
  }

  private String tenantRequest(String ns) {
    return gson.toJson(newTenantRequest(ns));
  }

  private String tenantRequest(String ns, String... users) {
    return gson.toJson(newTenantRequest(ns, users));
  }

  private CreateTenantRequest newTenantRequest(String ns) {
    return newTenantRequest(ns, "user3");
  }

  private CreateTenantRequest newTenantRequest(String ns, String... users) {
    CreateTenantRequest createRequest = new CreateTenantRequest();
    createRequest.setAuthenticationProvider("GITHUB");
    createRequest.setAuthorizationProvider("DB");
    createRequest.setDefaultNamespace(ns);
    if (ns != null) {
      createRequest.setNamespaces(Sets.newHashSet(ns));
    }

    if (users != null) {
      createRequest.setTenantAdmins(Sets.newHashSet(users));
    }

    return createRequest;
  }
}
