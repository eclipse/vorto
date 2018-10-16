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

package org.eclipse.vorto.repository.api.impl;

import org.apache.http.client.config.RequestConfig;

public class RequestContext {
	private String baseUrl;
	private RequestConfig requestConfig;
	private String tenantId;

	public RequestContext(String baseUrl, RequestConfig requestConfig, String tenantId) {
		this.baseUrl = baseUrl;
		this.requestConfig = requestConfig;
		this.tenantId = tenantId;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public void setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
	}
	
	public String getTenantId() {
		return this.tenantId;
	}

}
