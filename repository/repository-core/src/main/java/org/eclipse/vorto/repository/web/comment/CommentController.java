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
package org.eclipse.vorto.repository.web.comment;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Comment;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController
@RequestMapping(value = "/rest/{tenantId}/comments")
public class CommentController {

  @Autowired
  private ICommentService commentService;

  @RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}",
      produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public List<Comment> getCommentsforModelId(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {
    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    return commentService.getCommentsforModelId(modelID).stream()
        .map(comment -> ModelDtoFactory.createDto(comment,
            UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName(), tenantId)))
        .collect(Collectors.toList());
  }

  @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Void> addCommentToModel(@RequestBody @Valid Comment comment)
      throws Exception {
    commentService.createComment(comment);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

}
