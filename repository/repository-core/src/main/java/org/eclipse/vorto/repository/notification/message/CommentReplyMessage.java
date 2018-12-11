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
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.notification.INotificationService.NotificationProblem;

public class CommentReplyMessage extends AbstractMessage {

  private TemplateRenderer renderer;

  private ModelInfo modelInfo;

  private String commentMessage;

  public CommentReplyMessage(User recipient, ModelInfo modelInfo, String commentMessage) {
    super(recipient);
    this.renderer = new TemplateRenderer("comment_reply.ftl");
    this.modelInfo = modelInfo;
    this.commentMessage = commentMessage;
  }

  @Override
  public String getSubject() {
    return "Re: [Vorto] Comment Reply for " + modelInfo.getId().getPrettyFormat();
  }

  @Override
  public String getContent() {
    Map<String, Object> ctx = new HashMap<>(1);
    ctx.put("model", modelInfo);
    ctx.put("comment", commentMessage);
    try {
      return renderer.render(ctx);
    } catch (Exception e) {
      throw new NotificationProblem("Problem rendering notification message", e);

    }
  }

}
