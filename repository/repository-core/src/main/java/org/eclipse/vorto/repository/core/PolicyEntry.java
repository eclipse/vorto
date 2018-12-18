package org.eclipse.vorto.repository.core;

import java.security.Principal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
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
    return Arrays.asList(privileges).stream().map(p -> p.getName()).collect(Collectors.toSet());
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

  public boolean isSame(AccessControlEntry entry) {
    if (this.principalType == PrincipalType.Role) {
      return entry.getPrincipal().getName().equals("ROLE_"+this.principalId);
    } else {
      return entry.getPrincipal().getName().equals(this.principalId);
    }
  }


}
