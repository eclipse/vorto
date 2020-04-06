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
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.NamespaceRole;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.repositories.NamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.RepositoryRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleUtil {

  @Autowired
  private NamespaceRoleRepository namespaceRoleRepository;

  @Autowired
  private RepositoryRoleRepository repositoryRoleRepository;

  public Collection<IRole> toNamespaceRoles(long roles) {
    return namespaceRoleRepository.findAll().stream()
        .filter(r -> (roles & r.getRole()) == r.getRole()).collect(
            Collectors.toSet());
  }

  public Collection<IRole> toRepositoryRoles(long roles) {
    return repositoryRoleRepository.findAll().stream()
        .filter(r -> (roles & r.getRole()) == r.getRole()).collect(
            Collectors.toSet());
  }
}
