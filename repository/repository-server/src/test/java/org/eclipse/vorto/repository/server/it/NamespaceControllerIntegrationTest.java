/**
 * Copyright (c) 2018, 2020 Contributors to the Eclipse Foundation
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

import static org.eclipse.vorto.repository.domain.Role.TENANT_ADMIN;
import static org.eclipse.vorto.repository.domain.Role.USER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceOperationResult;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

public class NamespaceControllerIntegrationTest extends AbstractIntegrationTest {

  private static final String BOSCH_IOT_SUITE_AUTH = "BOSCH-IOT-SUITE-AUTH";

  private static final String GITHUB = "GITHUB";

  private IUserAccountService accountService;

  /**
   * Since this is set to 2 in test profile for reasons unclear, taking value directly from config
   * to test cases where users create an excessive number of namespaces.
   */
  @Value("${config.restrictTenant}")
  private String restrictTenantConfig;
  
  @Autowired
  private ApplicationContext context;

  @Override
  protected void setUpTest() throws Exception {
    accountService = context.getBean(IUserAccountService.class);
  }

  @Test
  public void getNamespaces() throws Exception {
    repositoryServer.perform(get("/rest/namespaces").with(userAdmin)).andExpect(status().isOk());
  }
  
  @Test
  public void getNamespace() throws Exception {
    // we are now clearing userAdmin and userCreator namespaces on @After, so
    // need to re-create the namespace first
    repositoryServer
        .perform(
            put("/rest/namespaces/com.mycompany")
                .contentType("application/json").with(userAdmin)
        )
        .andExpect(status().isOk()
    );
    repositoryServer.perform(get("/rest/namespaces/com.mycompany").with(userAdmin)).andExpect(status().isOk());
  }
  
  @Test
  public void updateCollaborator() throws Exception {

    // we are now clearing userAdmin and userCreator namespaces on @After, so
    // need to re-create the namespace first
    repositoryServer
        .perform(
            put("/rest/namespaces/com.mycompany")
                .contentType("application/json").with(userAdmin)
        )
        .andExpect(status().isOk()
    );

    Collaborator collaborator = new Collaborator("userstandard2", GITHUB, null, 
        Lists.newArrayList("USER", "MODEL_CREATOR"));
    updateCollaborator("com.mycompany", collaborator);
    checkCollaboratorRoles("com.mycompany", "userstandard2", null, "USER", "MODEL_CREATOR");
    
    collaborator = new Collaborator("userstandard2", GITHUB, null, 
        Lists.newArrayList("USER"));
    updateCollaborator("com.mycompany", collaborator);
    checkCollaboratorRoles("com.mycompany", "userstandard2", null, "USER");
  }

  private void checkCollaboratorRoles(String namespace, String user, String subject, String ... roles) throws Exception {
    repositoryServer.perform(get("/rest/namespaces/" + namespace).with(userAdmin))
      .andDo(handler -> {
        NamespaceDto ns = new Gson().fromJson(handler.getResponse().getContentAsString(), NamespaceDto.class);
        Collection<Collaborator> collabs = ns.getCollaborators();
        collabs.forEach(collab -> {
          if (collab.getUserId().equals(user)) {
            for(String role : roles) {
              assertTrue(collab.getRoles().contains(role));
            }
            assertTrue(collab.getRoles().size() == roles.length);
            if (subject == null) {
              assertTrue(collab.getSubject() == null);
            } else {
              assertTrue(collab.getSubject().equals(subject));
            }
          }
        });
      })
      .andExpect(status().isOk());
  }

  private void createTechnicalUser(String namespace, Collaborator technicalUser) throws Exception {
    repositoryServer.perform(
        post("/rest/namespaces/" + namespace + "/users")
            .content(new Gson().toJson(technicalUser))
            .contentType(MediaType.APPLICATION_JSON)
            .with(userAdmin))
        .andExpect(status().isOk());
  }

  private void updateCollaborator(String namespace, Collaborator collaborator) throws Exception {
    repositoryServer.perform(
       put("/rest/namespaces/" + namespace + "/users")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userAdmin))
      .andExpect(status().isOk());
  }
  
  @Test
  public void updateCollaboratorAddTechnicalUser() throws Exception {

    // we are now clearing userAdmin and userCreator namespaces on @After, so
    // need to re-create the namespace first
    repositoryServer
        .perform(
            put("/rest/namespaces/com.mycompany")
                .contentType("application/json").with(userAdmin)
        )
        .andExpect(status().isOk()
    );

    assertFalse(accountService.exists("my-technical-user"));
    
    Collaborator collaborator = new Collaborator("my-technical-user", BOSCH_IOT_SUITE_AUTH, "ProjectX", 
        Lists.newArrayList("USER", "MODEL_CREATOR"));
    collaborator.setTechnicalUser(true);
    createTechnicalUser("com.mycompany", collaborator);
    
    assertTrue(accountService.exists("my-technical-user"));
    assertTrue(accountService.getUser("my-technical-user").isTechnicalUser());
    
    checkCollaboratorRoles("com.mycompany", "my-technical-user", "ProjectX", "USER", "MODEL_CREATOR");
    
    collaborator = new Collaborator("my-technical-user", BOSCH_IOT_SUITE_AUTH, "ProjectX", 
        Lists.newArrayList("USER"));
    // cannot re-create tech user so adding as collaborator since it already exists now
    updateCollaborator("com.mycompany", collaborator);
    
    checkCollaboratorRoles("com.mycompany", "my-technical-user", "ProjectX", "USER");
  }
  
  @Test
  public void updateCollaboratorNonAdmin() throws Exception {
    Collaborator collaborator = new Collaborator("userstandard2", GITHUB, null, 
        Lists.newArrayList("USER", "MODEL_CREATOR"));
    repositoryServer.perform(
       put("/rest/namespaces/com.mycompany/users")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userStandard))
      .andExpect(status().isForbidden());
  }
  
  @Test
  public void updateCollaboratorUnknownProvider() throws Exception {
    Collaborator collaborator = new Collaborator("userstandard2", "unknownProvider", null, 
        Lists.newArrayList("USER", "MODEL_CREATOR"));
    repositoryServer.perform(
       put("/rest/namespaces/com.mycompany/users")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userAdmin))
      .andExpect(status().isPreconditionFailed());
  }
  
  @Test
  public void updateCollaboratorAddTechnicalUserNoSubject() throws Exception {
    Collaborator collaborator = new Collaborator("my-technical-user", BOSCH_IOT_SUITE_AUTH, null, 
        Lists.newArrayList("USER", "MODEL_CREATOR"));
    collaborator.setTechnicalUser(true);
    repositoryServer.perform(
       post("/rest/namespaces/com.mycompany/users")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userAdmin))
      .andExpect(status().isPreconditionFailed());
  }
  
  @Test
  public void updateCollaboratorUserDoesntExist() throws Exception {
    // we are now clearing userAdmin and userCreator namespaces on @After, so
    // need to re-create the namespace first
    repositoryServer
        .perform(
            put("/rest/namespaces/com.mycompany")
                .contentType("application/json").with(userAdmin)
        )
        .andExpect(status().isOk()
    );
    Collaborator collaborator = new Collaborator("unknownUser", GITHUB, null,
        Lists.newArrayList("USER", "MODEL_CREATOR"));
    repositoryServer.perform(
       put("/rest/namespaces/com.mycompany/users")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userAdmin))
      .andExpect(status().isOk())
      .andExpect(content().string("false"));
  }
  
  @Test
  public void updateCollaboratorTenantDoesntExist() throws Exception {
    Collaborator collaborator = new Collaborator("userstandard2", GITHUB, null, 
        Lists.newArrayList("USER", "MODEL_CREATOR"));
    repositoryServer.perform(
       put("/rest/namespaces/com.unknowntenant/users")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userAdmin))
      .andExpect(status().isNotFound());
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
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.failure("Namespace Quota of 1 exceeded."))
            )
        );
  }

  /**
   * Simply tests that adding an existing user to a namespace with a basic role works as intended.
   * @throws Exception
   */
  @Test
  public void testAddNamespaceUserWithOneRole() throws Exception {
    // first, creates the namespace for the admin user
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
    // first, creates the namespace for the admin user
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
    // first, creates the namespace for the admin user
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
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/myAdminNamespace/users/%s", ApplicationConfig.USER_CREATOR))
                .with(userAdmin)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().string(
                "true"
            )
        );
  }

  /**
   * Tests that removing a non-existing user from a namespace fails.<br/>
   * Note that the response will just return "false" if no user has been removed here.
   * @throws Exception
   */
  @Test
  public void testRemoveNonExistingUserFromNamespace() throws Exception {
    // first, creates the namespace for the admin user
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
    // now removes a user that has not been added
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/myAdminNamespace/users/%s", ApplicationConfig.USER_CREATOR))
                .with(userAdmin)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().string(
                "false"
            )
        );
  }

  /**
   * Tests that removing an existing user from a namespace fails if the user performing the operation
   * has no "tenant admin" role for that namespace. <br/>
   * In this case, we're creating a third user with tenant admin authority, who is not added as
   * admin of that namespace, and will try to remove the simple user.<br/>
   * Note that it might be worth thinking of the edge case where a user simply wants to be removed
   * from a namespace they have been added to, regardless of their role in that namespace. <br/>
   * See {@link org.eclipse.vorto.repository.web.api.v1.NamespaceController#removeUserFromNamespace(String, String)}
   * for specifications on how authorization is enforced.
   * @throws Exception
   */
  @Test
  public void testRemoveExistingUserFromNamespaceWithNoPrivileges() throws Exception {
    // first, creates the namespace for the admin user
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

    Collaborator userCreatorCollaborator = new Collaborator();
    userCreatorCollaborator.setUserId(ApplicationConfig.USER_CREATOR);
    Set<String> roles = new HashSet<>();
    roles.add("USER");
    userCreatorCollaborator.setRoles(roles);

    // adds the collaborator with "USER" roles to the namespace
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

    // creates a user with tenant admin privileges but no access to the namespace in question
    SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor thirdUser = user("thirdPartyUser")
        .password("pass")
        .authorities(SpringUserUtils.toAuthorityList(
            Sets.newHashSet(USER, TENANT_ADMIN))
        );

    // finally removes the user from the namespace but with the "thirdPartyUser" who is tenant admin
    // "somewhere else", which fails due to lack of tenant admin role on that given namespace
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/myAdminNamespace/users/%s", ApplicationConfig.USER_CREATOR))
                .with(thirdUser)
        )
        .andExpect(status().isPreconditionFailed());
  }

  /**
   * Tests that changing roles on a namespace works for an existing user who has been previously
   * added to that namespace.
   * @throws Exception
   */
  @Test
  public void testChangeUserRolesOnNamespaceUser() throws Exception {
    // first, creates the namespace for the admin user
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
    // finally, change the user roles on the DTO and PUT again
    roles.add("MODEL_CREATOR");
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
   * Tests that changing a user's roles on a namespace from a user who's tenant admin, but not on
   * that namespace, fails.
   * @throws Exception
   */
  @Test
  public void testChangeUserRolesOnNamespaceUserWithExtraneousUser() throws Exception {
    // first, creates the namespace for the admin user
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

    // creates a user with tenant admin privileges but no access to the namespace in question
    SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor thirdUser = user("thirdPartyUser")
        .password("pass")
        .authorities(SpringUserUtils.toAuthorityList(
            Sets.newHashSet(USER, TENANT_ADMIN))
        );

    // finally, change the user roles on the DTO and PUT again
    roles.add("MODEL_CREATOR");
    userCreatorCollaborator.setRoles(roles);
    repositoryServer
        .perform(
            put("/rest/namespaces/myAdminNamespace/users")
                .contentType("application/json")
                .content(gson.toJson(userCreatorCollaborator))
                .with(thirdUser)
        )
        .andExpect(status().isPreconditionFailed());
  }

  /**
   * Tests that checking whether the logged on user has a given role on a given namespace works as
   * expected.<br/>
   * The endpoint is a simplification of the former TenantService.js all deferred to the back-end,
   * and is used contextually to verifying whether a user can modify a model.
   * @throws Exception
   */
  @Test
  public void testHasRoleOnNamespace() throws Exception {
    // first, creates the namespace for the admin user
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
    // now checks whether the user has USER role
    repositoryServer
        .perform(
            get("/rest/namespaces/USER/myAdminNamespace")
                .with(userCreator)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().string(
                "true"
            )
        );
    // finally, checks whether the user has MODEL_CREATOR role, which they haven't
    repositoryServer
        .perform(
            get("/rest/namespaces/MODEL_CREATOR/myAdminNamespace")
                .with(userCreator)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().string(
                "false"
            )
        );
  }

  /**
   * Tests that checking whether the logged on user is the only admin on any of their namespaces
   * returns as expected. <br/>
   * The endpoint is a simplification of the former TenantService.js all deferred to the back-end,
   * and is used contextually to a user trying to delete their account.
   * @throws Exception
   */
  @Test
  public void testIsOnlyAdminOnAnyNamespace() throws Exception {
    // first, creates the namespace for the admin user
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

    // now checks whether the creator user is the only admin user of any namespace - since they
    // only have one, this will return true
    repositoryServer
        .perform(
            get("/rest/namespaces/userIsOnlyAdmin")
                .with(userCreator)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().string(
                "true"
            )
        );

    /*
    Now adds another user as tenant admin for the namespace.
    Note: this is done with the admin user here, because of the pre-authorization checks in the
    controller, that verify if a user has the Spring role at all.
    Since those users are mocked and their roles cannot be changed during tests, the userCreator
    user would fail to add a collaborator at this point (but not in real life, since they would be
    made tenant admin of the namespace they just created).
    */
    Collaborator userCreatorCollaborator = new Collaborator();
    userCreatorCollaborator.setUserId(ApplicationConfig.USER_CREATOR2);
    Set<String> roles = new HashSet<>();
    roles.add("USER");
    roles.add("TENANT_ADMIN");
    userCreatorCollaborator.setRoles(roles);
    repositoryServer
        .perform(
            put("/rest/namespaces/vorto.private.myNamespace/users")
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
    // finally, checks whether the original user is still only admin in any of their namespaces -
    // which they aren't now, since we've added another user with tenant admin privileges
    repositoryServer
        .perform(
            get("/rest/namespaces/userIsOnlyAdmin")
                .with(userCreator)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().string(
                "false"
            )
        );
  }

  /**
   * Verifies the list of namespaces where the logged on user has a given role is correct.
   * @throws Exception
   */
  @Test
  public void testAccessibleNamespacesWithRole() throws Exception {
    // first, creates a namespace for the userCreator user
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

    // now, creates a namespace for the userCreator2 user
    repositoryServer
        .perform(
            put("/rest/namespaces/vorto.private.myNamespace2")
                .contentType("application/json").with(userCreator2)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().json(
                gson.toJson(NamespaceOperationResult.success())
            )
        );
    //
    /*
    Now adds userCreator to userCreator2's namespace as model creator
    Note: this is done with the admin user here, because of the pre-authorization checks in the
    controller, that verify if a user has the Spring role at all.
    Since those users are mocked and their roles cannot be changed during tests, the userCreator
    user would fail to add a collaborator at this point (but not in real life, since they would be
    made tenant admin of the namespace they just created).
    */
    Collaborator userCreatorCollaborator = new Collaborator();
    userCreatorCollaborator.setUserId(ApplicationConfig.USER_CREATOR);
    Set<String> roles = new HashSet<>();
    roles.add("USER");
    roles.add("MODEL_CREATOR");
    userCreatorCollaborator.setRoles(roles);
    repositoryServer
        .perform(
            put("/rest/namespaces/vorto.private.myNamespace2/users")
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

    // finally, lists all namespaces where userCreator has the MODEL_CREATOR role, that is, their own
    // and userCreator2's namespace

    // expected namespaces
    Collection<NamespaceDto> expectedNamespaces = new ArrayList<>();

    // admins and users of the userCreator's namespace
    Collection<Collaborator> userCreatorNSAdmins = new ArrayList<>();
    Collection<Collaborator> userCreatorNSUsers = new ArrayList<>();

    /*
    Creating set of namespace owner roles - note: those reflect the admin's role but
    namespace admin Collaborators seem to not have any role by design (arguably because they
    implicitly have all roles) - so basically the roles below reflect userCreator as user of
    their namespace, but not as admin.
    */
    Set<String> ownerRoles = new HashSet<>();
    ownerRoles.add("USER");
    ownerRoles.add("MODEL_CREATOR");
    ownerRoles.add("TENANT_ADMIN");
    ownerRoles.add("MODEL_PROMOTER");
    ownerRoles.add("MODEL_REVIEWER");

    // for some WEIRD reason, the publisher role is not granted automatically to namespace owners...

    // creating Collaborator for userCreator as admin in their own namespace
    Collaborator userCreatorCollaboratorAsAdmin = new Collaborator();
    userCreatorCollaboratorAsAdmin.setUserId(ApplicationConfig.USER_CREATOR);
    userCreatorNSAdmins.add(userCreatorCollaboratorAsAdmin);

    // creating Collaborator for userCreator as user in their own namespace - all roles but sysadmin
    Collaborator userCreatorCollaboratorAsUserAdmin = new Collaborator();
    userCreatorCollaboratorAsUserAdmin.setUserId(ApplicationConfig.USER_CREATOR);
    userCreatorCollaboratorAsUserAdmin.setRoles(ownerRoles);
    userCreatorNSUsers.add(userCreatorCollaboratorAsUserAdmin);

    // creating namespace for userCreator
    NamespaceDto userCreatorNS = new NamespaceDto("vorto.private.myNamespace", userCreatorNSUsers, userCreatorNSAdmins);

    // creating userCreator2 as a Collaborator object
    Collaborator userCreator2CollaboratorAsAdmin = new Collaborator();
    userCreator2CollaboratorAsAdmin.setUserId(ApplicationConfig.USER_CREATOR2);

    Collaborator userCreator2CollaboratorAsUserAdmin = new Collaborator();
    userCreator2CollaboratorAsUserAdmin.setUserId(ApplicationConfig.USER_CREATOR2);
    userCreator2CollaboratorAsUserAdmin.setRoles(ownerRoles);

    // adding to userCreator2 namespace admins
    Collection<Collaborator> userCreator2NSAdmins = new ArrayList<>();
    userCreator2NSAdmins.add(userCreator2CollaboratorAsAdmin);

    // adding both userCreator2 collaborator and userCreator (the non-admin collaborator from up above)
    // to the userCreator2 namespace users
    Collection<Collaborator> userCreator2NSUsers = new ArrayList<>();
    userCreator2NSUsers.add(userCreator2CollaboratorAsUserAdmin);
    userCreator2NSUsers.add(userCreatorCollaborator);

    // creating ns for userCreator2
    NamespaceDto userCreator2NS = new NamespaceDto("vorto.private.myNamespace2", userCreator2NSUsers, userCreator2NSAdmins);

    // adding both ns to expected collection
    expectedNamespaces.add(userCreatorNS);
    expectedNamespaces.add(userCreator2NS);

    // late fix: some "com.test" namespace added to user1 in parent class where userCreator has creator role
    repositoryServer
        .perform(
            delete("/rest/namespaces/com.test")
                .with(userAdmin)
        );
    // don't care about the outcome here

    repositoryServer
        .perform(
            get("/rest/namespaces/role/ROLE_MODEL_CREATOR")
                .with(userCreator)
        )
        .andExpect(status().isOk())
        .andExpect(
            content().json(gson.toJson(expectedNamespaces))
        );

  }


}
