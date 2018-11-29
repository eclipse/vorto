/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.upgrade.impl;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.comment.Comment;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.upgrade.UpgradeProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class CommentAuthorUnhashUpgradeTask extends AbstractUserUpgradeTask {

  @Autowired
  private ICommentService commentService;

  private static final Logger logger = LoggerFactory.getLogger(ModelAuthorUnhashUpgradeTask.class);

  @Override
  public void doUpgrade(User user, Supplier<Object> upgradeContext) throws UpgradeProblem {
    Optional<String> emailPrefix = getEmailPrefix((OAuth2Authentication) upgradeContext.get());

    try {
      if (emailPrefix.isPresent()) {
        updateModelsFor(UserContext.user(emailPrefix.get()), user);
      }

      updateModelsFor(UserContext.user(user.getUsername()), user);
    } catch (Exception e) {
      logger.error("error while updating user " + user.getUsername(), e);
    }

    logger.info("Finished updating comments for '{}' with emailPrefix '{}'", user.getUsername(),
        emailPrefix);
  }

  private void updateModelsFor(IUserContext userContext, User user) {
    Collection<Comment> comments =
        commentService.getCommentsByAuthor(userContext.getHashedUsername());
    for (Comment comment : comments) {
      comment.setAuthor(user.getUsername());
      commentService.saveComment(comment);
      logger.info("Setting Comment '{}' to author '{}'", comment.getContent(), user.getUsername());
    }
  }

  @Override
  public String getShortDescription() {
    return "Unhash the author of Comments";
  }

  public void setCommentService(ICommentService commentService) {
    this.commentService = commentService;
  }
}
