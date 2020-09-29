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
package org.eclipse.vorto.repository.core;

import org.eclipse.vorto.repository.domain.RepositoryRole;

import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.Privilege;
import java.security.Principal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PolicyEntry {

  private String principalId;

  private PrincipalType principalType;

  private Permission permission = null;

  public static PolicyEntry of(String principalId, PrincipalType type, Permission permission) {
    PolicyEntry entry = new PolicyEntry();
    entry.principalId = principalId;
    entry.principalType = type;
    entry.permission = permission;
    return entry;
  }

  public static PolicyEntry of(AccessControlEntry ace) {
    PolicyEntry entry = new PolicyEntry();
    Principal principal = ace.getPrincipal();
    if (principal.getName().equalsIgnoreCase(IModelPolicyManager.ANONYMOUS_ACCESS_POLICY)) {
      entry.principalType = PrincipalType.User;
    } else {
      entry.principalType = PrincipalType.Role;
    }
    entry.principalId = principal.getName();

    final Set<String> privileges = privileges(ace.getPrivileges());
    if (privileges.contains("jcr:all")) {
      entry.permission = Permission.FULL_ACCESS;
    } else if (privileges.contains("jcr:write")) {
      entry.permission = Permission.MODIFY;
    } else if (privileges.contains("jcr:read")) {
      entry.permission = Permission.READ;
    }

    return entry;
  }

  private static Set<String> privileges(Privilege[] privileges) {
    return Arrays.stream(privileges).map(Privilege::getName).collect(Collectors.toSet());
  }

  public String getPrincipalId() {
    return principalId;
  }

  public void setPrincipalId(String principalId) {
    this.principalId = principalId;
  }

  public PrincipalType getPrincipalType() {
    return principalType;
  }

  public void setPrincipalType(PrincipalType principalType) {
    this.principalType = principalType;
  }

  public Permission getPermission() {
    return permission;
  }

  public void setPermission(Permission permission) {
    this.permission = permission;
  }

  public enum PrincipalType {
    User, Role
  }

  public enum Permission {
    FULL_ACCESS, MODIFY, READ;

    public boolean includes(Permission permission) {
      return this.name().equals("FULL_ACCESS")
          || this.name().equals("MODIFY")
              && (permission == Permission.READ || permission == Permission.MODIFY)
          || this.name().equals("READ") && permission == Permission.READ;
    }
  }

  public boolean isSame(AccessControlEntry entry) {
    return entry.getPrincipal().getName().equals(this.principalId);
  }

  @Override
  public String toString() {
    return "PolicyEntry [principalId=" + principalId + ", principalType=" + principalType
        + ", permission=" + permission + "]";
  }

  public String toACEPrincipal() {
    return this.principalId;
  }

  public boolean isAdminPolicy() {
    return this.principalId.equalsIgnoreCase(RepositoryRole.SYS_ADMIN.getName())
        && this.principalType == PrincipalType.Role;
  }



}
