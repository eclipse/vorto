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
package org.eclipse.vorto.mapping.engine.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvaluator;
import org.eclipse.vorto.mapping.engine.functions.ScriptClassFunction;

public class JavascriptFunctions implements Functions, IScriptEvaluator {

  private String namespace;

  private Map<String, String> functions;

  public JavascriptFunctions(String namespace) {
    this.namespace = namespace;
    this.functions = new HashMap<String, String>();
  }

  public void addFunction(String functionName, String functionBody) {
    this.functions.put(functionName, functionBody);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Set getUsedNamespaces() {
    return Collections.singleton(namespace);
  }

  @Override
  public Function getFunction(String namespace, String name, Object[] parameters) {
    if (!this.namespace.equals(namespace)) {
      return null;
    }

    if (!this.functions.containsKey(name)) {
      return null;
    }

    return new JavascriptEvalFunction(name, functions.get(name));
  }

  @Override
  public Functions getFunctions() {
    return this;
  }

  @Override
  public void addScriptFunction(ScriptClassFunction function) {
    this.functions.put(function.getName(), function.getValue());
  }

}
