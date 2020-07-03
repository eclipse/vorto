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
import org.eclipse.vorto.repository.domain.Privilege;
import org.eclipse.vorto.repository.init.DBTablesInitializer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Read-only repository yielding all applicable privileges for roles.<br/>
 * The repository is intended to be read-only at runtime, so no cache eviction strategy is used.<br/>
 * Still a {@link CrudRepository} because it can be populated at Vorto initialization by
 * {@link DBTablesInitializer}, if the table is found empty.
 */
@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege, Long> {

  @Query("select p from Privilege p where p.name = :name")
  Privilege find(@Param("name") String name);

  Set<Privilege> findAll();
}
