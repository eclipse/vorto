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

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.eclipse.vorto.repository.AbstractIntegrationTest;

/**
 * Parent class for all search-testing classes in package, extending {@link AbstractIntegrationTest}
 * and containing common constants and functionality.
 */
public abstract class ParentSearchTest extends AbstractIntegrationTest {
  /**
   * Used in child classes to test a generated query against an arbitrary number of fragments. <br/>
   * This is because parts of the generated queries are done so by iterating {@link HashSet}s, so
   * the order of appearance of {@literal OR}-separated elements cannot be inferred consistently. <br/>
   * As there is no functional need to use linked or sorted sets for those queries, this workaround
   * is used when testing them. <br/>
   * Obviously, this makes for a much weaker (and more convoluted) test of the generated query -
   * luckily it only occurs when the query contains multiple {@literal OR}-separated terms with
   * wildcards, contextually to a FTS search (think: {@literal CONTAINS([vorto:someField], '%someValue OR someOtherValue%')}).
   * The latter implies multiple identical tags, at least one of which tags a value with a wildcard.<br/>
   * On the other hand, non-repeated tags will always be generated in a specific order defined
   * arbitrarily in the business logic of the simple search service instead, hence the query string
   * can be tested as-is. <br/>
   *
   * There is no validation of any of the parameters here.
   * @param text
   * @param fragments
   */
  protected static void assertContains(String text, String... fragments) {
    assertTrue(Arrays.stream(fragments).allMatch(text::contains));
  }

  /**
   * File names common for all simple search test classes.
   */
  protected static final String DATATYPE_MODEL = "Color.type";
  protected static final String FUNCTIONBLOCK_MODEL = "Switcher.fbmodel";
  protected static final String INFORMATION_MODEL = "ColorLightIM.infomodel";
  protected static final String MAPPING_MODEL = "Color_ios.mapping";
}
