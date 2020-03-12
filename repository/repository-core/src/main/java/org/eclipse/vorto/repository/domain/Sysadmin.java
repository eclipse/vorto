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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Represents a user with system administrator privileges over the whole repository.
 */
@Entity
@Table(name = "sysadmins")
public class Sysadmin implements Serializable {

  private static final long serialVersionUID = 6366898093762444972L;

  @Id
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @OneToOne
  private User user;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sysadmin sysadmin = (Sysadmin) o;
    return user.equals(sysadmin.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user);
  }

}
