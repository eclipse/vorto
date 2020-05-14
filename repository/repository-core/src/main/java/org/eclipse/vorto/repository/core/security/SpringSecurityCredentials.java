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
package org.eclipse.vorto.repository.core.security;

import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Privilege;
import org.eclipse.vorto.repository.services.PrivilegeService;
import org.springframework.security.core.Authentication;

import javax.jcr.Credentials;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class SpringSecurityCredentials implements Credentials {

  private static final long serialVersionUID = -671171242483089098L;

  private Authentication authentication;

  private Set<IRole> rolesInNamespace;

  private PrivilegeService privilegeService;

  private final Predicate<String> hasPrivilege = privilegeName -> rolesInNamespace.stream()
          .map(IRole::getPrivileges)
          .map(privilegeService::getPrivileges)
          .flatMap(Collection::stream)
          .map(Privilege::getName)
          .anyMatch(privilegeName::equalsIgnoreCase);

  public SpringSecurityCredentials(Authentication authentication, Set<IRole> rolesInNamespace, PrivilegeService privilegeService) {
    this.authentication = authentication;
    this.rolesInNamespace = rolesInNamespace;
    this.privilegeService = privilegeService;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public Set<IRole> getRolesInNamespace() {
    return rolesInNamespace;
  }

  public boolean hasPrivilege(String privilege) {
    return hasPrivilege.test(privilege);
  }

  public boolean isSysAdmin() {
    return rolesInNamespace.stream().filter(role -> "sysadmin".equalsIgnoreCase(role.getName()))
        .map(role -> "sysadmin")
        .anyMatch(hasPrivilege);
  }
}
