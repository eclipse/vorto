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
package org.eclipse.vorto.repository.web.account.dto;

import org.eclipse.vorto.repository.web.api.v1.dto.ICollaborator;

/**
 * Represents a payload for requests to perform both:
 * <ul>
 *   <li>
 *     Create a new technical user with given name, roles and authentication provider ID
 *   </li>
 *   <li>
 *     Add to the given namespace
 *   </li>
 * </ul>
 * @author mena-bosch
 */
public class TenantTechnicalUserDto extends TenantUserDto implements ICollaborator {
  private String authenticationProviderId;
  private String subject;

  @Override
  public String getUserId() {
    return getUsername();
  }

  @Override
  public void setUserId(String userId) {
    setUsername(userId);
  }

  public String getAuthenticationProviderId() {
    return authenticationProviderId;
  }
  public void setAuthenticationProviderId(String value) {
    this.authenticationProviderId = value;
  }
  public String getSubject() {
    return subject;
  }
  public void setSubject(String value) {
    this.subject = value;
  }

  @Override
  public boolean isTechnicalUser() {
    return true;
  }

  @Override
  public void setTechnicalUser(boolean isTechnicalUser) {
    // nothing here
  }
}
