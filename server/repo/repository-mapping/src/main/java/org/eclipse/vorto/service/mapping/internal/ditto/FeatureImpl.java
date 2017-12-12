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
 */package org.eclipse.vorto.service.mapping.internal.ditto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.service.mapping.ditto.Feature;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "id","content"})
public class FeatureImpl implements Feature {

	private String id;
	
	private Map<String,Object> content = new HashMap<String, Object>();
	
	private Map<String,Object> properties = new HashMap<String, Object>();
		
	public FeatureImpl(String id) {
		this.id = id;
		this.content.put("properties", properties);
	}
	
	@SuppressWarnings("unchecked")
	public FeatureImpl(String id, Map<String,Object> content) {
		this.id = id;
		this.content = content;
		this.properties.putAll((Map<String,Object>)this.content.get("properties"));
	}
	
	protected FeatureImpl() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Map<String, Object> getContent() {
		return content;
	}
	
	public static FeatureBuilder newBuilder(String featureId) {
		return new FeatureBuilder(featureId);
	}

	@JsonGetter("properties")
	public Map<String, Object> getProperties() {
		return properties;
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	@Override
	public Map<String, Object> getStatusProperties() {
		if (properties.containsKey("status")) {
			return Collections.unmodifiableMap((Map<String, Object>)properties.get("status"));
		} else {
			return Collections.unmodifiableMap(Collections.emptyMap());
		}
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	@Override
	public Map<String, Object> getConfigurationProperties() {
		if (properties.containsKey("config")) {
			return Collections.unmodifiableMap((Map<String, Object>)properties.get("config"));
		} else {
			return Collections.unmodifiableMap(Collections.emptyMap());
		}
	}	
		
}
