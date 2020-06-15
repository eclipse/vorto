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

import java.util.Collection;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Holds all persisted namespaces.
 * TODO #2265 better caching key vs eviction
 */
@Repository
public interface NamespaceRepository extends CrudRepository<Namespace, Long> {

  Namespace findByName(String name);

  Namespace findByWorkspaceId(String workspaceId);

  @Query("SELECT n from Namespace n WHERE :name LIKE CONCAT(n.name, '.%') ")
  Namespace findParent(@Param("name") String name);

  @Query("SELECT n from Namespace n WHERE LOWER(n.name) LIKE %?1%")
  Collection<Namespace> findNamespaceByPartial(String partial);

  @Query("SELECT n from Namespace n WHERE n.name NOT LIKE 'vorto.private.%' AND LOWER(n.name) LIKE %?1%")
  Collection<Namespace> findPublicNamespaceByPartial(String partial);

  @Override
  Collection<Namespace> findAll();
}
