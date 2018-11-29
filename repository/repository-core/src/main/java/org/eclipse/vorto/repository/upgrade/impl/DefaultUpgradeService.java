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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.upgrade.IUpgradeService;
import org.eclipse.vorto.repository.upgrade.IUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUserUpgradeTask;
import org.eclipse.vorto.repository.upgrade.UpgradeProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultUpgradeService implements IUpgradeService {

  @Autowired
  private List<IUpgradeTask> tasks;

  @Autowired(required = false)
  private List<IUserUpgradeTask> userUpgradeTasks;

  private static final Logger logger = LoggerFactory.getLogger(DefaultUpgradeService.class);

  @Override
  @PostConstruct
  public void installUpgrades() {
    logger.info("Performing upgrade to the Vorto Repository and its content...");
    for (IUpgradeTask task : tasks) {
      if (!task.condition().isPresent() || task.condition().get().shouldExecuteTask()) {
        logger.info("Executing task - " + task.getShortDescription());
        try {
          task.doUpgrade();
        } catch (UpgradeProblem problem) {
          logger.error("Problem executing upgrade task", problem);
        }
      } else {
        logger.info("NOT Executing task - " + task.getShortDescription() + ". Conditions not met.");
      }
    }
  }

  @Override
  public void installUserUpgrade(User user, Supplier<Object> context) {
    Objects.requireNonNull(user, "user must be non-null");

    if (userUpgradeTasks == null || userUpgradeTasks.size() <= 0) {
      logger.info("Not performing user upgrade. No available user upgrade tasks.");
      return;
    }

    logger.info("Performing upgrade for " + user.getUsername() + " on " + new Date().toString());
    userUpgradeTasks.forEach(upgradeTask -> {
      if (!upgradeTask.condition(user, context).isPresent()
          || upgradeTask.condition(user, context).get().shouldExecuteTask()) {
        logger.info("Executing task - " + upgradeTask.getShortDescription());
        try {
          upgradeTask.doUpgrade(user, context);
        } catch (UpgradeProblem problem) {
          logger.error("Problem executing upgrade task for user " + user.getUsername(), problem);
        }
      } else {
        logger.info("NOT Executing task - " + upgradeTask.getShortDescription() + " for user "
            + user.getUsername() + ". Conditions not met.");
      }
    });
  }

  public void addTasks(IUpgradeTask... tasks) {
    this.tasks = Arrays.asList(tasks);
  }

  public void addUserUpgradeTask(IUserUpgradeTask... tasks) {
    this.userUpgradeTasks = Arrays.asList(tasks);
  }
}
