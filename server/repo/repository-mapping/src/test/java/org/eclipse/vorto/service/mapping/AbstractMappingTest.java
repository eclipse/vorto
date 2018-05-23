package org.eclipse.vorto.service.mapping;

import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.client.RepositoryClientBuilder;

public class AbstractMappingTest {
	
	public IModelRepository getModelRepository() {
		RepositoryClientBuilder builder = RepositoryClientBuilder.newBuilder()
				.setBaseUrl("http://vorto.eclipse.org");
		
		if (System.getProperty("http.proxyHost") != null) {
			builder.setProxyHost(System.getProperty("http.proxyHost"));
			builder.setProxyPort(Integer.valueOf(System.getProperty("http.proxyPort")));
			System.out.println("Using proxy -> " + System.getProperty("http.proxyHost") + ":" + Integer.valueOf(System.getProperty("http.proxyPort")));
		}
		
		return builder.buildModelRepositoryClient();
	}
	
}
