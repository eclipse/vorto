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
@Profile("!cloud")
public class RestTemplateConfigLocal {

	@Value("${http.proxyHost}")
	private String proxyHost;

	@Value("${http.proxyPort}")
	private String proxyPort;

	@Bean
	public RestTemplate restTemplate() {
		if (!"".equals(proxyHost) && !"".equals(proxyPort)) {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			InetSocketAddress address = new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort));
			Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
			factory.setProxy(proxy);
			return new RestTemplate(factory);
		} else {
			return new RestTemplate();
		}
	}
}
