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

import java.util.Collection;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.domain.UserRepositoryRoles;
import org.eclipse.vorto.repository.repositories.UserRepository;
import org.eclipse.vorto.repository.repositories.UserRepositoryRoleRepository;
import org.eclipse.vorto.repository.services.UserBuilder;
import org.eclipse.vorto.repository.services.exceptions.InvalidUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RepositoryInitializer {

  private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryInitializer.class);

  public static final String STANDARDIZATION = "standardization";

  @Value("${server.admin:#{null}}")
  private String[] admins;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserRepositoryRoleRepository userRepositoryRoleRepository;

  @EventListener(ApplicationReadyEvent.class)
  @Profile("!test")
  public void initRepo() {
    for (int id = 0; id < admins.length; id++) {
      // ids 1-indexed
      createAdminUser(admins[id], id + 1);
    }

  }

  private void createAdminUser(String username, long id) {
    User actualUser = null;
    Collection<User> users = userRepository.findByUsername(username);
    if (users.size() > 1) {
      LOGGER.warn("Could not resolve only one user with the given username - skipping admin creation");
    }
    else if (users.size() == 0) {
      LOGGER.info("Creating admin user with GITHUB OAuth provider");
      try {
        actualUser = new UserBuilder().withName(username).build();
      } catch (InvalidUserException iue) {
        LOGGER.warn("Unable to create admin user - skipping.");
        return;
      }
      // TODO : set to be configurable from configuration file
      actualUser.setEmailAddress("vorto-dev@bosch-si.com");
      actualUser.setAuthenticationProviderId("GITHUB");
      actualUser.setTechnicalUser(false);
      actualUser = userRepository.save(actualUser);
    }

    UserRepositoryRoles roles = userRepositoryRoleRepository.findByUser(actualUser.getId())
        .orElse(
            new UserRepositoryRoles()
        );
    if (roles.getUser() == null) {
      roles.setUser(actualUser);
    }
    if (roles.getId() == null) {
      roles.setId(id);
    }
    roles.setRoles(RepositoryRole.SYS_ADMIN.getRole());

    userRepositoryRoleRepository.save(roles);
  }

}
