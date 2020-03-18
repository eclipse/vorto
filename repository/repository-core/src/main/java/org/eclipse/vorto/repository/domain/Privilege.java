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

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Represents a supported privileges assigned to a user on a namespace.
 */
@Entity
@Table(name = "privileges")
  public class Privilege implements Serializable {

  private static final long serialVersionUID = -5897575755499074106L;

  @Id
  private long privilege;

  @Column
  private String name;

  public long getPrivilege() {
    return privilege;
  }

  public void setPrivilege(long permission) {
    this.privilege = permission;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Privilege that = (Privilege) o;
    return privilege == that.privilege &&
        name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(privilege, name);
  }
}