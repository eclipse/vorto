/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.mapping.engine.converter.string;

import org.apache.commons.lang3.StringUtils;

public class String2Utils extends StringUtils {

  public static String concat(String value, String value2) {
    return value + value2;
  }

  public static String concat(String value, String value2, String value3) {
    return value + value2 + value3;
  }

  public static String concat(String value, String value2, String value3, String value4) {
    return value + value2 + value3 + value4;
  }
}
