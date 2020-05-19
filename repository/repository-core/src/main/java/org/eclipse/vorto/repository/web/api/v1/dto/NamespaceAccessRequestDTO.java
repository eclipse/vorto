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

/**
 * Represents a request sent from the UI to the back-end, to give access to a given namespace for
 * a given user, with an optional set of suggested roles.
 */
public class NamespaceAccessRequestDTO {

  private String requestingUsername;
  private String targetUsername;
  private String namespaceName;
  private Collection<String> suggestedRoles;
  private boolean conditionsAcknowledged;

  public String getRequestingUsername() {
    return requestingUsername;
  }

  public void setRequestingUsername(String requestingUsername) {
    this.requestingUsername = requestingUsername;
  }

  public String getTargetUsername() {
    return targetUsername;
  }

  public void setTargetUsername(String targetUsername) {
    this.targetUsername = targetUsername;
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
}
