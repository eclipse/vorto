package oeg.eclipse.vorto.repository.client;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.vorto.repository.api.IModelGeneration;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.IModelResolver;
import org.eclipse.vorto.repository.api.impl.DefaultModelGeneration;
import org.eclipse.vorto.repository.api.impl.DefaultModelRepository;
import org.eclipse.vorto.repository.api.impl.DefaultModelResolver;
import org.eclipse.vorto.repository.api.impl.RequestContext;

public class RepositoryClientBuilder {
	private String baseUrl;
	private String proxyHost;
	private int proxyPort = 8080;
	
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
	
	public IModelGeneration buildModelGenerationClient() {
		return new DefaultModelGeneration(buildHttpClient(), buildRequestContext());
	}

	public IModelRepository buildModelRepositoryClient() {
		return new DefaultModelRepository(buildHttpClient(), buildRequestContext());
	}
	
	public IModelResolver buildModelResolverClient() {
		HttpClient client = buildHttpClient();
		RequestContext context = buildRequestContext();
		return new DefaultModelResolver(client, context, new DefaultModelRepository(client, context));
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
