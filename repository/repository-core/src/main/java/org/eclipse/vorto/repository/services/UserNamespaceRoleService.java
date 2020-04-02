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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.NamespaceRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceID;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.repositories.NamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserNamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(UserNamespaceRoleService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private NamespaceRepository namespaceRepository;

  @Autowired
  private NamespaceRoleRepository namespaceRoleRepository;

  @Autowired
  private UserNamespaceRoleRepository userNamespaceRoleRepository;

  @Autowired
  private ServiceValidationUtil validator;

  /**
   * @param user
   * @param namespace
   * @param role
   * @return
   * @see org.eclipse.vorto.repository.account.impl.DefaultUserAccountService#hasRole(String, String, String)
   */
  public boolean hasRole(User user, Namespace namespace, IRole role) {
    validator.validateNulls(user, namespace, role);
    LOGGER.info(String
        .format("Verify whether user [%s] has role [%s] on namespace [%s]", user.getUsername(),
            role.getName(), namespace.getName()));
    if (!namespaceRoleRepository.findAll().contains(role)) {
      throw new IllegalArgumentException(String.format("Role [%s] is unknown", role.getName()));
    }
    UserNamespaceRoles userNamespaceRoles = userNamespaceRoleRepository
        .findOne(new UserNamespaceID(user, namespace));
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
    IRole role = namespaceRoleRepository.find(roleName);
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
    validator.validateNulls(user, namespace);
    LOGGER.info(String
        .format("Retrieving roles for user [%s] on namespace [%s]", user.getUsername(),
            namespace.getName()));
    Set<NamespaceRole> allRoles = namespaceRoleRepository.findAll();
    UserNamespaceRoles userNamespaceRoles = userNamespaceRoleRepository
        .findOne(new UserNamespaceID(user, namespace));
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

  /**
   * Adds the given {@link IRole} to the given {@link User} on the given {@link Namespace}.
   *
   * @param user
   * @param namespace
   * @param role
   * @return {@literal true} if the user did not have the role on the namespace prior to adding it, {@literal false} if they already had the role.
   */
  public boolean addRole(User user, Namespace namespace, IRole role) {
    validator.validateNulls(user, namespace, role);
    UserNamespaceRoles roles = userNamespaceRoleRepository
        .findOne(new UserNamespaceID(user, namespace));
    // no association exists yet between given user and namespace
    if (roles == null) {
      roles = new UserNamespaceRoles();
      roles.setID(new UserNamespaceID(user, namespace));
      roles.setRoles(roles.getRoles() + role.getRole());
      return userNamespaceRoleRepository.save(roles) != null;
    } else {
      // user already has that role on that namespace
      if ((roles.getRoles() & role.getRole()) == role.getRole()) {
        return false;
      } else {
        roles.setRoles(roles.getRoles() + role.getRole());
        return userNamespaceRoleRepository.save(roles) != null;
      }
    }
  }

  /**
   * @param username
   * @param namespaceName
   * @param roleName
   * @return
   * @see UserNamespaceRoleService#addRole(User, Namespace, IRole)
   */
  public boolean addRole(String username, String namespaceName, String roleName) {
    LOGGER.info(String
        .format("Retrieving user [%s], namespace [%s] and role [%s]", username, namespaceName,
            roleName));
    User user = userRepository.findByUsername(username);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    IRole role = namespaceRoleRepository.find(roleName);
    return addRole(user, namespace, role);
  }

  /**
   * Removes the given {@link IRole} from the given {@link User} on the given {@link Namespace}.<br/>
   * Will return {@literal true} if the user and namespace association exists and the user had that
   * role on the namespace.<br/>
   * Will return {@literal false} for any other reason, such as: no user-namespace association
   * exists, the user did not have that role on the namespace to start with, other failures at
   * repository-level...
   *
   * @param user
   * @param namespace
   * @param role
   * @return {@literal true} if the operation succeeded, {@literal false} otherwise or if not applicable.
   */
  public boolean removeRole(User user, Namespace namespace, IRole role) {
    validator.validateNulls(user, namespace, role);
    UserNamespaceRoles roles = userNamespaceRoleRepository
        .findOne(new UserNamespaceID(user, namespace));
    // no association exists between given user and namespace
    if (roles == null) {
      return false;
    } else {
      // user does not have that role on that namespace
      if ((roles.getRoles() & role.getRole()) != role.getRole()) {
        return false;
      } else {
        roles.setRoles(roles.getRoles() - role.getRole());
        return userNamespaceRoleRepository.save(roles) != null;
      }
    }
  }

  /**
   * @param username
   * @param namespaceName
   * @param roleName
   * @return
   * @see UserNamespaceRoleService#removeRole(User, Namespace, IRole)
   */
  public boolean removeRole(String username, String namespaceName, String roleName) {
    LOGGER.info(String
        .format("Retrieving user [%s], namespace [%s] and role [%s]", username, namespaceName,
            roleName));
    User user = userRepository.findByUsername(username);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    IRole role = namespaceRoleRepository.find(roleName);
    return removeRole(user, namespace, role);
  }

  /**
   * Sets the roles of the given {@link User} on the given {@link Namespace} with a value as a power
   * of {@literal 2}.<br/>
   * This method is private as the numeric value is not checked.
   *
   * @param user
   * @param namespace
   * @param rolesValue
   * @return {@literal true} if operation succeeded, {@literal false} if operation not required or failed to persist.
   * @see UserNamespaceRoleService#setRoles(User, Namespace, Set)
   */
  private boolean setRoles(User user, Namespace namespace, long rolesValue) {
    validator.validateNulls(user, namespace);
    UserNamespaceRoles roles = userNamespaceRoleRepository
        .findOne(new UserNamespaceID(user, namespace));
    // no association exists yet between given user and namespace
    if (roles == null) {
      roles = new UserNamespaceRoles();
      roles.setRoles(rolesValue);
      roles.setUser(user);
      roles.setNamespace(namespace);
      return userNamespaceRoleRepository.save(roles) != null;
    } else {
      // user already has those roles on that namespace
      if (roles.getRoles() == rolesValue) {
        return false;
      } else {
        roles.setRoles(rolesValue);
        return userNamespaceRoleRepository.save(roles) != null;
      }
    }
  }

  /**
   * Assigns the given {@link IRole}s to the given {@link User} on the given {@link Namespace}.<br/>
   * Overwrites existing roles if any.
   *
   * @param user
   * @param namespace
   * @param roles
   * @return {@literal true} if operation succeeded, {@literal false} if operation not required or failed to persist.
   * @see UserNamespaceRoleService#addRole(User, Namespace, IRole) to add a new role while preserving existing ones.
   */
  public boolean setRoles(User user, Namespace namespace, Set<IRole> roles) {
    validator.validateNulls(user, namespace, roles);
    if (roles.stream().map(IRole::getName).anyMatch(s -> !namespaceRoleRepository.exists(s))) {
      throw new IllegalArgumentException("Unknown roles - cannot set.");
    }
    return setRoles(user, namespace,
        roles.stream().collect(Collectors.summingLong(IRole::getRole)));
  }

  /**
   * @param username
   * @param namespaceName
   * @param roleNames
   * @return
   * @see UserNamespaceRoleService#setRoles(User, Namespace, Set)
   */
  public boolean setRoles(String username, String namespaceName, Set<String> roleNames) {
    LOGGER.info(String
        .format("Retrieving user [%s], namespace [%s] and roles [%s]", username, namespaceName,
            roleNames));
    User user = userRepository.findByUsername(username);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    Set<IRole> roles = roleNames.stream().map(namespaceRoleRepository::find)
        .collect(Collectors.toSet());
    return setRoles(user, namespace, roles);
  }

  /**
   * Sets all available roles to the given {@link User} on the given {@link Namespace}.<br/>
   * Does not impact on namespace ownership.
   *
   * @param user
   * @param namespace
   * @return
   */
  public boolean setAllRoles(User user, Namespace namespace) {
    validator.validateNulls(user, namespace);
    return setRoles(user, namespace,
        namespaceRoleRepository.findAll().stream().map(r -> (IRole) r).collect(
            Collectors.toSet()));
  }

  /**
   * @param username
   * @param namespaceName
   * @return
   * @see UserNamespaceRoleService#setAllRoles(User, Namespace)
   */
  public boolean setAllRoles(String username, String namespaceName) {
    LOGGER.info(String
        .format("Retrieving user [%s] and namespace [%s]", username, namespaceName));
    User user = userRepository.findByUsername(username);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    return setAllRoles(user, namespace);
  }

}
