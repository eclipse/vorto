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
 * Sent to users if they have been added as collaborators to a namespace.
 */
public class AddedToNamespaceMessage extends AbstractMessage {

  private TemplateRenderer renderer;
  private String namespace;
  private List<String> roles;
  public static final String SUBJECT_FORMAT = "You have been added as a collaborator to the %s Vorto namespace";

  public AddedToNamespaceMessage(User recipient, String namespace, List<String> roles) {
    super(recipient);
    this.namespace = namespace;
    this.roles = roles;
    this.renderer = new TemplateRenderer("added_to_namespace.ftl");
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
      throw new NotificationProblem("Problem rendering collaborator added to namespace email content", e);
    }
  }
}
