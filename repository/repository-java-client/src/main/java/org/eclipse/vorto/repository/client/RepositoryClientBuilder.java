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
package org.eclipse.vorto.repository.client;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.vorto.repository.api.IModelGeneration;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.impl.DefaultMappingClient;
import org.eclipse.vorto.repository.api.impl.DefaultModelGeneration;
import org.eclipse.vorto.repository.api.impl.DefaultModelRepository;
import org.eclipse.vorto.repository.api.impl.RequestContext;
import org.eclipse.vorto.repository.api.mapping.IMapping;
import org.eclipse.vorto.repository.client.impl.DefaultRepositoryClient;

public class RepositoryClientBuilder {
	private String baseUrl = "http://vorto.eclipse.org";
	private String proxyHost;
	private int proxyPort = 8080;
	private String tenantId = null;
	
	public static RepositoryClientBuilder newBuilder() {
		return new RepositoryClientBuilder();
	}
	
	private RepositoryClientBuilder() {}

	public RepositoryClientBuilder setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}
	
	public RepositoryClientBuilder setTenant(String tenantId) {
		this.tenantId = tenantId;
		return this;
	}

	public RepositoryClientBuilder setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
		return this;
	}

	public RepositoryClientBuilder setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
		return this;
	}
	
	@Deprecated
	/**
	 * Please use {@link RepositoryClientBuilder#build()} instead
	 * @return
	 */
	public IModelGeneration buildModelGenerationClient() {
		return new DefaultModelGeneration(buildHttpClient(), buildRequestContext());
	}

	@Deprecated
	/**
     * Please use {@link RepositoryClientBuilder#build()} instead
     * @return
     */
	public IModelRepository buildModelRepositoryClient() {
		return new DefaultModelRepository(buildHttpClient(), buildRequestContext());
	}
	
	@Deprecated
	public IMapping buildIMappingClient() {
		return new DefaultMappingClient();
	}
	
	private HttpClient buildHttpClient() {
		return HttpClients.createDefault();
	}
	
	private RequestContext buildRequestContext() {
		if (hasProxy()) {
			return new RequestContext(baseUrl, RequestConfig.custom().setProxy(new HttpHost(proxyHost, proxyPort)).build(),this.tenantId);
		} else {
			return new RequestContext(baseUrl, RequestConfig.DEFAULT,this.tenantId);
		}
	}
	
	private boolean hasProxy() {
		return (proxyHost != null) && !(proxyHost.trim().isEmpty());
	}
	
	public IRepositoryClient build() {
	  return new DefaultRepositoryClient(buildHttpClient(), buildRequestContext2());
	}
	
	private org.eclipse.vorto.repository.client.impl.RequestContext buildRequestContext2() {
      if (hasProxy()) {
          return new org.eclipse.vorto.repository.client.impl.RequestContext(baseUrl, RequestConfig.custom().setProxy(new HttpHost(proxyHost, proxyPort)).build());
      } else {
          return new org.eclipse.vorto.repository.client.impl.RequestContext(baseUrl, RequestConfig.DEFAULT);
      }
  }
}
