/**
 * Copyright (c) 2018, 2019 Contributors to the Eclipse Foundation
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

import org.eclipse.vorto.repository.core.ModelInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Ported from original ModelRepositorySearchTest. <br/> This includes tests ranging across
 * different tags / filters.
 */
public class MixedSearchTest {

  static SearchTestInfrastructure testInfrastructure;

  @BeforeClass
  public static void beforeClass() throws Exception {
    testInfrastructure = new SearchTestInfrastructure();
    testInfrastructure.importModel("Color.type");
    testInfrastructure.importModel("Colorlight.fbmodel");
    testInfrastructure.importModel("Switcher.fbmodel");
    testInfrastructure.importModel("HueLightStrips.infomodel");
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testSearchAllModelsWithWildCard() {
    assertEquals(4, testInfrastructure.getSearchService().search("*").size());

    ModelInfo model = testInfrastructure.getSearchService().search("*").stream()
        .filter(m -> m.getId().getName().equals("ColorLight")).findAny().get();
    assertEquals(1, model.getReferences().size());
  }

  @Test
  public void testSearchByFreetextLeadingCaseInsensitive() {
    assertEquals(2,
        testInfrastructure.getSearchService().search("color").size());
  }

  /**
   * This test uses free text to search for a model name but provides no wildcard. <br/> As such,
   * while a trailing multi-wildcard is automatically added, no leading wildcard is. <br/> Given the
   * term does not appear at the start of either model containing it, no model is fetched.
   */
  @Test
  public void testSearchByFreetextTrailingCaseInsensitive() {
    assertEquals(0,
        testInfrastructure.getSearchService().search("light").size());
  }

  /*
   * Surrounding search term in wildcards so the one model name containing the sequence is
   * returned.
   */
  @Test
  public void testSearchByFreetextMiddleWildcardsSurrounding() {
    assertEquals(1, testInfrastructure.getSearchService().search("*tch*").size());
  }

  /*
   * Wildcard trailing but no model name starting with sequence and wildcard leading is <b>not</b>
   * implied by search.
   */
  @Test
  public void testSearchByFreetextTrailingWildCard() {
    assertEquals(0, testInfrastructure.getSearchService().search("tch*").size());
  }

  @Test
  public void testSearchModelByModelName() {
    assertEquals(2, testInfrastructure.getSearchService().search("name:Color").size());
  }

  @Test
  public void testSearchModelByNameWildcard() {
    assertEquals(2, testInfrastructure.getSearchService().search("name:Color*").size());
  }
}
