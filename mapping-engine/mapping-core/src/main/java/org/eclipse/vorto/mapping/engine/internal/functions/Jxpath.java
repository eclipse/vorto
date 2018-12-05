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
package org.eclipse.vorto.mapping.engine.internal.functions;

import org.apache.commons.jxpath.JXPathContext;
import org.eclipse.vorto.mapping.engine.internal.JxPathFactory;

public class Jxpath {

  private static JxPathFactory helper = new JxPathFactory(CustomFunctionsLibrary.createDefault());

  public static Object eval(String exp, Object value) {
    JXPathContext context = helper.newContext(value);
    return context.getValue(exp.replaceAll("\\.", "/"));
  }
}
