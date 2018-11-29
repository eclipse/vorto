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
