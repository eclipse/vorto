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
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Represents the roles a user has on a namespace.
 */
@Entity
@IdClass(UserNamespaceID.class)
@Table(name = "user_namespace_roles")
public class UserNamespaceRoles implements Serializable {

  private static final long serialVersionUID = -5348119599452266874L;

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Id
  @ManyToOne
  @JoinColumn(name = "namespace_id", referencedColumnName = "id")
  private Namespace namespace;

  @Column(nullable = false)
  private long roles;


  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Namespace getNamespace() {
    return namespace;
  }

  public void setNamespace(Namespace namespace) {
    this.namespace = namespace;
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
    UserNamespaceRoles that = (UserNamespaceRoles) o;
    return roles == that.roles &&
        Objects.equals(user, that.user) &&
        Objects.equals(namespace, that.namespace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, namespace, roles);
  }

}