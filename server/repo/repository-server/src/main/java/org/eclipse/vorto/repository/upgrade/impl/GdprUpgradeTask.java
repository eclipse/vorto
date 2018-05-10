package org.eclipse.vorto.repository.upgrade.impl;

import java.util.List;
import java.util.Optional;

import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.upgrade.AbstractUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTaskCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;

@Component
public class GdprUpgradeTask extends AbstractUpgradeTask implements IUpgradeTask {

	private static final Logger logger = LoggerFactory.getLogger(GdprUpgradeTask.class);
	
	@Autowired
	private GdprUpgradeTaskCondition upgradeTaskCondition;
	
	@Autowired
	public GdprUpgradeTask(IModelRepository repository) {
		super(repository);
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
		}
	}
	
	private boolean isNotEmptyAndNotHashed(String author) {
		return !Strings.nullToEmpty(author).trim().isEmpty() && author.length() < 64;
	}
	
	public Optional<IUpgradeTaskCondition> condition() {
		return Optional.of(upgradeTaskCondition);
	}
	
	public GdprUpgradeTaskCondition getUpgradeTaskCondition() {
		return upgradeTaskCondition;
	}

	public void setUpgradeTaskCondition(GdprUpgradeTaskCondition upgradeTaskCondition) {
		this.upgradeTaskCondition = upgradeTaskCondition;
	}

	@Override
	public String getShortDescription() {
		return "Task for hashing the model authors in compliance with GDPR.";
	}
}

