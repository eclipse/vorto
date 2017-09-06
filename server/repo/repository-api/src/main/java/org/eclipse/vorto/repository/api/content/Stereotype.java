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
package org.eclipse.vorto.repository.api.content;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Stereotype {

	private String name;

	private Map<String, String> attributes = new HashMap<String, String>();
	
	public static Stereotype createWithValue(String name, String value) {
		Map<String,String> attributes = new HashMap<String, String>(1);
		attributes.put("value",value);
		return new Stereotype(name, attributes);
	}
	
	public static Stereotype createWithXpath(String name, String xpath) {
		Map<String,String> attributes = new HashMap<String, String>(1);
		attributes.put("xpath",xpath);
		return new Stereotype(name, attributes);
	}
	
	public static Stereotype create(String name, Map<String,String> attributes) {
		return new Stereotype(name, attributes);
	}
		
	Stereotype(String name, Map<String,String> attributes) {
		this.name = name;
		this.attributes = attributes;
	}
	
	public String getName() {
		return name;
	}

	public Map<String, String> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "Stereotype [name=" + name + ", attributes=" + attributes + "]";
	}
	
	
}
