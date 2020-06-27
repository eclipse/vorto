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
package org.eclipse.vorto.repository.web.api.v1.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.TenantUser;

public class Collaborator {
  private String userId;
  private String authenticationProviderId;  
  private String subject;
  private boolean isTechnicalUser;
  private Collection<String> roles;
  
  public static Collaborator fromUser(TenantUser tenantUser) {
    return new Collaborator(tenantUser.getUser().getUsername(), 
        tenantUser.getUser().getAuthenticationProviderId(),
        tenantUser.getUser().getSubject(),
        tenantUser.getUser().isTechnicalUser(),
        tenantUser.getRoles().stream().map(role -> role.getRole().name())
          .collect(Collectors.toList()));
  }
  
  public Collaborator() {}
  
  public Collaborator(String userId, String providerId, String subject, Collection<String> roles) {
    this(userId, providerId, subject, false, roles);
  }
  
  public Collaborator(String userId, String providerId, String subject, boolean isTechnicalUser, Collection<String> roles) {
    this.userId = userId;
    this.authenticationProviderId = providerId;
    this.roles = roles;
    this.subject = subject;
    this.isTechnicalUser = isTechnicalUser;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAuthenticationProviderId() {
    return authenticationProviderId;
  }

  public void setAuthenticationProviderId(String providerId) {
    this.authenticationProviderId = providerId;
  }

  public Collection<String> getRoles() {
    return roles;
  }

  public void setRoles(Collection<String> roles) {
    this.roles = roles;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }
  
  public boolean isTechnicalUser() {
    return isTechnicalUser;
  }

  public void setTechnicalUser(boolean isTechnicalUser) {
    this.isTechnicalUser = isTechnicalUser;
  }
  
  // Jackson mapping oddity
  public boolean getIsTechnicalUser() {
    return isTechnicalUser;
  }
  
  public void setIsTechnicalUser(boolean isTechnicalUser) {
    this.isTechnicalUser = isTechnicalUser;
  }
}
