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

import java.util.Collection;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.Privilege;
import org.eclipse.vorto.repository.repositories.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides basic functionality with regards to privileges.
 */
@Service
public class PrivilegeService {

  @Autowired
  private PrivilegeRepository privilegeRepository;

  public Collection<Privilege> getPrivileges(long value) {
    return privilegeRepository.findAll().stream().filter(p -> (value & p.getPrivilege()) == p.getPrivilege())
        .collect(Collectors.toSet());
  }

}
