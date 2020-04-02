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
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Compound ID for user and namespace in the user_permissions table.<br/>
 * IDs are nullable in the unlikely case a query is run by only one of the criteria.
 */
@Embeddable
public class UserNamespaceID implements Serializable {

  private static final long serialVersionUID = -8417951819197969195L;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToOne
  @JoinColumn(name = "namespace_id")
  private Namespace namespace;

  /**
   * Required for JPA
   */
  public UserNamespaceID() {

  }

  public UserNamespaceID(User user, Namespace namespace) {
    this.user = user;
    this.namespace = namespace;
  }

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserNamespaceID that = (UserNamespaceID) o;
    return Objects.equals(user, that.user) &&
        Objects.equals(namespace, that.namespace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, namespace);
  }
}
