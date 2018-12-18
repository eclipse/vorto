/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.api.impl;

import org.apache.http.client.config.RequestConfig;

@Deprecated
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
