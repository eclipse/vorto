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
package org.eclipse.vorto.mapping.engine.internal;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathNotFoundException;
import org.apache.commons.jxpath.util.BasicTypeConverter;
import org.apache.commons.jxpath.util.TypeUtils;
import org.eclipse.vorto.mapping.engine.internal.functions.CustomFunctionsLibrary;

public class JxPathFactory {

  private boolean lenient = false;

  private CustomFunctionsLibrary functionLibrary;

  public JxPathFactory(CustomFunctionsLibrary functionLibrary) {
    this.functionLibrary = functionLibrary;
  }

  public JxPathFactory() {
    this(CustomFunctionsLibrary.createDefault());
  }

  public JXPathContext newContext(Object ctxObject) {
    JXPathContext context = JXPathContext.newContext(ctxObject);
    TypeUtils.setTypeConverter(new MyTypeConverter());
    context.setFunctions(functionLibrary.getConverterFunctions());
    context.setLenient(this.lenient);
    return context;
  }

  public void setLenient(boolean lenient) {
    this.lenient = lenient;
  }

  public static class MyTypeConverter extends BasicTypeConverter {

    @SuppressWarnings("rawtypes")
    @Override
    public Object convert(Object object, final Class toType) {
      if (object instanceof BasicNodeSet && ((BasicNodeSet) object).getValues().isEmpty()) {
        throw new JXPathNotFoundException("Could not find path in source");
      } else {
        return super.convert(object, toType);
      }
    }
  }
}
