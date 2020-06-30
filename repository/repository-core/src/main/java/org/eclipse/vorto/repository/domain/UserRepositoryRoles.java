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
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Represents the roles a user has on the repository.<br/>
 * Presently limited to expressing users with the {@literal sysadmin} role.<br/>
 * Other users are implicitly just {@literal repository_user} by default, and their repository role
 * is not persisted in the table. <br/>
 * The idea is that if new repository roles emerge, they will be persisted in the table alongside
 * the {@literal sysadmin} role.
 */
@Entity
@Table(name = "user_repository_roles")
public class UserRepositoryRoles implements Serializable {

  private static final long serialVersionUID = -6226755293886720665L;

  @Id
  @Column(unique = true, nullable = false)
  private Long id;

  @MapsId
  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  private long roles;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public long getRoles() {
    return roles;
  }

  public void setRoles(long roles) {
    this.roles = roles;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRepositoryRoles that = (UserRepositoryRoles) o;
    return roles == that.roles &&
        user.equals(that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, roles);
  }
}
