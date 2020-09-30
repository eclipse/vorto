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

import static org.eclipse.vorto.repository.services.NamespaceService.NAMESPACE_SEPARATOR;
import static org.eclipse.vorto.repository.services.NamespaceService.PRIVATE_NAMESPACE_PREFIX;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.repositories.NamespaceRepository;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
public class NamespaceRequestCache {

  private static final Logger LOGGER = Logger.getLogger(NamespaceRequestCache.class);

  public static final Predicate<Namespace> PUBLIC = n -> !n.getName()
      .startsWith(PRIVATE_NAMESPACE_PREFIX);

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
   * Resolves a cached {@link Namespace} by name if applicable.<br/>
   * This will also attempt to resolve a "virtual" namespace string, consisting in the real
   * namespace appended with an arbitrary number of {@literal .[sub-namespace]} strings to the
   * root namespace.<br/>
   * The latter is useful when invoking e.g. {@link NamespaceService#getByName(String)} with
   * {@link ModelId#getNamespace()} since the latter does not trim out the "virtual" sub-namespaces.
   * <br/>
     * For instance, when given {@literal com.bosch.iot.suite.example.octopussuiteedition}:
   * <ol>
   *   <li>
   *     Attempts to resolve {@literal com.bosch.iot.suite.example.octopussuiteedition} and get
   *     its workspace ID, which fails
   *   </li>
   *   <li>
   *     Attempts to resolve {@literal com.bosch.iot.suite.example} and get its workspace ID, which
   *     fails again
   *   </li>
   *   <li>
   *     Attempts to resolve {@literal com.bosch.iot.suite} and get its workspace ID, which
   *     succeeds
   *   </li>
   * </ol>
   * Note: this could be furtherly optimized by extracting the recursive code into a separate
   * method that takes two parameters: the virtual namespace and the currently processed segment.
   * If the segment is resolved to a real namespace, the virtual namespace would be put in map of
   * namespace entity by virtual namespace string, so it could be easily retrieved without
   * recursion next time within the same request. <br/>
   * Also note: a very similar implementation is featured in the
   * {@link org.eclipse.vorto.repository.services.UserNamespaceRoleService}, as the latter cannot
   * use the {@link NamespaceService} due to circular dependencies.
   *
   * @param name
   * @return
   */
  public Optional<Namespace> namespace(String name) {
    if (Objects.isNull(name) || name.trim().isEmpty()) {
      return Optional.empty();
    }
    populateIfEmpty();
    Optional<Namespace> result = this.namespaces.stream().filter(n -> n.getName().equals(name))
        .findAny();
    if (result.isPresent()) {
      LOGGER.debug(
          String.format(
              "Resolved namespace [%s]", name
          )
      );
      return result;
    } else {
      int lastSeparator = name.lastIndexOf(NAMESPACE_SEPARATOR);
      if (lastSeparator > 0) {
        String trimmed = name.substring(0, lastSeparator);
        LOGGER.debug(
            String.format(
                "Could not resolve namespace [%s]. Attempting with [%s]",
                name, trimmed
            )
        );
        return namespace(trimmed);
      } else {
        LOGGER.warn(
            String.format(
                "Could not resolve namespace [%s]. No further attempts can be made",
                name
            )
        );
        return Optional.empty();
      }
    }
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
