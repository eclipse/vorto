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
package org.eclipse.vorto.repository.workflow.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsAdminCondition;
import org.eclipse.vorto.repository.workflow.impl.conditions.IsOwnerCondition;
import org.eclipse.vorto.repository.workflow.model.IAction;
import org.eclipse.vorto.repository.workflow.model.IState;
import org.eclipse.vorto.repository.workflow.model.IWorkflowCondition;
import org.eclipse.vorto.repository.workflow.model.IWorkflowModel;

public class SimpleWorkflowModel implements IWorkflowModel {

	private String name;
	private String description;
	
	
	private static DefaultAction ACTION_INITAL = new DefaultAction("start");
	public static DefaultAction ACTION_RELEASE = new DefaultAction("release","Releasing a model will trigger an internal review process. Once approved, your model will be released and is publicly visible.");
	public static DefaultAction ACTION_APPROVE = new DefaultAction("approve","You agree to the model and its content.");
	public static DefaultAction ACTION_REJECT = new DefaultAction("reject", "You do not agree to the model and its content. Please use comments to give feedback to author.");
	public static DefaultAction ACTION_WITHDRAW = new DefaultAction("withdraw","When you withdraw, the review process is stopped and your model returns to Draft state.");
	public static DefaultAction ACTION_DEPRECATE = new DefaultAction("deprecate","When you deprecate the model, the model is marked as 'Deprecated' to users.");

	
	public static DefaultState STATE_DRAFT = new DefaultState("Draft","A draft model is only viewable and editable by the model owner.");
	public static DefaultState STATE_IN_REVIEW = new DefaultState("InReview","Being reviewed.");
	public static DefaultState STATE_RELEASED = new DefaultState("Released","A released model has been successfully reviewed and can viewed by everybody.");
	public static DefaultState STATE_DEPRECATED = new DefaultState("Deprecated","A deprecated model indicates that the model is obsolete and shall not be used any more.");

	
	private static final IWorkflowCondition ONLY_OWNER = new IsOwnerCondition();
	
	private static final List<IState> ALL_STATES = Arrays.asList(STATE_DRAFT,STATE_IN_REVIEW,STATE_RELEASED, STATE_DEPRECATED);
	
	public SimpleWorkflowModel(IUserRepository userRepository) {
		
		final IWorkflowCondition onlyAdminCondition = new IsAdminCondition(userRepository);
		
		ACTION_INITAL.setTo(STATE_DRAFT);
		
		ACTION_RELEASE.setTo(STATE_IN_REVIEW);
		ACTION_RELEASE.setConditions(ONLY_OWNER);
		
		ACTION_APPROVE.setTo(STATE_RELEASED);
		ACTION_APPROVE.setConditions(onlyAdminCondition);
		
		ACTION_REJECT.setTo(STATE_DRAFT);
		ACTION_REJECT.setConditions(onlyAdminCondition);
		
		ACTION_WITHDRAW.setTo(STATE_DRAFT);
		ACTION_WITHDRAW.setConditions(ONLY_OWNER);
		
		ACTION_DEPRECATE.setTo(STATE_DEPRECATED);
		ACTION_DEPRECATE.setConditions(onlyAdminCondition);
		
		STATE_DRAFT.setActions(ACTION_RELEASE);
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
