/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

import com.google.common.base.Strings;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.notification.INotificationService.NotificationProblem;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceAccessRequestDTO;

/**
 * Represents a message notifying a namespace administrator that a user has requested access to their
 * namespace.
 */
public class RequestAccessToNamespaceMessage extends AbstractMessage {

  private TemplateRenderer renderer;
  private NamespaceAccessRequestDTO request;
  public static final String SUBJECT_FORMAT = "Vorto request to access namespace %s for user %s";

  public RequestAccessToNamespaceMessage(NamespaceAccessRequestDTO request, User recipient) {
    super(recipient);
    this.renderer = new TemplateRenderer("request_access_to_namespace.ftl");
    this.request = request;
  }

  @Override
  public String getSubject() {
    return String.format(SUBJECT_FORMAT, request.getNamespaceName(), request.getTargetUsername());
  }

  @Override
  public String getContent() {
    Map<String, Object> map = new HashMap<>();
    map.put("namespace", request.getNamespaceName());
    map.put("requestingUser", request.getRequestingUsername());
    map.put("targetUser", request.getTargetUsername());
    map.put("suggestedRoles", request.getSuggestedRoles());
    map.put("recipient", recipient.getUsername());
    String subject = Strings.nullToEmpty(request.getTargetSubject()).trim().isEmpty() ? "n/a" : request.getTargetSubject();
    map.put("subject", subject);
    try {
      return renderer.render(map);
    } catch (Exception e) {
      throw new NotificationProblem("Problem rendering delete account email content", e);
    }
  }
}
