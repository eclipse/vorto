/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.web.comment;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.comment.Comment;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.core.impl.UserContext;
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
@RequestMapping(value = "/rest/{tenant}/comments")
public class CommentController {

  @Autowired
  private ICommentService commentService;

  @RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}",
      produces = "application/json")
  @PreAuthorize("hasRole('ROLE_USER')")
  public List<Comment> getCommentsforModelId(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId) {
    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    return commentService.getCommentsforModelId(modelID).stream()
        .map(comment -> ModelDtoFactory.createDto(comment,
            UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName())))
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
