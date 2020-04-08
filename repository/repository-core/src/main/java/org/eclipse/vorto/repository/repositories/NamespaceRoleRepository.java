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
import org.eclipse.vorto.repository.domain.IRole;
import org.eclipse.vorto.repository.domain.NamespaceRole;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Read-only repository for namespace roles.<br/>
 * Caching here is written for read-only access, as for now, namespace roles are pretty much
 * constant and not intended to be modified at runtime. <br/>
 * If this ever changes, then the caching strategy should adapt.
 */
@Repository
@Cacheable("namespaceRoles")
public interface NamespaceRoleRepository extends org.springframework.data.repository.Repository<NamespaceRole, Long> {

  @Query("select n from NamespaceRole n where n.name = :name")
  IRole find(@Param("name") String name);

  @Query("select case when count(n) > 0 then true else false end from NamespaceRole n where n.name = :role")
  boolean exists(@Param("role") String roleName);

  Set<NamespaceRole> findAll();
}
