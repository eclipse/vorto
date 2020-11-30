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
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests model version based tagged searches.<br/>
 *
 * @author mena-bosch
 */
public class VersionSearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;

  @BeforeClass
  public static void beforeClass() throws Exception {

    testInfrastructure = new SearchTestInfrastructure();

    // Color type with version 1.0.0 - will update to 1.0.1
    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL, testInfrastructure.getDefaultUser());

    // Switcher fb with version 1.0.0 - will update to 2.1.0
    testInfrastructure.importModel(testInfrastructure.FUNCTIONBLOCK_MODEL, testInfrastructure.getDefaultUser());

    List<ModelInfo> model = testInfrastructure.getRepositoryFactory()
        .getRepository(testInfrastructure.getDefaultUser()).search("*");
    // this is arguably over-cautious, as the next statement would fail all tests anyway
    if (model.isEmpty()) {
      fail("Model is empty after importing.");
    }
    IModelRepository repo = testInfrastructure.getRepositoryFactory().getRepository(testInfrastructure.getDefaultUser());
    ModelInfo type = model.stream()
        .filter(m -> m.getFileName().equals(testInfrastructure.DATATYPE_MODEL)).findFirst()
        .orElseGet(() -> {
          fail("Model not found");
          return null;
        });
    ModelInfo functionBlock = model.stream()
        .filter(m -> m.getFileName().equals(testInfrastructure.FUNCTIONBLOCK_MODEL)).findFirst()
        .orElseGet(() -> {
          fail("Model not found");
          return null;
        });
    // updating infomodel to 1.0.1 and removing previous
    repo.createVersion(type.getId(), "1.0.1", testInfrastructure.getDefaultUser());
    repo.removeModel(type.getId());
    // updating functionblock to 2.1.0 and removing previous
    repo.createVersion(functionBlock.getId(), "2.1.0", testInfrastructure.getDefaultUser());
    repo.removeModel(functionBlock.getId());
    // finally, importing mapping as-is (1.0.0)
    testInfrastructure.importModel(testInfrastructure.MAPPING_MODEL);
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testNoModel() {
    String query = "version:*potato*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(0, model.size());
  }

  @Test
  public void testNoModelWithValidVersion() {
    String query = "version:1.1.1";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(0, model.size());
  }

  @Test
  public void testPlainVersion() {
    String query = "version:1.0.1";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
  }

  @Test
  public void testLeadingMultiWildcardVersion() {
    String query = "version:*.0.1";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
  }

  /**
   * This will fetch both v. 1.0.1 and v. 1.0.0 models
   */
  @Test
  public void testTrailingMultiWildcardVersion() {
    String query = "version:1.0.*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(2, model.size());
  }

  /**
   * This will fetch both models whose version ends in 0
   */
  @Test
  public void testBroaderMultiWildcardVersion() {
    String query = "version:*0";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(2, model.size());
  }

  @Test
  public void testTrailingSingleWildcardsVersion() {
    String query = "version:2.?.*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.FUNCTIONBLOCK_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * This will fetch both models whose version starts with 1.0
   */
  @Test
  public void testSingleWildcardVersion() {
    String query = "version:1.0.?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(2, model.size());
  }

  /**
   * Wildcard is broad enough to fetch all model
   */
  @Test
  public void testBroadWildcardsVersion() {
    String query = "version:*.?.?";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(3, model.size());
  }

  /**
   * This (silly) query is a catch-all. The multi-wildcard implies 0+ occurrences of any character,
   * meaning *0* also fetches models whose version ends in 0, etc.
   */
  @Test
  public void testWildcardsAndMultipleTagsVersions() {
    String query = "version:2* version:*0* version:*1*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(3, model.size());
  }
}
