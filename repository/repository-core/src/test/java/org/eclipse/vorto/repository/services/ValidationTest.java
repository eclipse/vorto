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
package org.eclipse.vorto.repository.services;

import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import org.assertj.core.util.Strings;
import org.eclipse.vorto.repository.services.exceptions.NameSyntaxException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * This class verifies common value validation cases in the back-end, and can serve against
 * validation regressions.
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidationTest {

  static final Predicate<String> COMMENT_FILTER = s -> !Strings.isNullOrEmpty(s) && !s
      .startsWith("#");

  @Test
  public void validNamespaces() throws Exception {
    List<String> validNamespaces = Files
        .readAllLines(Paths.get("src/test/resources/namespaces/valid_namespaces.txt"));
    validNamespaces.stream().filter(COMMENT_FILTER).forEach(
        n -> {
          try {
            ServiceValidationUtil.validateNamespaceName(n);
          } catch (NameSyntaxException nse) {
            fail(nse.getMessage());
          }
        }
    );
  }

  @Test
  public void invalidNamespaces() throws Exception {
    List<String> invalidNamespaces = Files
        .readAllLines(Paths.get("src/test/resources/namespaces/invalid_namespaces.txt"));
    invalidNamespaces.stream().filter(COMMENT_FILTER).forEach(
        n -> {
          try {
            ServiceValidationUtil.validateNamespaceName(n);
            fail(String.format("Namespace [%s] should be invalid. ", n));
          } catch (NameSyntaxException nse) {
            // nope
          }
        }
    );
  }
}
