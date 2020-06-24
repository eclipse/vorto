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
package org.eclipse.vorto.repository.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;

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

  Set<String> workspaceIds = new HashSet<>();
  Set<String> taggedNames = new HashSet<>();
  Set<String> unTaggedNames = new HashSet<>();
  Set<String> states = new HashSet<>();
  Set<String> types = new HashSet<>();
  Set<String> authors = new HashSet<>();
  Set<String> userReferences = new HashSet<>();
  Set<String> visibilities = new HashSet<>();
  Set<String> namespaces = new HashSet<>();
  Set<String> versions = new HashSet<>();

  /**
   * Builds an instance of {@link SearchParameters} with the given collection of tenant IDs and
   * search {@link String}.<br/>
   * This iterates over all known tags (see {@link SearchTags} and collects values, then parses
   * un-tagged values and collects them as name search terms.
   * @param tenantIds
   * @param freeTextSearch
   * @return
   */
  public static SearchParameters build(Collection<String> tenantIds, String freeTextSearch) {
    // empty instance
    SearchParameters result = new SearchParameters();
    // adding tenants
    if (Objects.nonNull(tenantIds)) {
      tenantIds.forEach(result::withWorkspaceId);
    }
    // searching for tagged content and adding to instance
    Arrays.stream(SearchTags.values()).forEach(st -> st.parseValue(result, freeTextSearch));
    // searching for untagged content and adding to instance
    SearchTags.parseUntaggedValues(freeTextSearch).forEach(result::withUntaggedName);
    return result;
  }

  /**
   * Used in JCR query searches, where repositories are filtered and collected by tenant ID(s) 
   * before searching, and therefore not used in {@link SearchParameters}.<br/>
   * @see org.eclipse.vorto.repository.search.impl.SimpleSearchService#search(String) 
   * @see org.eclipse.vorto.repository.search.impl.SimpleSearchService#search(String, IUserContext) 
   * @see SearchParameters#build(Collection, String)
   * @param freeTextSearch
   * @return
   */
  public static SearchParameters build(String freeTextSearch) {
    return build(null, freeTextSearch);
  }

  /**
   *
   * @param workspaceId
   * @return
   */
  public SearchParameters withWorkspaceId(String workspaceId) {
    workspaceIds.add(workspaceId);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getWorkspaceIds() {
    return Collections.unmodifiableSet(workspaceIds);
  }

  /**
   *
   * @return
   */
  public boolean hasWorkspaceIds() {
    return !workspaceIds.isEmpty();
  }

  /**
   *
   * @param taggedName
   * @return
   */
  public SearchParameters withTaggedName(String taggedName) {
    taggedNames.add(taggedName);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getTaggedNames() {
    return Collections.unmodifiableSet(taggedNames);
  }

  /**
   *
   * @return
   */
  public boolean hasTaggedNames() {
    return !taggedNames.isEmpty();
  }

  /**
   *
   * @param unTaggedName
   * @return
   */
  public SearchParameters withUntaggedName(String unTaggedName) {
    unTaggedNames.add(unTaggedName);
    return this;
  }

  /**
   *
   * @return
   */
  public Set<String> getUntaggedNames() {
    return Collections.unmodifiableSet(unTaggedNames);
  }

  /**
   *
   * @return
   */
  public boolean hasUntaggedNames() {
    return !unTaggedNames.isEmpty();
  }

  /**
   * Legacy getter for the simple search, where name searches all resolve to {@literal [vorto:name]}.
   * <br/>This is in contrast with the Elasticsearch service, where tagged name searches specifically
   * resolve to the {@literal displayName} field (which seems to be always the same as the
   * {@link ModelId#getName()} model field), while un-tagged searches are broader, and resolve to
   * {@literal displayName}, {@literal description} and {@literal searchableName}.<br/>
   * This getter therefore returns a junction of {@link SearchParameters#getTaggedNames()} and
   * {@link SearchParameters#getUntaggedNames()}, and should only be used by the simple search
   * service.
   *
   * @return
   */
  public Set<String> getNames() {
    return Collections.unmodifiableSet(Stream.concat(taggedNames.stream(), unTaggedNames.stream()).collect(
        Collectors.toSet()));
  }

  /**
   *
   * @return whether either tagged or un-tagged names have any values.
   * @see SearchParameters#getNames() for context.
   */
  public boolean hasNames() {
    return !taggedNames.isEmpty() || !unTaggedNames.isEmpty();
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
   * @return
   */
  public boolean hasStates() {
    return !states.isEmpty();
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
   * @return
   */
  public boolean hasTypes() {
    return !types.isEmpty();
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
   * @return
   */
  public boolean hasAuthors() {
    return !authors.isEmpty();
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
   * @return
   */
  public boolean hasUserReferences() {
    return !userReferences.isEmpty();
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
   * @return
   */
  public boolean hasVisibilities() {
    return !visibilities.isEmpty();
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
   * @return
   */
  public boolean hasNamespaces() {
    return !namespaces.isEmpty();
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

  /**
   *
   * @return
   */
  public boolean hasVersions() {
    return !versions.isEmpty();
  }

  /**
   * @return whether no fields were populated (empty query).
   */
  public boolean isEmpty() {
    return !hasAuthors() && !hasTaggedNames() && !hasUntaggedNames() && !hasNamespaces() && !hasStates() && !hasStates() &&
        !hasWorkspaceIds() && !hasTypes() && !hasUserReferences() && !hasVersions() && !hasVisibilities();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchParameters that = (SearchParameters) o;
    return workspaceIds.equals(that.workspaceIds) &&
        taggedNames.equals(that.taggedNames) &&
        unTaggedNames.equals(that.unTaggedNames) &&
        states.equals(that.states) &&
        types.equals(that.types) &&
        authors.equals(that.authors) &&
        userReferences.equals(that.userReferences) &&
        visibilities.equals(that.visibilities) &&
        namespaces.equals(that.namespaces) &&
        versions.equals(that.versions);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(workspaceIds, taggedNames, unTaggedNames, states, types, authors, userReferences,
            visibilities, namespaces, versions);
  }
}
