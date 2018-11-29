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

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.upgrade.UpgradeProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
public class ModelAuthorUnhashUpgradeTask extends AbstractUserUpgradeTask {

  @Autowired
  private IModelRepository modelRepository;

  @Autowired
  private IUserRepository userRepository;

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


    user.setLastUpdated(new Timestamp(System.currentTimeMillis()));
    userRepository.save(user);

    logger.info("Updating user: {} with emailPrefix: {}", user.getUsername(), emailPrefix);
  }

  private void updateModelsFor(IUserContext userContext, User user) {
    List<ModelInfo> models = modelRepository.search("author:" + userContext.getHashedUsername());
    logger.info("Found {} models with author {} to update.", models.size(),
        userContext.getHashedUsername());

    for (ModelInfo model : models) {
      logger
          .info("Setting the author of " + model.getId().toString() + " to " + user.getUsername());
      model.setAuthor(user.getUsername());
      modelRepository.updateMeta(model);
    }
  }

  @Override
  public String getShortDescription() {
    return "Updating models whose authors are hashed username into CIAM subject IDs.";
  }

  public void setModelRepository(IModelRepository modelRepository) {
    this.modelRepository = modelRepository;
  }

  public void setUserRepository(IUserRepository userRepository) {
    this.userRepository = userRepository;
  }

}
