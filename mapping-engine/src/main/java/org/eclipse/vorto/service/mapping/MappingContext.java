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
package org.eclipse.vorto.service.mapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A Mapping Context contains further configuration attributes that are processed during the mapping
 *
 */
public class MappingContext {

	private Set<String> properties = null;
	
	/**
	 * Empty Mapping Context tries to map all function block properties of the information model
	 * @return new Mapping Context holding the configuration
	 */
	public static MappingContext empty() {
		return new EmptyContext();
	}
	
	/**
	 * adds the functionblock properties that ought to be mapped by the mapping engine.
	 * @param propertyNames the names of the function block properties of the Information Model
	 * @return new Mapping Context holding the configuration
	 */
	public static MappingContext functionblockProperties(String...propertyNames) {
		return new MappingContext(new HashSet<>(Arrays.asList(propertyNames)));
	}
	
	protected MappingContext(Set<String> properties) {
		this.properties = properties;
	}
	
	public boolean isIncluded(String property) {
		return properties.contains(property);
	}
	
	private static class EmptyContext extends MappingContext {

		protected EmptyContext() {
			super(null);
		}
		
		@Override
		public boolean isIncluded(String property) {
			return true;
		}
	}
}
