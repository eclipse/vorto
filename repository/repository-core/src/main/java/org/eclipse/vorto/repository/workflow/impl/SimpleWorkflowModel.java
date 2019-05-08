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
package org.eclipse.vorto.repository.workflow.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsAdminCondition;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsAnonymousModel;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsLoggedIn;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsOwnerCondition;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsReviewerCondition;
import org.eclipse.vorto.repository.workflow.impl.conditions.OrCondition;
import org.eclipse.vorto.repository.workflow.impl.functions.ClaimOwnership;
import org.eclipse.vorto.repository.workflow.impl.functions.GrantModelOwnerPolicy;
import org.eclipse.vorto.repository.workflow.impl.functions.GrantReviewerModelPolicy;
import org.eclipse.vorto.repository.workflow.impl.functions.PendingApprovalNotification;
import org.eclipse.vorto.repository.workflow.impl.functions.RemoveModelReviewerPolicy;
import org.eclipse.vorto.repository.workflow.impl.validators.CheckStatesOfDependenciesValidator;
import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IState;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.eclipse.vorto.repository.workflow.model.IWorkflowModel;

/**
 * Defines a simple workflow with the following states:
 * DRAFT -> IN_REVIEW -> RELEASED -> DEPRECATED
 *
 */
public class SimpleWorkflowModel implements IWorkflowModel {

	private String name;
	private String description;
	
	
	private static final DefaultAction ACTION_INITAL = new DefaultAction("start");
	public static final DefaultAction ACTION_RELEASE = new DefaultAction("Release","Releasing a model will trigger an internal review process, done by the Vorto Team. Once approved, your model will be released.");
	public static final DefaultAction ACTION_CLAIM = new DefaultAction("Claim","Claiming the model makes you owner and responsible for the model having full access.");
	public static final DefaultAction ACTION_APPROVE = new DefaultAction("Approve","You agree to the model and its content and confirm the model release.");
	public static final DefaultAction ACTION_REJECT = new DefaultAction("Reject", "You do not agree to the model and its content. Please use comments to give feedback to author.");
	public static final DefaultAction ACTION_WITHDRAW = new DefaultAction("Withdraw","When you withdraw, the review process is stopped and your model returns to Draft state where you can make changes.");
	public static final DefaultAction ACTION_DEPRECATE = new DefaultAction("Deprecate","Marks the model as deprecated but remains publicly visible.");

	
	public static final DefaultState STATE_DRAFT = new DefaultState("Draft","A draft model is only viewable and editable by the model owner.");
	public static final DefaultState STATE_IN_REVIEW = new DefaultState("InReview","Being reviewed by the Vorto Team.");
	public static final DefaultState STATE_RELEASED = new DefaultState("Released","A released model has been successfully reviewed and can viewed by everybody.");
	public static final DefaultState STATE_DEPRECATED = new DefaultState("Deprecated","A deprecated model indicates that the model is obsolete and shall not be used any more.");

	
	private static final IWorkflowCondition ONLY_OWNER_EXCLUDING_ANONYMOUS = new IsOwnerCondition();
	private static final IWorkflowCondition IS_ANONYMOUS_MODEL = new IsAnonymousModel();
	private static final IWorkflowCondition IS_LOGGED_IN = new IsLoggedIn();
		
	private static final List<IState> ALL_STATES = Arrays.asList(STATE_DRAFT,STATE_IN_REVIEW,STATE_RELEASED, STATE_DEPRECATED);
	
	public SimpleWorkflowModel(IUserAccountService userRepository, IModelRepositoryFactory repositoryFactory, INotificationService notificationService) {
		
		final IWorkflowCondition isAdminCondition = new IsAdminCondition();
		final IWorkflowCondition isReviewerCondition = new IsReviewerCondition(userRepository);
		final IWorkflowFunction pendingWorkItemNotification = new PendingApprovalNotification(notificationService, userRepository);
		
		final IWorkflowFunction grantModelOwnerPolicy = new GrantModelOwnerPolicy(repositoryFactory);
		final IWorkflowFunction grantReviewerModelAccess = new GrantReviewerModelPolicy(repositoryFactory);
		final IWorkflowFunction claimOwnership = new ClaimOwnership(repositoryFactory);
		final IWorkflowFunction removeModelReviewerPolicy = new RemoveModelReviewerPolicy(repositoryFactory);
		
		ACTION_INITAL.setTo(STATE_DRAFT);
		ACTION_INITAL.setFunctions(grantModelOwnerPolicy);
		
		ACTION_CLAIM.setTo(STATE_DRAFT);
		ACTION_CLAIM.setConditions(IS_LOGGED_IN,IS_ANONYMOUS_MODEL);
		ACTION_CLAIM.setFunctions(claimOwnership);
		
		ACTION_RELEASE.setTo(STATE_IN_REVIEW);
		ACTION_RELEASE.setConditions(new OrCondition(ONLY_OWNER_EXCLUDING_ANONYMOUS,isAdminCondition));
		ACTION_RELEASE.setValidators(new CheckStatesOfDependenciesValidator(repositoryFactory,STATE_IN_REVIEW.getName(),STATE_RELEASED.getName(),STATE_DEPRECATED.getName()));
		ACTION_RELEASE.setFunctions(pendingWorkItemNotification,grantReviewerModelAccess);
		
		ACTION_APPROVE.setTo(STATE_RELEASED);
		ACTION_APPROVE.setConditions(new OrCondition(isAdminCondition,isReviewerCondition));
		ACTION_APPROVE.setValidators(new CheckStatesOfDependenciesValidator(repositoryFactory,STATE_RELEASED.getName(),STATE_DEPRECATED.getName()));
		ACTION_APPROVE.setFunctions(removeModelReviewerPolicy);
		
		ACTION_REJECT.setTo(STATE_DRAFT);
		ACTION_REJECT.setConditions(new OrCondition(isAdminCondition, isReviewerCondition));
		
		ACTION_WITHDRAW.setTo(STATE_DRAFT);
		ACTION_WITHDRAW.setConditions(ONLY_OWNER_EXCLUDING_ANONYMOUS);
		
		ACTION_DEPRECATE.setTo(STATE_DEPRECATED);
		ACTION_DEPRECATE.setConditions(isAdminCondition);
		
		STATE_DRAFT.setActions(ACTION_RELEASE,ACTION_CLAIM);
		STATE_IN_REVIEW.setActions(ACTION_APPROVE,ACTION_REJECT,ACTION_WITHDRAW);
		
		STATE_RELEASED.setActions(ACTION_DEPRECATE);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public IAction getInitialAction() {
		return ACTION_INITAL;
	}

	@Override
	public Optional<IState> getState(String name) {
		return ALL_STATES.stream().filter(state -> state.getName().equals(name)).findFirst();
	}
}
