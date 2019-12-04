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
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.utils.ModelSearchUtil;
import org.eclipse.vorto.repository.search.SearchParameters;
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
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:namespace]) LIKE '%potato%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(0, model.size());
  }

  @Test
  public void testAllNamespaces() {
    String query = "namespace:*.*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:namespace]) LIKE '%.%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
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
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:namespace]) LIKE '%.%.%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(2, model.size());
    assertTrue(model.stream().map(ModelInfo::getFileName).noneMatch(testInfrastructure.INFORMATION_MODEL::equals));
  }

  @Test
  public void testPlainNamespace() {
    String query = "namespace:com.mycompany";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:namespace]) = 'com.mycompany'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getFileName(), testInfrastructure.INFORMATION_MODEL);
  }

  @Test
  public void testPlainNamespaceCaseInsensitive() {
    String query = "namespace:COM.MYCOMPANY";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:namespace]) = 'com.mycompany'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getFileName(), testInfrastructure.INFORMATION_MODEL);
  }

  /**
   * With the multi-character wildcard at the end of the query, this will match both models with
   * namespaces {@literal com.mycompany} and {@literal com.mycompany.fb}
   */
  @Test
  public void testTrailingMultiWildcardNamespaceCaseInsensitive() {
    String query = "namespace:cOm?mYcOmPaNy*";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:namespace]) LIKE 'com_mycompany%'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(2, model.size());
    assertTrue(model.stream().map(ModelInfo::getFileName)
        .allMatch(f -> f.equals(testInfrastructure.INFORMATION_MODEL) || f.equals(testInfrastructure.FUNCTIONBLOCK_MODEL)));
  }

  @Test
  public void testLeadingMultiWildcardNamespaceCaseInsensitive() {
    String query = "namespace:*.fb";
    assertEquals("SELECT * FROM [nt:file] WHERE LOWER([vorto:namespace]) LIKE '%.fb'",
        ModelSearchUtil.toJCRQuery(SearchParameters.build(query)));
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(1, model.size());
    assertEquals(model.get(0).getFileName(), testInfrastructure.FUNCTIONBLOCK_MODEL);
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
    testInfrastructure.assertContains(ModelSearchUtil.toJCRQuery(SearchParameters.build(query)),
        "SELECT * FROM [nt:file] WHERE (CONTAINS ([vorto:namespace], '",
        "%mycompany%", "%eclipse%"
    );
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query);
    assertEquals(3, model.size());
  }

}
