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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.NamespaceRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.repositories.NamespaceRolesRepository;
import org.eclipse.vorto.repository.repositories.UserNamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * This service returns information on user roles on namespaces.<br/>
 * It is session-scoped, as any user with administrative privileges on a namespace can change
 * other users' access to it by means of roles.
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserNamespaceRoleService {

  private static final Logger LOGGER = Logger.getLogger(UserNamespaceRoleService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private NamespaceRepository namespaceRepository;

  @Autowired
  private NamespaceRolesRepository namespaceRolesRepository;

  @Autowired
  private UserNamespaceRoleRepository userNamespaceRoleRepository;

  /**
   * @param user
   * @param namespace
   * @param role
   * @return
   * @see org.eclipse.vorto.repository.account.impl.DefaultUserAccountService#hasRole(String, String, String)
   */
  public boolean hasRole(User user, Namespace namespace, IRole role) {
    if (Stream.of(user, namespace, role).anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("Cannot verify role - at least one argument is null");
    }
    LOGGER.info(String
        .format("Verify whether user [%s] has role [%s] on namespace [%s]", user.getUsername(),
            role.getName(), namespace.getName()));
    if (!namespaceRolesRepository.findAll().contains(role)) {
      throw new IllegalArgumentException(String.format("Role [%s] is unknown", role.getName()));
    }
    UserNamespaceRoles userNamespaceRoles = userNamespaceRoleRepository
        .getUserNamespaceRolesByUserAndNamespace(user, namespace);
    if (Objects.isNull(userNamespaceRoles)) {
      return false;
    }
    return (userNamespaceRoles.getRoles() & role.getRole()) == role.getRole();
  }

  /**
   * @param username
   * @param namespaceName
   * @param roleName
   * @return
   * @see UserNamespaceRoleService#hasRole(User, Namespace, IRole)
   */
  public boolean hasRole(String username, String namespaceName, String roleName) {
    LOGGER.info(String
        .format("Retrieving user [%s], namespace [%s] and role [%s]", username, namespaceName,
            roleName));
    User user = userRepository.findByUsername(username);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    IRole role = namespaceRolesRepository.find(roleName);
    return hasRole(user, namespace, role);
  }

  /**
   * Returns all {@link IRole}s the given {@link User} has on the given {@link Namespace}.
   *
   * @param user
   * @param namespace
   * @return all {@link IRole}s the given {@link User} has on the given {@link Namespace}.
   */
  public Collection<IRole> getRoles(User user, Namespace namespace) {
    if (Stream.of(user, namespace).anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("Cannot get roles - at least one argument is null");
    }
    LOGGER.info(String
        .format("Retrieving roles for user [%s] on namespace [%s]", user.getUsername(),
            namespace.getName()));
    Set<NamespaceRole> allRoles = namespaceRolesRepository.findAll();
    UserNamespaceRoles userNamespaceRoles = userNamespaceRoleRepository
        .getUserNamespaceRolesByUserAndNamespace(user, namespace);
    return allRoles.stream()
        .filter(r -> (userNamespaceRoles.getRoles() & r.getRole()) == r.getRole()).collect(
            Collectors.toSet());
  }

  /**
   * @param username
   * @param namespaceName
   * @return
   * @see UserNamespaceRoleService#getRoles(User, Namespace)
   */
  public Collection<IRole> getRoles(String username, String namespaceName) {
    User user = userRepository.findByUsername(username);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    return getRoles(user, namespace);
  }
}
