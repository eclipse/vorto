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
package org.eclipse.vorto.mapping.engine.converter.binary;

import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.EndianUtils;
import org.apache.commons.lang3.Conversion;
import org.eclipse.vorto.mapping.engine.functions.ClassFunction;
import org.eclipse.vorto.mapping.engine.functions.IFunction;

public class BinaryFunctionFactory {

  private static final IFunction FUNC_CONVERSION =
      new ClassFunction("vorto_conversion1", Conversion.class);
  private static final IFunction FUNC_CONVERSION2 =
      new ClassFunction("vorto_conversion2", DatatypeConverter.class);
  private static final IFunction FUNC_BASE64 = new ClassFunction("vorto_base64", Base64.class);
  private static final IFunction FUNC_ENDIAN = new ClassFunction("vorto_endian", EndianUtils.class);

  @Deprecated
  private static final IFunction FUNC_CONVERSION_OLD =
      new ClassFunction("conversion", Conversion.class);
  @Deprecated
  private static final IFunction FUNC_CONVERSION2_OLD =
      new ClassFunction("binaryString", DatatypeConverter.class);
  @Deprecated
  private static final IFunction FUNC_BASE64_OLD = new ClassFunction("base64", Base64.class);
  @Deprecated
  private static final IFunction FUNC_ENDIAN_OLD = new ClassFunction("endian", EndianUtils.class);

  public static IFunction[] createFunctions() {
    return Arrays.asList(FUNC_CONVERSION, FUNC_CONVERSION2, FUNC_BASE64, FUNC_ENDIAN,
        FUNC_CONVERSION_OLD, FUNC_CONVERSION2_OLD, FUNC_BASE64_OLD, FUNC_ENDIAN_OLD)
        .toArray(new IFunction[8]);
  }
}
