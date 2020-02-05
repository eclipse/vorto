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
package org.eclipse.vorto.repository.server.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceOperationResult;
import org.eclipse.vorto.repository.web.tenant.dto.TenantDto;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

public class NamespaceServiceIntegrationTest extends AbstractIntegrationTest {

  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

  /*private String createNamespaceRequest = namespaceRequest("vorto.private.abc.xyz");

  private List<String> badTenantRequests = Arrays.asList(gson.toJson(newNamespaceRequest("com2")),
      new Gson().toJson(newNamespaceRequest("vorto.private")),
      new Gson().toJson(newNamespaceRequest("vorto")),
      new Gson().toJson(newNamespaceRequest("vorto.private.")));

  private List<String> nonConflictingTenantRequests =
      Arrays.asList(gson.toJson(newNamespaceRequest("vorto.private.xyz.ecm")));

  private List<String> conflictingTenantRequests =
      Arrays.asList(gson.toJson(newNamespaceRequest("vorto.private.abc")));*/

  /**
   * Since this is set to 2 in test profile for reasons unclear, taking value directly from config
   * to test cases where users create an excessive number of namespaces.
   */
  @Value("${config.restrictTenant}")
  private String restrictTenantConfig;

  @Override
  protected void setUpTest() throws Exception {

  }

  /**
   * Cleaning up namespaces for the users involved in tests, as this is not done by the parent class.
   * @throws Exception
   */
  @After
  public void afterEach() throws Exception {
    // only two users used
    repositoryServer
        .perform(
            get("/rest/namespaces/all").with(userCreator)
        )
        .andDo(result -> {
          NamespaceDto[] namespaces = (gson.fromJson(result.getResponse().getContentAsString(), NamespaceDto[].class));
          for (NamespaceDto n: namespaces) {
            repositoryServer
                .perform(delete(String.format("/rest/namespaces/%s", n.getName())).with(userAdmin));
          }
        });
    repositoryServer
        .perform(
            get("/rest/namespaces/all").with(userAdmin)
        )
        .andDo(result -> {
          NamespaceDto[] namespaces = (gson.fromJson(result.getResponse().getContentAsString(), NamespaceDto[].class));
          for (NamespaceDto n: namespaces) {
            repositoryServer
                .perform(delete(String.format("/rest/namespaces/%s", n.getName())).with(userAdmin));
          }
        });
  }

  /**
   * Uses a non-compliant namespace notation
   * @throws Exception
   */
  @Test
  public void testNamespaceCreationWithInvalidNotation() throws Exception {
    repositoryServer
        .perform(
            put("/rest/namespaces/badNamespace==name!")
            .contentType("application/json").with(userCreator)
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.failure("Invalid namespace notation."))
            )
        );
  }

  /**
   * Uses an empty namespace name after trimming
   * @throws Exception
   */
  @Test
  public void testNamespaceCreationWithEmptyNamespace() throws Exception {
    repositoryServer
        .perform(
            put("/rest/namespaces/ \t")
            .contentType("application/json").with(userCreator)
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.failure("Empty namespace"))
            )
        );
  }

  /**
   * Only sysadmins can create namespaces that don't start with {@literal vorto.private.}
   * @throws Exception
   */
  @Test
  public void testNamespaceCreationWithoutPrivatePrefixAsNonSysadmin() throws Exception {
    repositoryServer
        .perform(
            put("/rest/namespaces/myNamespace")
            .contentType("application/json").with(userCreator)
        )
        .andExpect(status().isBadRequest())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.failure("User can only register a private namespace."))
            )
        );
  }

  /**
   * Simply tests that creating a namespace with the proper prefix for non-sysadmin users works.
   * @throws Exception
   */
  @Test
  public void testNamespaceCreationWithPrivatePrefixAsNonSysadmin() throws Exception {
    repositoryServer
        .perform(
            put("/rest/namespaces/vorto.private.myNamespace")
                .contentType("application/json").with(userCreator)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.success())
            )
        );
  }

  /**
   * Simply tests that creating a namespace with no prefix for sysadmin users works.
   * @throws Exception
   */
  @Test
  public void testNamespaceCreationWithNoPrefixAsSysadmin() throws Exception {
    repositoryServer
        .perform(
            put("/rest/namespaces/myAdminNamespace")
                .contentType("application/json").with(userAdmin)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.success())
            )
        );
  }

  /**
   * Tests that creating {@literal x} namespaces for non-sysadmin users fails, where {@literal x > config.restrictTenant}.
   * <br/>
   * Note that {@literal config.restrictTenant} is set to {@literal 1} in the production profile,
   * but {@literal 2} in test for some reason.
   * @throws Exception
   */
  @Test
  public void testMultipleNamespaceCreationAsNonSysadmin() throws Exception {
    int maxNamespaces = -1;
    try {
      maxNamespaces = Integer.valueOf(restrictTenantConfig);
    }
    catch (NumberFormatException | NullPointerException e) {
      fail("restrictTenant configuration value not available within context.");
    }

    for (int i = 0; i < maxNamespaces; i++) {
      repositoryServer
          .perform(
              put("/rest/namespaces/vorto.private.myNamespace" + i)
                  .contentType("application/json").with(userCreator)
          )
          .andExpect(status().isOk())
          .andExpect(
              content().json(
                  gson.toJson(NamespaceOperationResult.success())
              )
          );
    }

    repositoryServer
        .perform(
            put("/rest/namespaces/vorto.private.myNamespace" + maxNamespaces)
                .contentType("application/json").with(userCreator)
        )
        .andExpect(status().isConflict())
        .andExpect(
            content().json(
                // hard-coded error message due to production profile limiting to 1 namespace for
                // non-sysadmins
                gson.toJson(NamespaceOperationResult.failure("Namespace Quota of 1 exceeded."))
            )
        );
  }

  /**
   * Tests that creating {@literal x} namespaces for sysadmin users succeeds even when
   * {@literal x > config.restrictTenant}.
   * @throws Exception
   */
  @Test
  public void testMultipleNamespaceCreationAsSysadmin() throws Exception {
    int maxNamespaces = -1;
    try {
      maxNamespaces = Integer.valueOf(restrictTenantConfig);
    }
    catch (NumberFormatException | NullPointerException e) {
      fail("restrictTenant configuration value not available within context.");
    }

    for (int i = 0; i < maxNamespaces; i++) {
      repositoryServer
          .perform(
              put("/rest/namespaces/vorto.private.myNamespace" + i)
                  .contentType("application/json").with(userCreator)
          )
          .andExpect(status().isOk())
          .andExpect(
              content().json(
                  gson.toJson(NamespaceOperationResult.success())
              )
          );
    }

    repositoryServer
        .perform(
            put("/rest/namespaces/vorto.private.myNamespace" + maxNamespaces)
                .contentType("application/json").with(userCreator)
        )
        .andExpect(status().isConflict())
        .andExpect(status().isOk())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.success())
            )
        );
  }

  /**
   * Simply tests that adding an existing user to a namespace with a basic role works as intended.
   * @throws Exception
   */
  @Test
  public void testAddNamespaceUserWithOneRole() throws Exception {
    // first, create the namespace for the admin user
    repositoryServer
        .perform(
            put("/rest/namespaces/myAdminNamespace")
                .contentType("application/json").with(userAdmin)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.success())
            )
        );

    // then, add the creator user
    Collaborator userCreatorCollaborator = new Collaborator();
    // you would think a user called "userCreator" has a username called "userCreator", but
    // the way it has been mapped to in the parent class is "user3" instead
    userCreatorCollaborator.setUserId(ApplicationConfig.USER_CREATOR);
    Set<String> roles = new HashSet<>();
    roles.add("USER");
    userCreatorCollaborator.setRoles(roles);
    repositoryServer
        .perform(
            put("/rest/namespaces/myAdminNamespace/users")
            .contentType("application/json")
            .content(gson.toJson(userCreatorCollaborator))
            .with(userAdmin)
        )
        .andExpect(status().isOk())
        // currently returns a simple boolean payload, matching IUserAccountService#addUserToTenant
        .andExpect(
            content().string(
                "true"
            )
        );

  }

  /**
   * Tests that adding an non-existing user to a namespace fails
   * @throws Exception
   */
  @Test
  public void testAddNamespaceNonExistingUser() throws Exception {
    // first, create the namespace for the admin user
    repositoryServer
        .perform(
            put("/rest/namespaces/myAdminNamespace")
                .contentType("application/json").with(userAdmin)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.success())
            )
        );

    // then, add the non-existing user
    Collaborator nonExistingCollaborator = new Collaborator();
    nonExistingCollaborator.setUserId("toto");
    Set<String> roles = new HashSet<>();
    roles.add("USER");
    nonExistingCollaborator.setRoles(roles);
    repositoryServer
        .perform(
            put("/rest/namespaces/myAdminNamespace/users")
                .contentType("application/json")
                .content(gson.toJson(nonExistingCollaborator))
                .with(userAdmin)
        )
        .andExpect(status().isOk())
        // currently returns a simple boolean payload, matching IUserAccountService#addUserToTenant
        .andExpect(
            content().string(
                "false"
            )
        );

  }

  /**
   * Tests that removing an existing user from a namespace works if the user has been added previously
   * and the user removing it has the rights to do so.
   * @throws Exception
   */
  @Test
  public void testRemoveExistingUserFromNamespace() throws Exception {
    // first, create the namespace for the admin user
    repositoryServer
        .perform(
            put("/rest/namespaces/myAdminNamespace")
                .contentType("application/json").with(userAdmin)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.success())
            )
        );

    // then, add the creator user
    Collaborator userCreatorCollaborator = new Collaborator();
    // you would think a user called "userCreator" has a username called "userCreator", but
    // the way it has been mapped to in the parent class is "user3" instead
    userCreatorCollaborator.setUserId(ApplicationConfig.USER_CREATOR);
    Set<String> roles = new HashSet<>();
    roles.add("USER");
    userCreatorCollaborator.setRoles(roles);
    repositoryServer
        .perform(
            put("/rest/namespaces/myAdminNamespace/users")
                .contentType("application/json")
                .content(gson.toJson(userCreatorCollaborator))
                .with(userAdmin)
        )
        .andExpect(status().isOk())
        // currently returns a simple boolean payload, matching IUserAccountService#addUserToTenant
        .andExpect(
            content().string(
                "true"
            )
        );
    // now removes the user

  }

  // TODO
  // remove non-existing user
  // remove existing user who was not added to namespace
  // remove exisiting user from namespace with insufficient privileges?
  // update user roles
  // check other endpoints, e.g. the ones replacing the dreaded TenantService.js
  // delete the TenantManagementController (already commented out) once coverage ok
  // delete the NamespaceRequest class if not used (likely)

  /*@Test
  public void testUpdateTenantAdmins() throws Exception {
    addTenant("myTenant1", "vorto.private.tre", "user2", userStandard);

    repositoryServer.perform(get("/rest/tenants/myTenant1").with(userAdmin))
        .andExpect(status().isOk());

    checkAdminCount(1);

    repositoryServer.perform(
        put("/rest/tenants/myTenant1").content(
            namespaceRequest("vorto.private.tre", "user2", "user1"))
            .contentType("application/json").with(userStandard))
        .andExpect(status().isOk());

    repositoryServer
        .perform(put("/rest/tenants/myTenant1").content(
            namespaceRequest("vorto.private.tre", "user3"))
            .contentType("application/json").with(userCreator))
        .andExpect(status().isBadRequest());

    checkAdminCount(2);

    assertTrue(true);
  }*/

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

 /* private void addTenant(String tenantId, String namespace, String admin,
      SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor userStandard) throws Exception {
    repositoryServer
        .perform(put("/rest/tenants/" + tenantId).content(namespaceRequest(namespace, admin))
            .contentType("application/json").with(userStandard))
        .andExpect(status().isOk());
  }*/

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

  /*@Test
  public void testUpdateNamespaces() throws Exception {
    addTenant("myTenant2", "vorto.private.mytenant2tre", "user2", userStandard3);

    NamespaceRequest updateNamespace = new NamespaceRequest();
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
  }*/

  /*@Test
  public void testAddNamespaces() throws Exception {
    addTenant("myTenant3", "vorto.private.mytenant3", "user2", userStandard3);

    NamespaceRequest addNamespaces = new NamespaceRequest();
    addNamespaces.getNamespaces().add("vorto.private.mytenant31");
    addNamespaces.getNamespaces().add("vorto.private.mytenant32");

    repositoryServer.perform(post("/rest/tenants/myTenant3/namespaces")
        .content(gson.toJson(addNamespaces)).contentType("application/json").with(userAdmin))
        .andExpect(status().isOk());

    NamespaceRequest additionalNamespaces = new NamespaceRequest();
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
  }*/

  /*
   * Empty namespace must return Bad request
   */
  /*@Test
  public void testEmptyOrNullNamespaces() throws Exception {
    addTenant("myTenant3", "vorto.private.mytenant3", "user2", userStandard3);

    NamespaceRequest addNamespaces = new NamespaceRequest();

    repositoryServer.perform(post("/rest/tenants/myTenant3/namespaces")
        .content(gson.toJson(addNamespaces)).contentType("application/json").with(userAdmin))
        .andExpect(status().isBadRequest());

    assertTrue(true);
  }

  @Test
  public void testIsValidNamespace() throws Exception {
    addTenant("myTenant4", "vorto.private.mytenant4", "user2", userStandard4);

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
          });// .andExpect(status().isBadRequest());
    }

    assertTrue(true);
  }

  @Test
  public void testCreateTenantWithNamespaceConflict() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant").content(createNamespaceRequest)
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
  }*/

  /*@Test
  public void testAdminCanRequestNonPrivateNamespace() throws Exception {
    String officialNs = gson.toJson(newNamespaceRequest("com.myofficial.namespace"));
    repositoryServer.perform(put("/rest/tenants/adminTenant").content(officialNs)
        .contentType("application/json").with(userAdmin)).andDo(result -> {
          System.out
              .println("Request: " + officialNs + " Reply:" + result.getResponse().getStatus());
        }).andExpect(status().isOk());

    assertTrue(true);
  }*/

  @Test
  public void testTenantNotFound() throws Exception {
    repositoryServer.perform(get("/rest/tenants/myTenantNotFound").with(userCreator))
        .andExpect(status().isNotFound());

    assertTrue(true);
  }

/*  @Test
  public void testDeleteTenant() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant").content(createNamespaceRequest)
        .contentType("application/json").with(userCreator)).andExpect(status().isOk());

    repositoryServer.perform(get("/rest/tenants/myTenant").with(userCreator))
        .andExpect(status().isOk());

    repositoryServer.perform(delete("/rest/tenants/myTenant").with(userCreator))
        .andExpect(status().isOk());

    repositoryServer.perform(get("/rest/tenants/myTenant").with(userCreator))
        .andExpect(status().isNotFound());

    assertTrue(true);
  }*/

  @Test
  public void testDeleteTenantPreconditions() throws Exception {
    repositoryServer.perform(delete("/rest/tenants/tenantThatDoesntExist").with(userCreator))
        .andExpect(status().isBadRequest());

    assertTrue(true);
  }

 /* @Test
  public void testCannotDeleteTenantIfNonTenantAdmin() throws Exception {
    repositoryServer.perform(put("/rest/tenants/myTenant").content(createNamespaceRequest)
        .contentType("application/json").with(userCreator)).andExpect(status().isOk());

    repositoryServer.perform(delete("/rest/tenants/myTenant").with(nonTenantUser))
        .andExpect(status().isForbidden());

    assertTrue(true);
  }*/

  /*@Test
  public void testGetTenants() throws Exception {
    for (int i = 0; i < 2; i++) {
      repositoryServer.perform(put("/rest/tenants/getTenants" + i)
          .content(namespaceRequest("vorto.private.testgettenants" + i, "user3"))
          .contentType("application/json").with(userCreator2)).andExpect(status().isOk());
    }

    repositoryServer
        .perform(get("/rest/tenants").contentType("application/json").with(userCreator2))
        .andDo(result -> {
          List<TenantDto> tenants = toTenantList(result);
          System.out.println("tenants.size() = " + tenants.size());
          // assertTrue(tenants.size() >= 10);
        }).andExpect(status().isOk());

    repositoryServer.perform(get("/rest/tenants").contentType("application/json").with(userAdmin))
        .andDo(result -> {
          List<TenantDto> tenants = toTenantList(result);
          System.out.println("tenants.size() = " + tenants.size());
          // assertTrue(tenants.size() >= 10);
        }).andExpect(status().isOk());

    repositoryServer
        .perform(get("/rest/tenants").contentType("application/json").with(nonTenantUser))
        .andDo(result -> {
          List<TenantDto> tenants = toTenantList(result);
          System.out.println("tenants.size() = " + tenants.size());
          // assertTrue(tenants.size() <= 0);
        }).andExpect(status().isOk());

    assertTrue(true);
  }*/

  private List<TenantDto> toTenantList(MvcResult result) throws UnsupportedEncodingException {
    Type founderListType = new TypeToken<ArrayList<TenantDto>>() {}.getType();
    List<TenantDto> tenants =
        gson.fromJson(result.getResponse().getContentAsString(), founderListType);
    return tenants;
  }

  /*@Test
  public void testGetTenant() throws Exception {
    repositoryServer.perform(put("/rest/tenants/getTenant")
        .content(namespaceRequest("vorto.private.testgettenant", "user3"))
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
  }*/



}
