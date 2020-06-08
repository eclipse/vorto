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
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.services.PrivilegeService;
import org.springframework.security.core.Authentication;

import javax.jcr.Credentials;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

public class SpringSecurityCredentials implements Credentials {

  private static final long serialVersionUID = -671171242483089098L;

  private Authentication authentication;

  private Set<IRole> roles;

  private PrivilegeService privilegeService;

  private final Predicate<String> hasPrivilege = privilegeName -> roles.stream()
          .map(IRole::getPrivileges)
          .map(privilegeService::getPrivileges)
          .flatMap(Collection::stream)
          .map(Privilege::getName)
          .anyMatch(privilegeName::equalsIgnoreCase);

  public SpringSecurityCredentials(Authentication authentication, Set<IRole> roles, PrivilegeService privilegeService) {
    this.authentication = authentication;
    this.roles = roles;
    this.privilegeService = privilegeService;
  }

  public Authentication getAuthentication() {
    return authentication;
  }

  public Set<IRole> getRolesInNamespace() {
    return roles;
  }

  public boolean hasPrivilege(String privilege) {
    return hasPrivilege.test(privilege);
  }

  public boolean isSysAdmin() {
    return roles.stream().filter(role -> RepositoryRole.SYS_ADMIN.getName()
        .equalsIgnoreCase(role.getName()))
        .map(role -> RepositoryRole.SYS_ADMIN.getName())
        .anyMatch(hasPrivilege);
  }

}
