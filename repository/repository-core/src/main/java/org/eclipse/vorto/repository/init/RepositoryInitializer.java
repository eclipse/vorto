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
package org.eclipse.vorto.repository.init;

import com.google.common.collect.Sets;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.IRepositoryManager;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.tenant.ITenantUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
public class RepositoryInitializer {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${server.admin:#{null}}")
  private String[] admins;

  @Autowired
  private IUserAccountService userAccountService;

  @Autowired
  private ITenantService tenantService;

  @Autowired
  private ITenantUserService tenantUserService;

  @Autowired
  private PredefinedTenants predefinedTenants;

  @Autowired
  private ModelRepositoryFactory repositoryFactory;

  @EventListener(ApplicationReadyEvent.class)
  @Profile("!test")
  public void initRepo() {
    Stream.of(admins).forEach(this::createAdminUser);

    predefinedTenants.getPredefinedTenants().forEach(this::createTenantIfNotExisting);

    Stream.of(admins).forEach(this::addSysAdRole);

    tenantService.getTenants().forEach(this::createWorkspaceIfNotExisting);
  }

  private void createAdminUser(String username) {
    if (!userAccountService.exists(username)) {
      logger.info("Creating admin user: {}", username);
      User user = User.create(username, null, null);
      // TODO : set to be configurable from configuration file
      user.setEmailAddress("vorto-dev@bosch-si.com");
      userAccountService.saveUser(user);
    }
  }

  private void addSysAdRole(String username) {
    tenantUserService.addRolesToUser(Tenant.STANDARDIZATION_TENANT_ID, username, Role.SYS_ADMIN,
        Role.TENANT_ADMIN, Role.USER, Role.MODEL_CREATOR, Role.MODEL_PROMOTER, Role.MODEL_REVIEWER);
  }

  private void createTenantIfNotExisting(PredefinedTenant tenant) {
    if (!tenantService.tenantExist(tenant.getTenantId())) {
      logger.info("Creating predefined tenant: {}", tenant);

      tenantService.createOrUpdateTenant(tenant.getTenantId(), tenant.getDefaultNamespace(),
          Sets.newHashSet(admins), Optional.empty(),
          Optional.of(tenant.getAuthenticationProvider()),
          Optional.of(tenant.getAuthorizationProvider()),
          createAdminContext(admins[0], tenant.getTenantId()));
    }
  }

  private void createWorkspaceIfNotExisting(Tenant tenant) {
    logger.info("Creating workspace for '" + tenant.getTenantId() + "' if NOT existing.");
    IRepositoryManager repoMgr = repositoryFactory.getRepositoryManager(null, null);
    repoMgr.createTenantWorkspace(tenant.getTenantId());
  }

  private IUserContext createAdminContext(String userId, String tenantId) {
    return new IUserContext() {
      @Override
      public Authentication getAuthentication() {
        return null;
      }

      @Override
      public String getUsername() {
        return userId;
      }

      @Override
      public String getWorkspaceId() {
        return tenantId;
      }

      @Override
      public String getHashedUsername() {
        return null;
      }

      @Override
      public boolean isAnonymous() {
        return false;
      }

      @Override
      public boolean isSysAdmin() {
        return true;
      }
    };
  }
}
