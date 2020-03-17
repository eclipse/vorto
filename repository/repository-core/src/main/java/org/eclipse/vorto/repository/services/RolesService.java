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
package org.eclipse.vorto.repository.services;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Privilege;
import org.eclipse.vorto.repository.repositories.NamespaceRolesRepository;
import org.eclipse.vorto.repository.repositories.RepositoryRolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides functionality common to all roles, i.e. {@link org.eclipse.vorto.repository.domain.NamespaceRole}
 * and {@link org.eclipse.vorto.repository.domain.RepositoryRole}
 */
@Service
public class RolesService {

  @Autowired
  private PrivilegesService privilegesService;

  @Autowired
  private NamespaceRolesRepository namespaceRolesRepository;

  @Autowired
  private RepositoryRolesRepository repositoryRolesRepository;

  /**
   * Aggregates both known role repositories and returns the first {@link IRole} matching the given
   * name.
   *
   * @param name
   * @return the first {@link IRole} matching the given name.
   */
  public Optional<IRole> findAnyByName(String name) {
    return Stream
        .of(Optional.ofNullable(namespaceRolesRepository.find(name)),
            Optional.ofNullable(repositoryRolesRepository.find(name)))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .findFirst();
  }

  /**
   * Returns all {@link Privilege}s for the given {@link IRole}, or the {@link Privilege} for role
   * {@literal 0}, i.e. the {@link Privilege} for role {@literal none}, which also happen to be
   * {@literal none} if the {@link IRole} happens to be {@code null}.
   *
   * @param role
   * @return all {@link Privilege}s for the given {@link IRole} or none.
   */
  public Collection<Privilege> getPrivileges(IRole role) {
    return privilegesService
        .getPrivileges(Optional.ofNullable(role).map(IRole::getPrivileges).orElse(0l));
  }

  /**
   * @param roleName
   * @return all {@link Privilege}s for the given role name.
   * @throws RoleDoesNotExistException if the role name does not match any known role.
   */
  public Collection<Privilege> getPrivileges(String roleName) throws RoleDoesNotExistException {
    return getPrivileges(
        findAnyByName(roleName).orElseThrow(() -> new RoleDoesNotExistException(roleName)));
  }
}
