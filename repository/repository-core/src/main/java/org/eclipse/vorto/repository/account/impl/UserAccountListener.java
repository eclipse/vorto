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
package org.eclipse.vorto.repository.account.impl;

import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.internal.SpringUserUtils;
import org.eclipse.vorto.repository.services.RoleService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAccountListener implements ApplicationListener<AppEvent> {

  @Autowired
  private DefaultUserAccountService userAccountService;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @Autowired
  private RoleService roleService;

  @Override
  public void onApplicationEvent(AppEvent event) {
    // update the user context in case of a namespace event (e.g. to display or hide the create model button - #2484).
    if (event.getEventType() == EventType.NAMESPACE_ADDED || event.getEventType() == EventType.NAMESPACE_DELETED || event.getEventType() == EventType.NAMESPACE_UPDATED) {
      refreshUserContext(null);
    }
    if (event.getEventType() == EventType.USER_MODIFIED
            || event.getEventType() == EventType.USER_ADDED) {
      String userId = (String) event.getSubject();
      refreshUserContext(userId);
    }
  }

  public void refreshUserContext(String userId) {
    if (SecurityContextHolder.getContext() != null
        && SecurityContextHolder.getContext().getAuthentication() != null) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (userId != null && !auth.getName().equals(userId)) {
        return;
      }
      User user = userAccountService.getUser(auth.getName());
      SpringUserUtils.refreshSpringSecurityUser(user, userNamespaceRoleService);
    }
  }

}
