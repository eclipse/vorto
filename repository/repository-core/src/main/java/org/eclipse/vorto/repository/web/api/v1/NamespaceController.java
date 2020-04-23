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

import io.swagger.annotations.ApiParam;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.eclipse.vorto.repository.services.UserService;
import org.eclipse.vorto.repository.services.UserUtil;
import org.eclipse.vorto.repository.services.exceptions.CollisionException;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.services.exceptions.NameSyntaxException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.services.exceptions.PrivateNamespaceQuotaExceededException;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceOperationResult;
import org.eclipse.vorto.repository.web.api.v1.util.EntityDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  private static final Logger LOGGER = Logger.getLogger(NamespaceController.class);

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @Autowired
  private UserUtil userUtil;

  @Autowired
  private UserRepositoryRoleService userRepositoryRoleService;

  @Autowired
  private NamespaceService namespaceService;

  @Autowired
  private EntityDTOConverter converter;

  @RequestMapping(method = RequestMethod.GET, value = "/test")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Boolean> test() {
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    userRepositoryRoleService.setSysadmin(userContext.getUsername());
    return new ResponseEntity<>(userRepositoryRoleService.isSysadmin(userContext.getUsername()),
        HttpStatus.OK);
  }

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
        namespaces.add(converter.createNamespaceDTO(entry.getKey(), entry.getValue()));
      }
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(namespaces, HttpStatus.FORBIDDEN);
    }
    return new ResponseEntity<>(namespaces, HttpStatus.OK);
  }

  /**
   * @param namespace
   * @return all users of a given namespace, if the user acting the call has either administrative rights on the namespace, or on the repository.
   */
  @RequestMapping(method = RequestMethod.GET, value = "/{namespace:.+}/users")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Collection<Collaborator>> getUsersForNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace) {

    Collection<Collaborator> collaborators = new HashSet<>();
    try {
      IUserContext userContext = UserContext
          .user(SecurityContextHolder.getContext().getAuthentication());
      collaborators = converter.createCollaborators(userNamespaceRoleService
          .getRolesByUser(userContext.getUsername(), namespace));
      return new ResponseEntity<>(collaborators, HttpStatus.OK);
    } catch (OperationForbiddenException ofe) {
      LOGGER.warn("Error in getUsersForNamespace()", ofe);
      return new ResponseEntity<>(collaborators, HttpStatus.FORBIDDEN);
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
      User user = converter.createUser(userUtil, collaborator);
      userNamespaceRoleService
          .createTechnicalUserAndAddAsCollaborator(userContext.getUsername(), user, namespace,
              collaborator.getRoles());
      return new ResponseEntity<>(true, HttpStatus.CREATED);
    } catch (InvalidUserException ie) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
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
  public ResponseEntity<Boolean> addOrUpdateUsersForNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace,
      @RequestBody @ApiParam(value = "The user to be associated with the namespace",
          required = true) final Collaborator collaborator) {

    try {
      // does not validate user creation here as it is a conversion from a payload that may not
      // contain all required data
      User user = converter.createUser(null, collaborator);
      IUserContext userContext = UserContext
          .user(SecurityContextHolder.getContext().getAuthentication());
      return new ResponseEntity<>(
          userNamespaceRoleService.setRoles(
              userContext.getUsername(), user.getUsername(), namespace, collaborator.getRoles()
          ),
          HttpStatus.OK);
    } catch (InvalidUserException | DoesNotExistException e) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
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
  @RequestMapping(method = RequestMethod.PUT, value = "/{namespace:.+}", produces = "application/json")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<NamespaceOperationResult> createNamespace(
      @ApiParam(value = "The name of the namespace to be created", required = true) final @PathVariable String namespace
  ) {

    try {
      IUserContext userContext = UserContext
          .user(SecurityContextHolder.getContext().getAuthentication());
      namespaceService
          .create(userContext.getUsername(), userContext.getUsername(), namespace);
      return new ResponseEntity<>(NamespaceOperationResult.success(), HttpStatus.CREATED);

    } catch (DoesNotExistException | NameSyntaxException e) {
      return new ResponseEntity<>(NamespaceOperationResult.failure(e.getMessage()),
          HttpStatus.BAD_REQUEST);
    } catch (PrivateNamespaceQuotaExceededException pnqee) {
      return new ResponseEntity<>(NamespaceOperationResult.failure(pnqee.getMessage()),
          HttpStatus.FORBIDDEN);
    }
    // omitting explicit collision message and just going with status here
    catch (CollisionException ce) {
      return new ResponseEntity<>(NamespaceOperationResult.failure(""), HttpStatus.CONFLICT);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(NamespaceOperationResult.failure(ofe.getMessage()),
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
      @ApiParam(value = "The (optional) role to filter namespaces which this user has access to",
          required = false) final @PathVariable(value = "role", required = false) String role) {

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
              .map(e -> converter.createNamespaceDTO(e.getKey(), e.getValue()))
              .collect(Collectors.toSet()),
          HttpStatus.OK
      );
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(
          Collections.emptyList(), HttpStatus.FORBIDDEN
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
    return new ResponseEntity<>(userNamespaceRoleService
        .hasRole(userContext.getUsername(), namespace, role), HttpStatus.OK);
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
  public ResponseEntity<NamespaceOperationResult> deleteNamespace(
      @ApiParam(value = "The name of the namespace to be deleted", required = true) final @PathVariable String namespace
  ) {
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    try {
      namespaceService.deleteNamespace(userContext.getUsername(), namespace);
      return new ResponseEntity<>(NamespaceOperationResult.success(), HttpStatus.NO_CONTENT);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(NamespaceOperationResult.failure(ofe.getMessage()),
          HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(NamespaceOperationResult.failure(dnee.getMessage()),
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
      userNamespaceRoleService.deleteAllRoles(userContext.getUsername(), userId, namespace);
      return new ResponseEntity<>(true, HttpStatus.GONE);
    } catch (OperationForbiddenException ofe) {
      return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    } catch (DoesNotExistException dnee) {
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
  }

}
