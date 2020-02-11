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
 * Tests name-based searches, with or without tags.<br/> The unit tests here are trivial, and follow
 * a schema common to all search fields. <br/>
 *
 * @author mena-bosch
 */
public class NameSearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;

  @BeforeClass
  public static void beforeClass() throws Exception {
    testInfrastructure = new SearchTestInfrastructure();
    testInfrastructure.importModel("Color.type");
    // "control group": extraneous model that should never appear in search
    testInfrastructure.importModel("HueLightStrips.infomodel");
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  /**
   * Tests that the extraneous model that is never referenced in other name searches is, in fact,
   * returned when the query is broad enough.
   */
  @Test
  public void testControlGroup() {
    String query = "name:Color name:*";
    assertEquals(2, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testSimpleName() {
    String query = "Color";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testSimpleNameCaseInsensitive() {
    String query = "color";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * Expected to return the relevant model anyway, as name terms without wildcards are appended a
   * multi-character wildcard automatically at back-end level.
   */
  @Test
  public void testSimpleNameIncompleteTrailing() {
    String query = "col";
    assertEquals(1, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  /**
   * Expected to return 0 model - name incomplete. Note that a wildcard is applied automatically at
   * the end (for backwards-compatibility), but not at the beginning, and for names only.
   */
  @Test
  public void testSimpleNameIncompleteLeading() {
    String query = "olor";
    assertEquals(0, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testLeadingMultiWildcardName() {
    String query = "*olor";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardName() {
    String query = "Colo*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleMultiWildcardName() {
    String query = "Co*or";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardNameCaseInsensitive() {
    String query = "colo*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingSingleWildcardName() {
    String query = "?olor";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingSingleWildcardName() {
    String query = "Colo?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardName() {
    String query = "Co?or";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardNameCaseInsensitive() {
    String query = "co?or";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiLeadingWildcardName() {
    String query = "*lo?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiTrailingWildcardName() {
    String query = "?olo*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testSimpleNameTagged() {
    String query = "name:Color";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testSimpleNameCaseInsensitiveTagged() {
    String query = "name:color";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testSimpleNameIncompleteTagged() {
    String query = "name:Colo";
    assertEquals(1, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testLeadingMultiWildcardNameTagged() {
    String query = "name:*olor";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardNameTagged() {
    String query = "name:Colo*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleMultiWildcardNameTagged() {
    String query = "name:Co*or";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardNameCaseInsensitiveTagged() {
    String query = "name:colo*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingSingleWildcardNameTagged() {
    String query = "name:?olor";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingSingleWildcardNameTagged() {
    String query = "name:Colo?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardNameTagged() {
    String query = "name:Co?or";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardNameCaseInsensitiveTagged() {
    String query = "name:co?or";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiLeadingWildcardNameTagged() {
    String query = "name:*lo?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiTrailingWildcardNameTagged() {
    String query = "name:?olo*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }
}
