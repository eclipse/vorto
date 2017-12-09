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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.service.mapping.JsonData;
import org.eclipse.vorto.service.mapping.internal.ditto.EmptyFeature;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DittoData implements JsonData {

	private Map<String, Feature> features = new HashMap<>();
	
	public void withFeature(Feature feature) {
		if (!(feature instanceof EmptyFeature)) {
			this.features.put(feature.getId(),feature);
		} 
	}

	@Override
	public String toJson() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	@JsonAnyGetter
	public Map<String,Feature> getFeatures() {
		return Collections.unmodifiableMap(features);
	}
	
}
