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
import java.util.Map;
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

/**
 * Caches namespace and virtual namespace resolution within the scope of a single request. <br/>
 * <b>All namespaces are lowercased for comparison</b>. <br/>
 * All virtual namespace mappings are also lowercased.
 */
@Service
@RequestScope
public class NamespaceRequestCache {

  private static final Logger LOGGER = Logger.getLogger(NamespaceRequestCache.class);

  public static final Predicate<Namespace> PUBLIC = n -> !n.getName()
      .startsWith(PRIVATE_NAMESPACE_PREFIX);

  private Collection<Namespace> namespaces = ConcurrentHashMap.newKeySet();
  private Map<String, Namespace> virtualNamespaces = new ConcurrentHashMap<>();

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
   * Note: virtual namespaces are cached within the scope of a request. <br/>
   *
   * @param name
   * @return
   */
  public Optional<Namespace> namespace(String name) {
    // boilerplate null/empty checks
    if (Objects.isNull(name) || name.trim().isEmpty()) {
      return Optional.empty();
    }
    // lazy population
    populateIfEmpty();

    // lookup into virtual namespace map first
    if (virtualNamespaces.containsKey(name)) {
      Namespace result = virtualNamespaces.get(name);
      LOGGER.debug(
          String.format(
              "Resolved virtual namespace [%s] to [%s]", name, result.getName()
          )
      );
      return Optional.of(result);
    }
    // resolving by name equality
    Optional<Namespace> result = this.namespaces.stream().filter(n -> n.getName().equalsIgnoreCase(name))
        .findAny();
    if (result.isPresent()) {
      LOGGER.debug(
          String.format(
              "Resolved namespace [%s]", name
          )
      );
      return result;
    } else {
      return resolveVirtualNamespaceRecursively(name.toLowerCase(), name.toLowerCase());
    }
  }

  /**
   * Recursive part of the virtual namespace resolution strategy. <br/>
   * Keeps the root namespace name identical along deep invocations in order to both log and
   * cache in the virtual namespaces map, if found.
   *
   * @param rootNamespaceName
   * @param currentName
   * @return
   */
  private Optional<Namespace> resolveVirtualNamespaceRecursively(String rootNamespaceName,
      String currentName) {
    int lastSeparator = currentName.lastIndexOf(NAMESPACE_SEPARATOR);
    if (lastSeparator > 0) {
      String trimmed = currentName.substring(0, lastSeparator);
      LOGGER.debug(
          String.format(
              "Could not resolve namespace [%s] with [%s]. Attempting with [%s]",
              rootNamespaceName, currentName, trimmed
          )
      );
      Optional<Namespace> result = namespaces.stream().filter(n -> n.getName().equals(trimmed))
          .findAny();
      if (result.isPresent()) {
        // updating virtual namespace cache
        virtualNamespaces.put(rootNamespaceName, result.get());
        LOGGER.debug(
            String.format(
                "Resolved namespace [%s] with [%s]", rootNamespaceName, trimmed
            )
        );
        return result;
        // recursing
      } else {
        return resolveVirtualNamespaceRecursively(rootNamespaceName, trimmed);
      }
      // no further namespace chunk available to recurse through - giving up
    } else {
      LOGGER.warn(
          String.format(
              "Could not resolve namespace [%s]. No further attempts can be made",
              rootNamespaceName
          )
      );
      return Optional.empty();
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
