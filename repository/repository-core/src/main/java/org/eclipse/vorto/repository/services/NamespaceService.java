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

import com.google.common.collect.Lists;
import org.eclipse.vorto.repository.core.IRepositoryManager;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.services.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Performs all business logic on {@link Namespace}s.<br/>
 * For operations on namespace collaborators, see {@link UserNamespaceRoleService}.
 */
@Service
public class NamespaceService implements ApplicationEventPublisherAware {

  private NamespaceRepository namespaceRepository;

  private UserRepository userRepository;

  private UserRepositoryRoleService userRepositoryRoleService;

  private UserNamespaceRoleService userNamespaceRoleService;

  private ISearchService searchService;

  private ModelRepositoryFactory repositoryFactory;

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

  public NamespaceService(
      @Autowired NamespaceRepository namespaceRepository,
      @Autowired UserRepository userRepository,
      @Autowired UserRepositoryRoleService userRepositoryRoleService,
      @Autowired UserNamespaceRoleService userNamespaceRoleService,
      @Autowired ISearchService searchService,
      @Autowired ModelRepositoryFactory repositoryFactory) {
    this.namespaceRepository = namespaceRepository;
    this.userRepository = userRepository;
    this.userRepositoryRoleService = userRepositoryRoleService;
    this.userNamespaceRoleService = userNamespaceRoleService;
    this.searchService = searchService;
    this.repositoryFactory = repositoryFactory;
  }


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
  @Override
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
   */
  @Transactional(rollbackFor = {DoesNotExistException.class, CollisionException.class,
      NameSyntaxException.class, PrivateNamespaceQuotaExceededException.class,
      OperationForbiddenException.class})
  public Namespace create(User actor, User target, String namespaceName)
      throws IllegalArgumentException, DoesNotExistException, CollisionException, NameSyntaxException, PrivateNamespaceQuotaExceededException, OperationForbiddenException {
    // boilerplate null validation
    ServiceValidationUtil.validate(actor, target);
    ServiceValidationUtil.validateNulls(namespaceName);
    // lightweight validation of required properties
    ServiceValidationUtil.validateNulls(actor.getId(), target.getId());

    if (namespaceName.trim().isEmpty()) {
      throw new NameSyntaxException(String
          .format("Namespace name is empty - aborting namespace creation.",
              namespaceName));
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

    // actor is not sysadmin - need to enforce quota validation and private namespace notation
    if (!userRepositoryRoleService.isSysadmin(actor)) {

      if (!namespaceName.startsWith(PRIVATE_NAMESPACE_PREFIX)) {
        throw new NameSyntaxException(
            String.format(
                "[%s] is an invalid name for a private namespace - aborting namespace creation.",
                namespaceName
            )
        );
      }
      verifyPrivateNamespaceQuota(actor, target);
    }

    // persists the new namespace
    Namespace namespace = new Namespace();
    namespace.setName(namespaceName);
    namespace.setWorkspaceId(UUID.randomUUID().toString().replace("-", ""));
    namespaceRepository.save(namespace);

    userNamespaceRoleService.setAllRoles(actor, target, namespace, true);

    // application event handling
    eventPublisher
        .publishEvent(new AppEvent(this, target.getUsername(),
            UserContext.user(target.getUsername(), namespace.getWorkspaceId()),
            EventType.NAMESPACE_ADDED));

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
   */
  @Transactional(rollbackFor = {DoesNotExistException.class, OperationForbiddenException.class})
  public void deleteNamespace(User actor, String namespaceName)
      throws DoesNotExistException, OperationForbiddenException {
    // boilerplate null validation
    ServiceValidationUtil.validateUser(actor);
    ServiceValidationUtil.validateNulls(namespaceName, actor.getId());

    // will throw DoesNotExistException if none found
    Namespace currentNamespace = validateNamespaceExists(namespaceName);

    searchForPublicModelsAndFailIfAnyExist(currentNamespace);
    authorizeActingUser(actor, currentNamespace);
    deleteAllCollaboratorAssociations(actor, currentNamespace);

    // finally deletes the actual namespace
    deleteModeshapeWorkspace(currentNamespace);
    namespaceRepository.delete(currentNamespace);

    publishNamespaceDeletedEvent(actor, currentNamespace);
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

  /**
   * Simple wrapper about {@link NamespaceRepository#findByName(String)}, throwing a checked
   * exception if not found.
   *
   * @param namespaceName
   * @return
   * @throws DoesNotExistException
   */
  public Namespace getByName(String namespaceName) throws DoesNotExistException {
    Namespace namespace = namespaceRepository.findByName(namespaceName);
    if (Objects.isNull(namespace)) {
      throw new DoesNotExistException(
          String.format("Namespace [%s] does not exist.", namespaceName));
    }
    return namespace;
  }

  /**
   * This searches all namespaces where the target user has any role, and filters by private
   * namespaces only, and whether the target user has the {@literal namespace_admin} role.<br/>
   * If the target user is {@literal namespace_admin} in more private namespaces than the quota
   * allows, {@link PrivateNamespaceQuotaExceededException} is thrown. <br/>
   * This verification can also fail if the acting user is not the same user as the target or does
   * not have the {@literal sysadmin} repository role, or if either actor or target do not exist.
   *
   * @param actor
   * @param target
   * @throws DoesNotExistException
   * @throws OperationForbiddenException
   * @throws PrivateNamespaceQuotaExceededException
   */
  public void verifyPrivateNamespaceQuota(User actor, User target)
      throws DoesNotExistException, OperationForbiddenException, PrivateNamespaceQuotaExceededException {
    if (userNamespaceRoleService.getNamespacesAndRolesByUser(actor, target).entrySet().stream()
        .filter(e -> e.getKey().getName().startsWith(PRIVATE_NAMESPACE_PREFIX) && e.getValue()
            .contains(userNamespaceRoleService.namespaceAdminRole())).count()
        >= privateNamespaceQuota) {
      throw new PrivateNamespaceQuotaExceededException(
          String.format(
              "User already has reached quota [%d] of private namespaces - aborting namespace creation.",
              privateNamespaceQuota)
      );
    }
  }

  /**
   * Resolves workspace ID for the given namespace. Tries to lookup the namespace by name and returns
   * the result, if there is one. Otherwise it filters all namespaces for ownership of the given
   * namespace to identify the main workspace for subdomains.
   *
   * @param namespace - the given namespace
   * @return Optional of the workspace ID or empty Optional, if no namespace was found.
   */
  public Optional<String> resolveWorkspaceIdForNamespace(String namespace) {
    Optional<String> foundByFullNamespaceName = Optional
        .ofNullable(namespaceRepository.findByName(namespace))
        .map(Namespace::getWorkspaceId);
    if (foundByFullNamespaceName.isPresent()) {
      return foundByFullNamespaceName;
    }
    return filterAllNamespacesByName(namespace).map(Namespace::getWorkspaceId);
  }

  public List<String> findAllWorkspaceIds() {
    return Lists.newArrayList(namespaceRepository.findAll()).stream().map(Namespace::getWorkspaceId)
        .collect(Collectors.toList());
  }

  public Namespace findNamespaceByWorkspaceId(String workspaceId) {
    return namespaceRepository.findByWorkspaceId(workspaceId);
  }

  private void searchForPublicModelsAndFailIfAnyExist(Namespace currentNamespace)
      throws OperationForbiddenException {
    if (!searchService
        .search(String.format(SEARCH_FOR_PUBLIC_MODELS_FORMAT, currentNamespace.getName()))
        .isEmpty()) {
      throw new OperationForbiddenException(String
          .format("Namespace [%s] has public models and cannot be deleted - aborting operation.",
              currentNamespace.getName()));
    }
  }

  private void authorizeActingUser(User actor, Namespace currentNamespace)
      throws DoesNotExistException, OperationForbiddenException {
    if (!userRepositoryRoleService.isSysadmin(actor)
        && !userNamespaceRoleService
        .hasRole(actor, currentNamespace, userNamespaceRoleService.namespaceAdminRole())) {
      throw new OperationForbiddenException(String
          .format("Acting user is not authorized to delete namespace [%s] - aborting operation.",
              currentNamespace.getName()));
    }
  }

  private void publishNamespaceDeletedEvent(User actor, Namespace currentNamespace) {
    eventPublisher
        .publishEvent(new AppEvent(this, actor.getUsername(), UserContext
            .user(SecurityContextHolder.getContext().getAuthentication(),
                currentNamespace.getWorkspaceId()), EventType.NAMESPACE_DELETED));
  }

  private void deleteAllCollaboratorAssociations(User actor, Namespace currentNamespace)
      throws OperationForbiddenException, DoesNotExistException {
    Collection<User> users = userNamespaceRoleService
        .getRolesByUser(actor, currentNamespace).keySet();
    boolean actorIsAmongstUsers = users.contains(actor);
    if (actorIsAmongstUsers) {
      users.remove(actor);
    }
    for (User user : users) {
      userNamespaceRoleService.deleteAllRoles(actor, user, currentNamespace, true);
    }
    if (actorIsAmongstUsers) {
      userNamespaceRoleService.deleteAllRoles(actor, actor, currentNamespace, true);
    }

  }

  private void deleteModeshapeWorkspace(Namespace currentNamespace) {
    IRepositoryManager repoMgr = repositoryFactory
        .getRepositoryManager(currentNamespace.getWorkspaceId(),
            SecurityContextHolder.getContext().getAuthentication());
    repoMgr.removeWorkspace(currentNamespace.getWorkspaceId());
  }

  private Optional<Namespace> filterAllNamespacesByName(String namespace) {
    return Lists.newArrayList(namespaceRepository.findAll()).stream()
        .filter(ns -> ns.owns(namespace)).findAny();
  }
}
