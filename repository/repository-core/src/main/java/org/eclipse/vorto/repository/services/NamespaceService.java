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

import java.util.Optional;
import java.util.regex.Pattern;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.exceptions.CollisionException;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.NameSyntaxException;
import org.eclipse.vorto.repository.services.exceptions.PrivateNamespaceQuotaExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }

  // TODO isInConflictWith once actual logic understood

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
   */
  @Transactional
  public Namespace create(User actor, User target, String namespaceName)
      throws IllegalArgumentException, DoesNotExistException, CollisionException, NameSyntaxException, PrivateNamespaceQuotaExceededException {
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

    userNamespaceRoleService.setAllRoles(target, namespace);

    // application event handling
    eventPublisher.publishEvent(new AppEvent(this, target.getUsername(), EventType.TENANT_ADDED));

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
      throws IllegalArgumentException, DoesNotExistException, CollisionException, NameSyntaxException, PrivateNamespaceQuotaExceededException {
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(targetUsername);
    return create(actor, target, namespaceName);
  }

  /**
   * Changes ownership of the {@link Namespace} with the given name, transferring it to the given
   * {@literal newOwner} {@link User}, as executed by the {@literal actor} {@link User}.<br/>
   * Adds all available {@link Namespace} roles to the {@literal newOwner} {@link User}, but <b>does
   * not remove or modify any {@link Namespace} roles pertaining to the previous owner</b>.
   *
   * @param actor
   * @param newOwner
   * @param namespaceName
   * @return the {@link Namespace} with new ownership.
   * @throws DoesNotExistException if either user or the namespace do not exist.
   */
  public Namespace updateNamespaceOwnership(User actor, User newOwner, String namespaceName)
      throws DoesNotExistException {
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

    Namespace currentNamespace = namespaceRepository.findByName(namespaceName);
    // checks if namespace exists
    if (currentNamespace == null) {
      throw new DoesNotExistException("Namespace does not exist - aborting change of ownership.");
    }

    // Set all roles ot the new owner
    userNamespaceRoleService.setAllRoles(newOwner, currentNamespace);

    // now changes ownership and persists
    currentNamespace.setOwner(newOwner);

    return namespaceRepository.save(currentNamespace);

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
      String namespaceName) throws DoesNotExistException {
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(newOwnerUsername);
    return updateNamespaceOwnership(actor, target, namespaceName);
  }

}
