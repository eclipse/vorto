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
package org.eclipse.vorto.plugin.generator;

import org.apache.commons.lang3.StringUtils;

public final class TestUtils {  
  
  /**
   * The normalizeEOL method replaces all end-of-line characters to \n in order to compare files consistently across platforms.
   * @param text
   * @return text with all line endings changed to \n
   */
  public static String normalizeEOL(String text) {
    return StringUtils
        .deleteWhitespace(StringUtils.normalizeSpace(text.replaceAll("\\\\r\\\\n", "\\\\n")));
  }
}
