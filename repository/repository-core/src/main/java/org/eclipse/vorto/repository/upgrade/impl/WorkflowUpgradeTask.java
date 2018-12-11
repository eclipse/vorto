/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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

import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.upgrade.AbstractUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTaskCondition;
import org.eclipse.vorto.repository.upgrade.UpgradeProblem;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WorkflowUpgradeTask extends AbstractUpgradeTask implements IUpgradeTask {

  private static final Logger logger = LoggerFactory.getLogger(WorkflowUpgradeTask.class);

  @Value("${server.upgrade.workflow:false}")
  private boolean shouldUpgrade;

  private IUpgradeTaskCondition upgradeTaskCondition = new IUpgradeTaskCondition() {

    @Override
    public boolean shouldExecuteTask() {
      return shouldUpgrade;
    }
  };

  @Autowired
  private IWorkflowService workflowService;

  public WorkflowUpgradeTask(@Autowired IModelRepository repository,
      @Autowired IWorkflowService workflowService) {
    super(repository);
    this.workflowService = workflowService;
  }

  @Override
  public void doUpgrade() throws UpgradeProblem {
    List<ModelInfo> modelInfos = getModelRepository().search("*");
    for (ModelInfo modelInfo : modelInfos) {
      if (modelInfo.getState() == null || modelInfo.getState().equals("")) {
        logger.info("Upgrading " + modelInfo.toString() + " for workflow state management.");
        try {
          workflowService.start(modelInfo.getId());
        } catch (WorkflowException e) {
          throw new UpgradeProblem("Upgrade failed because workflow cannot be started ", e);
        }
      }
    }
  }

  public Optional<IUpgradeTaskCondition> condition() {
    return Optional.of(upgradeTaskCondition);
  }

  @Override
  public String getShortDescription() {
    return "Task for setting model states to be controlled by the workflow management.";
  }

  public IUpgradeTaskCondition getUpgradeTaskCondition() {
    return upgradeTaskCondition;
  }

  public void setUpgradeTaskCondition(IUpgradeTaskCondition upgradeTaskCondition) {
    this.upgradeTaskCondition = upgradeTaskCondition;
  }
}
