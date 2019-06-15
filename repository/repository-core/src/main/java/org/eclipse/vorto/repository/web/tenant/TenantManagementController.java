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
package org.eclipse.vorto.repository.web.tenant;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.INotificationService.NotificationProblem;
import org.eclipse.vorto.repository.notification.message.OfficialNamespaceRequest;
import org.eclipse.vorto.repository.tenant.NamespaceExistException;
import org.eclipse.vorto.repository.tenant.NewNamespaceNotPrivateException;
import org.eclipse.vorto.repository.tenant.NewNamespacesNotSupersetException;
import org.eclipse.vorto.repository.tenant.RestrictTenantPerOwnerException;
import org.eclipse.vorto.repository.tenant.TenantAdminDoesntExistException;
import org.eclipse.vorto.repository.tenant.TenantService;
import org.eclipse.vorto.repository.tenant.UpdateNotAllowedException;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.tenant.dto.CreateTenantRequest;
import org.eclipse.vorto.repository.web.tenant.dto.NamespacesRequest;
import org.eclipse.vorto.repository.web.tenant.dto.TenantDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.common.base.Strings;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/rest")
public class TenantManagementController {

  private static Logger logger = Logger.getLogger(TenantManagementController.class);

  private TenantService tenantService;

  private INotificationService notificationServices;

  private IUserAccountService userAccountService;

  public TenantManagementController(@Autowired TenantService tenantService,
      @Autowired INotificationService notificationServices,
      @Autowired IUserAccountService userAccountService) {
    this.tenantService = tenantService;
    this.notificationServices = notificationServices;
    this.userAccountService = userAccountService;
  }

  @PreAuthorize("isAuthenticated()")
  @PutMapping(value = "/tenants/{tenantId:.+}", produces = "application/json")
  public ResponseEntity<Result> createTenant(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @RequestBody @ApiParam(value = "The information needed for the tenant creation request",
          required = true) final CreateTenantRequest tenantRequest) {

    IUserContext userContext =
        UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenantId);

    if (Strings.nullToEmpty(tenantRequest.getDefaultNamespace()).trim().isEmpty()) {
      return new ResponseEntity<>(Result.failure("Empty namespace"), HttpStatus.BAD_REQUEST);
    }

    if (tenantRequest.getTenantAdmins() == null || tenantRequest.getTenantAdmins().isEmpty()) {
      return new ResponseEntity<>(Result.failure("Empty tenantAdmin"), HttpStatus.BAD_REQUEST);
    }

    try {

      tenantService.createOrUpdateTenant(ControllerUtils.sanitize(tenantId),
          tenantRequest.getDefaultNamespace(), tenantRequest.getTenantAdmins(),
          Optional.ofNullable(tenantRequest.getNamespaces()),
          Optional.ofNullable(tenantRequest.getAuthenticationProvider()),
          Optional.ofNullable(tenantRequest.getAuthorizationProvider()), userContext);

      return new ResponseEntity<>(Result.success(), HttpStatus.OK);

    } catch (NamespaceExistException e) {
      return new ResponseEntity<>(Result.failure("Namespace already exist"),
          HttpStatus.CONFLICT);
    }catch (RestrictTenantPerOwnerException e) {
        return new ResponseEntity<>(Result.failure("Namespace Quota of 1 exceeded."),
            HttpStatus.CONFLICT);
      }catch (IllegalArgumentException | TenantAdminDoesntExistException | UpdateNotAllowedException
        | NewNamespacesNotSupersetException | NewNamespaceNotPrivateException e) {
      return new ResponseEntity<>(Result.failure(e.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      logger.error(e);
      return new ResponseEntity<>(
          Result.failure("Internal error. Consult the vorto administrators!"),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PreAuthorize("isAuthenticated()")
  @DeleteMapping(value = "/tenants/{tenantId:.+}", produces = "application/json")
  public ResponseEntity<Boolean> removeTenant(@ApiParam(value = "The id of the tenant",
      required = true) final @PathVariable String tenantId) {

    String tenantID = ControllerUtils.sanitize(tenantId);

    Tenant tenant = tenantService.getTenant(tenantID).orElseThrow(
        () -> new IllegalArgumentException("TenantID '" + tenantID + "' doesnt exist."));

    IUserContext userContext =
        UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenantID);

    if (!(userContext.isSysAdmin() || owner(tenant, userContext.getAuthentication()))) {
      return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
    }

    try {
      return new ResponseEntity<>(tenantService.deleteTenant(tenant, userContext), HttpStatus.OK);
    } catch (Exception e) {
      logger.error(e);
      return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private boolean owner(Tenant tenant, Principal user) {
    return tenant.getOwner().getUsername().equals(user.getName());
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/tenants", produces = "application/json")
  public ResponseEntity<Collection<TenantDto>> getTenants(
      @ApiParam(value = "If set, tenants are filtered based on the role of the user",
          required = false) final @RequestParam(value = "role", required = false) String role,
      Principal user) {

    Authentication userAuth = (Authentication) user;

    boolean isSysAdmin = UserContext.isSysAdmin(userAuth);

    Collection<Tenant> tenants = tenantService.getTenants();

    if (!isSysAdmin) {
      Predicate<Tenant> filter = isOwner(userAuth.getName());
      if (role != null) {
        Role roleFilter = Role.valueOf(role.replace(Role.rolePrefix, ""));
        filter = hasMemberWithRole(userAuth.getName(), roleFilter);
      }
      tenants = tenants.stream().filter(filter).collect(Collectors.toList());
    }

    return new ResponseEntity<>(
        tenants.stream().map(TenantDto::fromTenant).collect(Collectors.toList()), HttpStatus.OK);
  }

  private Predicate<Tenant> isOwner(String username) {
    return tenant -> tenant.getOwner().getUsername().equals(username)
        || tenant.hasTenantAdmin(username);
  }

  private Predicate<Tenant> hasMemberWithRole(String username, Role role) {
    return tenant -> tenant.getUsers().stream()
        .anyMatch(user -> user.getUser().getUsername().equals(username)
            && !user.getRoles().isEmpty() && user.hasRole(role));
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/tenants/{tenantId:.+}", produces = "application/json")
  public ResponseEntity<TenantDto> getTenant(@ApiParam(value = "The id of the tenant",
      required = true) final @PathVariable String tenantId, final Principal user) {

    boolean isSysAdmin = UserContext.isSysAdmin((Authentication) user);

    if (Strings.nullToEmpty(tenantId).trim().isEmpty()) {
      return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
    }

    String tenantID = ControllerUtils.sanitize(tenantId);

    return tenantService.getTenant(tenantID).flatMap(tenant -> {
      if (isSysAdmin || isOwner(user.getName()).test(tenant)) {
        return Optional.of(TenantDto.fromTenant(tenant));
      }
      return Optional.empty();
    }).map(tenant -> new ResponseEntity<>(tenant, HttpStatus.OK))
        .orElse(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  @PutMapping(value = "/tenants/{tenantId}/namespaces", produces = "application/json")
  public ResponseEntity<Boolean> updateNamespace(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "The information needed to update the namespaces of tenant",
          required = true) final @RequestBody NamespacesRequest namespacesRequest) {

    String tenantID = ControllerUtils.sanitize(tenantId);
    
    IUserContext userContext =
        UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenantID);

    if (namespacesRequest.getNamespaces() == null || namespacesRequest.getNamespaces().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    try {
      tenantService.updateTenantNamespaces(tenantID, namespacesRequest.getNamespaces(), userContext);

      return new ResponseEntity<>(true, HttpStatus.OK);
    } catch (NewNamespacesNotSupersetException e) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    } catch (NamespaceExistException e) {
      return new ResponseEntity<>(false, HttpStatus.CONFLICT);
    }
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
  @PostMapping(value = "/tenants/{tenantId}/namespaces", produces = "application/json")
  public ResponseEntity<Boolean> addNamespaces(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @RequestBody @ApiParam(value = "The information needed to update the namespaces of tenant",
          required = true) final NamespacesRequest namespacesRequest) {

    String tenantID = ControllerUtils.sanitize(tenantId);

    if (namespacesRequest.getNamespaces() == null || namespacesRequest.getNamespaces().isEmpty()) {
      return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    try {
      tenantService.addNamespacesToTenant(tenantID, namespacesRequest.getNamespaces());

      return new ResponseEntity<>(true, HttpStatus.OK);
    } catch (NamespaceExistException e) {
      return new ResponseEntity<>(false, HttpStatus.CONFLICT);
    }
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping(value = "/namespaces/{namespace}/valid", produces = "application/json")
  public ResponseEntity<Boolean> validNamespace(@ApiParam(value = "The namespace to validate",
      required = true) final @PathVariable String namespace) {
    return new ResponseEntity<>(!tenantService.conflictsWithExistingNamespace(namespace),
        HttpStatus.OK);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = "/tenants/{tenantId}/namespaces/{namespace}/requestOfficial")
  public ResponseEntity<Boolean> sendOfficialNamespaceRequest(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "The namespace to request",
          required = true) final @PathVariable String namespace) {

    Authentication user = SecurityContextHolder.getContext().getAuthentication();

    boolean hasSentEmail = false;

    for (User admin : userAccountService.getSystemAdministrators()) {
      if (admin.hasEmailAddress()) {
        OfficialNamespaceRequest officialNamespaceRequest = new OfficialNamespaceRequest(admin,
            ControllerUtils.sanitize(tenantId), ControllerUtils.sanitize(namespace), user.getName(), new Date());
        try {
          notificationServices.sendNotification(officialNamespaceRequest);
          hasSentEmail = true;
        } catch (NotificationProblem e) {
          logger.error(String.format(
              "Not able to send OfficialNamespaceRequest email [Recipient: %s, Request: %s].",
              admin.getEmailAddress(), officialNamespaceRequest), e);
        }
      }
    }

    return new ResponseEntity<>(Boolean.valueOf(hasSentEmail), HttpStatus.OK);
  }

  public static class Result {
    private boolean success;
    private String errorMessage;

    static Result success() {
      return new Result(true, null);
    }

    static Result failure(String msg) {
      return new Result(false, msg);
    }

    private Result(boolean success, String errorMessage) {
      this.success = success;
      this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
      return success;
    }

    public String getErrorMessage() {
      return errorMessage;
    }
  }
}
