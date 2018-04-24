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

import org.eclipse.vorto.service.mapping.internal.ditto.FeatureBuilder;

public interface Feature {

	String getId();
	
	/**
	 * Fetches all properties of the feature
	 * @return
	 */
	Map<String,Object> getProperties();
	
	/**
	 * Fetches only status properties
	 * @return
	 */
	Map<String,Object> getStatusProperties();
	
	/**
	 * Fetches only configuration properties
	 * @return
	 */
	Map<String,Object> getConfigurationProperties();
		
	static FeatureBuilder newBuilder(String id) {
		return new FeatureBuilder(id);
	}
}
