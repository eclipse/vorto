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
package org.eclipse.vorto.repository.core.impl.cache;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.vorto.repository.domain.IRole;
import org.modeshape.common.collection.Collections;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Provides a request-scoped cache for user-namespace role lookups by workspaceID. <br/>
 * Maps the roles by a composite of workspaceID and username. <br/>
 * Expires each request. <br/>
 * The idea is to mitigate the unnecessary load on the database called by highly frequent and often
 * identical invocations of
 * {@link org.eclipse.vorto.repository.core.impl.ModelRepositoryFactory#getUserRoles(String, String)}.
 */
@Service
@RequestScope
public class UserNamespaceRolesCache {

  private Map<String, Set<IRole>> map;

  public UserNamespaceRolesCache() {
    map = new ConcurrentHashMap<>();
  }

  public Optional<Set<IRole>> get(String compositeUserWorkspaceID) {
    Set<IRole> result = map.get(compositeUserWorkspaceID);
    if (result != null) {
      result = Collections.unmodifiableSet(result);
    }
    return Optional.ofNullable(result);
  }

  /**
   * Deliberately hides the return value of {@link ConcurrentHashMap#put(Object, Object)} - see API.
   * @param compositeUserWorkspaceID
   * @param roles
   * @return
   */
  public void put(String compositeUserWorkspaceID, Collection<IRole> roles) {
    map.put(compositeUserWorkspaceID, new HashSet<>(roles));
  }
}
