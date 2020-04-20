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
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.TenantNotFoundException;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.UserNamespaceRoleService;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.eclipse.vorto.repository.services.UserUtil;
import org.eclipse.vorto.repository.services.exceptions.CollisionException;
import org.eclipse.vorto.repository.services.exceptions.DoesNotExistException;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.eclipse.vorto.repository.services.exceptions.NameSyntaxException;
import org.eclipse.vorto.repository.services.exceptions.OperationForbiddenException;
import org.eclipse.vorto.repository.services.exceptions.PrivateNamespaceQuotaExceededException;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.web.ControllerUtils;
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
 * This controller gathers all controller functionality initially designed to work with tenant
 * objects (namely through passing a tenant id), from the following controllers:
 * <ul>
 *   <li>{@link org.eclipse.vorto.repository.web.account.AccountController}</li>
 *   <li>{@link org.eclipse.vorto.repository.web.tenant.TenantManagementController}</li>
 *   <li>{@link org.eclipse.vorto.repository.web.backup.BackupController}</li>
 * </ul>
 * The ultimate goal is full deprecation of "tenant"-based notations, model and DB table design
 * in favor of a simplified namespace-based architecture that is already partially in place.<br/>
 * In the meantime, this controller can act as a proxy for the UI, tests and servlet filters, while
 * delegating its core functionality to the existing controllers until the full replacement is in
 * place.<br/>
 * <b>Note</b>: while this controller is conveniently placed in the API v.1 package, the endpoints
 * stay with {@literal /rest/...} for now, until we are ready to release them as official API with
 * the relevant documentation.<br/>
 * Note on path variable validation: in order to avoid Spring's default config truncating the
 * dot-separated last part of a namespace, all "validation" is set with a loose regular expression
 * in the path variables. <br/>
 * When so required, namespace names are validated programmatically, i.e. before creation.
 */
@RestController
@RequestMapping(value = "/rest/namespaces")
public class NamespaceController {

  private static final Logger LOGGER = Logger.getLogger(NamespaceController.class);

  @Autowired
  private ITenantService tenantService;

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private IOAuthProviderRegistry providerRegistry;

  @Autowired
  private UserNamespaceRoleService userNamespaceRoleService;

  @Autowired
  private UserUtil userUtil;

  @Autowired
  private UserRepository userRepository;

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
   * This new endpoint aims at replacing the functionality of both the front-end TenantService and
   * {@link TenantManagementController#getTenants}. <br/>
   * The goal is to return whether the authenticated user has the specified role on the specified
   * namespace. <br/>
   * As most endpoints here, we are still temporarily querying the tenant-based service (and
   * subsequently the tenant repository) behind the scenes.
   *
   * @param role
   * @param namespace
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/{role}/{namespace:.+}", produces = "application/json")
  public ResponseEntity<Boolean> hasRoleOnNamespace(
      @ApiParam(value = "The role to verify", required = true) final @PathVariable(value = "role") String role,
      @ApiParam(value = "The target namespace", required = true) final @PathVariable(value = "namespace", required = true) String namespace
  ) {

    if (Strings.nullToEmpty(role).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());

    // user is sysadmin, the answer is yes regardless
    if (userContext.isSysAdmin()) {
      return new ResponseEntity(
          true,
          HttpStatus.OK
      );
    } else {
      Role roleFilter = Role.valueOf(role.replace(Role.rolePrefix, ""));
      Predicate<Tenant> filter = hasMemberWithRole(userContext.getUsername(), roleFilter);
      return new ResponseEntity<>(
          tenantService.getTenants().stream().filter(filter)
              .anyMatch(t -> namespace.startsWith(t.getDefaultNamespace())),
          HttpStatus.OK
      );
    }
  }

  /**
   * This endpoint replaces a specific function of the functionalities used by the  former
   * TenantService Angular service. <br/>
   * Namely, it verifies whether the given user has any namespace where they are the only
   * administrator. <br/>
   * In turn, this is used in the "remove account" Angular controller, in order to verify whether the
   * user can delete their account, or they should delete any namespace / add a different administrator
   * first.
   *
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/userIsOnlyAdmin", produces = "application/json")
  public ResponseEntity<Boolean> isOnlyAdminForAnyNamespace() {
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());

    return new ResponseEntity<>(
        tenantService
            .getTenants()
            .stream()
            // filter "tenants" where user has the "tenant admin" role
            .filter(hasMemberWithRole(userContext.getUsername(), Role.TENANT_ADMIN))
            // verifies there is no more than one user with the TENANT_ADMIN role for that "tenant"
            .anyMatch((n) -> n.getTenantAdmins().size() == 1),
        HttpStatus.OK
    );
  }

  @DeleteMapping(value = "/{namespace:.+}", produces = "application/json")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<NamespaceOperationResult> deleteNamespace(
      @ApiParam(value = "The name of the namespace to be deleted", required = true) final @PathVariable String namespace
  ) {
    // validates given namespace
    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Empty namespace"),
          HttpStatus.BAD_REQUEST);
    }

    // gets tenant for namespace
    Tenant tenant = tenantService.getTenantFromNamespace(namespace).orElse(null);
    if (tenant == null) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Namespace does not exist"),
          HttpStatus.NOT_FOUND);
    }

    // gets user (tenant ID required here, otherwise the service will fail to delete even with the given tenant)
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication(), tenant.getTenantId());

    // validates user permissions on this namespace
    if (!(userContext.isSysAdmin() || isOwner(userContext.getUsername()).test(tenant))) {
      return new ResponseEntity<>(
          NamespaceOperationResult.failure("Operation forbidden for this user"),
          HttpStatus.FORBIDDEN);
    }

    boolean success = false;
    String message = "Operation failed for unknown reasons";
    try {
      success = tenantService.deleteTenant(tenant, userContext);
    } catch (TenantNotFoundException e) {
      message = e.getMessage();
      LOGGER.warn(String.format("Could not delete namespace %s", namespace), e);
    }
    return new ResponseEntity<>(
        NamespaceOperationResult.generate(
            success,
            Optional.of(message)
        ),
        success ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  /**
   * Another temporary endpoint to accommodate the transition between tenant-based and namespace-based
   * taxonomy. <br/>
   * Uses the {@link IUserAccountService} behind the scenes to remove the given user from the given
   * namespace.<br/>
   * The permissions for this are checked twice differently. <br/>
   * In the pre-authorization, we chek whether the logged on user is either sys admin, or has
   * a "tenant admin" role. <br/>
   * However, the latter is not sufficient, because they could have that role in another namespace
   * unrelated to the given one. <br/>
   * Therefore, another check is performed within the method's body to ensure the logged on user
   * has that role for that given namespace (that is, assuming they are not sysadmin to start with). <br/>
   * The drawback for this is that a user who has been added to a namespace as non-admin cannot
   * remove themselves at this time.
   *
   * @param namespace
   * @param userId
   * @return
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace:.+}/users/{userId}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<Boolean> removeUserFromNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace,
      @ApiParam(value = "userId", required = true) @PathVariable String userId) {

    if (Strings.nullToEmpty(userId).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    // gets tenant for namespace
    Tenant tenant = tenantService.getTenantFromNamespace(namespace).orElse(null);
    if (tenant == null) {
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    // You cannot delete yourself if you are tenant admin of the same namespace
    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication());
    if (userContext.getUsername().equals(userId)) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    /*
    The @PreAuthorize annotation checks whether user is sysadmin or "tenant admin".
    The latter would return true regardless of the given namespace, as long as the user logged on
    has that role anywhere.
    However, we need to check if the user actually has admin rights on >> this << namespace.
    That does not apply to sysadmins.
    */
    if (!userContext.isSysAdmin()) {
      if (tenant.getTenantAdmins().stream().map(User::getUsername)
          .noneMatch(a -> a.equals(userContext.getUsername()))) {
        LOGGER.warn(String
            .format("User [%s] not authorized to remove user [%s] from namespace [%s].",
                userContext.getUsername(), userId, namespace));
        return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
      }
    }

    try {

      LOGGER.info(
          String.format(
              "Removing user [%s] from namespace [%s]",
              ControllerUtils.sanitize(userId),
              ControllerUtils.sanitize(namespace)
          )
      );
      return new ResponseEntity<>(
          accountService.removeUserFromTenant(tenant.getTenantId(), userId),
          HttpStatus.OK
      );

    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    } catch (Exception e) {
      LOGGER.error("Error in deleteUserFromNamespace()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private static Role[] toRoles(Collection<String> rolesStr) {
    Collection<Role> roles = rolesStr.stream().map(roleStr -> Role.of(roleStr))
        .collect(Collectors.toList());
    return roles.toArray(new Role[roles.size()]);
  }

  private static Predicate<Tenant> isOwner(String username) {
    return tenant -> tenant.hasTenantAdmin(username);
  }

  private static Predicate<Tenant> hasMemberWithRole(String username, Role role) {
    return tenant -> tenant.getUsers().stream()
        .anyMatch(user -> user.getUser().getUsername().equals(username)
            && !user.getRoles().isEmpty() && user.hasRole(role));
  }
}
