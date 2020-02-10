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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import java.util.Collection;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;

public class NamespaceControllerIntegrationTest extends AbstractIntegrationTest {

  private static final String BOSCH_IOT_SUITE_AUTH = "BOSCH-IOT-SUITE-AUTH";

  private static final String GITHUB = "GITHUB";

  private IUserAccountService accountService;

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
    repositoryServer.perform(get("/rest/namespaces/com.mycompany").with(userAdmin)).andExpect(status().isOk());
  }

  @Test
  public void updateCollaborator() throws Exception {

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

}
