package org.eclipse.vorto.repository.account.impl;

import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.sso.SpringUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserAccountListener implements ApplicationListener<AppEvent> {

  @Autowired
  private DefaultUserAccountService userAccountService;

  @Override
  public void onApplicationEvent(AppEvent event) {
    if (event.getEventType() == EventType.TENANT_ADDED) {
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
      SpringUserUtils.refreshSpringSecurityUser(user);
    }
  }

}
