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
package org.eclipse.vorto.repository.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests user reference-based tagged searches, which search for both {@literal author} and {@literal
 * lastModifiedBy} fields under the hood. <br/> Note that not all types are tested against all
 * possible case and wildcard scenarios here, to keep the number of tests reasonable.
 *
 * @author mena-bosch
 */
public class UserReferenceSearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;
  static IUserContext erleContext;

  @BeforeClass
  public static void beforeClass() throws Exception {
    testInfrastructure = new SearchTestInfrastructure();
    erleContext = testInfrastructure.createUserContext("erle");

    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL);
    List<ModelInfo> model = testInfrastructure.getRepositoryFactory()
        .getRepository(testInfrastructure.getDefaultUser()).search("*");
    // this is arguably over-cautious, as the next statement would fail all tests anyway
    if (model.isEmpty()) {
      fail("Model is empty after importing.");
    }
    // "reviewer" user updates the only imported model's visibility to public, i.e.
    // "lastModifiedBy" -> reviewer
    model.get(0).setLastModifiedBy("reviewer");
    ModelId updated = testInfrastructure.getRepositoryFactory().getRepository(testInfrastructure.createUserContext("reviewer"))
        .updateVisibility(model.get(0).getId(), "Public");

    // "control group": importing another model as another user
    testInfrastructure.importModel("HueLightStrips.infomodel", erleContext);
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
    assertEquals(2, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testSimpleUserReferenceByAuthor() {
    String query = "userReference:alex";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testSimpleUserReferenceCaseInsensitiveByAuthor() {
    String query = "userReference:ALEX";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * Expected to return 0 model - user reference incomplete
   */
  @Test
  public void testSimpleUserReferenceIncompleteByAuthor() {
    String query = "userReference:ale";
    assertEquals(0, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testLeadingMultiWildcardUserReferenceByAuthor() {
    String query = "userReference:*lex";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardUserReferenceByAuthor() {
    String query = "userReference:ale*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleMultiWildcardUserReferenceByAuthor() {
    String query = "userReference:a*ex";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardUserReferenceCaseInsensitiveByAuthor() {
    String query = "userReference:ALE*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingSingleWildcardUserReferenceByAuthor() {
    String query = "userReference:?lex";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingSingleWildcardUserReferenceByAuthor() {
    String query = "userReference:ale?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardUserReferenceByAuthor() {
    String query = "userReference:al?x";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardUserReferenceCaseInsensitiveByAuthor() {
    String query = "userReference:A?EX";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiLeadingWildcardUserReferenceByAuthor() {
    String query = "userReference:*l?x";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiTrailingWildcardUserReferenceByAuthor() {
    String query = "userReference:?le*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testSimpleUserReferenceByLastModifier() {
    String query = "userReference:reviewer";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testSimpleUserReferenceCaseInsensitiveByLastModifier() {
    String query = "userReference:REVIEWER";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * Expected to return 0 model - user reference incomplete
   */
  @Test
  public void testSimpleUserReferenceIncompleteByLastModifier() {
    String query = "userReference:review";
    assertEquals(0, testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testLeadingMultiWildcardUserReferenceByLastModifier() {
    String query = "userReference:*viewer";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardUserReferenceByLastModifier() {
    String query = "userReference:review*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleMultiWildcardUserReferenceByLastModifier() {
    String query = "userReference:r*iewer";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingMultiWildcardUserReferenceCaseInsensitiveByLastModifier() {
    String query = "userReference:REVIEW*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testLeadingSingleWildcardUserReferenceByLastModifier() {
    String query = "userReference:?eviewer";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testTrailingSingleWildcardUserReferenceByLastModifier() {
    String query = "userReference:reviewe?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardUserReferenceByLastModifier() {
    String query = "userReference:revie?er";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMiddleSingleWildcardUserReferenceCaseInsensitiveByLastModifier() {
    String query = "userReference:R?VIEWER";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiLeadingWildcardUserReferenceByLastModifier() {
    String query = "userReference:*v?ewer";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testMixedMultiTrailingWildcardUserReferenceByLastModifier() {
    String query = "userReference:?eview*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.DATATYPE_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

}
