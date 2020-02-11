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
import static org.junit.Assert.fail;

import java.util.List;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.search.SearchParameters;
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
    testInfrastructure.importModel(testInfrastructure.DATATYPE_MODEL);

    // Switcher fb with version 1.0.0 - will update to 2.1.0
    testInfrastructure.importModel(testInfrastructure.FUNCTIONBLOCK_MODEL);

    List<ModelInfo> model = testInfrastructure.getRepositoryFactory()
        .getRepository(testInfrastructure.createUserContext("alex")).search("*");
    // this is arguably over-cautious, as the next statement would fail all tests anyway
    if (model.isEmpty()) {
      fail("Model is empty after importing.");
    }
    IUserContext alex = testInfrastructure.createUserContext("alex");
    IModelRepository repo = testInfrastructure.getRepositoryFactory().getRepository(alex);
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
    repo.createVersion(type.getId(), "1.0.1", alex);
    repo.removeModel(type.getId());
    // updating functionblock to 2.1.0 and removing previous
    repo.createVersion(functionBlock.getId(), "2.1.0", alex);
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
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:version]) LIKE '%potato%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(0, model.size());
  }

  @Test
  public void testNoModelWithValidVersion() {
    String query = "version:1.1.1";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:version]) = '1.1.1'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(0, model.size());
  }

  @Test
  public void testPlainVersion() {
    String query = "version:1.0.1";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:version]) = '1.0.1'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
  }

  @Test
  public void testLeadingMultiWildcardVersion() {
    String query = "version:*.0.1";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:version]) LIKE '%.0.1'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
  }

  /**
   * This will fetch both v. 1.0.1 and v. 1.0.0 models
   */
  @Test
  public void testTrailingMultiWildcardVersion() {
    String query = "version:1.0.*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:version]) LIKE '1.0.%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(2, model.size());
  }

  /**
   * This will fetch both models whose version ends in 0
   */
  @Test
  public void testBroaderMultiWildcardVersion() {
    String query = "version:*0";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:version]) LIKE '%0'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(2, model.size());
  }

  @Test
  public void testTrailingSingleWildcardsVersion() {
    String query = "version:2.?.*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:version]) LIKE '2._.%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getFileName(), testInfrastructure.FUNCTIONBLOCK_MODEL);
  }

  /**
   * This will fetch both models whose version starts with 1.0
   */
  @Test
  public void testSingleWildcardVersion() {
    String query = "version:1.0.?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:version]) LIKE '1.0._'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(2, model.size());
  }

  /**
   * Wildcard is broad enough to fetch all model
   */
  @Test
  public void testBroadWildcardsVersion() {
    String query = "version:*.?.?";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:version]) LIKE '%._._'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(3, model.size());
  }

  /**
   * This (silly) query is a catch-all. The multi-wildcard implies 0+ occurrences of any character,
   * meaning *0* also fetches models whose version ends in 0, etc.
   */
  @Test
  public void testWildcardsAndMultipleTagsVersions() {
    String query = "version:2* version:*0* version:*1*";
    testInfrastructure.assertContains(ModelSearchUtil.toJCRQuery(SearchParameters.build(query)),
        "SELECT * FROM [nt:file] WHERE (CONTAINS ([vorto:version], '",
        "2%", "%0%", "%1%"
    );
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(3, model.size());
  }
}
