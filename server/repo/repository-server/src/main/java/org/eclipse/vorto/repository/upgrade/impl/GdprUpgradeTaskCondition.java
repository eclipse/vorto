package org.eclipse.vorto.repository.upgrade.impl;

import org.eclipse.vorto.repository.upgrade.IUpgradeTaskCondition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GdprUpgradeTaskCondition implements IUpgradeTaskCondition {

	@Value("${server.upgrade.gdpr:false}")
	private boolean shouldExecuteGdprUpgradeTask;
	
	@Override
	public boolean shouldExecuteTask() {
		return shouldExecuteGdprUpgradeTask;
	}

}
