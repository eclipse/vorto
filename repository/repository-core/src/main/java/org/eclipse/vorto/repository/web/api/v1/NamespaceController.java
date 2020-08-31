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
package org.eclipse.vorto.repository.web.api.v1;

import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserNamespaceRoles;
import org.eclipse.vorto.repository.notification.IMessage;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.INotificationService.NotificationProblem;
import org.eclipse.vorto.repository.notification.message.RequestAccessToNamespaceMessage;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.repositories.UserNamespaceRoleRepository;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.UserService;
import org.eclipse.vorto.repository.services.UserUtil;
import org.eclipse.vorto.repository.services.exceptions.CollisionException;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.services.exceptions.NameSyntaxException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.services.exceptions.PrivateNamespaceQuotaExceededException;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceAccessRequestDTO;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.eclipse.vorto.repository.web.api.v1.dto.OperationResult;
import org.eclipse.vorto.repository.web.api.v1.util.EntityDTOConverter;
import org.eclipse.vorto.repository.web.api.v1.util.NamespaceValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Performs a number of operations on namespaces and collaborators.
 */
@RestController
@RequestMapping(value = "/rest/namespaces")
public class NamespaceController {

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @Autowired
  private UserUtil userUtil;

  @Autowired
  private UserNamespaceRoleRepository userNamespaceRoleRepository;

  @Autowired
  private NamespaceService namespaceService;

  @Autowired
  private NamespaceRepository namespaceRepository;

  @Autowired
  private INotificationService emailNotificationService;

  @Value("${host:http://localhost:8080}")
  private String host;

  /**
   * @return all namespaces the logged on user has access to.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/all")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Collection<NamespaceDto>> getAllNamespacesForLoggedUser() {
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    Collection<NamespaceDto> namespaces = new TreeSet<>(
        Comparator.comparing(NamespaceDto::getName));
    try {
      for (Map.Entry<Namespace, Map<User, Collection<IRole>>> entry : userNamespaceRoleService
          .getNamespacesCollaboratorsAndRoles(userContext.getUsername(),
              userContext.getUsername(), "namespace_admin").entrySet()) {
        namespaces.add(EntityDTOConverter.createNamespaceDTO(entry.getKey(), entry.getValue()));
      }
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(namespaces, HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException d) {
      return new ResponseEntity<>(namespaces, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(namespaces, HttpStatus.OK);
  }

  /**
   * Finds all namespaces accessible to the authenticated user, by a partial name. <br/>
   * This is used in the UI to search for namespaces the user can view, aka all the public ones and
   * the private ones the user has at least one role in.
   *
   * @param partial
   * @return
   */
  @RequestMapping(method = RequestMethod.GET, value = "/search/{partial:.+}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Collection<NamespaceDto>> findAllAccessibleNamespacesByPartial(
      @ApiParam(value = "The partial name of the namespaces to be searched with", required = true) @PathVariable String partial
  ) {
    if (Strings.nullToEmpty(partial).trim().isEmpty()) {
      return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
    }
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    Collection<NamespaceDto> result = namespaceRepository
        .findNamespaceByPartial(partial.toLowerCase()).stream()
        .filter(n -> {
          try {
            return
                // all public namespaces
                !n.getName().startsWith(NamespaceValidator.PRIVATE_NAMESPACE_PREFIX)
                    ||
                    // or namespaces where user has a role
                    userNamespaceRoleService.hasAnyRole(userContext.getUsername(), n.getName());
            // should never occur here
          } catch (DoesNotExistException dnee) {
            return false;
          }
        })
        .map(EntityDTOConverter::createNamespaceDTO)
        .sorted(Comparator.comparing(NamespaceDto::getName))
        .collect(Collectors.toList());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @PostMapping("/requestAccess")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<OperationResult> requestAccessToNamespace(
      @RequestBody @ApiParam(
          value = "The request body specifying who initiates the request, the namespace, whom the request is intended for, and an optional collection of suggested roles", required = true)
          NamespaceAccessRequestDTO request) {
    Optional<OperationResult> validationError = NamespaceValidator
        .validateAccessRequest(request);
    if (validationError.isPresent()) {
      return new ResponseEntity<>(validationError.get(), HttpStatus.BAD_REQUEST);
    }
    // checks namespace exists
    // should only occur if namespace was deleted after user search, but before sending request
    Namespace target;
    try {
      target = namespaceService.getByName(request.getNamespaceName());
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(OperationResult.failure("Namespace not found."),
          HttpStatus.NOT_FOUND);
    }

    // checks any admin with an e-mail address set
    Set<User> adminsWithEmail = userNamespaceRoleRepository.findAllByNamespace(target)
        .stream()
        .map(UserNamespaceRoles::getUser)
        .filter(u -> !Strings.nullToEmpty(u.getEmailAddress()).trim().isEmpty())
        .collect(Collectors.toSet());
    if (adminsWithEmail.isEmpty()) {
      return new ResponseEntity<>(OperationResult.failure(String.format(
          "None of the users administrating namespace %s has set their own e-mail. Please contact them directly. ",
          request.getNamespaceName())), HttpStatus.PRECONDITION_FAILED);
    }
    int successCount = adminsWithEmail.size();
    // attempts to send the e-mails
    // ugly exception handling here, due to the way this was designed in the service
    Collection<IMessage> messages = adminsWithEmail.stream()
        .map(u -> new RequestAccessToNamespaceMessage(request, u, host))
        .collect(Collectors.toList());
    for (IMessage message : messages) {
      try {
        emailNotificationService.sendNotification(message);
      } catch (NotificationProblem np) {
        successCount--;
      }
    }
    // worked for all recipients
    if (successCount == adminsWithEmail.size()) {
      return new ResponseEntity<>(OperationResult.success(), HttpStatus.OK);
    }
    // worked for some recipients
    else if (successCount > 0) {
      return new ResponseEntity<>(OperationResult
          .success("The message could not be sent to all administrators."),
          HttpStatus.OK);
    }
    // did not work for any recipient
    else {
      return new ResponseEntity<>(OperationResult
          .failure("The message could not be sent to any administrator."),
          HttpStatus.SERVICE_UNAVAILABLE);
    }
  }

  /**
   * @param namespace
   * @return all users of a given namespace, if the user acting the call has either administrative rights on the namespace, or on the repository.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/{namespace:.+}/users")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Collection<Collaborator>> getCollaboratorsByNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace) {

    Collection<Collaborator> collaborators = new HashSet<>();
    try {
      IUserContext userContext = UserContext
          .user(SecurityContextHolder.getContext().getAuthentication());
      collaborators = EntityDTOConverter.createCollaborators(userNamespaceRoleService
          .getRolesByUser(userContext.getUsername(), namespace));
      return new ResponseEntity<>(collaborators, HttpStatus.OK);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(collaborators, HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException d) {
      return new ResponseEntity<>(collaborators, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Creates a technical user with the given {@link Collaborator} and associates them to the given
   * namespace, with the desired roles held by the collaborator.
   *
   * @param namespace
   * @param collaborator
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/{namespace:.+}/users")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Boolean> createTechnicalUserForNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace,
      @RequestBody @ApiParam(value = "The user to be associated with the namespace",
          required = true) final Collaborator collaborator) {

    try {
      IUserContext userContext = UserContext
          .user(SecurityContextHolder.getContext().getAuthentication());
      User user = EntityDTOConverter.createUser(userUtil, collaborator);
      userNamespaceRoleService
          .createTechnicalUserAndAddAsCollaborator(userContext.getUsername(), user, namespace,
              collaborator.getRoles());
      return new ResponseEntity<>(true, HttpStatus.CREATED);
    } catch (InvalidUserException ie) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException d) {
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Sets the roles of the given user on the given namespace.
   *
   * @param namespace
   * @param collaborator
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  @RequestMapping(method = RequestMethod.PUT, value = "/{namespace:.+}/users")
  public ResponseEntity<Boolean> addOrUpdateCollaboratorForNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace,
      @RequestBody @ApiParam(value = "The user to be associated with the namespace",
          required = true) final Collaborator collaborator) {

    try {
      // no validation here save for essentials: we are pointing to an existing user
      User user = EntityDTOConverter.createUser(null, collaborator);
      IUserContext userContext = UserContext
          .user(SecurityContextHolder.getContext().getAuthentication());
      return new ResponseEntity<>(
          userNamespaceRoleService.setRoles(
              userContext.getUsername(), user.getUsername(), namespace, collaborator.getRoles(),
              false
          ),
          HttpStatus.OK);
    } catch (InvalidUserException iue) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException d) {
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

  }


  /**
   * Creates a new namespace with the given name for the authenticated user. <br/>
   * Automatically adds the user as owner and gives them all applicable roles on the namespace.<br/>
   * Subject to restrictions in terms of number of private namespaces owned, and whether the user
   * has the sufficient repository privileges to own a non-private namespace.
   *
   * @param namespace
   * @return
   */
  @PutMapping(value = "/{namespace:.+}", produces = "application/json")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<OperationResult> createNamespace(
      @ApiParam(value = "The name of the namespace to be created", required = true) final @PathVariable String namespace) {

    try {
      IUserContext userContext = UserContext
          .user(SecurityContextHolder.getContext().getAuthentication());
      namespaceService
          .create(userContext.getUsername(), userContext.getUsername(), namespace);
      return new ResponseEntity<>(OperationResult.success(), HttpStatus.CREATED);

    } catch (DoesNotExistException | NameSyntaxException e) {
      return new ResponseEntity<>(OperationResult.failure(e.getMessage()),
          HttpStatus.BAD_REQUEST);
    } catch (PrivateNamespaceQuotaExceededException pnqee) {
      return new ResponseEntity<>(OperationResult.failure(pnqee.getMessage()),
          HttpStatus.FORBIDDEN);
    }
    // omitting explicit collision message and just going with status here
    catch (CollisionException ce) {
      return new ResponseEntity<>(OperationResult.failure(""), HttpStatus.CONFLICT);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(OperationResult.failure(ofe.getMessage()),
          HttpStatus.FORBIDDEN);
    }
  }

  /**
   * Returns a collection of namespaces where the logged on user has the given role, or any role if
   * the optional parameter is omitted.
   *
   * @param role
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/role/{role}", produces = "application/json")
  public ResponseEntity<Collection<NamespaceDto>> getUserAccessibleNamespacesWithRole(
      @ApiParam(value = "The (optional) role to filter namespaces which this user has access to") final @PathVariable(value = "role", required = false) String role) {

    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    try {
      // NamespaceDTO represents both namespaces and users with roles associated, so the full map
      // is necessary here
      Map<Namespace, Map<User, Collection<IRole>>> namespaces = userNamespaceRoleService
          .getNamespacesCollaboratorsAndRoles(userContext.getUsername(), userContext.getUsername(),
              role);
      return new ResponseEntity<>(
          namespaces.entrySet().stream()
              .map(e -> EntityDTOConverter.createNamespaceDTO(e.getKey(), e.getValue()))
              .collect(Collectors.toSet()),
          HttpStatus.OK
      );
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(
          Collections.emptyList(), HttpStatus.FORBIDDEN
      );
    } catch (DoesNotExistException d) {
      return new ResponseEntity<>(
          Collections.emptyList(), HttpStatus.NOT_FOUND
      );
    }

  }

  /**
   * Checks whether the logged on user has the given role on the given namespace.
   *
   * @param role
   * @param namespace
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/{role}/{namespace:.+}", produces = "application/json")
  public ResponseEntity<Boolean> hasRoleOnNamespace(
      @ApiParam(value = "The role to verify", required = true) final @PathVariable(value = "role") String role,
      @ApiParam(value = "The target namespace", required = true) final @PathVariable(value = "namespace") String namespace
  ) {

    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    try {
      return new ResponseEntity<>(userNamespaceRoleService
          .hasRole(userContext.getUsername(), namespace, role), HttpStatus.OK);
    } catch (DoesNotExistException d) {
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Verifies whether the given user has any namespace where they are the only
   * administrator. <br/>
   * This was used as a separate REST call when attempting to delete one's account, to verify whether
   * the namespace ownership required transferring first. <br/>
   * It is now likely obsolete, as the same check is performed behind the scenes by
   * {@link UserService#delete(User, User)}.
   *
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/userIsOnlyAdmin", produces = "application/json")
  public ResponseEntity<Boolean> isOnlyAdminForAnyNamespace() {
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());

    try {
      return new ResponseEntity<>(
          userNamespaceRoleService
              .isOnlyAdminInAnyNamespace(userContext.getUsername(), userContext.getUsername()),
          HttpStatus.OK
      );
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException d) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

  }

  /**
   * Deletes the given namespace.<br/>
   * This can fail if the acting user is not authorized (has no ownership or admin roles on the
   * impacted resource), or if the namespace has public models.
   *
   * @param namespace
   * @return
   */
  @DeleteMapping(value = "/{namespace:.+}", produces = "application/json")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<OperationResult> deleteNamespace(
      @ApiParam(value = "The name of the namespace to be deleted", required = true) final @PathVariable String namespace
  ) {
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    try {
      namespaceService.deleteNamespace(userContext.getUsername(), namespace);
      return new ResponseEntity<>(OperationResult.success(), HttpStatus.NO_CONTENT);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(OperationResult.failure(ofe.getMessage()),
          HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(OperationResult.failure(dnee.getMessage()),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Deletes a user-namespace association by means of removing all roles. <br/>
   * This can fail if the acting user is the same user as the target (cannot delete yourself), or
   * the acting user has no ownership or admin rights to the given namespace, or the
   * namespace or target user do not exist.
   *
   * @param namespace
   * @param userId
   * @return
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace:.+}/users/{userId}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Boolean> removeUserFromNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace,
      @ApiParam(value = "userId", required = true) @PathVariable String userId) {

    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());

    try {
      return new ResponseEntity<>(
          userNamespaceRoleService
              .deleteAllRoles(userContext.getUsername(), userId, namespace, false),
          HttpStatus.OK);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Returns a {@link NamespaceDto} corresponding to the given namespace name. <br/>
   * Fails if not found or user not authorized to access that namespace.
   *
   * @param namespace
   * @return
   */
  @RequestMapping(method = RequestMethod.GET, value = "/{namespace:.+}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<NamespaceDto> getNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace) {
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    try {
      Namespace entity = namespaceService.getByName(namespace);
      return new ResponseEntity<>(
          EntityDTOConverter.createNamespaceDTO(
              entity,
              userNamespaceRoleService.getRolesByUser(userContext.getUsername(), entity.getName())
          ),
          HttpStatus.OK
      );
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException d) {
      return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
  }
}
