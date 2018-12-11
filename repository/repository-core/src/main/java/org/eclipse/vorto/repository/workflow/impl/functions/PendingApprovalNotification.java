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
package org.eclipse.vorto.repository.workflow.impl.functions;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.WorkItemPendingMessage;
import org.eclipse.vorto.repository.workflow.model.IWorkflowFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PendingApprovalNotification implements IWorkflowFunction {

  private INotificationService notificationService;
  private IUserAccountService accountService;

  private static final Logger LOGGER = LoggerFactory.getLogger(PendingApprovalNotification.class);

  public PendingApprovalNotification(INotificationService notificationService,
      IUserAccountService accountService) {
    this.notificationService = notificationService;
    this.accountService = accountService;
  }

  @Override
  public void execute(ModelInfo model, IUserContext user) {
    LOGGER.debug("Executing workflow function: " + this.getClass());
    User account = accountService.getUser(user.getUsername());
    notificationService.sendNotification(new WorkItemPendingMessage(account, model));
  }

}
