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
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests model namespace based tagged searches.<br/>
 *
 * @author mena-bosch
 */
public class NamespaceSearchSimpleTest {

  static SearchTestInfrastructure testInfrastructure;

  @BeforeClass
  public static void beforeClass() throws Exception {

    testInfrastructure = new SearchTestInfrastructure();

    // Switcher fb with namespace com.mycompany.fb
    testInfrastructure.importModel(testInfrastructure.FUNCTIONBLOCK_MODEL);
    // ColorLight im with namespace com.mycompany
    testInfrastructure.importModel(testInfrastructure.INFORMATION_MODEL);
    // ColorLight ios mapping with very different namespace org.eclipse.vorto.examples.type
    testInfrastructure.importModel(testInfrastructure.MAPPING_MODEL);
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testNoNamespace() {
    String query = "namespace:*potato*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(0, model.size());
  }

  @Test
  public void testAllNamespaces() {
    String query = "namespace:*.*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(3, model.size());
  }

  /**
   * Uses a combination of multi-character wildcards and dots to query for namespaces that have at
   * least two dots, i.e. 3+ levels. <br/>This will exclude the infomodel with {@literal
   * com.mycompany} from the resulting model.
   */
  @Test
  public void testNamespacesWithThreeOrMoreLevels() {
    String query = "namespace:*.*.*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(2, model.size());
    assertTrue(model.stream().map(SearchTestInfrastructure::getFileName)
        .noneMatch(testInfrastructure.INFORMATION_MODEL::equals));
  }

  @Test
  public void testPlainNamespace() {
    String query = "namespace:com.mycompany";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  @Test
  public void testPlainNamespaceCaseInsensitive() {
    String query = "namespace:COM.MYCOMPANY";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.INFORMATION_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * With the multi-character wildcard at the end of the query, this will match both models with
   * namespaces {@literal com.mycompany} and {@literal com.mycompany.fb}
   */
  @Test
  public void testTrailingMultiWildcardNamespaceCaseInsensitive() {
    String query = "namespace:cOm?mYcOmPaNy*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(2, model.size());
    assertTrue(model.stream().map(SearchTestInfrastructure::getFileName)
        .allMatch(f -> f.equals(testInfrastructure.INFORMATION_MODEL) || f
            .equals(testInfrastructure.FUNCTIONBLOCK_MODEL)));
  }

  @Test
  public void testLeadingMultiWildcardNamespaceCaseInsensitive() {
    String query = "namespace:*.fb";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(1, model.size());
    assertEquals(testInfrastructure.FUNCTIONBLOCK_MODEL, SearchTestInfrastructure.getFileName(model.get(0)));
  }

  /**
   * This query has a repeated {@literal namespace} tag for sufficient elements to include all
   * models imported. The main idea is to verify the {@code OR} relation between tag repetition
   * which is proven by having all models returned regardless of the major difference in namespaces
   * between infomodel and functionblock on one side, and mapping on the other.
   */
  @Test
  public void testMultipleTagsWithWildcardsAreORRelated() {
    String query = "namespace:*mycompany* namespace:*eclipse*";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(3, model.size());
  }

}
