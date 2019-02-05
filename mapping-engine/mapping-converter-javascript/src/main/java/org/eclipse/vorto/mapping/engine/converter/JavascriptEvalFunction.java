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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.util.TypeUtils;
import org.eclipse.vorto.mapping.engine.MappingException;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

@SuppressWarnings("restriction")
public class JavascriptEvalFunction implements Function {

  private static final List<String> MALICIOUS_KEYWORDS = Arrays.asList("while", "for", "foreach");

  private String functionName;

  private String functionBody;

  public JavascriptEvalFunction(String funcName, String funcBody) {
    this.functionName = funcName;
    this.functionBody = funcBody;
  }

  @Override
  @SuppressWarnings({"rawtypes"})
  public Object invoke(ExpressionContext context, Object[] parameters) {
    checkScriptForMaliciousContent();

    NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine(new ClassFilter() {

      @Override
      public boolean exposeToScripts(String s) {
        return false;
      }

    });
    try {
      final Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
      bindings.remove("print");
      bindings.remove("load");
      bindings.remove("loadWithNewGlobal");
      bindings.remove("exit");
      bindings.remove("quit");
      engine.eval(functionBody);

    } catch (ScriptException e) {
      throw new JXPathException("Problem evaluating " + functionName, e);
    }

    Invocable inv = (Invocable) engine;
    Object[] args;
    int pi = 0;
    Class[] types = toTypes(parameters);
    if (types.length >= 1 && ExpressionContext.class.isAssignableFrom(types[0])) {
      pi = 1;
    }
    args = new Object[parameters.length + pi];
    if (pi == 1) {
      args[0] = context;
    }
    for (int i = 0; i < parameters.length; i++) {
      args[i + pi] = TypeUtils.convert(parameters[i], types[i + pi]);
    }

    try {
      return inv.invokeFunction(functionName, unwrap(args));
    } catch (NoSuchMethodException e) {
      throw new JXPathInvalidAccessException("Cannot find function with the list of parameters", e);
    } catch (ScriptException e) {
      throw new JXPathInvalidAccessException("Problem executing javascript", e);
    }
  }

  private void checkScriptForMaliciousContent() {
    for (String maliciousKeyword : MALICIOUS_KEYWORDS) {
      if (this.functionBody.contains(maliciousKeyword)) {
        throw new MappingException(
            "The keyword " + maliciousKeyword + " is not allowed in javascript function.");
      }
    }

  }

  private Object[] unwrap(Object[] wrappedArgs) {
    List<Object> unwrapped = new ArrayList<Object>();
    for (Object o : wrappedArgs) {
      if (o instanceof List<?>) {
        List<?> args = (List<?>) o;
        unwrapped.add(args.get(0));
      } else {
        unwrapped.add(o);
      }
    }
    return unwrapped.toArray();
  }

  private Class<?>[] toTypes(Object[] parameters) {
    List<Class<?>> result = new ArrayList<>();
    for (@SuppressWarnings("unused")
    Object o : parameters) {
      result.add(Object.class);
    }
    return result.toArray(new Class[parameters.length]);
  }

}
