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

import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
import org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.services.UserBuilder;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.stream.Stream;

@Component
public class RepositoryInitializer {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public static final String STANDARDIZATION = "standardization";

  @Value("${server.admin:#{null}}")
  private String[] admins;

  @Autowired
  private UserRepositoryRoleService userRepositoryRoleService;

  @Autowired
  private DefaultUserAccountService userAccountService;

  @Autowired
  private ModelRepositoryFactory repositoryFactory;

  @EventListener(ApplicationReadyEvent.class)
  @Profile("!test")
  public void initRepo() {
    Stream.of(admins).forEach(this::createAdminUser);

  }

  private void createAdminUser(String username) {
    if (!userAccountService.exists(username)) {
      logger.info("Creating admin user: {}", username);
      try {
        User user = new UserBuilder().withName(username).withAuthenticationProviderID("GITHUB")
            .build();
        // TODO: set e-mail and authentication provider to be configurable from configuration file
        user.setEmailAddress("vorto-dev@bosch-si.com");
        userAccountService.updateUser(user);
      }
      // should never happen
      catch (InvalidUserException iue) {
        logger.error(iue.getMessage());
      }

    }
    userRepositoryRoleService.setSysadmin(username);
  }

}
