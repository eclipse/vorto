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
package org.eclipse.vorto.repository.account;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.UserRole;

public class UserUtils {


  public static Set<Role> extractRolesAsList(Set<UserRole> userRoles) {
    Set<Role> existingRole = new HashSet<>();

    if (userRoles == null) {
      return existingRole;
    }

    for (UserRole userRole : userRoles) {
      existingRole.add(userRole.getRole());
    }
    return existingRole;
  }

}
