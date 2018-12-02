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

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.security.AccessControlList;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.AccessControlPolicyIterator;
import javax.jcr.security.Privilege;

import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.ModelIdHelper;
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
				
				setPermissions(modelInfo);
			}
		}
	}
	
	private Authentication createAuthentication() {
		return new Authentication() {

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

	private void setPermissions(ModelInfo model) {
		final Session session = modelRepository.getSession();
		try {
			ModelIdHelper modelIdHelper = new ModelIdHelper(model.getId());
			
			String path = modelIdHelper.getFullPath();
			String[] privileges = new String[]{Privilege.JCR_READ, Privilege.JCR_WRITE, Privilege.JCR_MODIFY_ACCESS_CONTROL,Privilege.JCR_READ_ACCESS_CONTROL};
			AccessControlManager acm = session.getAccessControlManager();
	 
			Privilege[] permissions = new Privilege[privileges.length];
			for (int i = 0; i < privileges.length; i++) {
				permissions[i] = acm.privilegeFromName(privileges[i]);
			}
	 
			AccessControlList acl = null;
			AccessControlPolicyIterator it = acm.getApplicablePolicies(path);
			if (it.hasNext()) {
			    acl = (AccessControlList)it.nextAccessControlPolicy();
			} else {
			    acl = (AccessControlList)acm.getPolicies(path)[0];
			}
			// add ACL for admin role
			acl.addAccessControlEntry(new Principal() {
				
				@Override
				public String getName() {
					return "admin";
				}
			}, permissions);
			
			// add ACL for user 
			acl.addAccessControlEntry(new Principal() {
				
				@Override
				public String getName() {
					return model.getAuthor();
				}
			}, permissions);
	 
			
			acm.setPolicy(path, acl);
			session.save();
		} catch(RepositoryException ex) {
			logger.error("Could not set permissions for model",ex);
			throw new RuntimeException("Problem setting permissions on model node",ex);
		}finally {
			session.logout();
		}
		
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
