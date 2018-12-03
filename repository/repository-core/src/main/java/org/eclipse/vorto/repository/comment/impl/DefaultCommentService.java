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
package org.eclipse.vorto.repository.comment.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.jcr.PathNotFoundException;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.comment.Comment;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.CommentReplyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class DefaultCommentService implements ICommentService {

  @Autowired
  private IModelRepository modelRepository;

  @Autowired
  private INotificationService notificationService;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private IUserAccountService accountService;

  public void createComment(Comment comment) {

    final ModelId id = ModelId.fromPrettyFormat(comment.getModelId());

    if (modelRepository.getById(id) != null) {
      comment.setAuthor(comment.getAuthor());
      comment.setModelId(id.getPrettyFormat());
      comment.setDate(new Date());
      commentRepository.save(comment);

      notifyAllCommentAuthors(comment);

    } else {
      throw new ModelNotFoundException("Model not found", new PathNotFoundException());
    }
  }

  private void notifyAllCommentAuthors(Comment comment) {
    Set<String> recipients = new HashSet<>();
    // Always add the model owner as the recipient of a comment reply
    ModelInfo model = modelRepository.getById(ModelId.fromPrettyFormat(comment.getModelId()));
    recipients.add(model.getAuthor());

    List<Comment> existingComments = this.commentRepository.findByModelId(comment.getModelId());
    for (Comment c : existingComments) {
      recipients.add(c.getAuthor());
    }

    recipients.stream().forEach(recipient -> notificationService.sendNotification(
        new CommentReplyMessage(accountService.getUser(recipient), model, comment.getContent())));
  }

  public List<Comment> getCommentsforModelId(ModelId modelId) {
    return commentRepository.findByModelId(modelId.getPrettyFormat());
  }

  public List<Comment> getCommentsByAuthor(String author) {
    return commentRepository.findByAuthor(author);
  }

  @Override
  public void saveComment(Comment comment) {
    this.commentRepository.save(comment);
  }

  public IModelRepository getModelRepository() {
    return modelRepository;
  }

  public void setModelRepository(IModelRepository modelRepository) {
    this.modelRepository = modelRepository;
  }

  public CommentRepository getCommentRepository() {
    return commentRepository;
  }

  public void setCommentRepository(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
  }

  public INotificationService getNotificationService() {
    return notificationService;
  }

  public void setNotificationService(INotificationService notificationService) {
    this.notificationService = notificationService;
  }



}
