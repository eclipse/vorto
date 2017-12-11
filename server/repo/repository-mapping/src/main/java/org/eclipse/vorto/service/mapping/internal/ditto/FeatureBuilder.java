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
package org.eclipse.vorto.service.mapping.internal.ditto;

import java.util.HashMap;
import java.util.Map;

public class FeatureBuilder {

	private FeatureImpl feature;
		
	public FeatureBuilder(String name) {
		this.feature = new FeatureImpl(name);
	}
	
	public FeatureBuilder withProperty(String name, Object value) {
		this.feature.getProperties().put(name,value);
		return this;
	}
	
	public FeatureBuilder withStatusProperty(String name, Object value) {
		Map<String,Object> statusProperties = new HashMap<>(this.feature.getStatusProperties());
		statusProperties.put(name, value);
		this.feature.getProperties().put("status", statusProperties);
		return this;
	}
	
	public FeatureBuilder withConfigurationProperty(String name, Object value) {
		Map<String,Object> statusProperties = new HashMap<>(this.feature.getConfigurationProperties());
		statusProperties.put(name, value);
		this.feature.getProperties().put("config", statusProperties);
		return this;
	}
		
	public FeatureImpl build() {
		return feature;
	}
}
