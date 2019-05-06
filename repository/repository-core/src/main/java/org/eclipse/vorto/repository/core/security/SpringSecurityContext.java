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
package org.eclipse.vorto.repository.core.security;

import java.util.Set;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.UserRole;
import org.modeshape.jcr.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

public class SpringSecurityContext implements SecurityContext {

  private static final Logger logger = LoggerFactory.getLogger(SpringSecurityContext.class);

  private Authentication authentication;
  private Set<Role> rolesInTenant;

  public SpringSecurityContext(Authentication authentication, Set<Role> rolesInTenant) {
    this.authentication = authentication;
    this.rolesInTenant = rolesInTenant;
  }

  @Override
  public boolean isAnonymous() {
    return authentication instanceof AnonymousAuthenticationToken;
  }

  @Override
  public String getUserName() {
    return authentication.getName();
  }

  @Override
  public boolean hasRole(String principalName) {
    // grant named model owners access to their models
    if (principalName.equals(authentication.getName())
        || principalName.equals(UserContext.getHash(authentication.getName()))) {
      return true;
    }

    // grant sys ads access to the models
    boolean isRoleValid = Role.isValid(principalName);
    if (isRoleValid && Role.of(principalName) == Role.SYS_ADMIN && isSysAdmin()) {
      return true;
    }

    // grant people with certain roles access to the models
    for (Role userRole : rolesInTenant) {
      if (userRole.hasPermission(principalName)
          || (isRoleValid && Role.of(principalName) == userRole)) {
        return true;
      }
    }

    // grant non-members of the tenant (including anonymous users) access to models
    if (rolesInTenant.size() < 1 && "ANONYMOUS".equals(principalName)) {
      return true;
    }

    // grant everyone read access to all repositories
    return "readonly".equals(principalName);
  }

  private boolean isSysAdmin() {
    return authentication.getAuthorities().stream()
        .anyMatch(auth -> auth.getAuthority().equals(UserRole.ROLE_SYS_ADMIN));
  }

  @Override
  public void logout() {
    logger.debug("logout of Vorto Repository");
  }

}
