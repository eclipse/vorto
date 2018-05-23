/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.upgrade.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.vorto.repository.upgrade.IUpgradeService;
import org.eclipse.vorto.repository.upgrade.IUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTask.UpgradeProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultUpgradeService implements IUpgradeService {

	@Autowired
	private List<IUpgradeTask> tasks;
	
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
				} catch (UpgradeProblem problem ) {
					logger.error("Problem executing upgrade task", problem);
				}
			} else {
				logger.info("NOT Executing task - " + task.getShortDescription() + ". Conditions not met.");
			}
		}
	}
	
	public void addTasks(IUpgradeTask...tasks) {
		this.tasks = Arrays.asList(tasks);
	}

}
