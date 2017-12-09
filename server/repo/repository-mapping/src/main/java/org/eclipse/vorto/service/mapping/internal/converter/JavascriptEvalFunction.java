/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.service.mapping.internal.converter;

import java.util.ArrayList;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.JXPathInvalidAccessException;
import org.apache.commons.jxpath.util.TypeUtils;

public class JavascriptEvalFunction implements Function {

	private String functionName;

	private String functionBody;

	public JavascriptEvalFunction(String funcName, String funcBody) {
		this.functionName = funcName;
		this.functionBody = funcBody;
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public Object invoke(ExpressionContext context, Object[] parameters) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		try {
			engine.eval(functionBody);
		} catch (ScriptException e) {
			throw new JXPathException("Problem evaluating " + functionName, e);
		}

		Invocable inv = (Invocable) engine;
		try {
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

			return inv.invokeFunction(functionName, unwrap(args));
		} catch (Throwable ex) {
			throw new JXPathInvalidAccessException("Cannot invoke " + functionName, ex);
		}
	}

	private Object[] unwrap(Object[] wrappedArgs) {
		List<Object> unwrapped = new ArrayList<Object>();
		for (Object o : wrappedArgs) {
			if (o instanceof List<?>)
			{
				List<?> args = (List<?>) o;
				unwrapped.add(args.get(0));
			}
			else
			{
				unwrapped.add(o);
			}
		}
		return unwrapped.toArray();
	}

	private Class<?>[] toTypes(Object[] parameters) {
		List<Class<?>> result = new ArrayList<>();
		for (@SuppressWarnings("unused") Object o : parameters) {		
			result.add(Object.class);
		}
		return result.toArray(new Class[parameters.length]);
	}

}
