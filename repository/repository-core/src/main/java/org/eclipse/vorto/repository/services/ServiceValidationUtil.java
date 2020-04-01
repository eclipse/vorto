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

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

/**
 * Utility class with boilerplate validation code for services.
 */
@Service
public class ServiceValidationUtil {

  /**
   * Simple wrapper for boilerplate null validation of given arguments.<br/>
   * Traverses collections and checks elements for null too if so configured.
   *
   * @param arguments
   * @throws IllegalArgumentException
   */
  public void validateNulls(boolean traverseCollections, Object... arguments)
      throws IllegalArgumentException {
    // args
    if (Stream.of(arguments).anyMatch(Objects::isNull)) {
      throw new IllegalArgumentException("At least one argument is null");
    }
    if (traverseCollections) {
      // non-null args that are collections
      Stream.of(arguments).filter(a -> a instanceof Collection).forEach(c -> {
        if (((Collection) c).stream().anyMatch(Objects::isNull)) {
          throw new IllegalArgumentException("At least one element is null");
        }
      });
    }
  }

  /**
   * Simple wrapper for boilerplate null validation of given arguments.<br/>
   * Traverses collections and checks elements for null too.
   *
   * @param arguments
   * @throws IllegalArgumentException
   * @see ServiceValidationUtil#validateNulls(boolean, Object...)
   */
  public void validateNulls(Object... arguments) throws IllegalArgumentException {
    validateNulls(true, arguments);
  }
}
