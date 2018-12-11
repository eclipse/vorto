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
package org.eclipse.vorto.mapping.engine.converter.types;

import java.util.Arrays;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.eclipse.vorto.mapping.engine.functions.ClassFunction;
import org.eclipse.vorto.mapping.engine.functions.IFunction;

public class TypeFunctionFactory {

  private static final IFunction FUNC_NUMBER = new ClassFunction("vorto_number", NumberUtils.class);
  private static final IFunction FUNC_TYPE = new ClassFunction("vorto_type", ConvertUtils.class);
  private static final IFunction FUNC_BOOL = new ClassFunction("vorto_boolean", BooleanUtils.class);
  private static final IFunction FUNC_ARRAY = new ClassFunction("vorto_array", ArrayUtils.class);

  @Deprecated
  private static final IFunction FUNC_NUMBER_OLD = new ClassFunction("number", NumberUtils.class);
  @Deprecated
  private static final IFunction FUNC_TYPE_OLD = new ClassFunction("type", ConvertUtils.class);
  @Deprecated
  private static final IFunction FUNC_BOOL_OLD = new ClassFunction("boolean", BooleanUtils.class);
  @Deprecated
  private static final IFunction FUNC_ARRAY_OLD = new ClassFunction("array", ArrayUtils.class);

  public static IFunction[] createFunctions() {
    return Arrays.asList(FUNC_NUMBER, FUNC_TYPE, FUNC_BOOL, FUNC_ARRAY, FUNC_NUMBER_OLD,
        FUNC_TYPE_OLD, FUNC_BOOL_OLD, FUNC_ARRAY_OLD).toArray(new IFunction[8]);
  }
}
