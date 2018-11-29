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
