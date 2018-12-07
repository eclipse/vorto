package org.eclipse.vorto.repository.core;

import java.security.Principal;
import javax.jcr.security.AccessControlEntry;
import javax.jcr.security.Privilege;
import org.eclipse.vorto.repository.account.Role;

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

    if (Role.isValid(principal.getName())) {
      entry.principalType = PrincipalType.Role;
      entry.principalId = Role.of(principal.getName()).name();
    } else {
      entry.principalType = PrincipalType.User;
      entry.principalId = principal.getName();
    }

    for (Privilege p : ace.getPrivileges()) {
      if (p.getName().equalsIgnoreCase("jcr:read")) {
        entry.permission = Permission.READ;
      } else if (p.getName().equalsIgnoreCase("jcr:write")) {
        entry.permission = Permission.MODIFY;
      } else if (p.getName().equalsIgnoreCase("jcr:all")) {
        entry.permission = Permission.FULL_ACCESS;
      }
    }

    return entry;
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


  public static enum PrincipalType {
    User, Role
  }

  public static enum Permission {
    FULL_ACCESS, READ, MODIFY
  }


}
