/**
 * Copyright (c) 2019 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * Basic enum utilities.
 * @author mena-bosch
 */
public interface EnumUtil {

  /**
   * Searches the given enum type for an element named after the given value, through the
   * {@link String#equalsIgnoreCase(String)} comparison. <br/>
   * @param clazz
   * @param value
   * @param <E>
   * @return {@literal true} if matching element found, {@literal false} otherwise.
   */
  static <E extends Enum<E>> boolean isAnyValueOfCaseInsensitive(Class<E> clazz, String value) {
    return Objects.nonNull(value) && Arrays.stream(clazz.getEnumConstants()).anyMatch(v -> v.name().equalsIgnoreCase(value));
  }

  /**
   * Searches the given enum type for an element named after the given value, through the
   * {@link String#equalsIgnoreCase(String)} comparison. <br/>
   * If no matching element found, returns the given value.<br/>
   * If the given value is {@literal null}, returns {@literal null}.
   * @param clazz
   * @param value
   * @param <E>
   * @return
   */
  static <E extends Enum<E>> String forNameIgnoreCase(Class<E> clazz, String value) {
    if (Objects.nonNull(value)) {
      return Arrays.stream(clazz.getEnumConstants()).filter(v -> v.name().equalsIgnoreCase(value))
          .findAny().map(Enum::name).orElse(value);
    }
    return null;
  }

}
