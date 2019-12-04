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
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.search.SearchParameters;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests model state based tagged searches.<br/> Note that not all types are tested against all
 * possible case and wildcard scenarios here, to keep the number of tests reasonable.<br/> Also,
 * some queries cannot be tested as full strings, because the order of appearance of {@literal
 * OR}-separated values in a {@literal CONTAINS} constraint, when different values containing at
 * least one wildcard are tested for the same tag, cannot be predicted.<br/> See {@link
 * SearchTestInfrastructure#assertContains(String, String...)} regarding that.
 *
 * @author mena-bosch
 */
public class StateSearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;

  private static void updateState(ModelInfo model, String state) {
    testInfrastructure.getRepositoryFactory().getRepository(testInfrastructure.createUserContext("reviewer"))
        .updateState(model.getId(), state);
  }

  @BeforeClass
  public static void beforeClass() throws Exception {

    testInfrastructure = new SearchTestInfrastructure();

    // will apply state updates after importing models
    // draft
    testInfrastructure.importModel(testInfrastructure.FUNCTIONBLOCK_MODEL);
    // in review
    testInfrastructure.importModel(testInfrastructure.INFORMATION_MODEL);
    // released
    testInfrastructure.importModel(testInfrastructure.MAPPING_MODEL);
    // deprecated
    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL);

    List<ModelInfo> model = testInfrastructure.getRepositoryFactory().getRepository(testInfrastructure.createUserContext("alex")).search("*");
    // this is arguably over-cautious, as the next statement would fail all tests anyway
    if (model.isEmpty()) {
      fail("Model is empty after importing.");
    }
    // state updates here
    model.forEach(
        mi -> {
          switch (mi.getType()) {
            case Functionblock: {
              updateState(mi, "Draft");
              break;
            }
            case InformationModel: {
              updateState(mi, "InReview");
              break;
            }
            case Mapping: {
              updateState(mi, "Released");
              break;
            }
            case Datatype: {
              updateState(mi, "Deprecated");
              break;
            }
          }
        }
    );
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testNoModel() {
    String query = "state:*potato?*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) LIKE '%potato_%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(0, model.size());
  }

  @Test
  public void testPlainDraftState() {
    String query = "state:Draft";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) = 'draft'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Draft.getName());
  }

  @Test
  public void testPlainDraftStateCaseInsensitive() {
    String query = "state:dRAFT";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) = 'draft'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Draft.getName());
  }

  @Test
  public void testDraftStateWithWildcards() {
    String query = "state:?R*t";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) LIKE '_r%t'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Draft.getName());
  }

  @Test
  public void testPlainInReviewState() {
    String query = "state:InReview";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) = 'inreview'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.InReview.getName());
  }

  @Test
  public void testPlainInReviewStateCaseInsensitive() {
    String query = "state:iNrEVIEW";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) = 'inreview'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.InReview.getName());
  }

  @Test
  public void testInReviewStateWithWildcards() {
    String query = "state:?Nr*i?w";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) LIKE '_nr%i_w'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.InReview.getName());
  }

  @Test
  public void testPlainReleasedState() {
    String query = "state:Released";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) = 'released'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Released.getName());
  }

  @Test
  public void testPlainReleasedStateCaseInsensitive() {
    String query = "state:rELEASED";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) = 'released'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Released.getName());
  }

  @Test
  public void testReleasedStateWithWildcards() {
    String query = "state:*l?as*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) LIKE '%l_as%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Released.getName());
  }

  @Test
  public void testPlainDeprecatedState() {
    String query = "state:Deprecated";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) = 'deprecated'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Deprecated.getName());
  }

  @Test
  public void testPlainDeprecatedStateCaseInsensitive() {
    String query = "state:dEPRECATED";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) = 'deprecated'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Deprecated.getName());
  }

  @Test
  public void testDeprecatedStateWithWildcards() {
    String query = "state:*p?ECaT*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) LIKE '%p_ecat%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Deprecated.getName());
  }

  /**
   * Uses a wildcard expression broad enough to match three states.
   */
  @Test
  public void testThreeStatesWithBroadWildcard() {
    String query = "state:*RE*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:state]) LIKE '%re%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(3, model.size());
  }

  /**
   * Some garbage query with repetitions of tags, wildcards all over, etc. Should match 3 states.
   */
  @Test
  public void testMultipleTagsRepeatedValuesSomeWildcards() {
    String query = "state:dr* state:*aft state:depre?ated state:?nr*";
    testInfrastructure.assertContains(
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)),
        "SELECT * FROM [nt:file] WHERE (CONTAINS ([vorto:state], '",
        "%aft", "depre_ated", "_nr%", "dr%"
    );
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(3, model.size());
  }


}
