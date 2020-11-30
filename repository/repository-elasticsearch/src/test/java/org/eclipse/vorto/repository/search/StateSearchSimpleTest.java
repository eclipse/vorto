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
import static org.junit.Assert.fail;

import java.util.List;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.workflow.ModelState;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests model state based tagged searches.<br/> Note that not all types are tested against all
 * possible case and wildcard scenarios here, to keep the number of tests reasonable.
 *
 * @author mena-bosch
 */
public class StateSearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;

  private static void updateState(ModelInfo model, String state) {
    testInfrastructure.getRepositoryFactory().getRepository(testInfrastructure.getDefaultUser())
        .updateState(model.getId(), state);
  }

  @BeforeClass
  public static void beforeClass() throws Exception {

    testInfrastructure = new SearchTestInfrastructure();

    // will apply state updates after importing models
    // draft
    testInfrastructure.importModel(testInfrastructure.FUNCTIONBLOCK_MODEL, testInfrastructure.getDefaultUser());
    // in review
    testInfrastructure.importModel(testInfrastructure.INFORMATION_MODEL, testInfrastructure.getDefaultUser());
    // released
    testInfrastructure.importModel(testInfrastructure.MAPPING_MODEL, testInfrastructure.getDefaultUser());
    // deprecated
    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL, testInfrastructure.getDefaultUser());

    List<ModelInfo> model = testInfrastructure.getRepositoryFactory().getRepository(testInfrastructure.getDefaultUser()).search("*");
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
    /*
    This is very ugly but necessary. The time it takes for the whole event-based cycle to occur when
    updating model states is smaller than the time between @AfterClass finalizes and tests start.
    There is no point in invoking reindex as Elasticsearch is already processing the reindex requests
    for each model - in fact it might very well fail.
    This is not necessary in the repository-core tests because the indexing service is mocked and
    does not depend on a third-party service like the Elasticsearch runtime spun by SearchTestInfrastructure.
     */
    try {
      Thread.sleep(1000);
    }
    // swallowing here on purpose
    catch (InterruptedException ie) {}
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testNoModel() {
    String query = "state:*potato?*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(0, model.size());
  }

  @Test
  public void testPlainDraftState() {
    String query = "state:Draft";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Draft.getName());
  }

  @Test
  public void testPlainDraftStateCaseInsensitive() {
    String query = "state:dRAFT";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Draft.getName());
  }

  @Test
  public void testDraftStateWithWildcards() {
    String query = "state:?R*t";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Draft.getName());
  }

  @Test
  public void testPlainInReviewState() {
    String query = "state:InReview";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.InReview.getName());
  }

  @Test
  public void testPlainInReviewStateCaseInsensitive() {
    String query = "state:iNrEVIEW";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.InReview.getName());
  }

  @Test
  public void testInReviewStateWithWildcards() {
    String query = "state:?Nr*i?w";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.InReview.getName());
  }

  @Test
  public void testPlainReleasedState() {
    String query = "state:Released";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Released.getName());
  }

  @Test
  public void testPlainReleasedStateCaseInsensitive() {
    String query = "state:rELEASED";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Released.getName());
  }

  @Test
  public void testReleasedStateWithWildcards() {
    String query = "state:*l?as*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Released.getName());
  }

  @Test
  public void testPlainDeprecatedState() {
    String query = "state:Deprecated";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Deprecated.getName());
  }

  @Test
  public void testPlainDeprecatedStateCaseInsensitive() {
    String query = "state:dEPRECATED";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Deprecated.getName());
  }

  @Test
  public void testDeprecatedStateWithWildcards() {
    String query = "state:*p?ECaT*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(model.get(0).getState(), ModelState.Deprecated.getName());
  }

  /**
   * Uses a wildcard expression broad enough to match three states.
   */
  @Test
  public void testThreeStatesWithBroadWildcard() {
    String query = "state:*RE*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(3, model.size());
  }

  /**
   * Some garbage query with repetitions of tags, wildcards all over, etc. Should match 3 states.
   */
  @Test
  public void testMultipleTagsRepeatedValuesSomeWildcards() {
    String query = "state:dr* state:*aft state:depre?ated state:?nr*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(3, model.size());
  }


}
