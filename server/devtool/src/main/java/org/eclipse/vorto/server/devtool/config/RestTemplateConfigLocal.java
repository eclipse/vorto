package org.eclipse.vorto.server.devtool.config;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("local")
public class RestTemplateConfigLocal {

	@Value("${http.proxyHost}")
	private String proxyHost;
	
	@Value("${http.proxyPort}")
	private int proxyPort;
	
	@Bean
	public RestTemplate restTemplate() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        InetSocketAddress address = new InetSocketAddress(proxyHost,proxyPort);
        Proxy proxy = new Proxy(Proxy.Type.HTTP,address);
        factory.setProxy(proxy);
		return new RestTemplate(factory);
	}
}
