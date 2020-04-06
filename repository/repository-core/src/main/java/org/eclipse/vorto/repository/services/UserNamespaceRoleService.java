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
import javax.transaction.Transactional;
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
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * This service reports information and manipulates user roles on namespaces.<br/>
 * It is session-scoped, as any user with administrative privileges on a namespace can change
 * other users' access to it by means of roles.
 */
// TODO #2265: some validation operations could be reused by autowiring the NamespaceService, which may, however, introduce a Spring circular dependency
// TODO #2265: should operations on namespace roles trigger new application events?
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
  private UserRepositoryRoleService userRepositoryRoleService;

  @Autowired
  private ServiceValidationUtil validator;

  /**
   * The {@literal namespace_admin} role, used in many authorization checks.
   */
  public final IRole namespaceAdmin;

  public UserNamespaceRoleService() {
    namespaceAdmin = namespaceRoleRepository.find("namespace_admin");
  }

  // utility methods

  /**
   * Verifies whether the given {@literal actor} {@link User} is either sysadmin, or has the
   * {@literal namespace_admin} role on the given {@link Namespace}.<br/>
   * Throws {@link OperationForbiddenException} if no condition above applies.
   *
   * @param actor
   * @param namespace
   * @throws OperationForbiddenException
   */
  public void authorizeActorOnNamespace(User actor, Namespace namespace)
      throws OperationForbiddenException {
    // authorizing actor
    // not sysadmin
    if (!userRepositoryRoleService.isSysadmin(actor)) {
      // not namespace admin
      if (!hasRole(actor, namespace, namespaceAdmin)) {
        throw new OperationForbiddenException(
            String.format(
                "Acting user is not authorized to manipulate namespace roles for target user on namespace [%s] - aborting operation.",
                namespace.getName()
            )
        );
      }
    }
  }

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
   * Adds the given {@link IRole} to the given {@link User} on the given {@link Namespace}, as
   * acted by the {@literal actor} {@link User} if so authorized.<br/>
   * The pre-condition for authorizing this operation is that the actor is either sysadmin, or has
   * administrative privileges on the given {@link Namespace}.
   *
   * @param actor
   * @param target
   * @param namespace
   * @param role
   * @return {@literal true} if the user did not have the role on the namespace prior to adding it, {@literal false} if they already had the role.
   */
  public boolean addRole(User actor, User target, Namespace namespace, IRole role)
      throws OperationForbiddenException {
    // boilerplate null validation
    validator.validateNulls(actor, target, namespace, role);
    validator.validateNulls(actor.getId(), target.getId());

    // authorizing actor
    authorizeActorOnNamespace(actor, namespace);

    UserNamespaceRoles roles = userNamespaceRoleRepository
        .findOne(new UserNamespaceID(target, namespace));
    // no association exists yet between given user and namespace
    if (roles == null) {
      roles = new UserNamespaceRoles();
      roles.setID(new UserNamespaceID(target, namespace));
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
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @param roleName
   * @return
   * @see UserNamespaceRoleService#addRole(User, User, Namespace, IRole)
   */
  public boolean addRole(String actorUsername, String targetUsername, String namespaceName,
      String roleName) throws OperationForbiddenException {
    LOGGER.info(String
        .format("Retrieving user, namespace [%s] and role [%s]", namespaceName,
            roleName));
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(targetUsername);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    IRole role = namespaceRoleRepository.find(roleName);
    return addRole(actor, target, namespace, role);
  }

  /**
   * Removes the given {@link IRole} from the given {@link User} on the given {@link Namespace}.<br/>
   * Will return {@literal true} if the user and namespace association exists and the user had that
   * role on the namespace.<br/>
   * Will return {@literal false} for any other reason, such as: no user-namespace association
   * exists, the user did not have that role on the namespace to start with, other failures at
   * repository-level...
   *
   * @param actor
   * @param target
   * @param namespace
   * @param role
   * @return {@literal true} if the operation succeeded, {@literal false} otherwise or if not applicable.
   */
  public boolean removeRole(User actor, User target, Namespace namespace, IRole role)
      throws OperationForbiddenException {
    // boilerplate null validation
    validator.validateNulls(actor, target, namespace, role);
    validator.validateNulls(actor.getId(), target.getId());

    // authorizing actor on namespace
    authorizeActorOnNamespace(actor, namespace);

    UserNamespaceRoles roles = userNamespaceRoleRepository
        .findOne(new UserNamespaceID(target, namespace));
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
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @param roleName
   * @return
   * @see UserNamespaceRoleService#removeRole(User, User, Namespace, IRole)
   */
  public boolean removeRole(String actorUsername, String targetUsername, String namespaceName,
      String roleName) throws OperationForbiddenException {
    LOGGER.info(String
        .format("Retrieving user, namespace [%s] and role [%s]", namespaceName,
            roleName));
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(targetUsername);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    IRole role = namespaceRoleRepository.find(roleName);
    return removeRole(actor, target, namespace, role);
  }

  /**
   * Sets the roles of the given {@link User} on the given {@link Namespace} with a value as a power
   * of {@literal 2}.<br/>
   * This method is private as the numeric value is not checked.
   *
   * @param actor
   * @param target
   * @param namespace
   * @param rolesValue
   * @return {@literal true} if operation succeeded, {@literal false} if operation not required or failed to persist.
   * @see UserNamespaceRoleService#setRoles(User, User, Namespace, Set)
   */
  private boolean setRoles(User actor, User target, Namespace namespace, long rolesValue)
      throws OperationForbiddenException {
    // boilerplate null validation
    validator.validateNulls(actor, target, namespace);
    validator.validateNulls(actor.getId(), target.getId());

    // authorizing actor on namespace
    authorizeActorOnNamespace(actor, namespace);

    // retrieving existing roles
    UserNamespaceRoles roles = userNamespaceRoleRepository
        .findOne(new UserNamespaceID(target, namespace));

    // no association exists yet between given user and namespace
    if (roles == null) {
      roles = new UserNamespaceRoles();
      roles.setRoles(rolesValue);
      roles.setUser(target);
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
   * @param target
   * @param actor
   * @param namespace
   * @param roles
   * @return {@literal true} if operation succeeded, {@literal false} if operation not required or failed to persist.
   * @see UserNamespaceRoleService#addRole(User, User, Namespace, IRole) to add a new role while preserving existing ones.
   */
  public boolean setRoles(User actor, User target, Namespace namespace, Set<IRole> roles)
      throws DoesNotExistException, OperationForbiddenException {
    // boilerplate null validation
    validator.validateNulls(actor, target, namespace, roles);
    validator.validateNulls(actor.getId(), target.getId(),
        roles.stream().map(IRole::getName).collect(Collectors.toSet()));

    if (roles.stream().map(IRole::getName).anyMatch(s -> !namespaceRoleRepository.exists(s))) {
      throw new DoesNotExistException("Unknown role - aborting operation.");
    }
    return setRoles(actor, target, namespace,
        roles.stream().collect(Collectors.summingLong(IRole::getRole)));
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @param roleNames
   * @return
   * @see UserNamespaceRoleService#setRoles(User, User, Namespace, Set)
   */
  public boolean setRoles(String actorUsername, String targetUsername, String namespaceName,
      Set<String> roleNames) throws DoesNotExistException, OperationForbiddenException {
    LOGGER.info(String
        .format("Retrieving user, namespace [%s] and roles [%s]", namespaceName,
            roleNames));
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(targetUsername);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    Set<IRole> roles = roleNames.stream().map(namespaceRoleRepository::find)
        .collect(Collectors.toSet());
    return setRoles(actor, target, namespace, roles);
  }

  /**
   * Sets all available roles to the given {@link User} on the given {@link Namespace}.<br/>
   * Does not impact on namespace ownership.
   *
   * @param actor
   * @param target
   * @param namespace
   * @return
   */
  public boolean setAllRoles(User actor, User target, Namespace namespace)
      throws DoesNotExistException, OperationForbiddenException {
    // boilerplate null validation
    validator.validateNulls(actor, target, namespace);
    return setRoles(actor, target, namespace,
        namespaceRoleRepository.findAll().stream().map(r -> (IRole) r).collect(
            Collectors.toSet()));
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @return
   * @see UserNamespaceRoleService#setAllRoles(User, User, Namespace)
   */
  public boolean setAllRoles(String actorUsername, String targetUsername, String namespaceName)
      throws DoesNotExistException, OperationForbiddenException {
    LOGGER.info(String
        .format("Retrieving user and namespace [%s]", namespaceName));
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(targetUsername);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    return setAllRoles(actor, target, namespace);
  }

  /**
   * Deletes the {@link User} + {@link Namespace} role association for the given {@link User} and
   * {@link Namespace} entirely. <br/>
   * If the given {@link User} owns the {@link Namespace}, logs a warning to signify the namespace
   * ownership will be "orphaned", and the namespace should either be deleted or its ownership
   * transferred to another user.<br/>
   * The operation is permitted only if the {@literal actor} {@link User} is either sysadmin, or
   * the owner of the given {@link Namespace}.
   *
   * @param actor
   * @param target
   * @param namespace
   * @return
   * @throws DoesNotExistException
   */
  @Transactional(rollbackOn = {DoesNotExistException.class, OperationForbiddenException.class})
  public boolean deleteAllRoles(User actor, User target, Namespace namespace)
      throws DoesNotExistException, OperationForbiddenException {
    // boilerplate null validation
    validator.validateNulls(actor, target, namespace);
    validator.validateNulls(actor.getId(), target.getId(), namespace.getId());

    // namespace does not exist
    if (!namespaceRepository.exists(namespace.getId())) {
      throw new DoesNotExistException(
          "Namespace [%s] does not exist - aborting deletion of user roles.");
    }

    // checking users
    if (!userRepository.exists(actor.getId())) {
      throw new DoesNotExistException("Acting user does not exist - aborting deletion of roles.");
    }
    if (!userRepository.exists(target.getId())) {
      throw new DoesNotExistException("Target user does not exist - aborting deletion of roles.");
    }

    // checking actor privileges

    // actor not sysadmin
    if (!userRepositoryRoleService.isSysadmin(actor)) {
      // actor not target, or target does not own the namespace
      if (!actor.equals(target) || !target.equals(namespace.getOwner())) {
        throw new OperationForbiddenException(
            String.format("Acting user cannot delete user roles for namespace [%s].",
                namespace.getName())
        );
      }
    }

    UserNamespaceRoles rolesToDelete = new UserNamespaceRoles();
    rolesToDelete.setUser(target);
    rolesToDelete.setNamespace(namespace);

    // user-namespace role association does not exist
    if (!userNamespaceRoleRepository.exists(rolesToDelete.getID())) {
      LOGGER.warn("Attempting to delete non existing user namespace roles. Aborting.");
      return false;
    }

    userNamespaceRoleRepository.delete(rolesToDelete.getID());
    LOGGER.info("Deleted user-namespace role association.");

    return true;
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @return
   * @throws DoesNotExistException
   * @throws OperationForbiddenException
   * @see UserNamespaceRoleService#deleteAllRoles(User, User, Namespace)
   */
  public boolean deleteAllRoles(String actorUsername, String targetUsername, String namespaceName)
      throws DoesNotExistException, OperationForbiddenException {
    LOGGER.info(String.format("Retrieving users and namespace [%s].", namespaceName));
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(targetUsername);
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    return deleteAllRoles(actor, target, namespace);
  }

  public Collection<User> getCollaborators(User actor, Namespace namespace) {
    // boilerplate null validation
    validator.validateNulls(actor, namespace);
    // TODO
    return null;
  }
}
