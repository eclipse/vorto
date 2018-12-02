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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.eclipse.vorto.repository.upgrade.AbstractUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTask;
import org.eclipse.vorto.repository.upgrade.IUpgradeTaskCondition;
import org.eclipse.vorto.repository.upgrade.UpgradeProblem;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class PermissionsUpgradeTask extends AbstractUpgradeTask implements IUpgradeTask {

	private static final Logger logger = LoggerFactory.getLogger(PermissionsUpgradeTask.class);
	
	@Value("${server.upgrade.permissions:false}")
	private boolean shouldUpgrade;

	private IUpgradeTaskCondition upgradeTaskCondition = new IUpgradeTaskCondition() {
		
		@Override
		public boolean shouldExecuteTask() {
			return shouldUpgrade;
		}
	};
	
	public PermissionsUpgradeTask(@Autowired IModelRepository repository) {
		super(repository);
	}

	@Override
	public void doUpgrade() throws UpgradeProblem {
		Authentication dummyAuthentication = createAuthentication() ;
		SecurityContextHolder.getContext().setAuthentication(dummyAuthentication);
		
		List<ModelInfo> modelInfos = getModelRepository().search("*");
		for(ModelInfo modelInfo : modelInfos) {
			if (modelInfo.getState() != null && modelInfo.getState().equalsIgnoreCase(SimpleWorkflowModel.STATE_DRAFT.getName())) {
				logger.info("Setting permissions for model " + modelInfo.toString());
				
				modelRepository.addModelPolicy(modelInfo.getId(), UserContext.user(modelInfo.getAuthor()));

			}
		}
	}
	
	private Authentication createAuthentication() {
		return new Authentication() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public String getName() {
				return "admin";
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return SpringUserUtils.toAuthorityList(new HashSet<>(Arrays.asList(Role.ADMIN)));
			}

			@Override
			public Object getCredentials() {
				return null;
			}

			@Override
			public Object getDetails() {
				return null;
			}

			@Override
			public Object getPrincipal() {
				return "admin";
			}

			@Override
			public boolean isAuthenticated() {
				return false;
			}

			@Override
			public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
				
			}
			
		};
	}

	public Optional<IUpgradeTaskCondition> condition() {
		return Optional.of(upgradeTaskCondition);
	}

	@Override
	public String getShortDescription() {
		return "Task for setting only-user permissions for models which are in draft state";
	}
	
	public IUpgradeTaskCondition getUpgradeTaskCondition() {
		return upgradeTaskCondition;
	}

	public void setUpgradeTaskCondition(IUpgradeTaskCondition upgradeTaskCondition) {
		this.upgradeTaskCondition = upgradeTaskCondition;
	}
}
