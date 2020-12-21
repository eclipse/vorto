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
import org.eclipse.vorto.repository.web.account.dto.UserDto;

/**
 * Represents a request sent from the UI to the back-end, to give access to a given namespace for
 * a given user, with an optional set of suggested roles.
 */
public class NamespaceAccessRequestDTO {

  private UserDto requestingUser;
  private UserDto targetUser;
  private String targetSubject = "n/a";
  private String namespaceName;
  private Collection<String> suggestedRoles;
  private boolean conditionsAcknowledged;

  public UserDto getRequestingUser() {
    return requestingUser;
  }

  public void setRequestingUser(UserDto requestingUser) {
    this.requestingUser = requestingUser;
  }

  public UserDto getTargetUser() {
    return targetUser;
  }

  public void setTargetUser(UserDto targetUser) {
    this.targetUser = targetUser;
  }

  public String getNamespaceName() {
    return namespaceName;
  }

  public void setNamespaceName(String namespaceName) {
    this.namespaceName = namespaceName;
  }

  public Collection<String> getSuggestedRoles() {
    return suggestedRoles;
  }

  public void setSuggestedRoles(Collection<String> suggestedRoles) {
    this.suggestedRoles = suggestedRoles;
  }

  public boolean isConditionsAcknowledged() {
    return conditionsAcknowledged;
  }

  public void setConditionsAcknowledged(boolean conditionsAcknowledged) {
    this.conditionsAcknowledged = conditionsAcknowledged;
  }

  public String getTargetSubject() {
    return targetSubject;
  }

  public void setTargetSubject(String targetSubject) {
    this.targetSubject = targetSubject;
  }
}
