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

import java.util.Arrays;
import org.eclipse.vorto.repository.domain.NamespaceRole;
import org.eclipse.vorto.repository.domain.Privilege;
import org.eclipse.vorto.repository.domain.RepositoryRole;
import org.eclipse.vorto.repository.repositories.NamespaceRoleRepository;
import org.eclipse.vorto.repository.repositories.PrivilegeRepository;
import org.eclipse.vorto.repository.repositories.RepositoryRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

/**
 * This class hooks up at repository initialization, autowires the 3 repositories where
 * "runtime-constant" data is held (privileges, namespace roles and repository roles) and verifies
 * whether those tables have any data.<br/>
 * If they do not, logs a debug message and populates them automatically with default values.<br/>
 * Typically, this would occur when starting a Vorto repository for the first time.
 */
@Service
public class DBTablesInitializer implements ApplicationRunner {

  public static final Privilege[] DEFAULT_PRIVILEGES;
  public static final NamespaceRole[] DEFAULT_NAMESPACE_ROLES;
  public static final RepositoryRole[] DEFAULT_REPOSITORY_ROLES;

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  static {
    DEFAULT_PRIVILEGES = new Privilege[]{
        new Privilege(1, "read"),
        new Privilege(2, "write"),
        new Privilege(3, "admin")
    };

    DEFAULT_NAMESPACE_ROLES = new NamespaceRole[]{
        new NamespaceRole(1, "model_viewer", 1),
        new NamespaceRole(2, "model_creator", 3),
        new NamespaceRole(4, "model_promoter", 3),
        new NamespaceRole(8, "model_reviewer", 3),
        new NamespaceRole(16, "model_publisher", 3),
        new NamespaceRole(32, "namespace_admin", 7)
    };

    DEFAULT_REPOSITORY_ROLES = new RepositoryRole[]{
        new RepositoryRole(1, "sysadmin", 7)
    };

  }

  @Autowired
  private PrivilegeRepository privilegeRepository;

  @Autowired
  private NamespaceRoleRepository namespaceRoleRepository;

  @Autowired
  private RepositoryRoleRepository repositoryRoleRepository;

  @Override
  public void run(ApplicationArguments applicationArguments) throws Exception {
    if (privilegeRepository.findAll().isEmpty()) {
      LOGGER.debug("Found privileges table empty - populating with default values.");
      Arrays.stream(DEFAULT_PRIVILEGES).forEach(privilegeRepository::save);
    }
    if (namespaceRoleRepository.findAll().isEmpty()) {
      LOGGER.debug("Found namespace roles table empty - populating with default values.");
      Arrays.stream(DEFAULT_NAMESPACE_ROLES).forEach(namespaceRoleRepository::save);
    }
    if (repositoryRoleRepository.findAll().isEmpty()) {
      LOGGER.debug("Found repository roles table empty - populating with default values.");
      Arrays.stream(DEFAULT_REPOSITORY_ROLES).forEach(repositoryRoleRepository::save);
    }
  }
}
