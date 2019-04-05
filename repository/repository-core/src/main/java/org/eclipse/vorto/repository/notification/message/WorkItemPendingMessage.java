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
package org.eclipse.vorto.repository.notification.message;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.notification.INotificationService.NotificationProblem;

public class WorkItemPendingMessage extends AbstractMessage {

  private TemplateRenderer renderer;

  private ModelInfo modelInfo;

  public WorkItemPendingMessage(User recipient, ModelInfo modelInfo) {
    super(recipient);
    this.renderer = new TemplateRenderer("pending_workitem.ftl");
    this.modelInfo = modelInfo;
  }

  @Override
  public String getSubject() {
    return "[Vorto] Pending Approval for " + modelInfo.getId().getPrettyFormat();
  }

  @Override
  public String getContent() {
    Map<String, Object> ctx = new HashMap<>(1);
    ctx.put("user", recipient);
    ctx.put("model", modelInfo);
    try {
      return renderer.render(ctx);
    } catch (Exception e) {
      throw new NotificationProblem("Problem rendering notification message", e);

    }
  }

}
