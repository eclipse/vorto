/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 * <p>
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 * <p>
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 * <p>
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core.search;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.search.SearchParameters;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests name-based searches, with or without tags.<br/> The unit tests here are trivial, and follow
 * a schema common to all search fields. <br/>
 *
 * @author mena-bosch
 */
public class NameSearchSimpleTest extends ParentSearchTest {

  @Before
  public void before() {
    importModel("Color.type");
    // "control group": extraneous model that should never appear in search
    importModel("HueLightStrips.infomodel");
  }

  /**
   * Tests that the extraneous model that is never referenced in other name searches is, in fact,
   * returned when the query is broad enough.
   */
  @Test
  public void testControlGroup() {
    String query = "name:Color name:*";
    // TODO use assertContains here
    assertEquals("SELECT * FROM [nt:file] WHERE (CONTAINS ([vorto:name], 'color% OR %'))",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(2, searchService.search(query).size());
  }

  @Test
  public void testSimpleName() {
    String query = "Color";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'color%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testSimpleNameCaseInsensitive() {
    String query = "color";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'color%'",
        ModelSearchUtil.toJCRQuery((SearchParameters.build(query))));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  /**
   * Expected to return the relevant model anyway, as name terms without wildcards are appended a
   * multi-character wildcard automatically at back-end level.
   */
  @Test
  public void testSimpleNameIncompleteTrailing() {
    String query = "col";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'col%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(1, searchService.search(query).size());
  }

  /**
   * Expected to return 0 model - name incomplete. Note that a wildcard is applied automatically at
   * the end (for backwards-compatibility), but not at the beginning, and for names only.
   */
  @Test
  public void testSimpleNameIncompleteLeading() {
    String query = "olor";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'olor%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(0, searchService.search(query).size());
  }

  @Test
  public void testLeadingMultiWildcardName() {
    String query = "*olor";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE '%olor'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardName() {
    String query = "Colo*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'colo%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleMultiWildcardName() {
    String query = "Co*or";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'co%or'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardNameCaseInsensitive() {
    String query = "colo*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'colo%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingSingleWildcardName() {
    String query = "?olor";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE '_olor'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingSingleWildcardName() {
    String query = "Colo?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'colo_'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardName() {
    String query = "Co?or";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'co_or'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardNameCaseInsensitive() {
    String query = "co?or";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'co_or'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiLeadingWildcardName() {
    String query = "*lo?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE '%lo_'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiTrailingWildcardName() {
    String query = "?olo*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE '_olo%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testSimpleNameTagged() {
    String query = "name:Color";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'color%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testSimpleNameCaseInsensitiveTagged() {
    String query = "name:color";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'color%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testSimpleNameIncompleteTagged() {
    String query = "name:Colo";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'colo%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(1, searchService.search(query).size());
  }

  @Test
  public void testLeadingMultiWildcardNameTagged() {
    String query = "name:*olor";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE '%olor'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardNameTagged() {
    String query = "name:Colo*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'colo%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleMultiWildcardNameTagged() {
    String query = "name:Co*or";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'co%or'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardNameCaseInsensitiveTagged() {
    String query = "name:colo*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'colo%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingSingleWildcardNameTagged() {
    String query = "name:?olor";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE '_olor'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingSingleWildcardNameTagged() {
    String query = "name:Colo?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'colo_'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardNameTagged() {
    String query = "name:Co?or";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'co_or'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardNameCaseInsensitiveTagged() {
    String query = "name:co?or";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE 'co_or'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiLeadingWildcardNameTagged() {
    String query = "name:*lo?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE '%lo_'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiTrailingWildcardNameTagged() {
    String query = "name:?olo*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:name]) LIKE '_olo%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = searchService.search(query);
    assertEquals(1, model.size());
    assertEquals(DATATYPE_MODEL, model.get(0).getFileName());
  }
}
