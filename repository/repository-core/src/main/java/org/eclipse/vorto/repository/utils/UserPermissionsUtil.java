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
package org.eclipse.vorto.repository.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Permission;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceID;
import org.eclipse.vorto.repository.domain.UserPermissions;
import org.eclipse.vorto.repository.repositories.PermissionsRepository;
import org.eclipse.vorto.repository.repositories.UserPermissionsRepository;
import org.eclipse.vorto.repository.tenant.repository.INamespaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Provides utility API methods to infer user permissions on namespaces.
 */
@Component
@Scope("session")
public class UserPermissionsUtil {

  @Autowired
  private UserPermissionsRepository userPermissionsRepository;

  @Autowired
  private IUserRepository userRepository;

  @Autowired
  private INamespaceRepository namespaceRepository;

  @Autowired
  private PermissionsRepository permissionsRepository;

  /**
   * Returns whether the given user has the given permission on the given namespace, by performing
   * bitwise {@code userPermissions & permission == permission}.
   *
   * @param user
   * @param namespace
   * @param permission
   * @return
   */
  public boolean hasPermission(User user, Namespace namespace, Permission permission) {
    Optional<UserPermissions> userPermissions = getUserPermissions(user, namespace);
    if (userPermissions.isPresent()) {
      return (userPermissions.get().getPermissions() & permission.getPermission()) == permission.getPermission();
    }
    else {
      return false;
    }
  }

  /**
   * @see UserPermissionsUtil#hasPermission(User, Namespace, Permission)
   * @param username
   * @param namespaceName
   * @param permissionName
   * @return
   */
  public boolean hasPermission(String username, String namespaceName, String permissionName) {
    return hasPermission(userRepository.findByUsername(username),
        namespaceRepository.findByName(namespaceName), permissionsRepository.find(permissionName));
  }

  /**
   * Retrieves the permission constants the given {@link User} has on the given {@link Namespace},
   * or an empty {@link Collection} if no such user-namespace association exists.
   * @param user
   * @param namespace
   * @return
   */
  public Collection<Permission> getPermissions(User user, Namespace namespace) {
    Optional<UserPermissions> userPermissions = getUserPermissions(user, namespace);
    if (!userPermissions.isPresent()) {
      return Collections.emptySet();
    }
    UserPermissions existingUserPermissions = userPermissions.get();
    return permissionsRepository.findAll().stream().map(
        p -> ((existingUserPermissions.getPermissions() & p.getPermission()) == p.getPermission() ? p : null)
    )
    .filter(Objects::nonNull)
    .collect(Collectors.toSet());
  }

  /**
   * @see UserPermissionsUtil#getPermissions(User, Namespace)
   * @param username
   * @param namespaceName
   * @return
   */
  public Collection<Permission> getPermissions(String username, String namespaceName) {
    return getPermissions(userRepository.findByUsername(username), namespaceRepository.findByName(namespaceName));
  }

  /**
   * Retrieves the {@link UserPermissions} entity associating the given {@link User} with the given
   * {@link Namespace}.
   * @param user
   * @param namespace
   * @return
   */
  public Optional<UserPermissions> getUserPermissions(User user, Namespace namespace) {
    return Optional.ofNullable(userPermissionsRepository.findOne(getUserPermissionID(user, namespace)));
  }

  /**
   * Creates a compound ID of the given {@link User} and {@link Namespace} to query for user
   * permissions associating the two.
   * @param user
   * @param namespace
   * @return
   */
  public static UserNamespaceID getUserPermissionID(User user, Namespace namespace) {
    return new UserNamespaceID(user, namespace);
  }

}
