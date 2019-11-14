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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.domain.AuthenticationProvider;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NamespaceControllerIntegrationTest extends AbstractIntegrationTest {

  private IUserAccountService accountService;
  
  @Autowired private ApplicationContext context;
  
  @Override
  protected void setUpTest() throws Exception {
    accountService = context.getBean(IUserAccountService.class);
  }

  @Test
  public void getNamespaces() throws Exception {
    repositoryServer.perform(get("/api/v1/namespaces").with(userAdmin)).andExpect(status().isOk());
  }
  
  @Test
  public void getNamespacesNonAdmin() throws Exception {
    repositoryServer.perform(get("/api/v1/namespaces").with(userStandard)).andExpect(status().isForbidden());
  }
  
  @Test
  public void getNamespace() throws Exception {
    repositoryServer.perform(get("/api/v1/namespaces/com.mycompany").with(userAdmin)).andExpect(status().isOk());
  }
  
  @Test
  public void getNamespaceNonAdmin() throws Exception {
    repositoryServer.perform(get("/api/v1/namespaces/com.mycompany").with(userStandard)).andExpect(status().isForbidden());
  }
  
  @Test
  public void updateNamespace() throws Exception {
    repositoryServer.perform(put("/api/v1/namespaces/com.mycompany").contentType(MediaType.APPLICATION_JSON)
      .content("[\"user1\"]").with(userAdmin))
      .andExpect(status().isOk());
    
    checkAdmins("user1");
    
    repositoryServer.perform(put("/api/v1/namespaces/com.mycompany").contentType(MediaType.APPLICATION_JSON)
      .content("[\"user1\", \"user2\"]").with(userAdmin))
      .andExpect(status().isOk());
    
    checkAdmins("user1", "user2");
    
    repositoryServer.perform(put("/api/v1/namespaces/com.mycompany").contentType(MediaType.APPLICATION_JSON)
      .content("[\"user1\"]").with(userAdmin))
      .andExpect(status().isOk());
    
    checkAdmins("user1");
  }

  private void checkAdmins(String ... admins) throws Exception {
    repositoryServer.perform(get("/api/v1/namespaces/com.mycompany").with(userAdmin))
      .andDo(handler -> {
        NamespaceDto ns = new Gson().fromJson(handler.getResponse().getContentAsString(), NamespaceDto.class);
        for(String admin: admins) {
          assertTrue(ns.getTenantAdmins().containsKey(admin));
        }
        assertTrue(ns.getTenantAdmins().size() == admins.length);
      })
      .andExpect(status().isOk());
  }
  
  @Test
  public void updateNamespaceUserDoesNotExist() throws Exception {
    repositoryServer.perform(put("/api/v1/namespaces/com.mycompany").contentType(MediaType.APPLICATION_JSON)
        .content("[\"userWhichDoesntExist\"]").with(userAdmin))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void updateNamespaceNoAdminPayload() throws Exception {
    repositoryServer.perform(put("/api/v1/namespaces/com.mycompany").contentType(MediaType.APPLICATION_JSON)
        .content("[]").with(userAdmin)).andExpect(status().isBadRequest());
  }
  
  @Test
  public void updateNamespaceNonAdmin() throws Exception {
    repositoryServer.perform(put("/api/v1/namespaces/com.mycompany").contentType(MediaType.APPLICATION_JSON)
        .content("[\"user1\"]").with(userStandard)).andExpect(status().isForbidden());
  }
  
  @Test
  public void getCollaborators() throws Exception {
    repositoryServer.perform(get("/api/v1/namespaces/com.mycompany/collaborators").with(userAdmin)).andExpect(status().isOk());
  }
  
  @Test
  public void getCollaboratorsNonAdmin() throws Exception {
    repositoryServer.perform(get("/api/v1/namespaces/com.mycompany/collaborators").with(userStandard)).andExpect(status().isForbidden());
  }
  
  @Test
  public void updateCollaborator() throws Exception {
    Collaborator collaborator = new Collaborator("userstandard2", AuthenticationProvider.GITHUB.name(), Lists.newArrayList("USER", "MODEL_CREATOR"));
    updateCollaborator("com.mycompany", collaborator);
    checkCollaboratorRoles("com.mycompany", "userstandard2", "USER", "MODEL_CREATOR");
    
    collaborator = new Collaborator("userstandard2", AuthenticationProvider.GITHUB.name(), Lists.newArrayList("USER"));
    updateCollaborator("com.mycompany", collaborator);
    checkCollaboratorRoles("com.mycompany", "userstandard2", "USER");
  }

  private void checkCollaboratorRoles(String namespace, String user, String ... roles) throws Exception {
    repositoryServer.perform(get("/api/v1/namespaces/" + namespace + "/collaborators").with(userAdmin))
      .andDo(handler -> {
        Type listType = new TypeToken<ArrayList<Collaborator>>(){}.getType();
        Collection<Collaborator> collabs = new Gson().fromJson(handler.getResponse().getContentAsString(), listType);
        collabs.forEach(collab -> {
          if (collab.getUserId().equals(user)) {
            for(String role : roles) {
              assertTrue(collab.getRoles().contains(role));
            }
            assertTrue(collab.getRoles().size() == roles.length);
          }
        });
      })
      .andExpect(status().isOk());
  }

  private void updateCollaborator(String namespace, Collaborator collaborator) throws Exception {
    repositoryServer.perform(
       put("/api/v1/namespaces/" + namespace + "/collaborators/" + collaborator.getUserId())
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userAdmin))
      .andExpect(status().isOk());
  }
  
  @Test
  public void updateCollaboratorAddTechnicalUser() throws Exception {
    assertFalse(accountService.exists("my-technical-user"));
    
    Collaborator collaborator = new Collaborator("my-technical-user", AuthenticationProvider.BOSCH_IOT_SUITE_AUTH.name(), Lists.newArrayList("USER", "MODEL_CREATOR"));
    updateCollaborator("com.mycompany", collaborator);
    
    assertTrue(accountService.exists("my-technical-user"));
    
    checkCollaboratorRoles("com.mycompany", "my-technical-user", "USER", "MODEL_CREATOR");
    
    collaborator = new Collaborator("my-technical-user", AuthenticationProvider.BOSCH_IOT_SUITE_AUTH.name(), Lists.newArrayList("USER"));
    updateCollaborator("com.mycompany", collaborator);
    
    checkCollaboratorRoles("com.mycompany", "my-technical-user", "USER");
  }
  
  @Test
  public void updateCollaboratorNonAdmin() throws Exception {
    Collaborator collaborator = new Collaborator("userstandard2", AuthenticationProvider.GITHUB.name(), Lists.newArrayList("USER", "MODEL_CREATOR"));
    repositoryServer.perform(
       put("/api/v1/namespaces/com.mycompany/collaborators/userstandard2")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userStandard))
      .andExpect(status().isForbidden());
  }
  
  @Test
  public void updateCollaboratorUnknownProvider() throws Exception {
    Collaborator collaborator = new Collaborator("userstandard2", "unknownProvider", Lists.newArrayList("USER", "MODEL_CREATOR"));
    repositoryServer.perform(
       put("/api/v1/namespaces/com.mycompany/collaborators/userstandard2")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userAdmin))
      .andExpect(status().isBadRequest());
  }
  
  @Test
  public void updateCollaboratorUserDoesntExist() throws Exception {
    Collaborator collaborator = new Collaborator("unknownUser", AuthenticationProvider.GITHUB.name(), Lists.newArrayList("USER", "MODEL_CREATOR"));
    repositoryServer.perform(
       put("/api/v1/namespaces/com.mycompany/collaborators/unknownUser")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userAdmin))
      .andExpect(status().isBadRequest());
  }
  
  @Test
  public void updateCollaboratorTenantDoesntExist() throws Exception {
    Collaborator collaborator = new Collaborator("userstandard2", AuthenticationProvider.GITHUB.name(), Lists.newArrayList("USER", "MODEL_CREATOR"));
    repositoryServer.perform(
       put("/api/v1/namespaces/com.unknowntenant/collaborators/userstandard2")
         .content(new Gson().toJson(collaborator))
         .contentType(MediaType.APPLICATION_JSON)
         .with(userAdmin))
      .andExpect(status().isBadRequest());
  }
}
