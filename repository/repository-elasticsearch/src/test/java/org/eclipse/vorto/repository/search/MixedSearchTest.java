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
package org.eclipse.vorto.repository.search;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
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
    testInfrastructure.importModel("Color.type", testInfrastructure.getDefaultUser());
    testInfrastructure.importModel("Colorlight.fbmodel", testInfrastructure.getDefaultUser());
    testInfrastructure.importModel("Switcher.fbmodel", testInfrastructure.getDefaultUser());
    testInfrastructure.importModel("HueLightStrips.infomodel", testInfrastructure.getDefaultUser());
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testMultipleTermsNotRepeated() {
    String query = "version:1.0* color";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(2, model.size());
  }

  /**
   * The two name expressions catch all names starting with c and al names ending with r, which
   * returns <b>C</b>olor, <b>C</b>olorLight but also Switche<b>r</b>.<br/> The version is the same
   * for all.<br/> The type is repeated and includes both types of the above models: datatype and
   * functionblock.<br/> Finally some repetition over patterns for author and userReference include
   * the default author / lastModifiedBy, i.e. {@literal alex}, alongside some negative noise
   * ({@literal nobody)}.<br/> Bottomline, this is supposed to return 3 models: {@literal
   * Color.type}, {@literal Colorlight.fbmodel} and {@literal Switcher.fbmodel}, but <b>not</b>
   * {@literal HueLightStrips.infomodel}.<br/> The reason why the latter is not included in the
   * result, despite the all-inclusive {@literal userReference:*}, is because <i>different</i> tags
   * are {@code AND}-related (while conversely, same, repeated tags are {@code OR}-related).<br/>
   * Worth reminding, {@literal userReference} aggregates {@literal author} and {@literal
   * lastModifiedBy} in an {@code OR} condition - yet in this case, the explicit {@literal author}
   * tag is handled separately.
   */
  @Test
  public void testMultipleTermsSomeRepeated() {
    String query = "version:1.0* c* *r type:data* type:function* author:alex author:?LE? author:NOBODY userReference:* userReference:nobody";
    List<ModelInfo> model = testInfrastructure.getSearchService().search(query, testInfrastructure.getDefaultUser());
    assertEquals(3, model.size());
  }

  @Test
  public void testSearchAllModelsWithWildCard() {
    assertEquals(4, testInfrastructure.getSearchService().search("*", testInfrastructure.getDefaultUser()).size());
    // references not fetched contextually to ES
  }

  @Test
  public void testSearchByFreetextLeadingCaseInsensitive() {
    assertEquals(2,
        testInfrastructure.getSearchService().search("color", testInfrastructure.getDefaultUser()).size());
  }

  /**
   * This test uses free text to search for a model name but provides no wildcard. <br/> As such,
   * while a trailing multi-wildcard is automatically added, no leading wildcard is. <br/> Given the
   * term does not appear at the start of either model containing it, no model is fetched.
   */
  @Test
  public void testSearchByFreetextTrailingCaseInsensitive() {
    assertEquals(0,
        testInfrastructure.getSearchService().search("light", testInfrastructure.getDefaultUser()).size());
  }

  /*
   * Surrounding search term in wildcards so the one model name containing the sequence is
   * returned.
   */
  @Test
  public void testSearchByFreetextMiddleWildcardsSurrounding() {
    assertEquals(1, testInfrastructure.getSearchService().search("*tch*", testInfrastructure.getDefaultUser()).size());
  }

  /*
   * Wildcard trailing but no model name starting with sequence and wildcard leading is <b>not</b>
   * implied by search.
   */
  @Test
  public void testSearchByFreetextTrailingWildCard() {
    assertEquals(0, testInfrastructure.getSearchService().search("tch*", testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testSearchModelByModelName() {
    assertEquals(2, testInfrastructure.getSearchService().search("name:Color", testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testSearchModelByNameWildcard() {
    assertEquals(2, testInfrastructure.getSearchService().search("name:Color*", testInfrastructure.getDefaultUser()).size());
  }
}
