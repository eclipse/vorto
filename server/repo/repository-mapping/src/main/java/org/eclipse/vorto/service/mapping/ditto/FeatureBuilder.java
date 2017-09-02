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
package org.eclipse.vorto.service.mapping.ditto;

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
		this.feature.getProperties().put(name, value);
		return this;
	}
	
	public FeatureBuilder withConfigurationProperty(String name, Object value) {
		this.feature.getProperties().put(name, value);
		return this;
	}
	
	public FeatureBuilder setFaultProperties(Map<String,Object> properties) {
		this.feature.getProperties().putAll(properties);
		return this;
	}
	
	public FeatureBuilder setConfigurationProperties(Map<String,Object> properties) {
		this.feature.getProperties().putAll(properties);
		return this;
	}
	
	public FeatureBuilder setStatusProperties(Map<String,Object> properties) {
		this.feature.getProperties().putAll(properties);
		return this;
	}
	
	public FeatureBuilder withFaultProperty(String name, Object value) {
		this.feature.getProperties().put(name, value);
		return this;
	}
	
	public FeatureImpl build() {
		return feature;
	}
}
