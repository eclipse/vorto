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

package org.eclipse.vorto.repository.client;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.vorto.repository.api.IModelGeneration;
import org.eclipse.vorto.repository.api.IModelPublisher;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.IModelResolver;
import org.eclipse.vorto.repository.api.impl.DefaultMappingClient;
import org.eclipse.vorto.repository.api.impl.DefaultModelGeneration;
import org.eclipse.vorto.repository.api.impl.DefaultModelPublisher;
import org.eclipse.vorto.repository.api.impl.DefaultModelRepository;
import org.eclipse.vorto.repository.api.impl.DefaultModelResolver;
import org.eclipse.vorto.repository.api.impl.RequestContext;
import org.eclipse.vorto.repository.api.mapping.IMapping;

public class RepositoryClientBuilder {
	private String baseUrl = "http://vorto.eclipse.org";
	private String proxyHost;
	private int proxyPort = 8080;
	
	private String username;
	private String password;
	
	public static RepositoryClientBuilder newBuilder() {
		return new RepositoryClientBuilder();
	}
	
	private RepositoryClientBuilder() {}

	public RepositoryClientBuilder setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
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
	
	public RepositoryClientBuilder setCredentials(String username, String password) {
		this.username = username;
		this.password = password;
		return this;
	}
	
	public IModelGeneration buildModelGenerationClient() {
		return new DefaultModelGeneration(buildHttpClient(), buildRequestContext());
	}

	public IModelRepository buildModelRepositoryClient() {
		return new DefaultModelRepository(buildHttpClient(), buildRequestContext());
	}
	
	public IModelPublisher buildModelPublishClient() {
		return new DefaultModelPublisher(buildHttpClient(), buildRequestContext(),this.username,this.password);
	}
	
	public IModelResolver buildModelResolverClient() {
		HttpClient client = buildHttpClient();
		RequestContext context = buildRequestContext();
		return new DefaultModelResolver(client, context, new DefaultModelRepository(client, context));
	}
	
	public IMapping buildIMappingClient() {
		return new DefaultMappingClient();
	}
	
	private HttpClient buildHttpClient() {
		return HttpClients.createDefault();
	}
	
	private RequestContext buildRequestContext() {
		if (hasProxy()) {
			return new RequestContext(baseUrl, RequestConfig.custom().setProxy(new HttpHost(proxyHost, proxyPort)).build());
		} else {
			return new RequestContext(baseUrl, RequestConfig.DEFAULT);
		}
	}
	
	private boolean hasProxy() {
		return (proxyHost != null) && !(proxyHost.trim().isEmpty());
	}
}
