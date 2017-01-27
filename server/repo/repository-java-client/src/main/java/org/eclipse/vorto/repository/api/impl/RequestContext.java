package org.eclipse.vorto.repository.api.impl;

import org.apache.http.client.config.RequestConfig;

public class RequestContext {
	private String baseUrl;
	private RequestConfig requestConfig;

	public RequestContext(String baseUrl, RequestConfig requestConfig) {
		this.baseUrl = baseUrl;
		this.requestConfig = requestConfig;
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

}
