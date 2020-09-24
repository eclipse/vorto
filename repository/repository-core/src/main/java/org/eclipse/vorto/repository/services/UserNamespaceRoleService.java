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

import static org.eclipse.vorto.repository.services.NamespaceService.NAMESPACE_SEPARATOR;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.eclipse.vorto.repository.core.impl.cache.NamespaceRequestCache;
import org.eclipse.vorto.repository.core.impl.cache.UserRolesRequestCache;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceID;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.AddedToNamespaceMessage;
import org.eclipse.vorto.repository.notification.message.RemovedFromNamespaceMessage;
import org.eclipse.vorto.repository.notification.message.RolesChangedInNamespaceMessage;
import org.eclipse.vorto.repository.repositories.NamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserNamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * This service reports information and manipulates user roles on namespaces.<br/>
 * It is session-scoped, as any user with administrative privileges on a namespace can change
 * other users' access to it by means of roles.<br/>
 * Role changes or collaborator removals trigger fire-and-forget notification messages to the
 * targeted user, through {@link INotificationService#sendNotificationAsync(org.eclipse.vorto.repository.notification.IMessage)}. <br/>
 * Failures to send are not logged, and sending does not block.
 */
@Service
public class UserNamespaceRoleService implements ApplicationEventPublisherAware {

  private static final Logger LOGGER = LoggerFactory.getLogger(UserNamespaceRoleService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private NamespaceRequestCache namespaceRequestCache;

  @Autowired
  private NamespaceRoleRepository namespaceRoleRepository;

  @Autowired
  private UserNamespaceRoleRepository userNamespaceRoleRepository;

  @Autowired
  private RoleUtil roleUtil;

  @Autowired
  private UserUtil userUtil;

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserRolesRequestCache cache;

  @Autowired
  private INotificationService notificationService;

  private ApplicationEventPublisher eventPublisher;

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }

  // utility methods

  /**
   * The {@literal namespace_admin} role used in many authorization scenarios.
   *
   * @return
   */
  public IRole namespaceAdminRole() {
    return namespaceRoleRepository.find("namespace_admin");
  }

  public Collection<IRole> allRoles() {
    return namespaceRoleRepository.findAll().stream().map(r -> (IRole) r)
        .collect(Collectors.toSet());
  }

  /**
   * Verifies whether the given {@literal user} {@link User} is either sysadmin, or has the
   * {@literal namespace_admin} role on the given {@link Namespace}.<br/>
   * Throws {@link OperationForbiddenException} if no condition above applies.
   *
   * @param user
   * @param namespace
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   */
  public void authorizeActorAsAdminOrOwnerOnNamespace(User user, Namespace namespace)
      throws OperationForbiddenException, DoesNotExistException {
    // authorizing user
    // not sysadmin
    if (!cache
        .withUser(user)
        .getUserRepositoryRoles()
        .stream()
        .anyMatch(
            urr ->
                (urr.getRoles() & RepositoryRole.SYS_ADMIN.getRole()) == RepositoryRole.SYS_ADMIN
                    .getRole()
        )
    ) {
      // not namespace admin
      if (!hasRole(user, namespace, namespaceAdminRole())) {
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
   * Verifies whether the given {@literal user} {@link User} is either sysadmin, or has any role on
   * the given {@link Namespace}.<br/>
   * Throws {@link OperationForbiddenException} if no condition above applies.
   *
   * @param user
   * @param namespace
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   */
  public void authorizeActorAsCollaboratorOnNamespace(User user, Namespace namespace)
      throws OperationForbiddenException, DoesNotExistException {
    // authorizing user
    // not sysadmin
    if (!cache
        .withUser(user)
        .getUserRepositoryRoles()
        .stream()
        .anyMatch(
            urr ->
                (urr.getRoles() & RepositoryRole.SYS_ADMIN.getRole()) == RepositoryRole.SYS_ADMIN
                    .getRole()
        )
    ) {
      // not namespace admin
      if (getRoles(user, namespace).isEmpty()) {
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
   * Returns whether the given {@link User} has the given {@link IRole} on the given
   * {@link Namespace}.<br/>
   * Cached - see {@link UserRolesRequestCache}.
   *
   * @param user
   * @param namespace
   * @param role
   * @return
   */
  public boolean hasRole(User user, Namespace namespace, IRole role) throws DoesNotExistException {
    ServiceValidationUtil.validate(user, namespace, role);
    LOGGER.debug(String
        .format("Verify whether user has role [%s] on namespace [%s]",
            role.getName(), namespace.getName()));

    // bypassing further tests if user is sysadmin
    if (cache
        .withUser(user)
        .getUserRepositoryRoles()
        .stream()
        .anyMatch(
            urr ->
                (urr.getRoles() & RepositoryRole.SYS_ADMIN.getRole()) == RepositoryRole.SYS_ADMIN
                    .getRole()
        )
    ) {
      return true;
    }

    if (!namespaceRoleRepository.findAll().contains(role)) {
      throw new IllegalArgumentException(String.format("Role [%s] is unknown", role.getName()));
    }
    return cache
        // initializes or retrieves request cache
        .withUser(user)
        // get user namespace roles for user
        .getUserNamespaceRoles()
        .stream()
        // filters by given namespace
        .filter(unr -> namespace.equals(unr.getNamespace()))
        .findAny()
        // roles found: returns whether user has role through bitwise &
        .map(unr -> (unr.getRoles() & role.getRole()) == role.getRole())
        // roles not found: returns false
        .orElse(false);
  }

  /**
   * @param username
   * @param namespaceName
   * @param roleName
   * @return
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#hasRole(User, Namespace, IRole)
   */
  public boolean hasRole(String username, String namespaceName, String roleName)
      throws DoesNotExistException {
    LOGGER.debug(String
        .format("Retrieving user, namespace [%s] and role [%s]", namespaceName,
            roleName));

    User user = cache.withUser(username).getUser();
    Namespace namespace = resolveByNameOrParentName(namespaceName);
    IRole role = namespaceRoleRepository.find(roleUtil.normalize(roleName));

    return hasRole(user, namespace, role);
  }

  /**
   * Returns whether there is any association between the given {@link Namespace} and the given
   * {@link User}.<br/>
   * Cached - see {@link UserRolesRequestCache}.
   *
   * @param user
   * @param namespace
   * @return
   */
  public boolean hasAnyRole(User user, Namespace namespace) throws DoesNotExistException {
    ServiceValidationUtil.validate(user, namespace);
    LOGGER.debug(
        String
            .format("Verifying whether user has any role on namespace [%s]",
                namespace.getName()
            )
    );
    return cache
        .withUser(user)
        .getUserNamespaceRoles()
        .stream()
        .anyMatch(unr -> namespace.equals(unr.getNamespace()));
  }

  /**
   * @param username
   * @param namespaceName
   * @return
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#hasAnyRole(User, Namespace)
   */
  public boolean hasAnyRole(String username, String namespaceName) throws DoesNotExistException {
    User user = cache.withUser(username).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    return hasAnyRole(user, namespace);
  }

  /**
   * Recursive method that resolves the given {@code namespaceName} argument, looks for an exact
   * match in the repository, and "moves one level up" if the exact match is not found, by
   * searching for a namespace name minus the last {@literal .}-delimited segment. <br/>
   * Returns {@literal null} if no match is found and no segment remains. <br/>
   * Used in conjunction with requests containing "virtual" namespace names (or "sub-domains") that
   * are only persisted as JCR child folder nodes, but not actual namespaces in addition to their
   * "parent" in the relevant DB tables.
   *
   * @param namespaceName
   * @return
   */
  public Namespace resolveByNameOrParentName(String namespaceName) {
    return namespaceRequestCache.namespace(namespaceName).orElseGet(
        () -> {
          int lastSeparator = namespaceName.lastIndexOf(NAMESPACE_SEPARATOR);
          if (lastSeparator > 0) {
            return resolveByNameOrParentName(namespaceName.substring(0, lastSeparator));
          } else {
            return null;
          }
        }
    );
  }

  /**
   * Returns all {@link IRole}s the given {@link User} has on the given {@link Namespace}.<br/>
   * Cached - see {@link UserRolesRequestCache}.
   *
   * @param user
   * @param namespace
   * @return all {@link IRole}s the given {@link User} has on the given {@link Namespace}.
   */
  public Collection<IRole> getRoles(User user, Namespace namespace) throws DoesNotExistException {
    ServiceValidationUtil.validate(user, namespace);
    LOGGER.debug(String
        .format("Retrieving roles for user and namespace [%s]", namespace.getName()));
    return cache
        // initializes or retrieves request cache
        .withUser(user)
        // get user namespace roles for user
        .getUserNamespaceRoles()
        .stream()
        // filters by given namespace
        .filter(unr -> namespace.equals(unr.getNamespace()))
        .findAny()
        // roles found: returns roles long converted to Collection<IRole>
        .map(unr -> roleUtil.toNamespaceRoles(unr.getRoles()))
        // roles not found: returns an empty list
        .orElse(Collections.emptyList());
  }

  /**
   * @param username
   * @param namespaceName
   * @return
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getRoles(User, Namespace)
   */
  public Collection<IRole> getRoles(String username, String namespaceName)
      throws DoesNotExistException {
    User user = cache.withUser(username).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    return getRoles(user, namespace);
  }

  /**
   * Only to be used contextually to Spring authorization integration as it artificially adds the
   * {@literal model_viewer} role, delegating context for only public models to the caller.<br/>
   * Cached - see {@link UserRolesRequestCache}.
   *
   * @param user
   * @return a set of all roles the user can have on any namespace, defaulting to {@literal model_viewer}.
   */
  public Set<IRole> getRolesOnAllNamespaces(User user) {
    Set<IRole> result = cache
        .withUser(user)
        .getUserNamespaceRoles()
        .stream()
        .distinct()
        .map(UserNamespaceRoles::getRoles)
        .map(roleUtil::toNamespaceRoles)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());
    return result.isEmpty() ? Sets.newHashSet(modelViewerRole()) : result;
  }

  private IRole modelViewerRole() {
    return roleService.findAnyByName("model_viewer")
        .orElseThrow(() -> new IllegalStateException("Role 'model_viewer' is not present."));
  }

  /**
   * Adds the given {@link IRole} to the given {@link User} on the given {@link Namespace}, as
   * acted by the {@literal actor} {@link User} if so authorized.<br/>
   * The pre-condition for authorizing this operation is that the actor is either sysadmin, or has
   * administrative privileges on the given {@link Namespace}.<br/>
   * Notifies the target user asynchronously if possible.
   *
   * @param actor
   * @param target
   * @param namespace
   * @param role
   * @return {@literal true} if the user did not have the role on the namespace prior to adding it, {@literal false} if they already had the role.
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   */
  public boolean addRole(User actor, User target, Namespace namespace, IRole role)
      throws OperationForbiddenException, DoesNotExistException {
    // boilerplate null validation
    ServiceValidationUtil.validate(actor, target, namespace, role);
    ServiceValidationUtil.validateNulls(actor.getId(), target.getId());

    // authorizing actor
    authorizeActorAsAdminOrOwnerOnNamespace(actor, namespace);

    UserNamespaceRoles roles = cache
        // creating or retrieving cached target user
        .withUser(target)
        // getting their namespace roles
        .getUserNamespaceRoles()
        // filtering by given namespace
        .stream().filter(unr -> unr.getNamespace().equals(namespace))
        .findAny()
        // no roles found on that namespace: creating new roles
        .orElseGet(
            () -> {
              UserNamespaceRoles result = new UserNamespaceRoles();
              result.setID(new UserNamespaceID(target, namespace));
              return result;
            }
        );
    // user already has that role on that namespace, nothing to do and returning false
    if ((roles.getRoles() & role.getRole()) == role.getRole()) {
      return false;
    }
    // adding given role to user roles and returning true if persisting successful
    roles.setRoles(roles.getRoles() + role.getRole());
    boolean result = userNamespaceRoleRepository.save(roles) != null;
    if (result) {
      notificationService.sendNotificationAsync(
          new AddedToNamespaceMessage(
              target,
              namespace.getName(),
              roleUtil.toNamespaceRoles(roles.getRoles())
                  .stream()
                  .map(IRole::getName)
                  .collect(Collectors.toList())
          )
      );
    }
    return result;
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @param roleName
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#addRole(User, User, Namespace, IRole)
   */
  public boolean addRole(String actorUsername, String targetUsername, String namespaceName,
      String roleName) throws OperationForbiddenException, DoesNotExistException {
    LOGGER.debug(String
        .format("Retrieving user, namespace [%s] and role [%s]", namespaceName,
            roleName));
    User actor = cache.withUser(actorUsername).getUser();
    User target = cache.withUser(targetUsername).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    IRole role = namespaceRoleRepository.find(roleUtil.normalize(roleName));
    return addRole(actor, target, namespace, role);
  }

  /**
   * Removes the given {@link IRole} from the given {@link User} on the given {@link Namespace}.<br/>
   * Will return {@literal true} if the user and namespace association exists and the user had that
   * role on the namespace.<br/>
   * Will return {@literal false} for any other reason, such as: no user-namespace association
   * exists, the user did not have that role on the namespace to start with, other failures at
   * repository-level...<br/>
   * Notifies the target user asynchronously if possible.
   *
   * @param actor
   * @param target
   * @param namespace
   * @param role
   * @return {@literal true} if the operation succeeded, {@literal false} otherwise or if not applicable.
   */
  public boolean removeRole(User actor, User target, Namespace namespace, IRole role)
      throws OperationForbiddenException, DoesNotExistException {
    // boilerplate null validation
    ServiceValidationUtil.validate(actor, target, namespace, role);
    ServiceValidationUtil.validateNulls(actor.getId(), target.getId());

    // authorizing actor on namespace
    authorizeActorAsAdminOrOwnerOnNamespace(actor, namespace);

    UserNamespaceRoles roles = cache
        // creating or retrieving cached target user
        .withUser(target)
        // getting their namespace roles
        .getUserNamespaceRoles()
        // filtering by given namespace
        .stream().filter(unr -> unr.getNamespace().equals(namespace))
        .findAny()
        // no roles found on that namespace: creating new roles
        .orElseGet(
            () -> {
              UserNamespaceRoles result = new UserNamespaceRoles();
              result.setID(new UserNamespaceID(target, namespace));
              return result;
            }
        );
    // user does not have that role on that namespace, nothing to do and returning false
    if ((roles.getRoles() & role.getRole()) != role.getRole()) {
      return false;
    } else {
      // removing given role to user roles and returning true if persisting successful
      roles.setRoles(roles.getRoles() - role.getRole());
      boolean result = userNamespaceRoleRepository.save(roles) != null;
      if (result) {
        notificationService.sendNotificationAsync(
            new RolesChangedInNamespaceMessage(
                target,
                namespace.getName(),
                roleUtil.toNamespaceRoles(roles.getRoles())
                    .stream()
                    .map(IRole::getName)
                    .collect(Collectors.toList())
            )
        );
      }
      return result;
    }
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @param roleName
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#removeRole(User, User, Namespace, IRole)
   */
  public boolean removeRole(String actorUsername, String targetUsername, String namespaceName,
      String roleName) throws OperationForbiddenException, DoesNotExistException {
    LOGGER.debug(String
        .format("Retrieving user, namespace [%s] and role [%s]", namespaceName,
            roleName));
    User actor = cache.withUser(actorUsername).getUser();
    User target = cache.withUser(targetUsername).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    IRole role = namespaceRoleRepository.find(roleUtil.normalize(roleName));
    return removeRole(actor, target, namespace, role);
  }

  /**
   * Sets the roles of the given {@link User} on the given {@link Namespace} with a value as a power
   * of {@literal 2}.<br/>
   * This method is private as the numeric value is not checked.<br/>
   * This can fail for a number of reasons:
   * <ul>
   *   <li>Either the actor user, target user or namespace do not exist</li>
   *   <li>
   *     Or, if not parametrized for a new namespace, if the acting user does not have the
   *     {@literal namespace_admin} role on that namespace.
   *   </li>
   * </ul>
   * <br/>
   * Notifies the target user asynchronously if possible.
   *
   * @param actor
   * @param target
   * @param namespace
   * @param rolesValue
   * @param newNamespace
   * @return {@literal true} if operation succeeded, {@literal false} if operation not required or failed to persist.
   */
  private boolean setRoles(User actor, User target, Namespace namespace, long rolesValue,
      boolean newNamespace)
      throws OperationForbiddenException, DoesNotExistException {
    // boilerplate null validation
    ServiceValidationUtil.validate(actor, target, namespace);
    ServiceValidationUtil.validateNulls(actor.getId(), target.getId());

    // authorizing actor on namespace, only if the namespace is not being created for the first time
    if (!newNamespace) {
      authorizeActorAsAdminOrOwnerOnNamespace(actor, namespace);
    }

    UserNamespaceRoles roles = cache
        // creating or retrieving cached target user
        .withUser(target)
        // getting their namespace roles
        .getUserNamespaceRoles()
        // filtering by given namespace
        .stream().filter(unr -> unr.getNamespace().equals(namespace))
        .findAny()
        // no roles found on that namespace: creating new roles
        .orElseGet(
            () -> {
              UserNamespaceRoles result = new UserNamespaceRoles();
              result.setID(new UserNamespaceID(target, namespace));
              return result;
            }
        );

    // user already has those roles on that namespace, nothing to do and returning false
    if (roles.getRoles() == rolesValue) {
      return false;
    } else {
      // assigning given roles to user and returning true if persisting successful
      roles.setRoles(rolesValue);
      boolean result = userNamespaceRoleRepository.save(roles) != null;
      // if saved successfully, notify the target user they have been added as collaborator to the
      // namespace - only triggers when namespace has not just been created
      if (result && !newNamespace) {
        notificationService.sendNotificationAsync(
            new AddedToNamespaceMessage(
                target,
                namespace.getName(),
                roleUtil.toNamespaceRoles(roles.getRoles())
                    .stream()
                    .map(IRole::getName)
                    .collect(Collectors.toList())
            )
        );
      }
      return result;
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
  public boolean setRoles(User actor, User target, Namespace namespace, Collection<IRole> roles,
      boolean newNamespace)
      throws DoesNotExistException, OperationForbiddenException {
    // boilerplate null validation
    ServiceValidationUtil.validate(actor, target, namespace);
    ServiceValidationUtil.validateNulls(roles);
    ServiceValidationUtil.validateNulls(actor.getId(), target.getId());

    return setRoles(actor, target, namespace,
        roles.stream().mapToLong(IRole::getRole).sum(), newNamespace);
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @param roleNames
   * @return
   * @see UserNamespaceRoleService#setRoles(User, User, Namespace, Collection, boolean)
   */
  public boolean setRoles(String actorUsername, String targetUsername, String namespaceName,
      Collection<String> roleNames, boolean newNamespace)
      throws DoesNotExistException, OperationForbiddenException {
    LOGGER.debug(String
        .format("Retrieving user, namespace [%s] and roles [%s]", namespaceName,
            roleNames));
    User actor = cache.withUser(actorUsername).getUser();
    User target = cache.withUser(targetUsername).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    Collection<IRole> roles = roleUtil.toNamespaceRoles(roleNames);
    return setRoles(actor, target, namespace, roles, newNamespace);
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
  public boolean setAllRoles(User actor, User target, Namespace namespace, boolean newNamespace)
      throws DoesNotExistException, OperationForbiddenException {
    // boilerplate null validation
    ServiceValidationUtil.validate(actor, target, namespace);

    return setRoles(actor, target, namespace,
        namespaceRoleRepository.findAll().stream().map(r -> (IRole) r).collect(
            Collectors.toSet()), newNamespace);
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @return
   * @see UserNamespaceRoleService#setAllRoles(User, User, Namespace, boolean)
   */
  public boolean setAllRoles(String actorUsername, String targetUsername, String namespaceName,
      boolean newNamespace)
      throws DoesNotExistException, OperationForbiddenException {
    LOGGER.debug(String
        .format("Retrieving user and namespace [%s]", namespaceName));
    User actor = cache.withUser(actorUsername).getUser();
    User target = cache.withUser(targetUsername).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    return setAllRoles(actor, target, namespace, newNamespace);
  }

  /**
   * Deletes the {@link User} + {@link Namespace} role association for the given {@link User} and
   * {@link Namespace} entirely. <br/>
   * The operation is permitted in the following cases:
   * <ol>
   *   <li>
   *     The acting user is sysadmin.
   *   </li>
   *   <li>
   *     The acting user is trying to remove themselves and is not the only administrator of the
   *     namespace.
   *   </li>
   *   <li>
   *     The acting user is trying to remove themselves and is the only administrator of the
   *     namespace, but they are deleting the namespace - this will fail early
   *     in {@link NamespaceService#deleteNamespace(User, String)} if the namespace has public
   *     models.
   *   </li>
   * </ol>
   * In any other case, the operation will fail and throw {@link OperationForbiddenException}.
   *
   * @param actor
   * @param target
   * @param namespace
   * @param deleteNamespace
   * @return
   * @throws DoesNotExistException
   */
  @Transactional(rollbackOn = {DoesNotExistException.class, OperationForbiddenException.class})
  public boolean deleteAllRoles(User actor, User target, Namespace namespace,
      boolean deleteNamespace)
      throws DoesNotExistException, OperationForbiddenException {
    // boilerplate null validation
    ServiceValidationUtil.validate(actor, target, namespace);
    ServiceValidationUtil.validateNulls(actor.getId(), target.getId(), namespace.getId());

    // namespace does not exist
    if (!namespaceRequestCache.namespace(namespace::equals).isPresent()) {
      throw new DoesNotExistException(
          "Namespace [%s] does not exist - aborting deletion of user roles.");
    }

    // authorizing actor

    // Actor can administrate namespace, but trying to remove themselves, not contextually to
    // deleting the whole namespace: forbidden
    if (hasRole(actor, namespace, namespaceAdminRole()) && actor.equals(target)
        && !deleteNamespace) {
      throw new OperationForbiddenException(
          String.format(
              "Acting user with namespace administrator role cannot remove themselves from namespace [%s].",
              namespace.getName())
      );
    }
    // Actor has no admin role on namespace and is trying to remove somebody else, without being
    // sysadmin
    else if (!hasRole(actor, namespace, namespaceAdminRole()) && !actor.equals(target)
        && !cache
        .withUser(actor)
        .getUserRepositoryRoles()
        .stream()
        .anyMatch(
            urr ->
                (urr.getRoles() & RepositoryRole.SYS_ADMIN.getRole()) == RepositoryRole.SYS_ADMIN
                    .getRole()
        )
    ) {
      throw new OperationForbiddenException(
          String.format("Acting user cannot delete user roles for namespace [%s].",
              namespace.getName())
      );
    }

    Optional<UserNamespaceRoles> rolesToDelete = cache
        // creating or retrieving cached target user
        .withUser(target)
        // getting their namespace roles
        .getUserNamespaceRoles()
        // filtering by given namespace
        .stream().filter(unr -> unr.getNamespace().equals(namespace))
        .findAny();

    // user-namespace role association does not exist
    if (!rolesToDelete.isPresent()) {
      LOGGER.warn("Attempting to delete non existing user namespace roles. Aborting.");
      return false;
    }

    userNamespaceRoleRepository.delete(rolesToDelete.get().getID());
    LOGGER.info("Deleted user-namespace role association.");
    notificationService.sendNotificationAsync(
        new RemovedFromNamespaceMessage(
            target,
            namespace.getName()
        )
    );
    return true;
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @param deleteNamespace
   * @return
   * @throws DoesNotExistException
   * @throws OperationForbiddenException
   * @see UserNamespaceRoleService#deleteAllRoles(User, User, Namespace, boolean)
   */
  public boolean deleteAllRoles(String actorUsername, String targetUsername, String namespaceName,
      boolean deleteNamespace)
      throws DoesNotExistException, OperationForbiddenException {
    LOGGER.debug(String.format("Retrieving users and namespace [%s].", namespaceName));
    User actor = cache.withUser(actorUsername).getUser();
    User target = cache.withUser(targetUsername).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    return deleteAllRoles(actor, target, namespace, deleteNamespace);
  }

  /**
   * Verifies whether the given {@link User} can view the given {@link Namespace}, which always
   * yields true if the user is {@literal sysadmin}, or if the user has any role on the
   * namespace.<br/>
   * Cached - see {@link UserRolesRequestCache}.
   *
   * @param user
   * @param namespace
   * @throws OperationForbiddenException if the user has no view privilege on the namespace.
   */
  public void verifyCanView(User user, Namespace namespace) throws OperationForbiddenException {
    // not sysadmin
    if (!cache
        .withUser(user)
        .getUserRepositoryRoles()
        .stream()
        .anyMatch(
            urr ->
                (urr.getRoles() & RepositoryRole.SYS_ADMIN.getRole()) == RepositoryRole.SYS_ADMIN
                    .getRole()
        )
    ) {
      cache
          // retrieving cached user
          .withUser(user)
          // getting their namespace roles
          .getUserNamespaceRoles()
          .stream()
          // filtering by given namespace
          .filter(unr -> unr.getNamespace().equals(namespace))
          // any role association present is a sufficient criterion here
          .findAny()
          // no roles found on that namespace for that user
          .orElseThrow(
              () -> new OperationForbiddenException(
                  String.format("User has no visibility on namespace [%s].", namespace.getName())
              )
          );
    }
  }

  /**
   * Retrieves the {@link User}s who collaborate (i.e. have any {@link IRole}) on the given
   * {@link Namespace}, filtered optionally by the given roles, expressed as {@code long}.<br/>
   * The operation fails if the acting {@link User} is not authorited to perform it, i.e. they are
   * neither {@literal sysadmin}, nor have any read privilege on the given {@link Namespace}.
   *
   * @param user
   * @param namespace
   * @param roles
   * @return
   * @throws OperationForbiddenException
   */
  public Collection<User> getUsers(User user, Namespace namespace, Long roles)
      throws OperationForbiddenException, DoesNotExistException {
    // boilerplate null validation
    ServiceValidationUtil.validate(user, namespace);

    // authorize actor
    verifyCanView(user, namespace);

    // not cached
    if (roles == null) {
      return userNamespaceRoleRepository.findAllByNamespace(namespace).stream()
          .map(UserNamespaceRoles::getUser).collect(Collectors.toSet());
    } else {
      return userNamespaceRoleRepository.findAllByNamespaceAndRoles(namespace, roles).stream()
          .map(UserNamespaceRoles::getUser).collect(Collectors.toSet());
    }
  }

  /**
   * @param actor
   * @param namespace
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getUsers(User, Namespace, Long)
   */
  public Collection<User> getUsers(User actor, Namespace namespace)
      throws OperationForbiddenException, DoesNotExistException {
    return getUsers(actor, namespace, null);

  }

  /**
   * @param actorUsername
   * @param namespaceName
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getUsers(User, Namespace)
   */
  public Collection<User> getUsers(String actorUsername, String namespaceName)
      throws OperationForbiddenException, DoesNotExistException {
    User actor = cache.withUser(actorUsername).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    return getUsers(actor, namespace);
  }

  /**
   * Builds a {@link Map} of {@link User}s with their respective {@link IRole}s in the given
   * {@link Namespace}, as performed by the acting {@link User}.<br/>
   * The operation will fail if the acting {@link User} is not authorized, i.e. if they are neither
   * {@literal sysadmin} or have no view privileges on the given {@link Namespace}.
   *
   * @param actor
   * @param namespace
   * @return
   * @throws OperationForbiddenException
   */
  public Map<User, Collection<IRole>> getRolesByUser(User actor, Namespace namespace)
      throws OperationForbiddenException, DoesNotExistException {

    ServiceValidationUtil.validate(actor, namespace);

    authorizeActorAsCollaboratorOnNamespace(actor, namespace);

    // not cached
    TreeMap<User, Collection<IRole>> result = new TreeMap<>(
        Comparator.comparing(User::getUsername));
    userNamespaceRoleRepository.findAllByNamespace(namespace)
        .forEach(unr -> result.put(unr.getUser(), roleUtil.toNamespaceRoles(unr.getRoles())));
    return result;
  }

  /**
   * Near-proxy call to repository: retrieves user roles by workspace ID and username, alternatively
   * to the usual namespace+user convention across this service. <br/>
   * This helps removing unnecessary queries to retrieve the namespace name by workspace ID first,
   * and then query for roles in some case.
   *
   * @param workspaceId
   * @param user
   * @return
   * @throws DoesNotExistException
   */
  public Collection<IRole> getRolesByWorkspaceIdAndUser(String workspaceId, User user)
      throws DoesNotExistException {
    ServiceValidationUtil.validateEmpties(workspaceId);
    ServiceValidationUtil.validate(user);
    Collection<IRole> result = cache
        // initializes or retrieves request cache
        .withUser(user)
        // get user namespace roles for user
        .getUserNamespaceRoles()
        .stream()
        // filters by given workspace ID
        .filter(unr -> unr.getNamespace().getWorkspaceId().equals(workspaceId))
        .findAny()
        // roles found: convert to Collection<IRole>
        .map(unr -> roleUtil.toNamespaceRoles(unr.getRoles()))
        // roles not found: returns empty list
        .orElse(Collections.emptyList());
    // weird mixup of role types - added according to logic previously in ModelRepositoryFactory
    if (!cache
        .withUser(user)
        .getUserRepositoryRoles()
        .stream()
        .anyMatch(
            urr ->
                (urr.getRoles() & RepositoryRole.SYS_ADMIN.getRole()) == RepositoryRole.SYS_ADMIN
                    .getRole()
        )
    ) {
      // result is presently returned as unmodifiable
      Collection<IRole> finalResult = new HashSet<>();
      finalResult.addAll(result);
      finalResult.add(RepositoryRole.SYS_ADMIN);
      return finalResult;
    }
    return result;
  }

  /**
   * @param workspaceId
   * @param username
   * @return
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getRolesByWorkspaceIdAndUser(String, String)
   */
  public Collection<IRole> getRolesByWorkspaceIdAndUser(String workspaceId, String username)
      throws DoesNotExistException {
    User user = cache.withUser(username).getUser();
    return getRolesByWorkspaceIdAndUser(workspaceId, user);
  }

  /**
   * Builds a map of all {@link IRole}s by {@link Namespace} where the given target {@link User} has
   * any role, as acted by the acting {@link User}.<br/>
   * Can return an empty map if none found. <br/>
   * Can fail if either the actor or the target do not exist.<br/>
   * Can fail to authorize if the actor is not the same user as the target, or the actor does not
   * have the {@literal sysadmin} repository role.
   *
   * @param actor
   * @param target
   * @return
   * @throws DoesNotExistException
   * @throws OperationForbiddenException
   */
  public Map<Namespace, Collection<IRole>> getNamespacesAndRolesByUser(User actor, User target)
      throws DoesNotExistException, OperationForbiddenException {

    ServiceValidationUtil.validate(actor, target);

    userUtil.authorizeActorAsTargetOrSysadmin(actor, target);

    Collection<UserNamespaceRoles> unr = cache
        .withUser(target)
        .getUserNamespaceRoles();

    if (Objects.isNull(unr) || unr.isEmpty()) {
      return Collections.emptyMap();
    }

    // not caching
    Map<Namespace, Collection<IRole>> result = new TreeMap(
        Comparator.comparing(Namespace::getName));
    unr.forEach(
        r ->
            result.put(r.getNamespace(), roleUtil.toNamespaceRoles(r.getRoles())));
    return result;
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @return
   * @throws DoesNotExistException
   * @throws OperationForbiddenException
   * @see UserNamespaceRoleService#getNamespacesAndRolesByUser(User, User)
   */
  public Map<Namespace, Collection<IRole>> getNamespacesAndRolesByUser(String actorUsername,
      String targetUsername)
      throws DoesNotExistException, OperationForbiddenException {
    User actor = cache.withUser(actorUsername).getUser();
    User target = cache.withUser(targetUsername).getUser();
    return getNamespacesAndRolesByUser(actor, target);
  }

  /**
   * @param actorUsername
   * @param namespaceName
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getRolesByUser(User, Namespace)
   */
  public Map<User, Collection<IRole>> getRolesByUser(String actorUsername,
      String namespaceName) throws OperationForbiddenException, DoesNotExistException {
    User actor = cache.withUser(actorUsername).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    return getRolesByUser(actor, namespace);
  }

  /**
   * This functionality is slightly out of scope in every service. <br/>
   * Its purpose is to create an ad-hoc "technical" user, and add it as collaborator to a given
   * {@link Namespace} with the desired roles.<br/>
   * Presently, there is no scenario where a technical user can be created without being also
   * added as collaborator to a namespace, hence the mixed operation here.
   *
   * @param actor
   * @param technicalUser
   * @param namespace
   * @param roles
   */
  @Transactional(rollbackOn = {OperationForbiddenException.class, InvalidUserException.class})
  public void createTechnicalUserAndAddAsCollaborator(User actor, User technicalUser,
      Namespace namespace, long roles)
      throws OperationForbiddenException, InvalidUserException, DoesNotExistException {
    // boilerplate null validation
    ServiceValidationUtil.validate(actor, namespace);
    ServiceValidationUtil.validateNulls(technicalUser);

    // delegates tech user creation and persistence to user service
    userService.createOrUpdateTechnicalUser(actor, technicalUser);

    // sets the desired namespace-roles association - authorization on namespace is
    // performed here, but failure will revert the transaction including saving the new user
    setRoles(actor, technicalUser, namespace, roles, false);
  }

  /**
   * @param actor
   * @param technicalUser
   * @param namespace
   * @param roles
   * @throws OperationForbiddenException
   * @throws InvalidUserException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#createTechnicalUserAndAddAsCollaborator(User, User, Namespace, long)
   */
  public void createTechnicalUserAndAddAsCollaborator(User actor, User technicalUser,
      Namespace namespace, Collection<IRole> roles)
      throws OperationForbiddenException, InvalidUserException, DoesNotExistException {
    createTechnicalUserAndAddAsCollaborator(actor, technicalUser, namespace,
        roleUtil.toLong(roles));
  }

  /**
   * @param actorUsername
   * @param technicalUser
   * @param namespaceName
   * @param roles
   * @throws OperationForbiddenException
   * @throws InvalidUserException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#createTechnicalUserAndAddAsCollaborator(User, User, Namespace, long)
   */
  public void createTechnicalUserAndAddAsCollaborator(String actorUsername, User technicalUser,
      String namespaceName, Collection<String> roles)
      throws OperationForbiddenException, InvalidUserException, DoesNotExistException {
    User actor = cache.withUser(actorUsername).getUser();
    Namespace namespace = namespaceRequestCache.namespace(namespaceName)
        .orElseThrow(() -> DoesNotExistException.withNamespace(namespaceName));
    Collection<IRole> actualRoles = roleUtil
        .toNamespaceRoles(roles.toArray(new String[roles.size()]));
    createTechnicalUserAndAddAsCollaborator(actor, technicalUser, namespace, actualRoles);
  }

  /**
   * Retrieves all {@link Namespace}s where the given target {@link User} is a collaborator, as
   * acted by the given acting {@link User}.<br/>
   * The {@code roleFilter} parameter can be {@code null} - in which case all {@link Namespace}s
   * where the target {@link User} has any role are returned; otherwise only the {@link Namespace}s
   * where the target {@link User} has the roles represented by the filter are returned.<br/>
   * The operation can fail if the acting {@link User} is neither {@literal sysadmin} nor the same
   * {@link User} as the target.
   *
   * @param actor
   * @param target
   * @param roleFilter
   * @return
   * @throws OperationForbiddenException
   */
  public Collection<Namespace> getNamespaces(User actor, User target, Long roleFilter)
      throws OperationForbiddenException, DoesNotExistException {
    // boilerplate null validation - role filter can be null
    ServiceValidationUtil.validate(actor, target);

    // authorizes actor
    userUtil.authorizeActorAsTargetOrSysadmin(actor, target);

    // if target user is sysadmin, then retrieves all namespaces optionally filtered by role
    if (cache
        .withUser(target)
        .getUserRepositoryRoles()
        .stream()
        .anyMatch(
            urr ->
                (urr.getRoles() & RepositoryRole.SYS_ADMIN.getRole()) == RepositoryRole.SYS_ADMIN
                    .getRole()
        )
    ) {
      return
          namespaceRequestCache.namespaces();
    }

    // retrieves namespaces either filtered by role(s) or all namespaces where target has a role
    return cache
        .withUser(target)
        .getUserNamespaceRoles()
        .stream()
        // filtering:
        .filter(
            // ... by bitwise & if roleFilter present
            roleFilter != null ?
                unr -> ((unr.getRoles() & roleFilter) == roleFilter)
                :
                    // ... or not filtering otherwise
                    unr -> true
        )
        .map(UserNamespaceRoles::getNamespace)
        .collect(Collectors.toSet());
  }

  /**
   * Retrieves all {@link Namespace}s where the given target {@link User} has any role, as acted by
   * the given actor {@link User}.
   *
   * @param actor
   * @param target
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getNamespaces(User, User, Long)
   */
  public Collection<Namespace> getNamespaces(User actor, User target)
      throws OperationForbiddenException, DoesNotExistException {
    return getNamespaces(actor, target, (Long) null);
  }

  /**
   * @param actor
   * @param target
   * @param roles
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getNamespaces(User, User, Long)
   */
  public Collection<Namespace> getNamespaces(User actor, User target, IRole... roles)
      throws OperationForbiddenException, DoesNotExistException {
    return getNamespaces(actor, target,
        roles != null ? roleUtil.toLong(Arrays.asList(roles)) : null);
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param roles
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getNamespaces(User, User, Long)
   */
  public Collection<Namespace> getNamespaces(String actorUsername, String targetUsername,
      IRole... roles) throws OperationForbiddenException, DoesNotExistException {

    ServiceValidationUtil.validateNulls(actorUsername, targetUsername);

    User actor = cache.withUser(actorUsername).getUser();
    User target = cache.withUser(targetUsername).getUser();
    Long actualRoles = null;
    if (roles != null && roles.length > 0) {
      actualRoles = roleUtil.toLong(Arrays.asList(roles));
    }
    return getNamespaces(actor, target, actualRoles);
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param roles
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getNamespaces(User, User, Long)
   */
  public Collection<Namespace> getNamespaces(String actorUsername, String targetUsername,
      String... roles) throws OperationForbiddenException, DoesNotExistException {
    return getNamespaces(actorUsername, targetUsername,
        roles != null ? Arrays.stream(roles).map(namespaceRoleRepository::find)
            .toArray(IRole[]::new) : null);
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getNamespaces(User, User, Long)
   */
  public Collection<Namespace> getNamespaces(String actorUsername, String targetUsername)
      throws OperationForbiddenException, DoesNotExistException {
    return getNamespaces(actorUsername, targetUsername, (IRole[]) null);
  }

  /**
   * Returns all {@link Namespace}s where the target {@link User} has the given roles, mapping by
   * {@link Namespace}, then by collaborators ({@link User}s) and {@link IRole}s, as acted by the
   * given actor {@link User}.<br/>
   * The operation can fail to authorize if the acting {@link User} neither has the
   * {@literal sysadmin} repository role, and they are not the target {@link User}.<br/>
   * If no role filter is specified, will return the map of all {@link Namespace}s where the
   * target {@link User} has any role.
   *
   * @param actor
   * @param target
   * @param roleFilter
   * @return
   */
  public Map<Namespace, Map<User, Collection<IRole>>> getNamespacesCollaboratorsAndRoles(User actor,
      User target, Long roleFilter) throws OperationForbiddenException, DoesNotExistException {
    // boilerplate null validation - role filter can be null
    ServiceValidationUtil.validate(actor, target);

    // authorizes actor
    userUtil.authorizeActorAsTargetOrSysadmin(actor, target);

    Map<Namespace, Map<User, Collection<IRole>>> result = new HashMap<>();

    // not caching
    Collection<Namespace> namespaces = getNamespaces(actor, target, roleFilter);
    for (Namespace n : namespaces) {
      result.putIfAbsent(n, getRolesByUser(actor, n));
    }
    return result;
  }

  /**
   * @param actor
   * @param target
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getNamespacesCollaboratorsAndRoles(User, User, Long)
   */
  public Map<Namespace, Map<User, Collection<IRole>>> getNamespacesCollaboratorsAndRoles(User actor,
      User target) throws OperationForbiddenException, DoesNotExistException {
    return getNamespacesCollaboratorsAndRoles(actor, target, null);
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param roles
   * @return
   * @throws OperationForbiddenException
   * @see UserNamespaceRoleService#getNamespacesCollaboratorsAndRoles(User, User, Long)
   */
  public Map<Namespace, Map<User, Collection<IRole>>> getNamespacesCollaboratorsAndRoles(
      String actorUsername, String targetUsername, IRole... roles)
      throws OperationForbiddenException, DoesNotExistException {
    // boilerplate null validation - role filter can be null
    ServiceValidationUtil.validateNulls(actorUsername, targetUsername);

    User actor = cache.withUser(actorUsername).getUser();
    User target = cache.withUser(targetUsername).getUser();

    Long actualRoles = null;
    if (roles != null && roles.length > 0) {
      actualRoles = roleUtil.toLong(Arrays.asList(roles));
    }

    return getNamespacesCollaboratorsAndRoles(actor, target, actualRoles);
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param roles
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getNamespacesCollaboratorsAndRoles(User, User, Long)
   */
  public Map<Namespace, Map<User, Collection<IRole>>> getNamespacesCollaboratorsAndRoles(
      String actorUsername, String targetUsername, String... roles)
      throws OperationForbiddenException, DoesNotExistException {
    return getNamespacesCollaboratorsAndRoles(actorUsername, targetUsername,
        roles != null ? Arrays.stream(roles).map(namespaceRoleRepository::find)
            .filter(Objects::nonNull)
            .toArray(IRole[]::new) : null);
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#getNamespacesCollaboratorsAndRoles(User, User, Long)
   */
  public Map<Namespace, Map<User, Collection<IRole>>> getNamespacesCollaboratorsAndRoles(
      String actorUsername, String targetUsername)
      throws OperationForbiddenException, DoesNotExistException {
    return getNamespacesCollaboratorsAndRoles(actorUsername, targetUsername, (IRole[]) null);
  }

  /**
   * Verifies whether the target {@link User} is the only user yielding the namespace_admin role
   * in any namespace this user is associated to. <br/>
   * The operation can fail if the acting {@link User} is not authorized to perform the operation.
   *
   * @param actor
   * @param target
   * @return
   * @throws OperationForbiddenException
   */
  public boolean isOnlyAdminInAnyNamespace(User actor, User target)
      throws OperationForbiddenException, DoesNotExistException {
    // boilerplate validatoin
    ServiceValidationUtil.validate(actor, target);
    // authorizing actor
    userUtil.authorizeActorAsTargetOrSysadmin(actor, target);

    // retrieving namespaces where user is namespace_admin, alongside users and roles
    Map<Namespace, Map<User, Collection<IRole>>> namespacesWhereTargetIsAdmin = getNamespacesCollaboratorsAndRoles(
        actor, target,
        roleUtil.toLong(namespaceAdminRole()));

    // crawling namespaces and inferring whether target user is only admin for any
    for (Map.Entry<Namespace, Map<User, Collection<IRole>>> userRoles : namespacesWhereTargetIsAdmin
        .entrySet()) {
      if (userRoles.getValue().size() == 1) {
        LOGGER.debug(
            String.format(
                "User is the only administrator of at least one namespace : [%s].",
                userRoles.getKey().getName()
            )
        );
        return true;
      }
    }
    return false;
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserNamespaceRoleService#isOnlyAdminInAnyNamespace(User, User)
   */
  public boolean isOnlyAdminInAnyNamespace(String actorUsername, String targetUsername)
      throws OperationForbiddenException, DoesNotExistException {
    ServiceValidationUtil.validateNulls(actorUsername, targetUsername);
    User actor = cache.withUser(actorUsername).getUser();
    User target = cache.withUser(targetUsername).getUser();
    return isOnlyAdminInAnyNamespace(actor, target);
  }


}
