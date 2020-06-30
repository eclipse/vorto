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
package org.eclipse.vorto.repository.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a role applicable to a namespace.<br/>
 * A role is made of the sum of a number of {@link Privilege}s, expressed as a 64-bit integer value.
 * <br/>
 * Inferring which privileges a role has can be done through bitwise {@code &}.
 */
@Entity
@Table(name = "namespace_roles")
public class NamespaceRole implements Serializable, IRole {

  private static final long serialVersionUID = 760754614830691891L;

  public static final NamespaceRole[] DEFAULT_NAMESPACE_ROLES;

  static {
    DEFAULT_NAMESPACE_ROLES = new NamespaceRole[]{
        new NamespaceRole(1, "model_viewer", 1),
        new NamespaceRole(2, "model_creator", 3),
        new NamespaceRole(4, "model_promoter", 3),
        new NamespaceRole(8, "model_reviewer", 3),
        new NamespaceRole(16, "model_publisher", 3),
        new NamespaceRole(32, "namespace_admin", 7)
    };
  }

  public NamespaceRole() {}

  public NamespaceRole(long role, String name, long privileges) {
    this.role = role;
    this.name = name;
    this.privileges = privileges;
  }

  @Id
  private long role;

  private String name;

  private long privileges;

  @Override
  public long getRole() {
    return role;
  }

  public void setRole(long role) {
    this.role = role;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public long getPrivileges() {
    return privileges;
  }

  public void setPrivileges(long privileges) {
    this.privileges = privileges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NamespaceRole that = (NamespaceRole) o;
    return role == that.role &&
        privileges == that.privileges &&
        name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, name, privileges);
  }
}
