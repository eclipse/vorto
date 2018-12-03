/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
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
