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
package org.eclipse.vorto.repository;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.vorto.core.api.repository.Attachment;
import org.eclipse.vorto.core.api.repository.AuthenticationException;
import org.eclipse.vorto.core.api.repository.CheckInModelException;
import org.eclipse.vorto.core.api.repository.ConfigurationException;
import org.eclipse.vorto.core.api.repository.RepositoryException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.google.common.base.Function;
import com.google.common.base.Strings;

public class RestClient {

	private ConnectionInfo connectionInfo;

	private static final String RESOURCE_URL = "/rest/";

	public RestClient(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	public <Result> Result executeGet(String query, final Function<String, Result> responseConverter)
			throws ClientProtocolException, IOException {
		ProxyConfiguration proxyProvider = getProxyConfiguration();

		CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(proxyProvider.credentialsProvider).build();

		HttpUriRequest request = RequestBuilder.get().setConfig(proxyProvider.requestConfig).setUri(createQuery(query)).build();
		
		return client.execute(request, new DefaultResponseHandler<Result>() {
			
			@Override
			public Result handleSuccess(HttpResponse response) throws ClientProtocolException, IOException {
				return responseConverter.apply(IOUtils.toString(response.getEntity().getContent()));
			}
		});
	}
	
	public Attachment executeGetAttachment(String query) throws ClientProtocolException, IOException {
		ProxyConfiguration proxyProvider = getProxyConfiguration();
		
		CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(proxyProvider.credentialsProvider).build();

		HttpUriRequest request = RequestBuilder.get().setConfig(proxyProvider.requestConfig).setUri(createQuery(query)).build();
		
		return client.execute(request, new DefaultResponseHandler<Attachment>() {
	
			@Override
			public Attachment handleSuccess(HttpResponse response) throws ClientProtocolException, IOException {
				String content_disposition = response.getFirstHeader("Content-Disposition").getValue();
				String filename = content_disposition.substring(content_disposition.indexOf("filename = ") + "filename = ".length());
				long length = response.getEntity().getContentLength();
				String type = response.getEntity().getContentType().toString();
				byte[] content = IOUtils.toByteArray(response.getEntity().getContent());
				return new Attachment(filename, length, type, content);
			}
		});
	}

	public <Result> Result executePost(String query, HttpEntity content,
			final Function<String, Result> responseConverter) throws ClientProtocolException, IOException {
		ProxyConfiguration proxyProvider = getProxyConfiguration();
		
		CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(proxyProvider.credentialsProvider).build();

		HttpUriRequest request = RequestBuilder.post().setConfig(proxyProvider.requestConfig).setUri(createQuery(query))
				.addHeader(createSecurityHeader()).setEntity(content).build();
		
		return client.execute(request, new DefaultResponseHandler<Result>() {
			
			@Override
			public Result handleSuccess(HttpResponse response) throws ClientProtocolException, IOException {
				return responseConverter.apply(IOUtils.toString(response.getEntity().getContent()));
			}

			@Override
			protected Result handleFailure(HttpResponse response) throws ClientProtocolException, IOException {
				throw new CheckInModelException("Error in uploading file to remote repository");
			}
		});
	}

	public void executePut(String query) throws ClientProtocolException, IOException {
		ProxyConfiguration proxyProvider = getProxyConfiguration();
		
		CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(proxyProvider.credentialsProvider).build();

		HttpUriRequest request = RequestBuilder.put().setConfig(proxyProvider.requestConfig).setUri(createQuery(query))
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
	
	private ProxyConfiguration getProxyConfiguration() {
		IProxyService proxyService = getProxyService();
		IProxyData[] proxyDataForHost = proxyService.select(java.net.URI.create(connectionInfo.getUrl()));
		
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		RequestConfig.Builder configBuilder = RequestConfig.custom();
		
		for (IProxyData data : proxyDataForHost) {
			if (!Strings.isNullOrEmpty(data.getHost())) {
				HttpHost proxyConfig = new HttpHost(data.getHost(), data.getPort(), data.getType());
				configBuilder.setProxy(proxyConfig);
				if (!Strings.isNullOrEmpty(data.getUserId())) {
					credsProvider.setCredentials(new AuthScope(data.getHost(), data.getPort()),
							new UsernamePasswordCredentials(data.getUserId(), data.getPassword()));
				}
			}
		}
		
		return new ProxyConfiguration(configBuilder.build(), credsProvider);
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
	
	private abstract class DefaultResponseHandler<T> implements ResponseHandler<T> {
		public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			int statusCode = response.getStatusLine().getStatusCode(); 
			if (statusCode >= 200 && statusCode < 300) {
				return handleSuccess(response);
			} else if (statusCode == 401) {
				throw new AuthenticationException();
			} else if (statusCode == 400 || statusCode == 404) {
				throw new ConfigurationException(); 
			} else {
				return handleFailure(response);
			}
		}
		
		protected T handleFailure(HttpResponse response) throws ClientProtocolException, IOException {
			HttpStatus status = HttpStatus.statusFor(response.getStatusLine().getStatusCode());
			throw new RepositoryException("Error : request returned the status code: " + status.getCode() + " - " + status.getMessage());
		}
		
		protected abstract T handleSuccess(HttpResponse response) throws ClientProtocolException, IOException;
	}
	
	private class ProxyConfiguration {
		RequestConfig requestConfig;
		CredentialsProvider credentialsProvider;
		
		public ProxyConfiguration(RequestConfig requestConfig,
				CredentialsProvider credentialsProvider) {
			this.requestConfig = requestConfig;
			this.credentialsProvider = credentialsProvider;
		}
	}
}