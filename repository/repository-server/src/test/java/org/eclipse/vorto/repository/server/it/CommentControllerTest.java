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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.comment.impl.CommentRepository;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.server.it.TestModel.TestModelBuilder;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.CommentDTO;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class CommentControllerTest extends IntegrationTestBase {

  @Autowired
  private CommentRepository commentRepository;

  /**
   * This performs the following:
   * <ol>
   *   <li>
   *     Creates a test namespace and model as {@literal userModelCreator}
   *   </li>
   *   <li>
   *     Assigns {@literal userModelCreator} as {@literal namespace_admin}
   *   </li>
   *   <li>
   *     Assigns {@literal userModelCreator2} as {@literal model_viewer}
   *   </li>
   *   <li>
   *     Creates a comment as {@literal userModelCreator}
   *   </li>
   *   <li>
   *     Creates two comments as {@literal userModelCreator2}
   *   </li>
   *   <li>
   *     Deletes {@literal userModelCreator}'s comment as {@literal userModelCreator2} (failure)
   *   </li>
   *   <li>
   *     Deletes {@literal userModelCreator2}'s first comment as {@literal userModelCreator2}
   *     (success)
   *   </li>
   *   <li>
   *     Deletes remaining {@literal userModelCreator2}'s comment as {@literal userModelCreator}
   *     (success)
   *   </li>
   *   <li>
   *     Deletes {@literal userModelCreator}'s comment as the sysadmin user (success)
   *   </li>
   * </ol>
   *
   * @throws Exception
   */
  @Test
  public void testDeleteComment() throws Exception {
    // creates random namespace and test model
    TestModel model = TestModelBuilder.aTestModel().build();
    model.createModel(repositoryServer, userModelCreator);

    // assigns userModelCreator with namespace admin role on namespace
    // (this is not done automatically by TestModel#createModel)
    Collaborator userModelCreatorCollaborator = new Collaborator();
    userModelCreatorCollaborator.setUserId(USER_MODEL_CREATOR_NAME);
    userModelCreatorCollaborator.setAuthenticationProviderId(GITHUB);
    Set<String> userModelCreatorCollaboratorRoles = new HashSet<>();
    userModelCreatorCollaboratorRoles.add("namespace_admin");
    userModelCreatorCollaborator.setRoles(userModelCreatorCollaboratorRoles);

    addCollaboratorToNamespace(model.getNamespace(), userModelCreatorCollaborator);

    // assigns other user with read/comment permission on namespace
    Collaborator userModelCreator2Collaborator = new Collaborator();
    userModelCreator2Collaborator.setUserId(USER_MODEL_CREATOR_NAME_2);
    userModelCreator2Collaborator.setAuthenticationProviderId(GITHUB);
    Set<String> userModelCreatorCollaborator2Roles = new HashSet<>();
    userModelCreatorCollaborator2Roles.add("model_viewer");
    userModelCreator2Collaborator.setRoles(userModelCreatorCollaborator2Roles);

    addCollaboratorToNamespace(model.getNamespace(), userModelCreator2Collaborator);

    // writes a comment on the model as userModelCreator
    CommentDTO adminComment = new CommentDTO();
    adminComment.setAuthor(UserDto.of(USER_MODEL_CREATOR_NAME, GITHUB));
    adminComment.setContent("A comment from userModelCreator");
    adminComment.setDate(ICommentService.DATE_FORMAT.format(new Date()));
    adminComment.setModelId(model.getId().getPrettyFormat());
    repositoryServer
        .perform(
            post("/rest/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminComment))
                .with(userModelCreator)
        )
        .andExpect(status().isCreated());

    // writes two comments on the model as user creator 2
    CommentDTO userComment0 = new CommentDTO();
    userComment0.setAuthor(UserDto.of(USER_MODEL_CREATOR_NAME_2, GITHUB));
    userComment0.setContent("First comment from userModelCreator2");
    userComment0.setDate(ICommentService.DATE_FORMAT.format(new Date()));
    userComment0.setModelId(model.getId().getPrettyFormat());
    repositoryServer
        .perform(
            post("/rest/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userComment0))
                .with(userModelCreator2)
        )
        .andExpect(status().isCreated());
    CommentDTO userComment1 = new CommentDTO();
    userComment1.setAuthor(UserDto.of(USER_MODEL_CREATOR_NAME_2, GITHUB));
    userComment1.setContent("Second comment from userModelCreator2");
    userComment1.setDate(ICommentService.DATE_FORMAT.format(new Date()));
    userComment1.setModelId(model.getId().getPrettyFormat());
    repositoryServer
        .perform(
            post("/rest/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userComment1))
                .with(userModelCreator2)
        )
        .andExpect(status().isCreated());

    // given the order of creation, we know the comments' IDs already
    // tries to delete userModelCreator's comment as creator2 - fail
    repositoryServer
        .perform(
            delete(String.format("/rest/comments/%s", 1))
                .with(userModelCreator2)
        )
        .andExpect(status().isForbidden());
    // tries to delete first creator2's comment as creator2 - succeed
    repositoryServer
        .perform(
            delete(String.format("/rest/comments/%s", 2))
                .with(userModelCreator2)
        )
        .andExpect(status().isNoContent());
    // tries to delete 1 userModelCreator2 comment as userModelCreator (who is NS admin here) - succeed
    repositoryServer
        .perform(
            delete(String.format("/rest/comments/%s", 3))
                .with(userModelCreator)
        )
        .andExpect(status().isNoContent());
    // tries to delete first comment from userModelCreator (who is NS admin), as sysadmin - succeed
    repositoryServer
        .perform(
            delete(String.format("/rest/comments/%s", 1))
                .with(userSysadmin)
        )
        .andExpect(status().isNoContent());
  }

  /**
   * This performs the following:
   * <ol>
   *   <li>
   *    Creates a test namespace and model as {@literal userModelCreator}
   *   </li>
   *   <li>
   *    Creates a test namespace and model as {@literal userModelCreator2}
   *   </li>
   *   <li>
   *    Assigns {@literal userModelCreator} as {@literal namespace_admin} in their own namespace
   *   </li>
   *   <li>
   *    Assigns {@literal userModelCreator2} as {@literal model_viewer} in
   *    {@literal userModelCreator}'s namespace
   *   </li>
   *   <li>
   *    Assigns {@literal userModelCreator2} as {@literal namespace_admin} in their own namespace
   *   </li>
   *   <li>
   *     Creates a comment as {@literal userModelCreator2} in {@literal userModelCreator}'s namespace
   *   </li>
   *   <li>
   *     Creates a comment as {@literal userModelCreator2} in their own namespace
   *   </li>
   *   <li>
   *     Removes {@literal userModelCreator2} from the namespace as {@literal userModelCreator}
   *   </li>
   *   <li>
   *     Fetches the comments for the model's ID as {@literal userModelCreator}
   *   </li>
   *   <li>
   *     Verifies there is one comment on {@literal userModelCreator}'s model and the author is now
   *     anonymous (because {@literal userModelCreator2} has no roles anymore on the namespace)
   *   </li>
   *   <li>
   *     Verifies there is one comment on {@literal userModelCreator2}'s model and the comment has
   *     <b>not</b> been anonymized, since {@literal userModelCreator2} still has roles in their
   *     own namespace.
   *   </li>
   * </ol>
   *
   * @throws Exception
   */
  @Test
  public void testAnonymizeComment() throws Exception {
    // creates random namespace and test model
    TestModel userCreatorModel = TestModelBuilder.aTestModel().build();
    userCreatorModel.createModel(repositoryServer, userModelCreator);

    // creates specific namespace (to avoid overlapping with base namespace) and test model
    String userCreator2Namespace = "vorto.private.usercreator2";
    TestModel userCreator2Model = new TestModelBuilder()
        .withNamespace(userCreator2Namespace).build();
    createNamespaceSuccessfully(userCreator2Namespace, userModelCreator2);
    userCreator2Model.createModel(repositoryServer, userModelCreator2);

    // assigns userModelCreator with namespace admin role on namespace
    // (this is not done automatically by TestModel#createModel)
    Collaborator userModelCreatorCollaborator = new Collaborator();
    userModelCreatorCollaborator.setUserId(USER_MODEL_CREATOR_NAME);
    userModelCreatorCollaborator.setAuthenticationProviderId(GITHUB);
    Set<String> userModelCreatorCollaboratorRoles = new HashSet<>();
    userModelCreatorCollaboratorRoles.add("namespace_admin");
    userModelCreatorCollaborator.setRoles(userModelCreatorCollaboratorRoles);

    addCollaboratorToNamespace(userCreatorModel.getNamespace(), userModelCreatorCollaborator);

    // assigns other user with read/comment permission on namespace
    Collaborator userModelCreator2Collaborator = new Collaborator();
    userModelCreator2Collaborator.setUserId(USER_MODEL_CREATOR_NAME_2);
    userModelCreator2Collaborator.setAuthenticationProviderId(GITHUB);
    Set<String> userModelCreator2CollaboratorRoles = new HashSet<>();
    userModelCreator2CollaboratorRoles.add("model_viewer");
    userModelCreator2Collaborator.setRoles(userModelCreator2CollaboratorRoles);

    addCollaboratorToNamespace(userCreatorModel.getNamespace(), userModelCreator2Collaborator);

    // assigns other user with read/comment permission on namespace
    Collaborator userModelCreator2CollaboratorAdmin = new Collaborator();
    userModelCreator2CollaboratorAdmin.setUserId(USER_MODEL_CREATOR_NAME_2);
    userModelCreator2CollaboratorAdmin.setAuthenticationProviderId(GITHUB);
    Set<String> userModelCreator2CollaboratorAdminRoles = new HashSet<>();
    userModelCreator2CollaboratorAdminRoles.add("namespace_admin");
    userModelCreator2CollaboratorAdmin.setRoles(userModelCreator2CollaboratorAdminRoles);

    addCollaboratorToNamespace(userCreator2Model.getNamespace(),
        userModelCreator2CollaboratorAdmin);

    // writes a comment on userCreator's model as user creator 2
    CommentDTO userComment0 = new CommentDTO();
    userComment0.setAuthor(UserDto.of(USER_MODEL_CREATOR_NAME_2, GITHUB));
    userComment0.setContent("A comment from userModelCreator2");
    userComment0.setDate(ICommentService.DATE_FORMAT.format(new Date()));
    userComment0.setModelId(userCreatorModel.getId().getPrettyFormat());
    repositoryServer
        .perform(
            post("/rest/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userComment0))
                .with(userModelCreator2)
        )
        .andExpect(status().isCreated());

    // writes a comment on userCreator2 model as user creator 2
    CommentDTO userComment1 = new CommentDTO();
    userComment1.setAuthor(UserDto.of(USER_MODEL_CREATOR_NAME_2, GITHUB));
    userComment1.setContent("A comment from userModelCreator2 on my own model");
    userComment1.setDate(ICommentService.DATE_FORMAT.format(new Date()));
    userComment1.setModelId(userCreator2Model.getId().getPrettyFormat());
    repositoryServer
        .perform(
            post("/rest/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userComment1))
                .with(userModelCreator2)
        )
        .andExpect(status().isCreated());

    // removes userModelCreator2 from namespace as userModelCreator
    repositoryServer
        .perform(
            delete(String.format("/rest/namespaces/%s/users", userCreatorModel.getNamespace()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserDto.of(USER_MODEL_CREATOR_NAME_2, GITHUB)))
                .with(userModelCreator)
        )
        .andExpect(status().isOk());

    // fetches and tests comments for userCreator's model ID
    repositoryServer
        .perform(
            get(String.format("/rest/comments/%s", userCreatorModel.getId().getPrettyFormat()))
                .with(userModelCreator)
        )
        .andExpect(
            jsonPath("$").isArray()
        )
        .andExpect(
            jsonPath("$", hasSize(1))
        )
        .andExpect(
            jsonPath("$[0].author.username").value(User.USER_ANONYMOUS)
        );

    // fetches and tests comments for userCreator2's model ID
    repositoryServer
        .perform(
            get(String.format("/rest/comments/%s", userCreator2Model.getId().getPrettyFormat()))
                .with(userModelCreator2)
        )
        .andExpect(
            jsonPath("$").isArray()
        )
        .andExpect(
            jsonPath("$", hasSize(1))
        )
        .andExpect(
            jsonPath("$[0].author.username").value(USER_MODEL_CREATOR_NAME_2)
        );

  }

  /**
   * Verifies that a user with no access to a namespace can neither write, nor fetch comments as
   * long as the model in question is not public.
   *
   * @throws Exception
   */
  @Test
  public void testCommentsForbidden() throws Exception {
    // creates random namespace and test model
    TestModel model = TestModelBuilder.aTestModel().build();
    model.createModel(repositoryServer, userModelCreator);

    // assigns userModelCreator with namespace admin role on namespace
    // (this is not done automatically by TestModel#createModel)
    Collaborator userModelCreatorCollaborator = new Collaborator();
    userModelCreatorCollaborator.setUserId(USER_MODEL_CREATOR_NAME);
    userModelCreatorCollaborator.setAuthenticationProviderId(GITHUB);
    Set<String> userModelCreatorCollaboratorRoles = new HashSet<>();
    userModelCreatorCollaboratorRoles.add("namespace_admin");
    userModelCreatorCollaborator.setRoles(userModelCreatorCollaboratorRoles);

    addCollaboratorToNamespace(model.getNamespace(), userModelCreatorCollaborator);

    // writes a comment on the model as user creator 2 - forbidden
    CommentDTO userComment0 = new CommentDTO();
    userComment0.setAuthor(UserDto.of(USER_MODEL_CREATOR_NAME_2, GITHUB));
    userComment0.setContent("A comment from userModelCreator2");
    userComment0.setDate(ICommentService.DATE_FORMAT.format(new Date()));
    userComment0.setModelId(model.getId().getPrettyFormat());
    repositoryServer
        .perform(
            post("/rest/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userComment0))
                .with(userModelCreator2)
        )
        .andExpect(status().isForbidden());

    // fetches comments for that model as userModelCreator2 - forbidden
    repositoryServer
        .perform(
            get(String.format("/rest/comments/%s", model.getId().getPrettyFormat()))
                .with(userModelCreator2)
        )
        .andExpect(
            status().isForbidden()
        );
  }

  /**
   * Tests that comments on a public model are visible regardless of other authorization rules, as
   * long as the requesting user is logged on.
   *
   * @throws Exception
   */
  @Test
  public void testCanViewCommentsOnPublicModel() throws Exception {
    // creates random namespace and test model
    TestModel model = TestModelBuilder.aTestModel().build();
    model.createModel(repositoryServer, userModelCreator);

    // assigns userModelCreator with namespace admin role on namespace
    // (this is not done automatically by TestModel#createModel)
    Collaborator userModelCreatorCollaborator = new Collaborator();
    userModelCreatorCollaborator.setUserId(USER_MODEL_CREATOR_NAME);
    userModelCreatorCollaborator.setAuthenticationProviderId(GITHUB);
    Set<String> userModelCreatorCollaboratorRoles = new HashSet<>();
    userModelCreatorCollaboratorRoles.add("namespace_admin");
    userModelCreatorCollaborator.setRoles(userModelCreatorCollaboratorRoles);

    addCollaboratorToNamespace(model.getNamespace(), userModelCreatorCollaborator);

    // writes a comment on the model as user creator
    CommentDTO userComment0 = new CommentDTO();
    userComment0.setAuthor(UserDto.of(USER_MODEL_CREATOR_NAME, GITHUB));
    userComment0.setContent("A comment from userModelCreator");
    userComment0.setDate(ICommentService.DATE_FORMAT.format(new Date()));
    userComment0.setModelId(model.getId().getPrettyFormat());
    repositoryServer
        .perform(
            post("/rest/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userComment0))
                .with(userModelCreator)
        )
        .andExpect(status().isCreated());

    // releases, approves, then publishes the model as sysadmin (shortcut to pre-authorization in
    // WorkflowController)
    repositoryServer
        .perform(
            put(String.format("/rest/workflows/%s/actions/Release", model.getPrettyName()))
                .with(userSysadmin)
        )
        .andExpect(
            status().isOk()
        );

    // releases, approves, then publishes the model
    repositoryServer
        .perform(
            put(String.format("/rest/workflows/%s/actions/Approve", model.getPrettyName()))
                .with(userSysadmin)
        )
        .andExpect(
            status().isOk()
        );

    repositoryServer
        .perform(
            post(String.format("/rest/models/%s/makePublic", model.getPrettyName()))
                .with(userSysadmin)
        )
        .andExpect(
            status().isOk()
        );

    // fetches comments for that model ID as user creator 2, with no explicit access to the ns
    repositoryServer
        .perform(
            get(String.format("/rest/comments/%s", model.getId().getPrettyFormat()))
                .with(userModelCreator2)
        )
        .andExpect(
            status().isOk()
        );
  }

  /**
   * Tests that comments on a public model can be written by anybody, as
   * long as the requesting user is logged on.
   *
   * @throws Exception
   */
  @Test
  public void testCanWriteAndDeleteCommentsOnPublicModel() throws Exception {
    // creates random namespace and test model
    TestModel model = TestModelBuilder.aTestModel().build();
    model.createModel(repositoryServer, userModelCreator);

    // assigns userModelCreator with namespace admin role on namespace
    // (this is not done automatically by TestModel#createModel)
    Collaborator userModelCreatorCollaborator = new Collaborator();
    userModelCreatorCollaborator.setUserId(USER_MODEL_CREATOR_NAME);
    userModelCreatorCollaborator.setAuthenticationProviderId(GITHUB);
    Set<String> userModelCreatorCollaboratorRoles = new HashSet<>();
    userModelCreatorCollaboratorRoles.add("namespace_admin");
    userModelCreatorCollaborator.setRoles(userModelCreatorCollaboratorRoles);

    addCollaboratorToNamespace(model.getNamespace(), userModelCreatorCollaborator);

    // releases, approves, then publishes the model as sysadmin (shortcut to pre-authorization in
    // WorkflowController)
    repositoryServer
        .perform(
            put(String.format("/rest/workflows/%s/actions/Release", model.getPrettyName()))
                .with(userSysadmin)
        )
        .andExpect(
            status().isOk()
        );

    // releases, approves, then publishes the model
    repositoryServer
        .perform(
            put(String.format("/rest/workflows/%s/actions/Approve", model.getPrettyName()))
                .with(userSysadmin)
        )
        .andExpect(
            status().isOk()
        );

    repositoryServer
        .perform(
            post(String.format("/rest/models/%s/makePublic", model.getPrettyName()))
                .with(userSysadmin)
        )
        .andExpect(
            status().isOk()
        );

    // writes a comment on the model as user creator2
    CommentDTO userComment0 = new CommentDTO();
    userComment0.setAuthor(UserDto.of(USER_MODEL_CREATOR_NAME_2, GITHUB));
    userComment0.setContent("A comment from userModelCreator2");
    userComment0.setDate(ICommentService.DATE_FORMAT.format(new Date()));
    userComment0.setModelId(model.getId().getPrettyFormat());
    repositoryServer
        .perform(
            post("/rest/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userComment0))
                .with(userModelCreator2)
        )
        .andExpect(status().isCreated());

    // fetches the comment to infer the id
    final AtomicLong idContainer = new AtomicLong();
    repositoryServer
        .perform(
            get(String.format("/rest/comments/%s", model.getId().getPrettyFormat()))
            .with(userModelCreator2)
        )
        .andDo(
            rh -> {
              List<CommentDTO> list = objectMapper
                  .readValue(
                      rh.getResponse().getContentAsString(),
                      new TypeReference<List<CommentDTO>>() {}
                  );
              CommentDTO comment = list
                  .stream()
                  .filter(
                      c -> c.getModelId().equals(model.getId().getPrettyFormat())
                          && c.getAuthor().getUsername().equals(USER_MODEL_CREATOR_NAME_2)
                  )
                  .findFirst()
                  .get();
              idContainer.set(comment.getId());
            }
        );


    // deletes the comment
    repositoryServer
        .perform(
            delete(String.format("/rest/comments/%s", idContainer.get()))
                .with(userModelCreator2)
        )
        .andExpect(
            status().isNoContent()
        );
  }

  /**
   * This ensures that comments on a private namespace on a draft model are visible for sysamdins.
   *
   * @throws Exception
   */
  @Test
  public void testCanViewAnyCommentAsSysadmin() throws Exception {
    // creates private namespace and test model
    String userCreatorNamespace = "vorto.private.usercreator";
    TestModel model = new TestModelBuilder()
        .withNamespace(userCreatorNamespace).build();
    createNamespaceSuccessfully(userCreatorNamespace, userModelCreator);
    model.createModel(repositoryServer, userModelCreator);

    // writes a comment on the model as user creator
    CommentDTO userComment0 = new CommentDTO();
    userComment0.setAuthor(UserDto.of(USER_MODEL_CREATOR_NAME, GITHUB));
    userComment0.setContent("A comment from userModelCreator");
    userComment0.setDate(ICommentService.DATE_FORMAT.format(new Date()));
    userComment0.setModelId(model.getId().getPrettyFormat());
    repositoryServer
        .perform(
            post("/rest/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userComment0))
                .with(userModelCreator)
        )
        .andExpect(status().isCreated());

    // fetches and tests comments for the model ID, as sysadmin
    repositoryServer
        .perform(
            get(String.format("/rest/comments/%s", model.getId().getPrettyFormat()))
                .with(userSysadmin)
        )
        .andExpect(
            jsonPath("$").isArray()
        )
        .andExpect(
            jsonPath("$", hasSize(1))
        )
        .andExpect(
            jsonPath("$[0].author.username").value(USER_MODEL_CREATOR_NAME)
        );
  }

  @After
  public void after() throws Exception {
    commentRepository.deleteAll();
  }

}
