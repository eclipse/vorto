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

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.mapping.engine.functions.ClassFunction;
import org.eclipse.vorto.mapping.engine.functions.IFunction;

public class StringFunctionFactory {

  @Deprecated
  private static final IFunction FUNC_STRING = new ClassFunction("string", StringUtils.class);
  @Deprecated
  private static final IFunction FUNC_STRING2 = new ClassFunction("string2", String2Utils.class);

  private static final IFunction VORTO_FUNC_STRING =
      new ClassFunction("vorto_string", StringUtils.class);

  public static IFunction[] createFunctions() {
    return Arrays.asList(FUNC_STRING, FUNC_STRING2, VORTO_FUNC_STRING).toArray(new IFunction[3]);
  }
}
