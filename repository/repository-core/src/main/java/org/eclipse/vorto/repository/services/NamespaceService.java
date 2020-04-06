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
import java.util.Set;
import java.util.regex.Pattern;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.services.exceptions.CollisionException;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.NameSyntaxException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.services.exceptions.PrivateNamespaceQuotaExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Performs all business logic on {@link Namespace}s.<br/>
 * For operations on namespace collaborators, see {@link UserNamespaceRoleService}.
 * TODO migrate TenantService#isInConflictWith if needed, once actual logic understood
 */
@Service
public class NamespaceService implements ApplicationEventPublisherAware {

  @Autowired
  private NamespaceRepository namespaceRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserRepositoryRoleService userRepositoryRoleService;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @Autowired
  private ServiceValidationUtil validator;

  @Autowired
  private ISearchService searchService;

  @Value("${config.privateNamespaceQuota}")
  private Integer privateNamespaceQuota;

  private ApplicationEventPublisher eventPublisher = null;

  /**
   * Defines valid namespace names:
   * <ul>
   *   <li>
   *     Starts with 1+ ASCII alnums / underscores
   *   </li>
   *   <li>
   *     Optionally has 0+ following fragments composed by a dot, followed by ASCII alnums / underscores
   *   </li>
   * </ul>
   */
  public static final Pattern VALID_NAMESPACE_NAME = Pattern.compile("[\\w\\d_]+(\\.[\\w\\d_]+)*");

  /**
   * Defines the prefix all private namespaces have
   */
  public static final String PRIVATE_NAMESPACE_PREFIX = "vorto.private.";

  /**
   * Boilerplate format string representing a search expression for public models in a given namespace.
   * <br/>
   * This is used e.g. when attempting to delete a namespace, in order to infer whether the
   * operation is possible.
   */
  public static final String SEARCH_FOR_PUBLIC_MODELS_FORMAT = "namespace:%s visibility:Public";

  private static final Logger LOGGER = LoggerFactory.getLogger(NamespaceService.class);

  // utilities

  /**
   * This infers whether a {@link Namespace} with the given name exists, without throwing
   * {@link Exception} if it does not.
   *
   * @param namespace
   * @return whether the given {@link Namespace} exists.
   */
  public boolean exists(Namespace namespace) {
    return namespaceRepository.exists(namespace.getId());
  }

  /**
   * @param namespaceName
   * @return
   * @see NamespaceService#exists(Namespace)
   */
  public boolean exists(String namespaceName) {
    return Optional.ofNullable(namespaceRepository.findByName(namespaceName)).isPresent();
  }

  /**
   * Used internally to this service to infer whether a {@link Namespace} with the given name
   * exists. <br/>
   * The checked {@link DoesNotExistException} thrown in case it doesn't is designed to be
   * propagated up to the controller, where appropriate error messages can be conveyed back to the
   * end-user.
   *
   * @param namespaceName
   * @throws DoesNotExistException
   */
  public Namespace validateNamespaceExists(String namespaceName) throws DoesNotExistException {
    Namespace result = namespaceRepository.findByName(namespaceName);
    // checks if namespace exists
    if (result == null) {
      throw new DoesNotExistException("Namespace does not exist - aborting change of ownership.");
    }
    return result;
  }

  /**
   * @param applicationEventPublisher
   */
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }

  // namespace operations

  /**
   * This creates a new namespace with the given name, setting the given target {@link User} as
   * owner, and giving them all available roles on that namespace. <br/>
   * The difference between the actor and target users is that depending on the repository roles of
   * the actor user, some operations may not succeed.<br/>
   * For instance, if the actor and target are the same user (typical situation when a user
   * requests to create their own namespace), but the user is not a sysadmin and already has a
   * private namespace, the creation would fail (one private namespace per user). <br/>
   * However, if the actor was sysadmin and creating, e.g. an official namespace for a target user
   * who happens to already have a private namespace, the operation should succeed. <br/>
   * The operation can fail the given {@link User} or namespace name are invalid. <br/>
   * Examples of invalid {@link User} or namespace are:
   * <ul>
   *   <li>
   *     The user is {@code null}, has a {@code null} user name, or does not exist.
   *   </li>
   *   <li>
   *     The namespace name is {@code null}, empty, does not conform to naming standards (lowercase
   *     ASCII alphanumerics, dots as separators, underscores allowed).
   *   </li>
   *   <li>
   *     A namespace with that name already exists.
   *   </li>
   * </ul>
   * This method is invoked in two distinct cases:
   * <ol>
   *   <li>
   *     A user creates their own private namespace.
   *   </li>
   *   <li>
   *     A sysadmin creates an official namespace for any user, including themselves.
   *   </li>
   * </ol>
   * This method only deals with creating namespaces. <br/>
   *
   * @param actor
   * @param target
   * @param namespaceName
   * @return
   * @throws IllegalArgumentException
   * @throws DoesNotExistException
   * @throws CollisionException
   * @throws NameSyntaxException
   * @see NamespaceService#updateNamespaceOwnership(User, User, String) to change ownership of an existing namespace.
   * @see org.eclipse.vorto.repository.tenant.TenantService#createOrUpdateTenant(String, String, Set, Optional, Optional, Optional, IUserContext)
   */
  @Transactional(rollbackFor = {DoesNotExistException.class, CollisionException.class,
      NameSyntaxException.class, PrivateNamespaceQuotaExceededException.class,
      OperationForbiddenException.class})
  public Namespace create(User actor, User target, String namespaceName)
      throws IllegalArgumentException, DoesNotExistException, CollisionException, NameSyntaxException, PrivateNamespaceQuotaExceededException, OperationForbiddenException {
    // boilerplate null validation
    validator.validateNulls(actor, target, namespaceName);
    // lightweight validation of required properties
    validator.validateNulls(actor.getId(), target.getId());

    // user validation
    if (userRepository.findOne(actor.getId()) == null) {
      throw new DoesNotExistException("Acting user does not exist - aborting namespace creation.");
    }
    if (userRepository.findOne(target.getId()) == null) {
      throw new DoesNotExistException("Target user does not exist - aborting namespace creation.");
    }

    // pattern-based namespace name validation
    if (!VALID_NAMESPACE_NAME.matcher(namespaceName).matches()) {
      throw new NameSyntaxException(String
          .format("[%s] is not a valid namespace name - aborting namespace creation.",
              namespaceName));
    }

    // namespace collision validation
    if (namespaceRepository.findByName(namespaceName) != null) {
      throw new CollisionException(String
          .format("A namespace with name [%s] already exists - aborting namespace creation.",
              namespaceName));
    }

    // actor is not sysadmin - need to enforce quota validation
    if (!userRepositoryRoleService.isSysadmin(actor)) {
      if (namespaceRepository.findByOwner(target).stream()
          .filter(n -> n.getName().startsWith(PRIVATE_NAMESPACE_PREFIX)).count()
          >= privateNamespaceQuota) {
        throw new PrivateNamespaceQuotaExceededException(
            String.format(
                "User already has reached quota [%d] of private namespaces - aborting namespace creation.",
                privateNamespaceQuota)
        );
      }
    }

    // persists the new namespace
    Namespace namespace = new Namespace();
    namespace.setName(namespaceName);
    namespace.setOwner(target);
    namespaceRepository.save(namespace);

    userNamespaceRoleService.setAllRoles(actor, target, namespace);

    // application event handling
    eventPublisher
        .publishEvent(new AppEvent(this, target.getUsername(), EventType.NAMESPACE_ADDED));

    return namespace;
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @param namespaceName
   * @return
   * @throws IllegalArgumentException
   * @throws DoesNotExistException
   * @throws CollisionException
   * @throws NameSyntaxException
   * @see NamespaceService#create(User, User, String)
   */
  public Namespace create(String actorUsername, String targetUsername, String namespaceName)
      throws IllegalArgumentException, DoesNotExistException, CollisionException, NameSyntaxException, PrivateNamespaceQuotaExceededException, OperationForbiddenException {
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(targetUsername);
    return create(actor, target, namespaceName);
  }

  /**
   * Changes ownership of the {@link Namespace} with the given name, transferring it to the given
   * {@literal newOwner} {@link User}, as executed by the {@literal actor} {@link User}.<br/>
   * Adds all available {@link Namespace} roles to the {@literal newOwner} {@link User}, but <b>does
   * not remove or modify any {@link Namespace} roles pertaining to the previous owner</b>.<br/>
   * The authorization for this functionality works as follows:
   * <ul>
   *   <li>
   *     If the acting user is sysadmin, then anything is permitted. However, this operation will
   *     log a warning, should the new owner exceed the configured quota of private namespaces as
   *     a result of this operation. The verification on whether the change of ownership concerns
   *     a private namespace should be performed at controller level, in order to provide the
   *     acting sysadmin user with adequate warnings / chances to abort.
   *   </li>
   *   <li>
   *     If the acting user is not sysadmin, then this operation will succeed only in two cases:
   *     <ol>
   *       <li>
   *         The namespace being transferred is private, but the target user has no private namespace
   *         or has {@code quota - 1} private namespaces.
   *       </li>
   *       <li>
   *         The namespace being transferred is not private.
   *       </li>
   *     </ol>
   *     In both cases above, the operation is designed as part of the workflow subsequent to a user
   *     deleting their account.
   *   </li>
   * </ul>
   *
   * @param actor
   * @param newOwner
   * @param namespaceName
   * @return the {@link Namespace} with new ownership.
   * @throws DoesNotExistException if either user or the namespace do not exist.
   * @see org.eclipse.vorto.repository.tenant.TenantService#createOrUpdateTenant(String, String, Set, Optional, Optional, Optional, IUserContext)
   */
  @Transactional(rollbackFor = {DoesNotExistException.class,
      PrivateNamespaceQuotaExceededException.class, OperationForbiddenException.class})
  public Namespace updateNamespaceOwnership(User actor, User newOwner, String namespaceName)
      throws DoesNotExistException, PrivateNamespaceQuotaExceededException, OperationForbiddenException {
    // boilerplate null validation
    validator.validateNulls(actor, newOwner, namespaceName);

    // user validation
    if (userRepository.findOne(actor.getId()) == null) {
      throw new DoesNotExistException(
          "Acting user does not exist - aborting change of namespace ownership.");
    }
    if (userRepository.findOne(newOwner.getId()) == null) {
      throw new DoesNotExistException("New owner does not exist - aborting namespace creation.");
    }

    // will throw DoesNotExistException if none found
    Namespace currentNamespace = validateNamespaceExists(namespaceName);

    // check quota
    // actor is not sysadmin - need to enforce quota validation

    if (namespaceRepository.findByOwner(newOwner).stream()
        .filter(n -> n.getName().startsWith(PRIVATE_NAMESPACE_PREFIX)).count()
        >= privateNamespaceQuota && currentNamespace.getName()
        .startsWith(PRIVATE_NAMESPACE_PREFIX)) {
      // actor not sysadmin and quota would exceed - aborting
      if (!userRepositoryRoleService.isSysadmin(actor)) {
        throw new PrivateNamespaceQuotaExceededException(
            String.format(
                "User would exceed quota [%d] of private namespaces by acquiring ownership of namespace [%s] - aborting operation.",
                privateNamespaceQuota, currentNamespace.getName()
            )
        );
        // actor is sysadmin so only logging a warning
      } else {
        LOGGER.warn(
            String.format(
                "User will exceed quota [%d] of private namespaces by acquiring ownership of namespace [%s].",
                privateNamespaceQuota, currentNamespace.getName()
            )
        );
      }

    }

    // Set all roles ot the new owner
    userNamespaceRoleService.setAllRoles(actor, newOwner, currentNamespace);

    // now changes ownership and persists
    currentNamespace.setOwner(newOwner);

    Namespace saved = namespaceRepository.save(currentNamespace);

    if (saved != null) {
      // application event handling
      eventPublisher
          .publishEvent(new AppEvent(this, newOwner.getUsername(), EventType.NAMESPACE_UPDATED));
      return saved;
    }
    return null;
  }

  /**
   * @param actorUsername
   * @param newOwnerUsername
   * @param namespaceName
   * @return
   * @throws DoesNotExistException
   * @see NamespaceService#updateNamespaceOwnership(User, User, String)
   */
  public Namespace updateNamespaceOwnership(String actorUsername, String newOwnerUsername,
      String namespaceName)
      throws DoesNotExistException, PrivateNamespaceQuotaExceededException, OperationForbiddenException {
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(newOwnerUsername);
    return updateNamespaceOwnership(actor, target, namespaceName);
  }

  /**
   * Validates that the given {@link Namespace} exists and returns its owning {@link User}.
   *
   * @param namespace
   * @return
   * @throws DoesNotExistException
   */
  public User getOwner(Namespace namespace) throws DoesNotExistException {
    // boilerplate null validation
    validator.validateNulls(namespace);
    validator.validateNulls(namespace.getName());
    return validateNamespaceExists(namespace.getName()).getOwner();
  }

  /**
   * Validates that the given {@link Namespace} exists and returns its owning {@link User}.
   *
   * @param namespaceName
   * @return
   * @throws DoesNotExistException
   */
  public User getOwner(String namespaceName) throws DoesNotExistException {
    // boilerplate null validation
    validator.validateNulls(namespaceName);
    return validateNamespaceExists(namespaceName).getOwner();
  }

  /**
   * Deletes all namespace roles from any user-namespace association for the given {@link Namespace},
   * including its ownership, then deletes the actual {@link Namespace}.<br/>
   * The operation will fail if:
   * <ul>
   *   <li>
   *     The acting {@link User} is not {@literal sysadmin}, or does not own the namespace, or
   *     does not have {@literal namespace_admin} role on the {@link Namespace}.
   *   </li>
   *   <li>
   *     The {@link Namespace} contains public models.
   *   </li>
   * </ul>
   *
   * @param actor
   * @param namespaceName
   * @throws DoesNotExistException
   * @throws OperationForbiddenException
   * @see org.eclipse.vorto.repository.tenant.TenantService#deleteTenant(Tenant, IUserContext)
   */
  @Transactional(rollbackFor = {DoesNotExistException.class, OperationForbiddenException.class})
  public void deleteNamespace(User actor, String namespaceName)
      throws DoesNotExistException, OperationForbiddenException {
    // boilerplate null validation
    validator.validateNulls(actor, namespaceName);
    validator.validateNulls(actor.getId());

    // will throw DoesNotExistException if none found
    Namespace currentNamespace = validateNamespaceExists(namespaceName);

    // searches for public models and fails if any
    if (!searchService
        .search(String.format(SEARCH_FOR_PUBLIC_MODELS_FORMAT, currentNamespace.getName()))
        .isEmpty()) {
      throw new OperationForbiddenException(String
          .format("Namespace [%s] has public models and cannot be deleted - aborting operation.",
              currentNamespace.getName()));
    }
    User owner = currentNamespace.getOwner();

    // authorizing acting user
    if (!userRepositoryRoleService.isSysadmin(actor) || !actor.equals(owner)
        || !userNamespaceRoleService
        .hasRole(actor, currentNamespace, userNamespaceRoleService.namespaceAdminRole())) {
      throw new OperationForbiddenException(String
          .format("Acting user is not authorized to delete namespace [%s] - aborting operation.",
              currentNamespace.getName()));
    }

    // deletes all collaborator associations
    Collection<User> collaborators = userNamespaceRoleService
        .getCollaborators(actor, currentNamespace);
    for (User collaborator : collaborators) {
      userNamespaceRoleService.deleteAllRoles(actor, collaborator, currentNamespace);
    }

    // delete all roles from owner
    userNamespaceRoleService.deleteAllRoles(actor, owner, currentNamespace);

    // finally deletes the actual namespace
    namespaceRepository.delete(currentNamespace);

    eventPublisher
        .publishEvent(new AppEvent(this, currentNamespace.getName(), EventType.NAMESPACE_DELETED));
  }

  /**
   * @param actorUsername
   * @param namespaceName
   * @throws DoesNotExistException
   * @throws OperationForbiddenException
   * @see NamespaceService#deleteNamespace(User, String)
   */
  public void deleteNamespace(String actorUsername, String namespaceName)
      throws DoesNotExistException, OperationForbiddenException {
    User actor = userRepository.findByUsername(actorUsername);
    deleteNamespace(actor, namespaceName);
  }

}
