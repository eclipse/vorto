/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.upgrade.impl;

import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
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
public class TargetPlatformUpgradeTask extends AbstractUpgradeTask implements IUpgradeTask {

  private static final Logger logger = LoggerFactory.getLogger(TargetPlatformUpgradeTask.class);

  @Value("${server.upgrade.targetplatformkey:false}")
  private boolean shouldUpgrade;

  private IModelRepositoryFactory repositoryFactory;

  private IUpgradeTaskCondition upgradeTaskCondition = new IUpgradeTaskCondition() {
    @Override
    public boolean shouldExecuteTask() {
      return shouldUpgrade;
    }
  };

  public TargetPlatformUpgradeTask(@Autowired ISearchService searchService,
      @Autowired IModelRepositoryFactory repositoryFactory) {
    super(searchService);
    this.repositoryFactory = repositoryFactory;
  }

  @Override
  public void doUpgrade() throws UpgradeProblem {
    setAdminUserContext();

    List<ModelInfo> searchResult = modelSearchService.search("type:Mapping");

    for (ModelInfo modelInfo : searchResult) {
      logger.info("Upgrading " + modelInfo.toString() + " for target platform key attribute....");

      ModelRepository modelRepository =
          (ModelRepository) repositoryFactory.getRepositoryByModel(modelInfo.getId());
      ModelResource mappingModel = modelRepository.getEMFResource(modelInfo.getId());
      modelRepository.save(mappingModel, UserContext.user(modelInfo.getAuthor(), modelRepository.getTenantId()));
    }
  }

  public Optional<IUpgradeTaskCondition> condition() {
    return Optional.of(upgradeTaskCondition);
  }

  @Override
  public String getShortDescription() {
    return "Task for setting target platform key for all mapping models.";
  }

  public IUpgradeTaskCondition getUpgradeTaskCondition() {
    return upgradeTaskCondition;
  }

  public void setUpgradeTaskCondition(IUpgradeTaskCondition upgradeTaskCondition) {
    this.upgradeTaskCondition = upgradeTaskCondition;
  }
}
