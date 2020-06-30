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

/**
 * Common interface for roles, i.e. {@link NamespaceRole} and {@link RepositoryRole}.<br/>
 * TODO rename to Role without hungarian notation once homonymous enum removed
 */
public interface IRole {
  long getRole();
  String getName();
  long getPrivileges();
}
