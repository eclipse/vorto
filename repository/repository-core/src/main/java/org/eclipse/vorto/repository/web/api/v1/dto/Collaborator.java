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
package org.eclipse.vorto.repository.web.api.v1.dto;

import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.TenantUser;

public class Collaborator {
  private String userId;
  private String providerId;
  private String subject;
  private Collection<String> roles;
  
  public static Collaborator fromUser(TenantUser tenantUser) {
    return new Collaborator(tenantUser.getUser().getUsername(), 
        tenantUser.getUser().getAuthenticationProvider(),
        tenantUser.getUser().getSubject(),
        tenantUser.getRoles().stream().map(role -> role.getRole().name())
          .collect(Collectors.toList()));
  }
  
  public Collaborator() {
    
  }
  
  public Collaborator(String userId, String providerId, String subject, Collection<String> roles) {
    this.userId = userId;
    this.providerId = providerId;
    this.roles = roles;
    this.subject = subject;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getProviderId() {
    return providerId;
  }

  public void setProviderId(String providerId) {
    this.providerId = providerId;
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
}
