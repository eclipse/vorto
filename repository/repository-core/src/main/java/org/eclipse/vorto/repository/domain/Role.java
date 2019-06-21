/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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

import java.util.Arrays;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public enum Role {
  // minimal role for using the repository. It is restricted to only read access, but can generate
  // things and write comments
  USER("readonly"),
  // may execute system admin functionalities
  SYS_ADMIN("readonly", "readwrite", "admin"),
  // may execute tenant admin functionalities
  TENANT_ADMIN("readonly", "readwrite", "admin"),
  // users with this role may create models in the system, import models, edit his own models and
  // delete own models as long as they are not released
  MODEL_CREATOR("readonly", "readwrite"),
  // users with this role may start a release process for a model and deprecate a model
  MODEL_PROMOTER("readonly", "readwrite"),
  // user with this role may review models and either approve or reject them
  MODEL_REVIEWER("readonly", "readwrite"),
  // user with this role may change the visibility of a model
  MODEL_PUBLISHER("readonly", "readwrite");

  public static final String rolePrefix = "ROLE_";

  private String[] permissions;

  private Role(String... permissions) {
    this.permissions = permissions;
  }

  public static boolean isValid(String name) {
    return name.equalsIgnoreCase("admin") || name.startsWith(rolePrefix);
  }
  
  public static boolean exist(String name) {
    String roleName = name.replace(Role.rolePrefix, "");
    for (Role role : Role.values()) {
      if (role.name().equals(roleName)) {
        return true;
      }
    }
    return false;
  }

  public static Role of(String value) {
    if (value.equalsIgnoreCase("admin")) {
      return Role.SYS_ADMIN;
    } else if (value.startsWith(rolePrefix)) {
      return Role.valueOf(value.substring(value.indexOf(rolePrefix) + rolePrefix.length()));
    } else {
      return Role.valueOf(value);
    }
  }

  public boolean hasPermission(String permission) {
    return Arrays.asList(this.permissions).contains(permission);
  }
}
