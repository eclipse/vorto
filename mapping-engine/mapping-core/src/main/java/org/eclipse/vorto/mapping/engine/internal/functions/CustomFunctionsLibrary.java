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

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.jxpath.ClassFunctions;
import org.apache.commons.jxpath.FunctionLibrary;
import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.mapping.engine.functions.IFunction;

public class CustomFunctionsLibrary {

  private FunctionLibrary converterLibrary;

  private Map<String, Object> conditionFunctions = new HashMap<>();

  private CustomFunctionsLibrary() {
    this.converterLibrary = new FunctionLibrary();
  }

  public static CustomFunctionsLibrary createDefault() {
    return new CustomFunctionsLibrary();
  }

  public void addConverterFunction(IFunction function) {
    this.converterLibrary
        .addFunctions(new ClassFunctions(function.getFunctionClass(), function.getNamespace()));
  }

  public void addConditionFunction(IFunction function) {
    this.conditionFunctions.put(function.getNamespace(), function.getFunctionClass());
  }

  public FunctionLibrary getConverterFunctions() {
    return converterLibrary;
  }

  public Map<String, Object> getConditionFunctions() {
    return this.conditionFunctions;
  }

  public void addConverterFunctions(Functions functions) {
    this.converterLibrary.addFunctions(functions);
  }
}
