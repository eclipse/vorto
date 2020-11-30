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
package org.eclipse.vorto.repository.comment.impl;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.core.impl.cache.NamespaceRequestCache;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CommentAnonymizationListener implements ApplicationListener<AppEvent> {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private NamespaceRequestCache namespaceRequestCache;
  
  @Override
  public void onApplicationEvent(AppEvent event) {
    EventType type = event.getEventType();
    switch(type) {
      // subject here is the username
      case USER_DELETED: {
        UserDto user = (UserDto) event.getSubject();

        commentRepository.findByAuthor(user.getUsername(), user.getAuthenticationProvider())
            .forEach(comment -> {
              // null author means anonymous
              comment.setAuthor(null);
              commentRepository.save(comment);
            }
        );
        break;
      }
      // subject is the UserNamespaceRoles so we can have both target user and namespace
      case USER_REMOVED_FROM_NAMESPACE: {
        UserNamespaceRoles roles = (UserNamespaceRoles)event.getSubject();
        Namespace namespace = roles.getNamespace();
        commentRepository
            .findByAuthor(
              roles.getUser().getUsername(), roles.getUser().getAuthenticationProviderId()
            )
            .forEach(
            comment -> {
              ModelId id = ModelId.fromPrettyFormat(comment.getModelId());
              Namespace commentNS = namespaceRequestCache
                  .namespace(id.getNamespace())
                  .orElse(null);
              if (namespace.equals(commentNS)) {
                comment.setAuthor(null);
                commentRepository.save(comment);
              }
            }
        );
        break;
      }
    }
  }

}
