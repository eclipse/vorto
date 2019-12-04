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
    testInfrastructure.importModel("Color.type");
  }

  @AfterClass
  public static void afterClass() throws Exception {
    testInfrastructure.terminate();
  }

  @Test
  public void testSearchWithNull() {
    assertEquals(1, testInfrastructure.getSearchService().search(null).size());
  }

  @Test
  public void testSearchWithEmptyExpression() {
    assertEquals(1, testInfrastructure.getSearchService().search("").size());
  }

  @Test
  public void testSearchWithSpecialCharacter() {
    assertEquals(0, testInfrastructure.getSearchService().search("!$@").size());
  }
}
