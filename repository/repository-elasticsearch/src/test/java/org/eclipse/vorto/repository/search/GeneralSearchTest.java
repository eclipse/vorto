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

import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Ported from original ModelRepositorySearchTest
 */
public class GeneralSearchTest {

  static SearchTestInfrastructure testInfrastructure;

  @BeforeClass
  public static void beforeClass() throws Exception {
    testInfrastructure = new SearchTestInfrastructure();
    ModelInfo datatype = testInfrastructure.importModel("Color.type", testInfrastructure.getDefaultUser());
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testSearchWithNull() {
    assertEquals(1, testInfrastructure.getSearchService().search(null, testInfrastructure.getDefaultUser()).size());
  }

  @Test
  public void testSearchWithEmptyExpression() {
    assertEquals(1, testInfrastructure.getSearchService().search("", testInfrastructure.getDefaultUser()).size());
  }

  /**
   * Note: originally  in the JRC tests, the garbage query string was: {@literal !$@}. <br/>
   * That was correctly not returning any model with JCR queries.<br/>
   * However since this is an un-tagged query, it will be interpreted as an ElasticSearch
   * query string. <br/>
   * The edge case here is the {@literal !} starting character, which is a reserved character.<br/>
   * While the documentation is not entirely clear on the matter, I suspect this may negate the
   * following characters, which in turn would return any model whose name does not contain
   * {@literal $} or {@literal @}, i.e. in our case (and likely all cases), any model.
   */
  @Test
  public void testSearchWithSpecialCharacter() {
    assertEquals(0, testInfrastructure.getSearchService().search("$@!", testInfrastructure.getDefaultUser()).size());
  }
}
