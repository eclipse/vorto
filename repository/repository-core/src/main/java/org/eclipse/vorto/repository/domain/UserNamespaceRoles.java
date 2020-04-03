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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * Represents the roles a user has on a namespace.
 */
@Entity
@Table(name = "user_namespace_roles")
public class UserNamespaceRoles implements Serializable {

  private static final long serialVersionUID = -5348119599452266874L;

  @EmbeddedId
  private UserNamespaceID id;

  @Column(nullable = false)
  private long roles;

  public void setID(UserNamespaceID id) {
    this.id = id;
  }

  public UserNamespaceID getID() {
    return this.id;
  }

  public User getUser() {
    return this.id.getUser();
  }

  public void setUser(User user) {
    if (this.id == null) {
      this.id = new UserNamespaceID();
    }
    this.id.setUser(user);
  }

  public Namespace getNamespace() {
    return this.id.getNamespace();
  }

  public void setNamespace(Namespace namespace) {
    if (this.id == null) {
      this.id = new UserNamespaceID();
    }
    this.id.setNamespace(namespace);
  }

  public long getRoles() {
    return roles;
  }

  public void setRoles(long roles) {
    this.roles = roles;
  }

  /**
   * Equality only inferred by ID ({@link User} + {@link Namespace}).
   * @param o
   * @return
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserNamespaceRoles that = (UserNamespaceRoles) o;
    return id.equals(that.id);
  }

  /**
   * Hash only inferred by ID ({@link User} + {@link Namespace}).
   * @return
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
