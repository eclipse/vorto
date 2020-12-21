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

import com.google.common.base.Strings;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.domain.Comment;
import org.eclipse.vorto.repository.web.account.dto.UserDto;

public class CommentDTO {

  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm a dd-MM-yyyy");

  private long id;
  private UserDto author;
  private String content;
  private String date;
  private String modelId;
  private boolean canDelete;

  public CommentDTO() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public UserDto getAuthor() {
    return author;
  }

  public void setAuthor(UserDto author) {
    this.author = author;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getModelId() {
    return modelId;
  }

  public void setModelId(String modelId) {
    this.modelId = modelId;
  }

  @JsonProperty("canDelete")
  public boolean isCanDelete() {
    return canDelete;
  }

  @JsonProperty("canDelete")
  public void setCanDelete(boolean canDelete) {
    this.canDelete = canDelete;
  }

  public static CommentDTO with(ICommentService service, UserDto user, Comment comment) {
    CommentDTO dto = new CommentDTO();
    dto.setAuthor(UserDto.fromUser(comment.getAuthor()));
    dto.setContent(comment.getContent());
    dto.setDate(comment.getDate());
    dto.setId(comment.getId());
    dto.setModelId(comment.getModelId());
    dto.setCanDelete(service.canDelete(user, comment));
    return dto;
  }

  @JsonIgnore
  public Comment toComment() {
    Comment comment = new Comment();
    comment.setAuthor(getAuthor().toUser());
    comment.setContent(getContent());
    String date = getDate();
    comment.setDate(Strings.isNullOrEmpty(date) ? DATE_FORMAT.format(new Date()) : date);
    comment.setId(getId());
    comment.setModelId(getModelId());
    return comment;
  }

}
