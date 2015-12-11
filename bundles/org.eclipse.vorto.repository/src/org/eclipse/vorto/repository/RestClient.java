/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.repository;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.google.common.base.Function;
import com.google.common.base.Strings;

public class RestClient {

	private ConnectionInfo connectionInfo;

	private static final String RESOURCE_URL = "/rest/model/";

	public RestClient(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	@SuppressWarnings("restriction")
	public <Result> Result executeGet(String query, final Function<String, Result> responseConverter)
			throws ClientProtocolException, IOException {

		CloseableHttpClient client = HttpClients.custom().build();

		HttpUriRequest request = RequestBuilder.get().setConfig(createProxyConfiguration()).setUri(createQuery(query)).build();
		return client.execute(request, new ResponseHandler<Result>() {

			@Override
			public Result handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				return responseConverter.apply(IOUtils.toString(response.getEntity().getContent()));
			}
		});
	}

	@SuppressWarnings("restriction")
	public <Result> Result executePost(String query, HttpEntity content,
			final Function<String, Result> responseConverter) throws ClientProtocolException, IOException {

		CloseableHttpClient client = HttpClients.custom().build();

		HttpUriRequest request = RequestBuilder.post().setConfig(createProxyConfiguration()).setUri(createQuery(query))
				.addHeader(createSecurityHeader()).setEntity(content).build();
		
		return client.execute(request, new ResponseHandler<Result>() {

			@Override
			public Result handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				return responseConverter.apply(IOUtils.toString(response.getEntity().getContent()));
			}
		});
	}

	@SuppressWarnings("restriction")
	public void executePut(String query) throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.custom().build();

		HttpUriRequest request = RequestBuilder.put().setConfig(createProxyConfiguration()).setUri(createQuery(query))
				.addHeader(createSecurityHeader()).build();
		client.execute(request);
	}

	private String createQuery(String queryFragment) {
		StringBuilder connectionUrl = new StringBuilder();
		connectionUrl.append(connectionInfo.getUrl()).append(RESOURCE_URL);

		if (!Strings.isNullOrEmpty(queryFragment)) {
			connectionUrl.append(queryFragment);
		}
		
		return connectionUrl.toString();
	}

	private RequestConfig createProxyConfiguration() {
		IProxyService proxyService = getProxyService();
		IProxyData[] proxyDataForHost = proxyService.select(java.net.URI.create(connectionInfo.getUrl()));
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		for (IProxyData data : proxyDataForHost) {
			HttpHost proxyConfig = new HttpHost(data.getHost(), data.getPort(), data.getType());
			configBuilder.setProxy(proxyConfig);
		}
		return configBuilder.build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static IProxyService getProxyService() {
		BundleContext bc = Activator.getDefault().getBundle().getBundleContext();
		ServiceReference serviceReference = bc.getServiceReference(IProxyService.class.getName());
		IProxyService service = (IProxyService) bc.getService(serviceReference);
		return service;
	}

	/**
	 * TODO: Currently the repo only supports form based authentication. That
	 * way a REST controller authenticate would need to be invoked and session
	 * needs to be passed along.
	 * 
	 * @return
	 */
	private Header createSecurityHeader() {
		return new BasicHeader("Authorization", "Basic " + createAuth());
	}

	private String createAuth() {
		return new String(Base64.encodeBase64((connectionInfo.getUserName() + ":" + connectionInfo.getPassword())
				.getBytes()));
	}
}
