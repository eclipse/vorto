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

import org.eclipse.vorto.repository.repositories.NamespaceRolesRepository;
import org.eclipse.vorto.repository.repositories.PrivilegesRepository;
import org.eclipse.vorto.repository.repositories.RepositoryRolesRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Basic integration tests for the {@link RolesService}.
 */
@RunWith(SpringRunner.class)
public class RolesServiceTest {

  @TestConfiguration
  static class RolesServiceTestConfig {

    @Bean
    public RolesService getRolesService() {
      return new RolesService();
    }
  }

  @Autowired
  private RolesService rolesService;

  @MockBean
  private NamespaceRolesRepository namespaceRolesRepository;

  @MockBean
  private RepositoryRolesRepository repositoryRolesRepository;

  @MockBean
  private PrivilegesRepository privilegesRepository;

  @MockBean
  private PrivilegesService privilegesService;

  @Test(expected = RoleDoesNotExistException.class)
  public void testGetPrivilegesFromFakeRole() throws Exception {
    rolesService.getPrivileges("foo");
  }
}
