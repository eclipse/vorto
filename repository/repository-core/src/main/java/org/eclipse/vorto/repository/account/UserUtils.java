package org.eclipse.vorto.repository.account;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public class UserUtils {


  public static String getUserRolesAsCommaSeparatedString(User user) {
    Set<Role> userRoles = UserUtils.extractRolesAsList(user.getRoles());
    StringJoiner roles = new StringJoiner(",");

    for (Role userRole : userRoles) {
      roles.add("ROLE_" + userRole);

    }
    return roles.toString();
  }

  public static Set<Role> extractRolesAsList(Set<UserRole> userRoles) {
    Set<Role> existingRole = new HashSet<>();

    if (userRoles == null)
      return existingRole;

    for (UserRole userRole : userRoles) {
      existingRole.add(userRole.getRole());
    }
    return existingRole;
  }

}
