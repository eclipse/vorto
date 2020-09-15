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
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class NamespaceRequestCache {

  public static final Predicate<Namespace> PUBLIC = n -> !n.getName().startsWith(NamespaceService.PRIVATE_NAMESPACE_PREFIX);

  private Collection<Namespace> namespaces = ConcurrentHashMap.newKeySet();
  private NamespaceRepository namespaceRepository;

  public NamespaceRequestCache(@Autowired NamespaceRepository namespaceRepository) {
    this.namespaceRepository = namespaceRepository;
  }

  private void populateIfEmpty() {
    if (this.namespaces.isEmpty()) {
      this.namespaces = namespaceRepository.findAll();
    }
  }

  /**
   * Marks the cache as stale (e.g. after deleting a {@link Namespace}).<br/>
   * The cache will reload all {@link Namespace}s from the repository next time it is
   * used.
   *
   * @return
   */
  public NamespaceRequestCache stale() {
    this.namespaces.clear();
    return this;
  }

  /**
   * @return all cached {@link Namespace}s.
   */
  public Collection<Namespace> namespaces() {
    populateIfEmpty();
    return Collections.unmodifiableCollection(this.namespaces);
  }

  /**
   * Filters the cached {@link Namespace}s by the given {@link Predicate} and returns a subset if
   * applicable.
   *
   * @param filter
   * @return
   */
  public Collection<Namespace> namespaces(Predicate<Namespace> filter) {
    if (Objects.isNull(filter)) {
      return Collections.emptyList();
    }
    populateIfEmpty();
    return Collections.unmodifiableCollection(this.namespaces.stream().filter(filter).collect(
        Collectors.toList()));
  }

  /**
   * Resolves a cached {@link Namespace} by name if applicable.
   *
   * @param name
   * @return
   */
  public Optional<Namespace> namespace(String name) {
    if (Objects.isNull(name) || name.trim().isEmpty()) {
      return Optional.empty();
    }
    populateIfEmpty();
    return this.namespaces.stream().filter(n -> n.getName().equals(name)).findAny();
  }

  /**
   * Filter the cached {@link Namespace}s by the given {@link Predicate} and returns one if
   * applicable.
   *
   * @param filter
   * @return
   */
  public Optional<Namespace> namespace(Predicate<Namespace> filter) {
    if (Objects.isNull(filter)) {
      return Optional.empty();
    }
    populateIfEmpty();
    return this.namespaces.stream().filter(filter).findAny();
  }

}
