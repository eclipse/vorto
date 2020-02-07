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
package org.eclipse.vorto.repository.core.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.eclipse.vorto.repository.search.SearchParameters;
import org.junit.Test;

/**
 * Unit tests for the search input parsing, which is independent from the business logic in each
 * search service (simple search or Elasticsearch). <br/> Each test provides an input string and
 * compares the {@link org.eclipse.vorto.repository.search.SearchParameters} built with it with a
 * pre-initialized instance of SearchParameters.<br/> The scope is to verify that tagged, un-tagged
 * and mixed inputs are parsed into the {@link org.eclipse.vorto.repository.search.SearchParameters}
 * POJO appropriately, contrary to the unit/integration tests specific to each of the services,
 * which analyze the model retrieved by actually initiating a search. <br/> Most if not all of the
 * logic tested here pertains to {@link org.eclipse.vorto.repository.search.SearchTags}, whose
 * parsing utility methods are invoked behind the scenes by {@link org.eclipse.vorto.repository.search.SearchParameters}.<br/>
 * It is important to notice that the term correlation logic ({@code AND} vs. {@code OR},
 * essentially) pertains to the dedicated search services, as they are ultimately responsible for
 * building the search query (respectively Elasticsearch JSON or JCR SQL-2 string), so that aspect
 * cannot be tested here.
 *
 * @author mena-bosch
 */
public class SearchUnitTest {

  /**
   * Notice how values without wildcards are automatically appended a multi-character one behind the
   * scenes, for {@code name} searches <i>only</i>.
   */
  @Test
  public void testMixedNameSearch() {
    String query = "name:abc def *ghi j?l";
    SearchParameters expected = new SearchParameters()
        .withTaggedName("abc*") // adding artificially here since we're not invoking "build"
        .withUntaggedName("def*") // as above
        .withUntaggedName("*ghi")
        .withUntaggedName("j?l");
    assertEquals(expected, SearchParameters.build(query));
  }

  @Test
  public void testMixedNameSearchWithCaseInsensitiveTags() {
    String query = "NAME:abc aUtHoR:def USERreference:ghi vISIBILITY:jkl TyPe:mno sTaTe:pqr nAmesPace:stu Version:vwx";
    SearchParameters expected = new SearchParameters()
        .withTaggedName("abc*")
        .withAuthor("def")
        .withUserReference("ghi")
        .withVisibility("jkl")
        .withType("mno")
        .withState("pqr")
        .withNamespace("stu")
        .withVersion("vwx");
    assertEquals(expected, SearchParameters.build(query));
  }

  /**
   * This tests the uniqueness of values per same tag. <br/> While all values are searched by the
   * services as case-insensitive, they are not pre-processed or altered save for some wildcard
   * manipulation.
   */
  @Test
  public void testRepeatedTagValuesUniqueCaseSensitive() {
    String query = "name:mena NAME:mena NAME:mEnA name:potato potato POTATOOO?";
    SearchParameters expected = new SearchParameters()
        .withTaggedName("mena*")
        .withTaggedName("mEnA*")
        .withTaggedName("potato*")
        .withUntaggedName("potato*")
        .withUntaggedName("POTATOOO?");
    assertEquals(expected, SearchParameters.build(query));
  }

  /**
   * The tagged value will be ignored here, but the {@literal name:} expression will instead be
   * parsed as an un-tagged (and incorrect) {@code name} search.
   */
  @Test
  public void testEmptyValueForTag() {
    String query = "name:";
    SearchParameters expected = new SearchParameters()
        .withUntaggedName("name:*");
    assertEquals(expected, SearchParameters.build(query));
  }

  /**
   * As above. Note the value is trimmed.
   */
  @Test
  public void testWhitespaceValueForTag() {
    String query = "name: ";
    SearchParameters expected = new SearchParameters()
        .withUntaggedName("name:*");
    assertEquals(expected, SearchParameters.build(query));
  }

  @Test
  public void testEmptyValueForTagDoesNotPreventParsingFollowingTags() {
    String query = "name: author:mena";
    SearchParameters expected = new SearchParameters()
        .withUntaggedName("name:*")
        .withAuthor("mena");
    assertEquals(expected, SearchParameters.build(query));
  }

  @Test
  public void testEmptyValueForTagDoesNotPreventParsingPrecedingTags() {
    String query = "author:mena name: ";
    SearchParameters expected = new SearchParameters()
        .withUntaggedName("name:*")
        .withAuthor("mena");
    assertEquals(expected, SearchParameters.build(query));
  }

  /**
   * As seen in other tests and in {@link org.eclipse.vorto.repository.search.SearchTags#appendPostfixWildcard(String)},
   * both tagged and un-tagged name tokens have a wildcard appended automatically when parsing.
   * <br/> This test verifies cases where they should or shouldn't be added.
   */
  @Test
  public void testWildcardAppendingForNames() {
    String query = "name:mena name:?n? name:*na alex *evin erl?";
    SearchParameters expected = new SearchParameters()
        .withTaggedName("mena*")
        .withTaggedName("?n?")
        .withTaggedName("*na")
        .withUntaggedName("alex*")
        .withUntaggedName("*evin")
        .withUntaggedName("erl?");
    assertEquals(expected, SearchParameters.build(query));
  }

  /**
   * This tests that unicode alphabetic characters extending the ASCII range are correctly parsed
   * internally to {@link org.eclipse.vorto.repository.search.SearchTags}, and therefore end up
   * populating the {@link SearchParameters} POJO as required (with appended wildcards if
   * necessary).
   */
  @Test
  public void testExtendedCharacters() {
    String query = "name:ṂⱸꞤẴ ẶḶḜẌ ꝁꜫꝩỉṊ name:ʒɽɭ?";
    SearchParameters expected = new SearchParameters()
        .withTaggedName("ṂⱸꞤẴ*")
        .withUntaggedName("ẶḶḜẌ*")
        .withUntaggedName("ꝁꜫꝩỉṊ*")
        .withTaggedName("ʒɽɭ?");
    assertEquals(expected, SearchParameters.build(query));
  }

  /**
   * Boilerplate null-safety check et al. below.
   */
  @Test
  public void testNullQuery() {
    String query = null;
    SearchParameters expected = new SearchParameters();
    assertEquals(expected, SearchParameters.build(query));
  }

  @Test
  public void testEmptyQuery() {
    String query = "";
    SearchParameters expected = new SearchParameters();
    assertEquals(expected, SearchParameters.build(query));
  }

  @Test
  public void testWhitespaceQuery() {
    String query = " ";
    SearchParameters expected = new SearchParameters();
    assertEquals(expected, SearchParameters.build(query));
  }

  @Test
  public void testExtendedWhitespaceQuery() {
    String query = "\u0009\r\n";
    SearchParameters expected = new SearchParameters();
    assertEquals(expected, SearchParameters.build(query));
  }

  /**
   * These characters (and many more) are not trimmed out by Java natively and are presently
   * not supported. <br/>
   * The search will be performed with those characters as an un-tagged name query.<br/>
   * Hopefully, this is not particularly important to fix.
   */
  @Test
  public void testNastyWhitespaceQuery() {
    String query = "\u00A0\u0009\u0085";
    SearchParameters expected = new SearchParameters();
    assertNotEquals(expected, SearchParameters.build(query));
  }

}
