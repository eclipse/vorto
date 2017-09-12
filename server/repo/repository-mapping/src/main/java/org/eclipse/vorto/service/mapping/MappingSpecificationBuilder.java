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

import java.util.Optional;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.service.mapping.spec.RemoteMappingSpecification;

public class MappingSpecificationBuilder {

	private ModelId modelId;
	private String key;
	private String endpoint = "http://vorto.eclipse.org";
	private String proxyHost = null;
	private int proxyPort = 8080;
	
	public MappingSpecificationBuilder modelId(String infoModelId) {
		this.modelId = ModelId.fromPrettyFormat(infoModelId);
		this.key = calculateKey(infoModelId);
		return this;
	}

	private static String calculateKey(String modelId) {
		return modelId.replace(".", "_").replace(":", "_");
	}
	
	public MappingSpecificationBuilder endpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}
	
	public MappingSpecificationBuilder proxy(String proxyHost, int proxyPort) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		return this;
	}
	
	public MappingSpecificationBuilder key(String key) {
		this.key = key;
		return this;
	}
	
	public IMappingSpecification build() {
		return new RemoteMappingSpecification(this.modelId, key, endpoint,Optional.ofNullable(proxyHost),Optional.ofNullable(proxyPort));
	}
	
}
