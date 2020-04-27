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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

  public static final String LEGACY_ROLE_PREFIX = "ROLE_";

  public static final Map<String, String> LEGACY_ROLE_CONVERSION = new HashMap<>();

  static {
    LEGACY_ROLE_CONVERSION.put("ROLE_USER", "model_viewer");
    LEGACY_ROLE_CONVERSION.put("TENANT_ADMIN", "namespace_admin");
  }

  @Autowired
  private NamespaceRoleRepository namespaceRoleRepository;

  @Autowired
  private RepositoryRoleRepository repositoryRoleRepository;

  /**
   * Converts a sum of roles expressed as {@code long} to a set of {@link NamespaceRole}.
   *
   * @param roles
   * @return
   */
  public Collection<IRole> toNamespaceRoles(long roles) {
    return namespaceRoleRepository.findAll().stream()
        .filter(r -> !r.getName().equals("none"))
        .filter(r -> (roles & r.getRole()) == r.getRole()).collect(
            Collectors.toSet());
  }

  /**
   * Converts a sum of roles expressed as {@code long} to a set of {@link RepositoryRole}.
   *
   * @param roles
   * @return
   */
  public Collection<IRole> toRepositoryRoles(long roles) {
    return repositoryRoleRepository.findAll().stream()
        .filter(r -> (roles & r.getRole()) == r.getRole()).collect(
            Collectors.toSet());
  }

  /**
   * Converts a collection of whichever {@link IRole} to the sum of their value, expressed as a
   * {@code long}.
   *
   * @param roles
   * @return
   */
  public long toLong(Collection<IRole> roles) {
    // boilerplate null validation
    if (roles == null || roles.isEmpty()) {
      return 0l;
    }
    return roles.stream().collect(Collectors.summingLong(IRole::getRole));
  }

  /**
   * Converts a collection of whichever {@link IRole} to the sum of their value, expressed as a
   * {@code long}.
   *
   * @param roles
   * @return
   */
  public long toLong(IRole... roles) {
    // boilerplate null validation
    if (roles == null || roles.length == 0) {
      return 0l;
    }
    return Arrays.stream(roles).collect(Collectors.summingLong(IRole::getRole));
  }

  public Collection<IRole> toNamespaceRoles(String... roles) {
    Collection<IRole> result = new HashSet<>();
    if (roles == null) {
      return result;
    }
    return Arrays.stream(roles).map(this::normalize).map(namespaceRoleRepository::find)
        .collect(Collectors.toSet());
  }

  public Collection<IRole> toNamespaceRoles(Collection<String> roles) {
    return toNamespaceRoles(roles.toArray(new String[roles.size()]));
  }

  public String normalize(String role) {
    if (null == role || role.trim().isEmpty()) {
      return "";
    }
    String conversion = LEGACY_ROLE_CONVERSION.get(role);
    if (conversion == null) {
      conversion = role;
    }
    return conversion.replace(LEGACY_ROLE_PREFIX, "").toLowerCase();
  }
}
