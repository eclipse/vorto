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
package org.eclipse.vorto.mapping.engine.converter.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jxpath.ExpressionContext;
import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.util.TypeUtils;
import org.eclipse.vorto.mapping.engine.MappingException;
import org.liquidplayer.javascript.JSContext;
import org.liquidplayer.javascript.JSFunction;
import org.liquidplayer.javascript.JSObject;
import org.liquidplayer.javascript.JSValue;


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
	@SuppressWarnings({ "rawtypes" })
	public Object invoke(ExpressionContext context, Object[] parameters) {
		checkScriptForMaliciousContent();
		JSContext jsContext = new JSContext();
		jsContext.evaluateScript(functionBody);
		context.getContextNodeList();
        // Get Parameters from string
        Pattern functionParameterPattern = Pattern.compile("function\\s.*?\\(([^)]*)\\)");
        Matcher functionParameterMatcher = functionParameterPattern.matcher(functionBody);
        functionParameterMatcher.find();
        String[] functionParameters = functionParameterMatcher.group(1).split(",");

		// Get JavaScript function body from string which contains the full function block.
        Pattern functionBodyPattern = Pattern.compile("function\\s*\\w*\\s*\\([\\w\\s,]*\\)\\s*\\{([\\w\\W]*?)\\}");
        Matcher functionBodyMatcher = functionBodyPattern.matcher(functionBody);
        functionBodyMatcher.find();
        functionBody = functionBodyMatcher.group(1);

		JSFunction jsFunction = new JSFunction(jsContext, functionName, functionBody, functionParameters);
		JSObject jsObject = new JSObject();
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
		JSValue result = jsFunction.call(jsObject, unwrap(args));
		if(result.isUndefined()){
		    throw new MappingException("Error occurred during JavaScript evaluation");
        }
		return result;
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
