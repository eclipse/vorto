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

import java.util.List;
import java.util.Optional;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.comment.Comment;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.upgrade.AbstractUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTaskCondition;
import org.eclipse.vorto.repository.upgrade.UpgradeProblem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class GdprUpgradeTask extends AbstractUpgradeTask implements IUpgradeTask {

	private static final Logger logger = LoggerFactory.getLogger(GdprUpgradeTask.class);
	
	@Value("${server.upgrade.gdpr:false}")
	private boolean shouldExecuteGdprUpgradeTask;
	
	@Autowired
	private ICommentService commentService;
	
	private IUpgradeTaskCondition upgradeTaskCondition = new IUpgradeTaskCondition() {
		
		@Override
		public boolean shouldExecuteTask() {
			return shouldExecuteGdprUpgradeTask;
		}
	};
	
	public GdprUpgradeTask(@Autowired IModelRepository repository, @Autowired ICommentService commentService) {
		super(repository);
		this.commentService = commentService;
	}
	
	@Override
	public void doUpgrade() throws UpgradeProblem {
		List<ModelInfo> modelInfos = getModelRepository().search(null);
		for(ModelInfo modelInfo : modelInfos) {
			if (isNotEmptyAndNotHashed(modelInfo.getAuthor())) {
				logger.info("Upgrading " + modelInfo.toString() + " to comply to GDPR.");
				modelInfo.setAuthor(UserContext.user(modelInfo.getAuthor()).getHashedUsername());
				getModelRepository().updateMeta(modelInfo);
			}
			upgradeComments(commentService.getCommentsforModelId(modelInfo.getId()));
		}
	}
	
	private void upgradeComments(List<Comment> commentsforModelId) {
		commentsforModelId.stream().forEach(comment -> {
			if (isNotEmptyAndNotHashed(comment.getAuthor())) {
				logger.info("Upgrading comment " + comment.toString() + " to comply to GDPR.");
				comment.setAuthor(UserContext.user(comment.getAuthor()).getHashedUsername());
				commentService.saveComment(comment);
			}
		});
	}

	private boolean isNotEmptyAndNotHashed(String author) {
		return !Strings.nullToEmpty(author).trim().isEmpty() && author.length() < 64;
	}
	
	public Optional<IUpgradeTaskCondition> condition() {
		return Optional.of(upgradeTaskCondition);
	}
	
	public IUpgradeTaskCondition getUpgradeTaskCondition() {
		return upgradeTaskCondition;
	}

	public void setUpgradeTaskCondition(IUpgradeTaskCondition upgradeTaskCondition) {
		this.upgradeTaskCondition = upgradeTaskCondition;
	}

	@Override
	public String getShortDescription() {
		return "Task for hashing the model authors in compliance with GDPR.";
	}
}

