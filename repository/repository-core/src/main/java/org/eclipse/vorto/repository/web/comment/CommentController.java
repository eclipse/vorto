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
package org.eclipse.vorto.repository.web.comment;

import io.swagger.annotations.ApiParam;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelVisibility;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.comment.impl.DefaultCommentService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.web.api.v1.dto.CommentDTO;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController
@RequestMapping(value = "/rest/comments")
public class CommentController {

  @Autowired
  private ICommentService commentService;

  @Autowired
  private NamespaceService namespaceService;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @Autowired
  private UserRepositoryRoleService userRepositoryRoleService;

  @Autowired
  private ModelRepositoryFactory modelRepositoryFactory;

  private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

  @RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}",
      produces = "application/json")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<List<CommentDTO>> getCommentsforModelId(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    IUserContext context = UserContext.user(SecurityContextHolder.getContext().getAuthentication());

    // this fetches the model and infers whether it is public
    // if so, the rest of the authorization is bypassed
    // can still fail authorization is the model itself is not visible to the user
    try {
      ModelInfo resource = modelRepositoryFactory.getRepositoryByModel(modelID)
          .getByIdWithPlatformMappings(modelID);
      if (resource.getVisibility().equalsIgnoreCase(ModelVisibility.Public.name())) {
        return new ResponseEntity<>(
            commentService.getCommentsforModelId(modelID).stream()
                .map(comment -> CommentDTO.with(commentService, context.getUsername(), comment))
                .collect(Collectors.toList()),
            HttpStatus.OK
        );
      }
    }
    catch (NotAuthorizedException nae) {
      return new ResponseEntity<>(Collections.emptyList(), HttpStatus.FORBIDDEN);
    }

    try {
      if (
          userRepositoryRoleService.isSysadmin(context.getUsername()) ||
          userNamespaceRoleService.hasAnyRole(context.getUsername(), modelID.getNamespace())
      ) {
        return new ResponseEntity<>(
            commentService.getCommentsforModelId(modelID).stream()
            .map(comment -> CommentDTO.with(commentService, context.getUsername(), comment))
            .collect(Collectors.toList()),
            HttpStatus.OK
        );
      }
      else {
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.FORBIDDEN);
      }
    }
    catch (DoesNotExistException dnee) {
      LOGGER.warn(
          String.format("Namespace [%s] does not exist.", modelID.getNamespace())
      );
      return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> addCommentToModel(@RequestBody CommentDTO comment) {

    IUserContext context = UserContext.user(SecurityContextHolder.getContext().getAuthentication());

    try {
      commentService.createComment(context.getUsername(), comment);
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

  }

  /**
   * For details on authorization, see {@link DefaultCommentService#deleteComment(String, long)}
   *
   * @param id
   * @return
   */
  @DeleteMapping("/{id:\\d+}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteComment(@PathVariable long id) {
    IUserContext context = UserContext.user(SecurityContextHolder.getContext().getAuthentication());
    try {
      if (commentService.deleteComment(context.getUsername(), id)) {
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
      } else {
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
      }
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
  }

}
