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
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.comment.impl.DefaultCommentService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Comment;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

  @RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}",
      produces = "application/json")
  @PreAuthorize("hasAuthority('model_viewer')")
  public List<Comment> getCommentsforModelId(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {
    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    Optional<String> maybeWorkspaceId = namespaceService
        .resolveWorkspaceIdForNamespace(modelID.getNamespace());
    if (!maybeWorkspaceId.isPresent()) {
      LOGGER.error(
          String.format("Namespace [%s] does not exist.", modelID.getNamespace()));
      return Collections.emptyList();
    }

    return commentService.getCommentsforModelId(modelID).stream()
        .map(comment -> ModelDtoFactory.createDto(comment,
            UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName(),
                maybeWorkspaceId.get())))
        .collect(Collectors.toList());
  }

  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  @PreAuthorize("hasAuthority('model_viewer')")
  public ResponseEntity<Void> addCommentToModel(@RequestBody @Valid Comment comment)
      throws Exception {
    commentService.createComment(comment);
    return new ResponseEntity<>(HttpStatus.CREATED);
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
      commentService.deleteComment(context.getUsername(), id);
      return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
  }

}
