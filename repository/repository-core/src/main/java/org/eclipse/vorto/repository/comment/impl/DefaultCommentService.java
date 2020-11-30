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
package org.eclipse.vorto.repository.comment.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.jcr.PathNotFoundException;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelVisibility;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory;
import org.eclipse.vorto.repository.domain.Comment;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.CommentReplyMessage;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.ServiceValidationUtil;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.eclipse.vorto.repository.web.api.v1.dto.CommentDTO;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class DefaultCommentService implements ICommentService {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommentService.class);

  private IModelRepositoryFactory modelRepositoryFactory;

  private INotificationService notificationService;

  private CommentRepository commentRepository;

  private DefaultUserAccountService accountService;

  private NamespaceService namespaceService;

  private UserNamespaceRoleService userNamespaceRoleService;

  private UserRepositoryRoleService userRepositoryRoleService;

  public DefaultCommentService(
      @Autowired ModelRepositoryFactory modelRepositoryFactory,
      @Autowired INotificationService notificationService,
      @Autowired CommentRepository commentRepository,
      @Autowired DefaultUserAccountService defaultUserAccountService,
      @Autowired NamespaceService namespaceService,
      @Autowired UserNamespaceRoleService userNamespaceRoleService,
      @Autowired UserRepositoryRoleService userRepositoryRoleService) {
    this.modelRepositoryFactory = modelRepositoryFactory;
    this.notificationService = notificationService;
    this.commentRepository = commentRepository;
    this.accountService = defaultUserAccountService;
    this.namespaceService = namespaceService;
    this.userNamespaceRoleService = userNamespaceRoleService;
    this.userRepositoryRoleService = userRepositoryRoleService;
  }

  @Override
  public void createComment(UserDto user, CommentDTO commentDTO) throws DoesNotExistException, OperationForbiddenException {

    final ModelId id = ModelId.fromPrettyFormat(commentDTO.getModelId());

    if (!canCreate(user, commentDTO)) {
      throw new OperationForbiddenException(
          String.format(
              "User cannot create a comment for model ID [%s]", id.getPrettyFormat()
          )
      );
    }

    Optional<String> workspaceId = namespaceService
        .resolveWorkspaceIdForNamespace(id.getNamespace());
    if (!workspaceId.isPresent()) {
      throw new DoesNotExistException(
          String.format("Namespace [%s] does not exist.", id.getNamespace()));
    }

    IModelRepository modelRepo = modelRepositoryFactory
        .getRepository(workspaceId.get());

    if (modelRepo.exists(id)) {
      saveComment(commentDTO);
      User author = accountService.getUser(commentDTO.getAuthor());
      notifyAllCommentAuthors(author, commentDTO, modelRepo.getById(id));

    } else {
      throw new ModelNotFoundException("Model not found", new PathNotFoundException());
    }
  }

  private void notifyAllCommentAuthors(User author, CommentDTO comment, ModelInfo model) {
    Set<User> recipients = new HashSet<>();

    recipients.add(author);

    List<Comment> existingComments = this.commentRepository.findByModelId(comment.getModelId());
    for (Comment c : existingComments) {
      if (c.getAuthor() != null) {
        recipients.add(c.getAuthor());
      }
    }

    recipients.stream()
        .forEach(recipient -> {
            notificationService.sendNotification(
                new CommentReplyMessage(recipient, model, comment.getContent()));
        });
  }

  public List<Comment> getCommentsforModelId(ModelId modelId) {
    return commentRepository.findByModelId(modelId.getPrettyFormat());
  }

  public List<Comment> getCommentsByAuthor(UserDto author) {
    return commentRepository.findByAuthor(author.getUsername(), author.getAuthenticationProvider());
  }

  @Override
  public void saveComment(CommentDTO comment) throws DoesNotExistException {
    ServiceValidationUtil.validateNulls(
        comment, comment.getAuthor(), comment.getContent(), comment.getDate(), comment.getModelId()
    );
    User author = accountService.getUser(comment.getAuthor());
    if (null == author) {
      throw new DoesNotExistException("Author cannot be resolved to a real user");
    }
    Comment entity = comment.toComment();
    entity.setAuthor(author);
    this.commentRepository.save(entity);
  }

  public IModelRepositoryFactory getModelRepositoryFactory() {
    return modelRepositoryFactory;
  }

  public void setModelRepositoryFactory(IModelRepositoryFactory modelRepositoryFactory) {
    this.modelRepositoryFactory = modelRepositoryFactory;
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

  /**
   * Deletes the given comment, provided that either:
   * <ul>
   *   <li>
   *     The user requesting is the same user who wrote the comment (comparison by username)
   *   </li>
   *   <li>
   *     The user requesting has the {@literal namespace_admin} role on the namespace where the
   *     commented model is stored.
   *   </li>
   * </ul>
   * @param user
   * @param id
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   */
  @Override
  public boolean deleteComment(UserDto user, long id) throws DoesNotExistException {

    Comment comment = commentRepository.findOne(id);

    if (Objects.isNull(comment)) {
      throw new DoesNotExistException(
          String.format(
              "Comment with id [%s] not found", id
          )
      );
    }

    if (canDelete(user, comment)) {
      commentRepository.delete(comment.getId());
      return true;
    }
    else {
      LOGGER.warn("User cannot delete comment with id [%s]", id);
      return false;
    }

  }

  /**
   *
   * @param user
   * @param comment
   * @return
   */
  @Override
  public boolean canDelete(UserDto user, Comment comment) {
    String namespace = ModelId.fromPrettyFormat(comment.getModelId()).getNamespace();

    if (user.is(comment.getAuthor())) {
      return true;
    } else {
      try {
        if (userNamespaceRoleService
            .hasRole(
                user,
                namespace,
                userNamespaceRoleService.namespaceAdminRole().getName()
            )
        ) {
          return true;
        } else {
          return userRepositoryRoleService.isSysadmin(user);
        }
      } catch (DoesNotExistException dnee) {
        return false;
      }
    }
  }

  /**
   *
   * @param user
   * @param comment
   * @return
   */
  @Override
  public boolean canCreate(UserDto user, CommentDTO comment) {
    String namespace = ModelId.fromPrettyFormat(comment.getModelId()).getNamespace();
    try {
      // sysadmin?
      if (userRepositoryRoleService.isSysadmin(user)) {
        return true;
        // has role in namespace?
      } else if (userNamespaceRoleService.hasAnyRole(user, namespace)) {
        return true;
        // is model public?
      } else {
        try {
          ModelId id = ModelId.fromPrettyFormat(comment.getModelId());
          ModelInfo model = modelRepositoryFactory
              .getRepositoryByModel(id)
              .getById(id);
          return model.getVisibility().equalsIgnoreCase(ModelVisibility.Public.name());
        }
        // user cannot access the model - meaning it's not public
        catch (NotAuthorizedException nae) {
          return false;
        }
      }
    } catch (DoesNotExistException dnee) {
      return false;
    }
  }
}
