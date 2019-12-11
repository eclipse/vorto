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

public class CollaboratorRequest extends Collaborator {

  private boolean isTechnicalUser;
  
  public CollaboratorRequest() { }
  
  public CollaboratorRequest(String userId, String providerId, String subject,
      Collection<String> roles) {
    super(userId, providerId, subject, roles);
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
