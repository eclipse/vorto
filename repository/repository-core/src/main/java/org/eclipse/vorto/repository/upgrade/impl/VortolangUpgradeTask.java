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
package org.eclipse.vorto.repository.upgrade.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.ModelRepository;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.upgrade.AbstractUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTaskCondition;
import org.eclipse.vorto.repository.upgrade.UpgradeProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VortolangUpgradeTask extends AbstractUpgradeTask implements IUpgradeTask {

  private static final Logger logger = LoggerFactory.getLogger(VortolangUpgradeTask.class);

  @Value("${server.upgrade.vortolang:false}")
  private boolean shouldUpgrade;

  private IModelRepositoryFactory repositoryFactory;

  private IUpgradeTaskCondition upgradeTaskCondition = new IUpgradeTaskCondition() {
    @Override
    public boolean shouldExecuteTask() {
      return shouldUpgrade;
    }
  };

  public VortolangUpgradeTask(@Autowired ISearchService searchService,
      @Autowired IModelRepositoryFactory repositoryFactory) {
    super(searchService);
    this.repositoryFactory = repositoryFactory;
  }

  @Override
  public void doUpgrade() throws UpgradeProblem {
    setAdminUserContext();

    List<ModelInfo> searchResult = modelSearchService.search("*");

    final String newline = System.getProperty("line.separator");
    for (ModelInfo modelInfo : searchResult) {
      ModelRepository modelRepository =
          (ModelRepository) repositoryFactory.getRepositoryByModel(modelInfo.getId());
      Optional<FileContent> content =
          modelRepository.getFileContent(modelInfo.getId(), Optional.of(modelInfo.getFileName()));
      if (content.isPresent()) {
        String currentModelContent = "";
        try {
          currentModelContent = new String(content.get().getContent(), "utf-8");
        } catch (UnsupportedEncodingException e1) {
          logger.warn("Could not set vortolang field for model "
              + modelInfo.getId().getPrettyFormat() + ".Skipping...", e1);

        }
        if (currentModelContent.contains("vortolang 1.0")) {
          continue;
        }
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("vortolang 1.0");
        contentBuilder.append(newline);
        contentBuilder.append(newline);
        try {
          contentBuilder.append(currentModelContent);
          logger.info("Upgrading " + modelInfo.toString() + " for vortolang attribute....");
          modelRepository.save(modelInfo.getId(), contentBuilder.toString().getBytes(),
              modelInfo.getFileName(), UserContext.user(modelInfo.getAuthor(), modelRepository.getTenantId()), false);
          logger.info("Upgrade of " + modelInfo.toString() + " successful.");
        } catch (Throwable e) {
          logger.warn("Could not set vortolang field for model "
              + modelInfo.getId().getPrettyFormat() + ".Skipping...", e);
        }

      }
    }
  }

  public Optional<IUpgradeTaskCondition> condition() {
    return Optional.of(upgradeTaskCondition);
  }

  @Override
  public String getShortDescription() {
    return "Task for setting the vortolang 1.0 for all released models.";
  }

  public IUpgradeTaskCondition getUpgradeTaskCondition() {
    return upgradeTaskCondition;
  }

  public void setUpgradeTaskCondition(IUpgradeTaskCondition upgradeTaskCondition) {
    this.upgradeTaskCondition = upgradeTaskCondition;
  }
}
