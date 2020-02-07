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

import java.util.List;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.search.SearchParameters;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests author-based tagged searches.<br/>
 *
 * @author mena-bosch
 */
public class AuthorSearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;

  @BeforeClass
  public static void beforeClass() throws Exception {
    testInfrastructure = new SearchTestInfrastructure();
    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL);
    // "control group": importing another model as another user
    testInfrastructure.importModel("HueLightStrips.infomodel", testInfrastructure.createUserContext("erle", "playground"));
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  /**
   * Tests that the extraneous model that is never referenced in other author searches is, in fact,
   * returned when the query is broad enough.
   */
  @Test
  public void testControlGroup() {
    String query = "author:alex author:*";
    assertEquals("SELECT * FROM [nt:file] WHERE (CONTAINS ([vorto:author], 'alex OR %'))",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(2, testInfrastructure.getSearchService().search(query).size());
  }

  /**
   * Note: all models seem to have {@literal alex} as author.
   */
  @Test
  public void testSimpleAuthor() {
    String query = "author:alex";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) = 'alex'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testSimpleAuthorCaseInsensitive() {
    String query = "author:ALEX";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) = 'alex'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  /**
   * Expected to return 0 model - author incomplete
   */
  @Test
  public void testSimpleAuthorIncomplete() {
    String query = "author:ale";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) = 'ale'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(0, testInfrastructure.getSearchService().search(query).size());
  }

  @Test
  public void testLeadingMultiWildcardAuthor() {
    String query = "author:*lex";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE '%lex'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardAuthor() {
    String query = "author:ale*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE 'ale%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleMultiWildcardAuthor() {
    String query = "author:a*ex";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE 'a%ex'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardAuthorCaseInsensitive() {
    String query = "author:ALE*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE 'ale%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingSingleWildcardAuthor() {
    String query = "author:?lex";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE '_lex'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingSingleWildcardAuthor() {
    String query = "author:ale?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE 'ale_'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardAuthor() {
    String query = "author:al?x";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE 'al_x'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardAuthorCaseInsensitive() {
    String query = "author:A?EX";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE 'a_ex'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiLeadingWildcardAuthor() {
    String query = "author:*l?x";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE '%l_x'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiTrailingWildcardAuthor() {
    String query = "author:?le*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:author]) LIKE '_le%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }
}
