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

import static org.eclipse.vorto.repository.core.impl.ModelRepository.VORTO_AUTHOR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.events.AppEvent;
import org.eclipse.vorto.repository.core.events.EventType;
import org.eclipse.vorto.repository.core.impl.PrivilegedUserContextProvider;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.cache.UserRolesRequestCache;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.DeleteAccountMessage;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.search.ISearchService;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.web.account.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides functionalities specific to user manipulation.<br/>
 */
@Service
public class UserService implements ApplicationEventPublisherAware {

  private UserUtil userUtil;

  private UserRepository userRepository;

  private UserNamespaceRoleService userNamespaceRoleService;

  private INotificationService notificationService;

  private ApplicationEventPublisher eventPublisher;

  private UserRolesRequestCache cache;

  private IModelRepositoryFactory modelRepositoryFactory;

  private ISearchService searchService;

  private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

  public UserService(@Autowired UserRolesRequestCache cache,
      @Autowired UserUtil userUtil, @Autowired UserRepository userRepository,
      @Autowired IModelRepositoryFactory modelRepositoryFactory,
      @Autowired UserNamespaceRoleService userNamespaceRoleService,
      @Autowired INotificationService notificationService,
      @Autowired ISearchService searchService) {
    this.cache = cache;
    this.userUtil = userUtil;
    this.userRepository = userRepository;
    this.modelRepositoryFactory = modelRepositoryFactory;
    this.userNamespaceRoleService = userNamespaceRoleService;
    this.notificationService = notificationService;
    this.searchService = searchService;
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
  public User createOrUpdateTechnicalUser(User actor, User technicalUser) throws InvalidUserException {
    // boilerplate null validation
    ServiceValidationUtil.validateNulls(technicalUser);

    // validates technical user
    userUtil.validateNewUser(technicalUser);

    // sets createdby for tech user
    technicalUser.setCreatedBy(actor.getId());

    // save the technical user
    User result = userRepository.save(technicalUser);

    if (result != null) {
      eventPublisher
          .publishEvent(new AppEvent(this, technicalUser.getUsername(), EventType.USER_ADDED));
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

    if (cache.withUser(target).getUser() == null) {
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

    // collecting target user's e-mail address if any
    DeleteAccountMessage message = null;
    if (target.hasEmailAddress()) {
      message = new DeleteAccountMessage(target);
    }

    /*
    #2529 note on consequences of strengthening user resolution by both username AND (new) OAuth
    provider ID.

    This event will be consumed by the comment anonymization listener.
    However, the model anonymization (formerly in a listener too) needs a guarantee that both
    the User entity and the user's authentication are still there by the time it searches for models
    and anonymizes them.
    This is because there is no persisted OAuth provider ID identifying users safely in models or
    search indices, nor would it be possible to apply this change retroactively.

    As such, an additional check to verify the user really has ownership of a given model is
    necessary before anonymizing it.

    In fairness, even that additional check is somehow brittle, in the very rare edge case that two
    homonymous but distinct users have the same access to a namespace and both have models in there.
    In the latter case, both users' models in that namespace would be anonymized if one of them
    deleted their account.

    The projected occurrence for this edge case is minimal, even in the long term.

    */
    eventPublisher.publishEvent(new AppEvent(this, UserDto.fromUser(target), EventType.USER_DELETED));

    LOGGER.debug("Starting model anonymization process for user to be deleted");

    /*
    Creates a context for the target user: if the target user is deleting their own account
    (most common case), the context can be derived from the current Authentication.
    Otherwise, the context needs to be created ad-hoc, as we need the search to only yield
    models whose namespace the target user has access to, as opposed to sysadmin who can access
    everything.
     */
    IUserContext targetUserContext;
    if (actor.equals(target)) {
      targetUserContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication());
    }
    else {
      List<GrantedAuthority> authorities = new ArrayList<>();
      userNamespaceRoleService.getRolesOnAllNamespaces(target).forEach(
          r -> {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
          }
      );
      targetUserContext = UserContext
          .user(
              new UsernamePasswordAuthenticationToken(
                  target.getUsername(), target.getUsername(), authorities
              )
          );
    }

    /*
     Searches for models authored by target user.
     Formerly when in the MOdelRepositoryEventListener, this search used the userReference tag
     instead of the author tag.
     This is a functional bug, because it would anonymize a model when the user-to-be-deleted is
     the last user having changed it (the "lastModifiedBy" attribute) even if the author was a
     different user.
     Changing the search to "author" only fixes that, at the cost of not anonymizing the
     "lastModifiedBy" tag.
     In theory, we could also intersect two searches - one by "author" and another by "userReference"
     (there is no specific search parametrization for the "lastModifiedBy" attribute only), then
     additionally anonymize the "lastModifiedBy" property of models where the user is not the direct
     author.
     However, considering the cost of additionally verifying for each of those models that the user
     indeed has access (since OAuth provider ID is not persisted in models or search indices) seems
     to outweight the benefit.
     Bottomline: until users can be identified by *both* username (as currently) AND OAuth provider
     ID in search indices and persisted model (ModeShape), it makes little sense to anonymize
     the lastModifiedBy attribute.
     This can be revisited once the latter is implemented.
     */
    LOGGER.debug("Starting search of candidate models whose author property will be anonymized");
    List<ModelInfo> result = searchService
        .search(
            String.format(
                "author:%s", target.getUsername()
            ),
            targetUserContext
        );

    // creates an escalated context to modify the models - arguably this could be done by using
    // the acting user as well if they are != target user, since that would imply acting user
    // is sysadmin
    IUserContext technicalUserContext = PrivilegedUserContextProvider
        .systemAdminContext(PrivilegedUserContextProvider.USER_ADMIN);

    result.forEach(
        model -> {
          IModelRepository repository = modelRepositoryFactory
              .getRepositoryByModelEscalated(model.getId());
          if (target.getUsername().equals(model.getAuthor())) {
            model.setAuthor(User.USER_ANONYMOUS);
          }
          Map<String, String> properties = new HashMap<>();
          properties.put(VORTO_AUTHOR, User.USER_ANONYMOUS);
          LOGGER.debug(
              String.format(
                "Anonymizing author in model [%s]",
                  model.getId().getPrettyFormat()
              )
          );
          repository.updatePropertyInElevatedSession(
              model.getId(), properties, technicalUserContext
          );
        }
    );

    LOGGER.debug("Done anonymizing models");

    // then, retries namespaces where target has any role
    Collection<Namespace> namespacesWhereTargetHasAnyRole = userNamespaceRoleService
        .getNamespaces(actor, target);

    // and remove association for all namespaces
    for (Namespace namespace : namespacesWhereTargetHasAnyRole) {
      userNamespaceRoleService.deleteAllRoles(actor, target, namespace, true);
    }

    // finally, delete target user
    userRepository.delete(target);

    // and send them a message if possible
    if (message != null) {
      notificationService.sendNotification(message);
    }

    return true;
  }

  /**
   * @param targetUser
   * @return
   * @throws OperationForbiddenException
   * @throws DoesNotExistException
   * @see UserService#delete(User, User)
   */
  public boolean delete(UserDto targetUser)
      throws OperationForbiddenException, DoesNotExistException {
    User actor = cache.withSelf().getUser();
    User target = cache.withUser(targetUser).getUser();
    return delete(actor, target);
  }

  /**
   * @param user
   * @return whether the given user name pertains to an existing user.
   */
  public boolean exists(UserDto user) {
    try {
      return cache.withUser(user).getUser() != null;
    }
    catch (DoesNotExistException dnee) {
      return false;
    }
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }
}
