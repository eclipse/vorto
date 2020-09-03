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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.notification.INotificationService.NotificationProblem;

/**
 * Sent to users if their roles have changed in a namespace.
 */
public class RolesChangedInNamespaceMessage extends AbstractMessage {

  private TemplateRenderer renderer;
  private String namespace;
  private List<String> roles;
  public static final String SUBJECT_FORMAT = "Your roles as a collaborator to the %s Vorto namespace have changed";

  public RolesChangedInNamespaceMessage(User recipient, String namespace, List<String> roles) {
    super(recipient);
    this.namespace = namespace;
    this.roles = roles;
    this.renderer = new TemplateRenderer("roles_changed_in_namespace.ftl");
  }

  @Override
  public String getSubject() {
    return String.format(SUBJECT_FORMAT, namespace);
  }

  @Override
  public String getContent() {
    Map<String, Object> map = new HashMap<>();
    map.put("namespace", namespace);
    map.put("roles", roles);
    map.put("recipient", recipient.getUsername());
    try {
      return renderer.render(map);
    } catch (Exception e) {
      throw new NotificationProblem("Problem rendering roles changed in namespace email content",
          e);
    }
  }
}
