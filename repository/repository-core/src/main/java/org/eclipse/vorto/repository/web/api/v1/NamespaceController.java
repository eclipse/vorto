/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.TenantNotFoundException;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.TenantUser;
import org.eclipse.vorto.repository.domain.UserRole;
import org.eclipse.vorto.repository.oauth.IOAuthProvider;
import org.eclipse.vorto.repository.oauth.IOAuthProviderRegistry;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.tenant.NamespaceExistException;
import org.eclipse.vorto.repository.tenant.NewNamespaceNotPrivateException;
import org.eclipse.vorto.repository.tenant.NewNamespacesNotSupersetException;
import org.eclipse.vorto.repository.tenant.RestrictTenantPerOwnerException;
import org.eclipse.vorto.repository.tenant.TenantAdminDoesntExistException;
import org.eclipse.vorto.repository.tenant.TenantDoesntExistException;
import org.eclipse.vorto.repository.tenant.UpdateNotAllowedException;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.account.dto.TenantUserDto;
import org.eclipse.vorto.repository.web.api.v1.dto.Collaborator;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceDto;
import org.eclipse.vorto.repository.web.api.v1.dto.NamespaceOperationResult;
import org.eclipse.vorto.repository.web.tenant.TenantManagementController;
import org.eclipse.vorto.repository.web.tenant.dto.CreateTenantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

  /**
   * As the name indicates, validates a namespace name based on whether:
   * <ul>
   *   <li>
   *     the user is a sysadmin and does not need to prepend {@link NamespaceValidator#NAMESPACE_PREFIX}
   *     to the namespace's name, and
   *   </li>
   *   <li>
   *     the actual namespace name is valid according to the {@link NamespaceValidator#VALID_NAMESPACE}
   *     pattern.
   *   </li>
   * </ul>
   * The {@link NamespaceValidator#validate(String, IUserContext)} method returns an {@link Optional}
   * of {@link NamespaceOperationResult} that will be empty if the namespace is valid, and
   */
  public static final class NamespaceValidator {
    public static final String NAMESPACE_PREFIX = "vorto.private.";
    public static final String VALID_NAMESPACE = "(\\p{Alnum}|_)+(\\.(\\p{Alnum}|_)+)*";
    public static Optional<NamespaceOperationResult> validate(String namespace, IUserContext context) {
      if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
        return Optional.of(NamespaceOperationResult.failure("Empty namespace"));
      }
      if (!namespace.matches(VALID_NAMESPACE)) {
        return Optional.of(NamespaceOperationResult.failure("Invalid namespace notation."));
      }
      if (!context.isSysAdmin()) {
        if (!namespace.startsWith(NAMESPACE_PREFIX)) {
          return Optional.of(NamespaceOperationResult.failure("User can only register a private namespace."));
        }
      }
      return Optional.empty();
    }
  }

  
  @Autowired
  private ITenantService tenantService;
  
  @Autowired
  private IUserAccountService accountService;
  
  @Autowired
  private IOAuthProviderRegistry providerRegistry;
  
  @RequestMapping(method = RequestMethod.GET)
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Collection<NamespaceDto>> getNamespaces(Principal user) {
    Collection<NamespaceDto> namespaces = tenantService.getTenants().stream()
      .filter(tenant -> tenant.hasUser(user.getName()))
      .map(NamespaceDto::fromTenant)
      .collect(Collectors.toList());
    
    return new ResponseEntity<>(namespaces, HttpStatus.OK);
  }

  /**
   * This endpoint is supposed to replace {@link NamespaceController#getNamespaces(Principal)} in
   * the long run, if we agree that injecting a principal is not required, i.e. there is no need
   * to retrieve namespaces for a given user programmatically, and the body of the method can
   * infer the logged on user instead. <br/>
   * Since there is no easy way to inject the given {@link Principal} from the front-end (let alone
   * that it is not designed an API parameter), the sibling endpoint seems rather useless in a REST
   * context.
   * @return all namespaces the logged on user has access to.
   */
  @RequestMapping(method = RequestMethod.GET, value="/all")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Collection<NamespaceDto>> getAllNamespacesForLoggedUser() {
    IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication());
    Collection<NamespaceDto> namespaces = tenantService.getTenants().stream()
        .filter(tenant -> userContext.isSysAdmin() || tenant.hasTenantAdmin(userContext.getUsername()))
        .map(NamespaceDto::fromTenant)
        .collect(Collectors.toList());
    return new ResponseEntity<>(namespaces, HttpStatus.OK);
  }

  /**
   * This endpoint is temporary and adapted from {@link org.eclipse.vorto.repository.web.account.AccountController#getUsersForTenant}. <br/>
   * Instead of returning a list of {@link TenantUserDto}, it returns {@link Collaborator}s.<br/>
   * It still uses the {@link ITenantService} behind the scenes, until that can be refactored/removed.
   * @param namespace
   * @return
   */
  @RequestMapping(method = RequestMethod.GET, value = "/{namespace:.+}/users")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<Collection<Collaborator>> getUsersForNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace) {

    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(Collections.emptyList(), HttpStatus.PRECONDITION_FAILED);
    }

    try {
      Optional<Tenant> maybeTenant = tenantService.getTenantFromNamespace(namespace);
      if (maybeTenant.isPresent()) {
        Set<TenantUser> tenantUserSet= maybeTenant.get().getUsers();
        Set<Collaborator> collaborators = tenantUserSet.stream().map(Collaborator::fromTenantUser).collect(
            Collectors.toSet());
        return new ResponseEntity<>(collaborators, HttpStatus.OK);
      }
      else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    }
    catch (Exception e) {
      LOGGER.error("Error in getUsersForNamespace()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This endpoint is temporary and adapted from {@link org.eclipse.vorto.repository.web.account.AccountController#createTechnicalUserForTenant}. <br/>
   * It still uses the {@link ITenantService} behind the scenes, until that can be refactored/removed.
   * @param namespace
   * @param userId
   * @param user
   * @return
   */
  @RequestMapping(method = RequestMethod.POST, value = "/{namespace:.+}/users/{userId}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<Boolean> createTechnicalUserForNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace,
      @ApiParam(value = "userId", required = true) @PathVariable String userId,
      @RequestBody @ApiParam(value = "The user to be added to the namespace",
          required = true) final Collaborator user) {

    if (Strings.nullToEmpty(userId).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    Optional<Tenant> maybeTenant = tenantService.getTenantFromNamespace(namespace);
    if (!maybeTenant.isPresent()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(user.getAuthenticationProviderId()).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(user.getSubject()).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (user.getRoles().stream()
        .anyMatch(role -> role.equals(UserRole.ROLE_SYS_ADMIN))) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (userId.equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {

      LOGGER.info(
          String.format(
              "Creating technical user [%s] and adding to namespace [%s] with role(s) [%s]",
              userId,
              namespace,
              user.getRoles()
          )
      );

      Role[] roles = user.getRoles().stream().map(Role::of).toArray(Role[]::new);

      return new ResponseEntity<>(
          accountService.createTechnicalUserAndAddToTenant(
            maybeTenant.get().getTenantId(), userId, user, roles
          ),
          HttpStatus.OK
      );
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    } catch (Exception e) {
      LOGGER.error("error at addOrUpdateUsersForTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This endpoint is temporary and adapted from
   * {@link org.eclipse.vorto.repository.web.account.AccountController#addOrUpdateUsersForTenant(String, String, TenantUserDto)}. <br/>
   * It still uses the {@link ITenantService} behind the scenes, until that can be refactored/removed.<br/>
   * The main difference with the same POST endpoint {@link NamespaceController#createTechnicalUserForNamespace(String, String, Collaborator)}
   * is that we assume the user exists here, and will not be created.<br/>
   * The bottomline is that creating a user on the spot to add to a namespace implies we are dealing
   * with a technical user - otherwise one can only add an existing user (technical or not).<br/>
   * Technical users also must have a subject.<br/>
   * Note for debugging purposes: the {@link Collaborator} represented in the payload will default
   * some properties, since they are not populated in the UI - because we are dealing with an existing
   * user. <br/>
   * Therefore, it is possible to see some incoherences between the payload and the actual user,
   * since only the username/userId really matters here. <br/>
   * For instance, when this endpoint is employed to add an <i>existing</i> technical user to a
   * given namespace, the {@link Collaborator} payload will show {@code isTechnicalUser == false},
   * since this property is not sent by the UI and will default. <br/>
   * Since the user itself is not and should not be persisted here, that's rather irrelevant.<br/>
   * <b>TL;DR</b>The only reason why this is not implemented with an empty body is that we need the roles from
   * the UI.
   * @param namespace
   * @param user
   * @return
   */
  @RequestMapping(method = RequestMethod.PUT, value = "/{namespace:.+}/users")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<Boolean> addOrUpdateUsersForNamespace(
      @ApiParam(value = "namespace", required = true) @PathVariable String namespace,
      @RequestBody @ApiParam(value = "The user to be added to the namespace",
          required = true) final Collaborator user) {

    if (Strings.nullToEmpty(user.getUserId()).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (user.getRoles().stream()
        .anyMatch(role -> role.equals(UserRole.ROLE_SYS_ADMIN))) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    Optional<Tenant> maybeTenant = tenantService.getTenantFromNamespace(namespace);
    if (!maybeTenant.isPresent()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (user.getUserId().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {

      LOGGER.info(
          String.format(
              "Adding user [%s] to namespace [%s] with role(s) [%s]",
              user.getUserId(),
              namespace,
              user.getRoles()
          )
      );

        Role[] roles = user.getRoles().stream().map(Role::of).toArray(Role[]::new);
        ResponseEntity<Boolean> test = new ResponseEntity<>(true, HttpStatus.OK);
        String foo = test.toString();

      return new ResponseEntity<>(
          accountService.addUserToTenant(maybeTenant.get().getTenantId(), user.getUserId(), roles),
          HttpStatus.OK
      );
    }
    catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }
    catch (Exception e) {
      LOGGER.error("error at addOrUpdateUsersForTenant()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  
  @RequestMapping(method = RequestMethod.GET, value="/{namespace:.+}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<NamespaceDto> getNamespace(
      @ApiParam(value = "The namespace you want to retrieve", required = true) final @PathVariable String namespace,
      Principal user) {
    
    Tenant tenant = tenantService.getTenantFromNamespace(ControllerUtils.sanitize(namespace))
        .orElseThrow(() -> TenantDoesntExistException.missingForNamespace(namespace));
    
    if (!tenant.hasUser(user.getName())) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    
    return new ResponseEntity<>(NamespaceDto.fromTenant(tenant), HttpStatus.OK);
  }

  /**
   * This is a transitory endpoint to create namespaces that will ultimately replace
   * {@link TenantManagementController#createTenant(String, CreateTenantRequest)}. <br/>
   * The endpoint takes an empty body and a namespace path variable, then tries to create it by
   * associating the necessary user/tenant information before handing over to the autowired
   * {@link ITenantService}.<br/>
   * Everything "tenant" should be refactored and simplified further on, so the tenant service
   * should be gone and replaced with a namespace service eventually.
   * @param namespace
   * @return
   */
  @RequestMapping(method = RequestMethod.PUT, value="/{namespace:.+}", produces = "application/json")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<NamespaceOperationResult> createNamespace(
      @ApiParam(value = "The name of the namespace to be created", required = true)
      final @PathVariable String namespace
  ) {
    /*
      This creates a fake "tenant ID" to feed the tenant service and pointlessly populate the
      tenant table. Once everything "tenant-wise" is replaced with a simpler namespace-oriented
      architecture, the UUID itself should be removed, i.e. a "namespace service" would not care
      to use an additional unique ID since the namespace names are unique.
      */
    String fakeTenantId = UUID.randomUUID().toString().replace("-", "");
    IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication(), fakeTenantId);
    // validating namespace notation and user-related (private vs public)
    Optional<NamespaceOperationResult> validationError = NamespaceValidator.validate(namespace, userContext);
    if (validationError.isPresent()) {
      return new ResponseEntity<>(validationError.get(), HttpStatus.BAD_REQUEST);
    }

    try {
      /*
      This wraps the only admin (i.e. the given user for this request) in a set, as the service
      API allows multiple admins (but it makes no sense upon creation of a new namespace - only
      one admin and that is the user who initiated the request).
       */
      Set<String> fakeAdmins = new HashSet<>();
      fakeAdmins.add(userContext.getUsername());
      /*
      This wraps the only namespace in a set, according to service API .
       */
      Set<String> fakeNamespaces = new HashSet<>();
      fakeNamespaces.add(namespace);
      tenantService.createOrUpdateTenant(
          fakeTenantId,
          namespace,
          fakeAdmins,
          Optional.of(fakeNamespaces),
          // no authentication or authorization provider necessary
          Optional.ofNullable(null),
          Optional.ofNullable(null),
          userContext
      );

      return new ResponseEntity<>(NamespaceOperationResult.success(), HttpStatus.OK);
    }
    catch (NamespaceExistException e) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Namespace already exist"),
          HttpStatus.CONFLICT);
    }
    catch (RestrictTenantPerOwnerException e) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Namespace Quota of 1 exceeded."),
          HttpStatus.CONFLICT);
    }
    catch (IllegalArgumentException | TenantAdminDoesntExistException | UpdateNotAllowedException
        | NewNamespacesNotSupersetException | NewNamespaceNotPrivateException e) {
      return new ResponseEntity<>(NamespaceOperationResult.failure(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    catch (Exception e) {
      LOGGER.error(e);
      return new ResponseEntity<>(
          NamespaceOperationResult.failure("Internal error. Consult the vorto administrators!"),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * This is a port of {@link TenantManagementController#getTenants} and was used in the UI by the
   * TenantService (now deleted), to return a list of namespaces where the user had a specific role,
   * e.g. for model details, a list of namespaces where the user is creator (the list was then
   * filtered again in the front-end to match the namespace of the specific model).<br/>
   * This still uses the {@link ITenantService} behind the scenes for now. <br/>
   * Due to current usage, the role is always passed as one argument, so it does not seem useful to
   * allow a collection of roles at this time - therefore, the parameter has been modified to be a
   * path variable. <br/>
   * It is, however, still optional: if absent, the response will contain all namespaces where this
   * user has any permission.<br/>
   * Note also that for UI REST calls, we can privilege {@link NamespaceController#hasRoleOnNamespace},
   * since originally the UI would contain that logic but it can be more efficiently handled in
   * the back-end (see for instance the model details controller Javascript resource).
   * @param role
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/{role}", produces = "application/json")
  public ResponseEntity<Collection<NamespaceDto>> getUserAccessibleNamespacesWithRole(
      @ApiParam(value = "The (optional) role to filter namespaces which this user has access to",
          required = false) final @PathVariable(value = "role", required = false) String role) {

    IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication());

    // user is sysadmin, return all namespaces and ignore role
    if (userContext.isSysAdmin()) {
      return new ResponseEntity(
          tenantService.getTenants().stream().map(NamespaceDto::fromTenant).collect(Collectors.toList()),
          HttpStatus.OK
      );
    }
    else {
      Predicate<Tenant> filter = isOwner(userContext.getUsername());
      if (role != null) {
        Role roleFilter = Role.valueOf(role.replace(Role.rolePrefix, ""));
        filter = hasMemberWithRole(userContext.getUsername(), roleFilter);
      }
      return new ResponseEntity<>(
          tenantService.getTenants().stream().filter(filter).map(NamespaceDto::fromTenant).collect(Collectors.toList()),
          HttpStatus.OK
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
   * @param role
   * @param namespace
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/{role}/{namespace:.+}", produces = "application/json")
  public ResponseEntity<Boolean> hasRoleOnNamespace(
      @ApiParam(value = "The role to verify", required = true)
      final @PathVariable(value = "role") String role,
      @ApiParam(value = "The target namespace", required = true)
      final @PathVariable(value = "namespace", required = true) String namespace
      )
  {

    if (Strings.nullToEmpty(role).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }

    IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication());

    // user is sysadmin, the answer is yes regardless
    if (userContext.isSysAdmin()) {
      return new ResponseEntity(
          true,
          HttpStatus.OK
      );
    }
    else {
      Role roleFilter = Role.valueOf(role.replace(Role.rolePrefix, ""));
      Predicate<Tenant> filter = hasMemberWithRole(userContext.getUsername(), roleFilter);
      return new ResponseEntity<>(
          tenantService.getTenants().stream().filter(filter).anyMatch((t) -> t.getDefaultNamespace().equals(namespace)),
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
   * @return
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/userIsOnlyAdmin", produces = "application/json")
  public ResponseEntity<Boolean> isOnlyAdminForAnyNamespace() {
    IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication());

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
      @ApiParam(value = "The name of the namespace to be deleted", required = true)
      final @PathVariable String namespace
  ) {
    // validates given namespace
    if (Strings.nullToEmpty(namespace).trim().isEmpty()) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Empty namespace"), HttpStatus.BAD_REQUEST);
    }

    // gets tenant for namespace
    Tenant tenant = tenantService.getTenantFromNamespace(namespace).orElse(null);
    if (tenant == null) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Namespace does not exist"), HttpStatus.NOT_FOUND);
    }

    // gets user (tenant ID required here, otherwise the service will fail to delete even with the given tenant)
    IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenant.getTenantId());

    // validates user permissions on this namespace
    if (!(userContext.isSysAdmin() || isOwner(userContext.getUsername()).test(tenant))) {
      return new ResponseEntity<>(NamespaceOperationResult.failure("Operation forbidden for this user"), HttpStatus.FORBIDDEN);
    }

    boolean success = false;
    String message = "Operation failed for unknown reasons";
    try {
      success = tenantService.deleteTenant(tenant, userContext);
    }
    catch (TenantNotFoundException e) {
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
   * namespace.
   * @param namespace
   * @param userId
   * @return
   */
  @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace:.+}/users/{userId}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<Boolean> deleteUserFromNamespace(
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

    // You cannot delete yourself if you are tenant admin
    Authentication user = SecurityContextHolder.getContext().getAuthentication();
    if (user.getName().equals(userId)) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
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

    }
    catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }
    catch (Exception e) {
      LOGGER.error("Error in deleteUserFromNamespace()", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", value="/{namespace:.+}/collaborators/{userId}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or hasRole('ROLE_TENANT_ADMIN')")
  public ResponseEntity<String> updateCollaborator(
      @ApiParam(value = "The namespace you want to add a collaborator to.", required = true) 
      final @PathVariable String namespace,
      @ApiParam(value = "The collaborator you want to add to this namespace.", required = true) 
      final @PathVariable String userId,
      @RequestBody @ApiParam(value = "Collaborator information", required = true) 
      final Collaborator collaboratorInfo,
      Principal user) {
    
    collaboratorInfo.setUserId(userId);
    
    Tenant tenant = tenantService.getTenantFromNamespace(ControllerUtils.sanitize(namespace))
        .orElseThrow(() -> TenantDoesntExistException.missingForNamespace(namespace));
    
    if (!tenant.hasTenantAdmin(user.getName())) {
      return new ResponseEntity<>("User is not admin", HttpStatus.FORBIDDEN);
    }
    
    Optional<IOAuthProvider> authProvider = providerRegistry.getById(collaboratorInfo.getAuthenticationProviderId());
    if (!authProvider.isPresent()) {
      return new ResponseEntity<>("AuthenticationProviderId is not found.", HttpStatus.BAD_REQUEST);
    }
    
    if (!collaboratorInfo.isTechnicalUser() && !accountService.exists(collaboratorInfo.getUserId())) {
      return new ResponseEntity<>("Account doesn't exist.", HttpStatus.BAD_REQUEST);
    }
    
    if (collaboratorInfo.isTechnicalUser() && Strings.nullToEmpty(collaboratorInfo.getSubject()).trim().isEmpty()) {
      return new ResponseEntity<>("Subject is empty.", HttpStatus.BAD_REQUEST);
    }
    
    if (collaboratorInfo.getRoles().isEmpty()) {
      accountService.removeUserFromTenant(tenant.getTenantId(), collaboratorInfo.getUserId());
    } else {
      accountService.createOrUpdate(collaboratorInfo.getUserId(), authProvider.get().getId(), collaboratorInfo.getSubject(), 
          collaboratorInfo.isTechnicalUser(), tenant.getTenantId(), toRoles(collaboratorInfo.getRoles()));
    }
    
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private Role[] toRoles(Collection<String> rolesStr) {
    Collection<Role> roles = rolesStr.stream().map(roleStr -> Role.of(roleStr)).collect(Collectors.toList());
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
