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
package org.eclipse.vorto.repository.workflow.impl.functions;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.WorkItemPendingMessage;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;

public class PendingApprovalNotification implements IWorkflowFunction {

	private INotificationService notificationService;
	private IUserAccountService accountService;
	
	public PendingApprovalNotification(INotificationService notificationService, IUserAccountService accountService) {
		this.notificationService = notificationService;
		this.accountService = accountService;
	}
	
	@Override
	public void execute(ModelInfo model, IUserContext user) {
		User account = accountService.getUser(user.getUsername());
		if (account.hasEmailAddress()) {
			notificationService.sendNotification(new WorkItemPendingMessage(account, model));
		}
	}

}
