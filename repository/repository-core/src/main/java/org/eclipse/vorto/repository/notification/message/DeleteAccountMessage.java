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
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.notification.INotificationService.NotificationProblem;

public class DeleteAccountMessage extends AbstractMessage {

  private TemplateRenderer renderer;

  public DeleteAccountMessage(User recipient) {
    super(recipient);
    this.renderer = new TemplateRenderer("delete_account.ftl");
  }

  @Override
  public String getSubject() {
    return "[Vorto] Your Vorto account was deleted.";
  }

  @Override
  public String getContent() {
    Map<String, Object> ctx = new HashMap<>(1);
    ctx.put("user", recipient);
    try {
      return renderer.render(ctx);
    } catch (Exception e) {
      throw new NotificationProblem("Problem rendering delete account email content", e);
    }
  }

}
