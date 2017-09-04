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
package org.eclipse.vorto.service.mapping.converters;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jxpath.Function;
import org.apache.commons.jxpath.Functions;

public class JavascriptFunctions implements Functions {
	
	private String namespace;
	
	private String functionBody;
	
	public JavascriptFunctions(String namespace, String functionBody) {
		this.namespace = namespace;
		this.functionBody = functionBody;
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
		
		return functions.get(name);
	}

}
