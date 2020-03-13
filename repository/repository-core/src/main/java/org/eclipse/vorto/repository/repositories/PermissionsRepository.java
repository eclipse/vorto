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
package org.eclipse.vorto.repository.repositories;

import java.util.Set;
import org.eclipse.vorto.repository.domain.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionsRepository extends org.springframework.data.repository.Repository<Permission, Long> {
  @Query("select p from Permission p where p.name = :name")
  Permission find(String name);
  Set<Permission> findAll();
}
