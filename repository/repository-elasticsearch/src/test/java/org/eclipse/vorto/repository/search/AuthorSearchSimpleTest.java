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

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
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
    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL, testInfrastructure.getDefaultUser());
    // "control group": importing another model as another user
    testInfrastructure.importModel("HueLightStrips.infomodel",
        testInfrastructure.createUserContext("erle", "playground"));
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
    assertEquals(2,
        testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser())
            .size());
  }

  /**
   * Note: all models seem to have {@literal alex} as author.
   */
  @Test
  public void testSimpleAuthor() {
    String query = "author:alex";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testSimpleAuthorCaseInsensitive() {
    String query = "author:ALEX";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * Expected to return 0 model - author incomplete
   */
  @Test
  public void testSimpleAuthorIncomplete() {
    String query = "author:ale";
    assertEquals(0, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testLeadingMultiWildcardAuthor() {
    String query = "author:*lex";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardAuthor() {
    String query = "author:ale*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleMultiWildcardAuthor() {
    String query = "author:a*ex";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardAuthorCaseInsensitive() {
    String query = "author:ALE*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingSingleWildcardAuthor() {
    String query = "author:?lex";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingSingleWildcardAuthor() {
    String query = "author:ale?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardAuthor() {
    String query = "author:al?x";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardAuthorCaseInsensitive() {
    String query = "author:A?EX";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiLeadingWildcardAuthor() {
    String query = "author:*l?x";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiTrailingWildcardAuthor() {
    String query = "author:?le*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }
}
