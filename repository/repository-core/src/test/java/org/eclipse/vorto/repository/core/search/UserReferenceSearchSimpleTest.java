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
import static org.junit.Assert.fail;

import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.search.SearchParameters;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests user reference-based tagged searches, which search for both {@literal author} and {@literal
 * lastModifiedBy} fields under the hood. <br/>
 * Note that not all types are tested against all possible case and wildcard scenarios here, to
 * keep the number of tests reasonable.<br/>
 * Also, some queries cannot be tested as full strings, because the order of appearance of
 * {@literal OR}-separated values in a {@literal CONTAINS} constraint, when different values
 * containing at least one wildcard are tested for the same tag, cannot be predicted.<br/>
 * See {@link SearchTestInfrastructure#assertContains(String, String...)} regarding that.
 *
 * @author mena-bosch
 */
public class UserReferenceSearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;

  @BeforeClass
  public static void beforeClass() throws Exception {
    testInfrastructure = new SearchTestInfrastructure();
    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL);
    List<ModelInfo> model = testInfrastructure.getRepositoryFactory().getRepository(testInfrastructure.createUserContext("alex")).search("*");
    // this is arguably over-cautious, as the next statement would fail all tests anyway
    if (model.isEmpty()) {
      fail("Model is empty after importing.");
    }
    // "reviewer" user updates the only imported model's visibility to public, i.e.
    // "lastModifiedBy" -> reviewer
    ModelId updated = testInfrastructure.getRepositoryFactory().getRepository(testInfrastructure.createUserContext("reviewer"))
        .updateVisibility(model.get(0).getId(), "Public");

    // "control group": importing another model as another user
    testInfrastructure.importModel("HueLightStrips.infomodel", testInfrastructure.createUserContext("erle", "playground"));
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  /**
   * Tests that the extraneous model that is never referenced in other user reference searches is,
   * in fact, returned when the query is broad enough.
   */
  @Test
  public void testControlGroup() {
    String query = "userReference:alex userReference:reviewer userReference:*";
    testInfrastructure.assertContains(
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)),
        "SELECT * FROM [nt:file] WHERE ", "CONTAINS ([vorto:author], '", "alex", "%", "reviewer",
        "CONTAINS ([jcr:lastModifiedBy], '"
    );
    assertEquals(2, testInfrastructure.getSearchService().search(query).size());
  }

  @Test
  public void testSimpleUserReferenceByAuthor() {
    String query = "userReference:alex";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) = 'alex' OR LOWER([jcr:lastModifiedBy]) = 'alex')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testSimpleUserReferenceCaseInsensitiveByAuthor() {
    String query = "userReference:ALEX";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) = 'alex' OR LOWER([jcr:lastModifiedBy]) = 'alex')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  /**
   * Expected to return 0 model - user reference incomplete
   */
  @Test
  public void testSimpleUserReferenceIncompleteByAuthor() {
    String query = "userReference:ale";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) = 'ale' OR LOWER([jcr:lastModifiedBy]) = 'ale')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(0, testInfrastructure.getSearchService().search(query).size());
  }

  @Test
  public void testLeadingMultiWildcardUserReferenceByAuthor() {
    String query = "userReference:*lex";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE '%lex' OR LOWER([jcr:lastModifiedBy]) LIKE '%lex')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardUserReferenceByAuthor() {
    String query = "userReference:ale*";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'ale%' OR LOWER([jcr:lastModifiedBy]) LIKE 'ale%')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleMultiWildcardUserReferenceByAuthor() {
    String query = "userReference:a*ex";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'a%ex' OR LOWER([jcr:lastModifiedBy]) LIKE 'a%ex')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardUserReferenceCaseInsensitiveByAuthor() {
    String query = "userReference:ALE*";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'ale%' OR LOWER([jcr:lastModifiedBy]) LIKE 'ale%')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingSingleWildcardUserReferenceByAuthor() {
    String query = "userReference:?lex";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE '_lex' OR LOWER([jcr:lastModifiedBy]) LIKE '_lex')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingSingleWildcardUserReferenceByAuthor() {
    String query = "userReference:ale?";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'ale_' OR LOWER([jcr:lastModifiedBy]) LIKE 'ale_')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardUserReferenceByAuthor() {
    String query = "userReference:al?x";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'al_x' OR LOWER([jcr:lastModifiedBy]) LIKE 'al_x')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardUserReferenceCaseInsensitiveByAuthor() {
    String query = "userReference:A?EX";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'a_ex' OR LOWER([jcr:lastModifiedBy]) LIKE 'a_ex')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiLeadingWildcardUserReferenceByAuthor() {
    String query = "userReference:*l?x";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE '%l_x' OR LOWER([jcr:lastModifiedBy]) LIKE '%l_x')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiTrailingWildcardUserReferenceByAuthor() {
    String query = "userReference:?le*";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE '_le%' OR LOWER([jcr:lastModifiedBy]) LIKE '_le%')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testSimpleUserReferenceByLastModifier() {
    String query = "userReference:reviewer";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) = 'reviewer' OR LOWER([jcr:lastModifiedBy]) = 'reviewer')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testSimpleUserReferenceCaseInsensitiveByLastModifier() {
    String query = "userReference:REVIEWER";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) = 'reviewer' OR LOWER([jcr:lastModifiedBy]) = 'reviewer')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  /**
   * Expected to return 0 model - user reference incomplete
   */
  @Test
  public void testSimpleUserReferenceIncompleteByLastModifier() {
    String query = "userReference:review";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) = 'review' OR LOWER([jcr:lastModifiedBy]) = 'review')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    assertEquals(0, testInfrastructure.getSearchService().search(query).size());
  }

  @Test
  public void testLeadingMultiWildcardUserReferenceByLastModifier() {
    String query = "userReference:*viewer";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE '%viewer' OR LOWER([jcr:lastModifiedBy]) LIKE '%viewer')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardUserReferenceByLastModifier() {
    String query = "userReference:review*";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'review%' OR LOWER([jcr:lastModifiedBy]) LIKE 'review%')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleMultiWildcardUserReferenceByLastModifier() {
    String query = "userReference:r*iewer";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'r%iewer' OR LOWER([jcr:lastModifiedBy]) LIKE 'r%iewer')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingMultiWildcardUserReferenceCaseInsensitiveByLastModifier() {
    String query = "userReference:REVIEW*";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'review%' OR LOWER([jcr:lastModifiedBy]) LIKE 'review%')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testLeadingSingleWildcardUserReferenceByLastModifier() {
    String query = "userReference:?eviewer";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE '_eviewer' OR LOWER([jcr:lastModifiedBy]) LIKE '_eviewer')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testTrailingSingleWildcardUserReferenceByLastModifier() {
    String query = "userReference:reviewe?";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'reviewe_' OR LOWER([jcr:lastModifiedBy]) LIKE 'reviewe_')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardUserReferenceByLastModifier() {
    String query = "userReference:revie?er";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'revie_er' OR LOWER([jcr:lastModifiedBy]) LIKE 'revie_er')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMiddleSingleWildcardUserReferenceCaseInsensitiveByLastModifier() {
    String query = "userReference:R?VIEWER";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE 'r_viewer' OR LOWER([jcr:lastModifiedBy]) LIKE 'r_viewer')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiLeadingWildcardUserReferenceByLastModifier() {
    String query = "userReference:*v?ewer";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE '%v_ewer' OR LOWER([jcr:lastModifiedBy]) LIKE '%v_ewer')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

  @Test
  public void testMixedMultiTrailingWildcardUserReferenceByLastModifier() {
    String query = "userReference:?eview*";
    assertEquals(
        "SELECT * FROM [nt:file] WHERE (LOWER([vorto:author]) LIKE '_eview%' OR LOWER([jcr:lastModifiedBy]) LIKE '_eview%')",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, model.get(0).getFileName());
  }

}
