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
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.account.impl.AccountDeletionNotAllowed;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.impl.EmailNotificationService;
import org.eclipse.vorto.repository.notification.message.DeleteAccountMessage;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides functionalities specific to user manipulation.<br/>
 */
@Service
public class UserService implements ApplicationEventPublisherAware {

  private UserUtil userUtil;

  private UserRepository userRepository;

  private UserRepositoryRoleService userRepositoryRoleService;

  private UserNamespaceRoleService userNamespaceRoleService;

  private INotificationService notificationService;

  private ApplicationEventPublisher eventPublisher;

  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  public UserService(@Autowired UserUtil userUtil, @Autowired UserRepository userRepository,
      @Autowired UserRepositoryRoleService userRepositoryRoleService,
      @Autowired UserNamespaceRoleService userNamespaceRoleService,
      @Autowired INotificationService notificationService) {
    this.userUtil = userUtil;
    this.userRepository = userRepository;
    this.userRepositoryRoleService = userRepositoryRoleService;
    this.userNamespaceRoleService = userNamespaceRoleService;
    this.notificationService = notificationService;
  }

  /**
   * Validates and persists the given {@link User} as technical user.<br/>
   * This functionality is available to all users regardless of their privileges. <br/>
   * Failures in authorization may occur in a broader context, as technical users are created
   * and specifically associated to a {@link org.eclipse.vorto.repository.domain.Namespace}, so the
   * user performing the operation must have the right authorities to do so in context.
   *
   * @param technicalUser
   * @return
   * @throws InvalidUserException
   */
  public User createOrUpdateTechnicalUser(User technicalUser) throws InvalidUserException {
    // boilerplate null validation
    ServiceValidationUtil.validateNulls(technicalUser);

    // validates technical user
    userUtil.validateNewUser(technicalUser);

    // save the technical user
    User result = userRepository.save(technicalUser);

    if (result != null) {
      eventPublisher
          .publishEvent(new AppEvent(this, technicalUser.getUsername(), EventType.USER_ADDED));
    }

    return result;
  }

  /**
   * This validates and persists a {@link User}, technical or not.<br/>
   * The functionality is only meant for internal usage (e.g. in tests), therefore the acting
   * {@link User} must have the {@literal sysadmin} repository role.
   *
   * @param actor
   * @param target
   * @return
   * @throws InvalidUserException
   * @see org.eclipse.vorto.repository.account.impl.DefaultUserAccountService#create(String, String, String, boolean)
   */
  public User createOrUpdateUser(User actor, User target)
      throws InvalidUserException, OperationForbiddenException {
    // boilerplate null validation
    ServiceValidationUtil.validateNulls(actor, target);

    // validates technical user
    userUtil.validateNewUser(target);

    if (!userRepositoryRoleService.isSysadmin(actor)) {
      throw new OperationForbiddenException(
          "Acting user is not authorized to create a user - aborting operation.");
    }
    // save the technical user
    User result = userRepository.save(target);

    if (result != null) {
      eventPublisher.publishEvent(new AppEvent(this, target.getId(), EventType.USER_ADDED));
    }

    return result;
  }

  /**
   * Deletes the given {@link User} and their namespace-role associations, as acted by the given
   * acting {@link User}.<br/>
   * This can fail for a number of reasons:
   * <ul>
   *   <li>
   *     The acting {@link User} does not have the {@literal sysadmin} repository role, or is not
   *     the same {@link User} as the target.
   *   </li>
   *   <li>
   *     The target {@link User} owns a {@link org.eclipse.vorto.repository.domain.Namespace} - in
   *     which case, ownership should be given to another {@link User} before deleting.
   *   </li>
   *   <li>
   *     The target {@link User} is the only one listed with namespace role {@literal namespace_admin}
   *     on one or more {@link org.eclipse.vorto.repository.domain.Namespace}s - in which case, the
   *     role should be given to at least one other {@link User} before deleting.
   *   </li>
   * </ul>
   * Failures above will throw checked exceptions. <br/>
   * It is also possible that this method will fail by returning {@code false}, should the target
   * {@link User} simply not exist.
   *
   * @param actor
   * @param target
   * @return
   */
  @Transactional(rollbackFor = {OperationForbiddenException.class, DoesNotExistException.class})
  public boolean delete(User actor, User target)
      throws OperationForbiddenException, DoesNotExistException {
    // boilerplate null validation
    ServiceValidationUtil.validateNulls(actor, target);

    if (!userRepository.exists(target.getId())) {
      LOGGER.info("Attempting to delete a user that does not exist. ");
      return false;
    }

    // authorizing actor
    userUtil.authorizeActorAsTargetOrSysadmin(actor, target);

    // checking if only admin in any namespace
    if (userNamespaceRoleService.isOnlyAdminInAnyNamespace(actor, target)) {
      throw new OperationForbiddenException(
          "User is the only administrator of at least one namespace - aborting delete operation."
      );
    }

    // retrieving namespaces target manages
    Collection<Namespace> namespacesManagedByTarget = userNamespaceRoleService
        .getNamespacesAndRolesByUser(actor, target).entrySet().stream()
        .filter(e -> e.getValue().contains(userNamespaceRoleService.namespaceAdminRole())).map(
            Entry::getKey).collect(Collectors.toSet());

    // target owns at least one namespace - failing
    if (!namespacesManagedByTarget.isEmpty()) {
      throw new OperationForbiddenException(
          "User is administrator in at least one namespace. Ownership must change before user can be deleted. Aborting operation."
      );
    }

    // collecting target user's e-mail address if any
    DeleteAccountMessage message = null;
    if (target.hasEmailAddress()) {
      message = new DeleteAccountMessage(target);
    }

    // firstly, publish the user deleted event - this way, the models are all anonymized while the
    // user and their namespace associations are still there
    eventPublisher.publishEvent(new AppEvent(this, target.getUsername(), EventType.USER_DELETED));

    // then, retrie namespaces where target has any role
    Collection<Namespace> namespacesWhereTargetHasAnyRole = userNamespaceRoleService
        .getNamespaces(actor, target);

    // and remove association for all namespaces
    for (Namespace namespace : namespacesWhereTargetHasAnyRole) {
      userNamespaceRoleService.deleteAllRoles(actor, target, namespace, false);
    }

    // finally, delete target user
    userRepository.delete(target);

    // and send them a message if possible
    if (message != null) {
      notificationService.sendNotification(message);
    }

    return true;
  }

  // just testing
  @javax.transaction.Transactional
  public void delete(final String userId) {
    User userToDelete = userRepository.findByUsername(userId);
    if (userToDelete != null) {

      eventPublisher.publishEvent(new AppEvent(this, userId, EventType.USER_DELETED));
      userRepository.delete(userToDelete);
      if (userToDelete.hasEmailAddress()) {
        notificationService.sendNotification(new DeleteAccountMessage(userToDelete));
      }
    }
  }

  /**
   * @param actorUsername
   * @param targetUsername
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserService#delete(User, User)
   */
  public boolean delete(String actorUsername, String targetUsername)
      throws OperationForbiddenException, DoesNotExistException {
    User actor = userRepository.findByUsername(actorUsername);
    User target = userRepository.findByUsername(targetUsername);
    return delete(actor, target);
  }

  /**
   * @param userName
   * @return whether the given user name pertains to an existing user.
   */
  public boolean exists(String userName) {
    return userRepository.findByUsername(userName) != null;
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }
}
