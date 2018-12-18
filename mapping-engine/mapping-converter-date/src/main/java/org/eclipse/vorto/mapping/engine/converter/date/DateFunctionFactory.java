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
package org.eclipse.vorto.mapping.engine.converter.date;

import java.util.Arrays;
import org.eclipse.vorto.mapping.engine.functions.ClassFunction;
import org.eclipse.vorto.mapping.engine.functions.IFunction;

public class DateFunctionFactory {

  private static final IFunction VORTO_FUNC_DATE = new ClassFunction("vorto_date", DateUtils.class);

  @Deprecated
  private static final IFunction VORTO_FUNC_DATE_OLD = new ClassFunction("date", DateUtils.class);

  public static IFunction[] createFunctions() {
    return Arrays.asList(VORTO_FUNC_DATE, VORTO_FUNC_DATE_OLD).toArray(new IFunction[2]);
  }
}
