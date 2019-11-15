/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple POJO to contain search parameters from text search once normalized. <br/>
 * The setters (or rather, {@literal with...}-methods) are fluent-builder-styled. <br/>
 * The getters return immutable {@link Set <String>}. <br/>
 * Most service-agnostic parsing and validating logic in the search is performed instead by
 * {@link SearchTags}, whose methods are invoked here in {@link SearchParameters#build(Collection, String)}.
 * @see SearchTags
 * @author mena-bosch
 */
public class SearchParameters {

  Set<String> tenantIds = new HashSet<>();
  Set<String> names = new HashSet<>();
  Set<String> states = new HashSet<>();
  Set<String> types = new HashSet<>();
  Set<String> authors = new HashSet<>();
  Set<String> userReferences = new HashSet<>();
  Set<String> visibilities = new HashSet<>();
  Set<String> namespaces = new HashSet<>();
  Set<String> versions = new HashSet<>();

  public static SearchParameters build(Collection<String> tenantIds, String freeTextSearch) {
    // empty instance
    SearchParameters result = new SearchParameters();
    // adding tenants
    tenantIds.forEach(result::withTenantId);
    // searching for tagged content and adding to instance
    Arrays.stream(SearchTags.values()).forEach(st -> st.parseValue(result, freeTextSearch));
    // searching for untagged content and adding to instance
    SearchTags.parseUntaggedValues(freeTextSearch).forEach(result::withName);
    return result;
  }

  /**
   *
   * @param tenantId
   * @return
   */
  public SearchParameters withTenantId(String tenantId) {
    tenantIds.add(tenantId);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getTenantIds() {
    return Collections.unmodifiableSet(tenantIds);
  }

  /**
   *
   * @param name
   * @return
   */
  public SearchParameters withName(String name) {
    names.add(name);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getNames() {
    return Collections.unmodifiableSet(names);
  }

  /**
   *
   * @param state
   * @return
   */
  public SearchParameters withState(String state) {
    states.add(state);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getStates() {
    return Collections.unmodifiableSet(states);
  }

  /**
   *
   * @param type
   * @return
   */
  public SearchParameters withType(String type) {
    types.add(type);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getTypes() {
    return Collections.unmodifiableSet(types);
  }

  /**
   *
   * @param author
   * @return
   */
  public SearchParameters withAuthor(String author) {
    authors.add(author);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getAuthors() {
    return Collections.unmodifiableSet(authors);
  }

  /**
   *
   * @param userReference
   * @return
   */
  public SearchParameters withUserReference(String userReference) {
    userReferences.add(userReference);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getUserReferences() {
    return Collections.unmodifiableSet(userReferences);
  }

  /**
   *
   * @param visibility
   * @return
   */
  public SearchParameters withVisibility(String visibility) {
    visibilities.add(visibility);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getVisibilities() {
    return Collections.unmodifiableSet(visibilities);
  }

  /**
   *
   * @param namespace
   * @return
   */
  public SearchParameters withNamespace(String namespace) {
    namespaces.add(namespace);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getNamespaces() {
    return Collections.unmodifiableSet(namespaces);
  }

  /**
   *
   * @param version
   * @return
   */
  public SearchParameters withVersion(String version) {
    versions.add(version);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getVersions() {
    return Collections.unmodifiableSet(versions);
  }

}
