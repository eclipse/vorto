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

public class NamespaceDto {

  private String name;
  private Collection<Collaborator> collaborators;
  private Collection<Collaborator> admins;

  public NamespaceDto(String name, Collection<Collaborator> collaborators,
      Collection<Collaborator> admins) {
    this.name = name;
    this.collaborators = collaborators;
    this.admins = admins;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Collection<Collaborator> getCollaborators() {
    return collaborators;
  }

  public void setCollaborators(Collection<Collaborator> collaborators) {
    this.collaborators = collaborators;
  }

  public Collection<Collaborator> getAdmins() {
    return this.admins;
  }

  public void setAdmins(Collection<Collaborator> admins) {
    this.admins = admins;
  }
}
