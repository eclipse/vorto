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
package org.eclipse.vorto.repository.web.api.v1.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.domain.Comment;

public class CommentDTO extends Comment {

  public CommentDTO() {}

  @JsonProperty("canDelete")
  private boolean canDelete;

  @JsonProperty("canDelete")
  public boolean isCanDelete() {
    return canDelete;
  }

  @JsonProperty("canDelete")
  public void setCanDelete(boolean canDelete) {
    this.canDelete = canDelete;
  }

  public static CommentDTO with(ICommentService service, String username, Comment comment) {
    CommentDTO dto = new CommentDTO();
    dto.setAuthor(comment.getAuthor());
    dto.setContent(comment.getContent());
    dto.setDateAsString(comment.getDate());
    dto.setId(comment.getId());
    dto.setModelId(comment.getModelId());
    dto.setCanDelete(service.canDelete(username, comment));
    return dto;
  }

}
