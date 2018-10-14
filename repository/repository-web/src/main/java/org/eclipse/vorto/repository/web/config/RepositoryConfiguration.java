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
package org.eclipse.vorto.repository.web.config;

import org.eclipse.vorto.repository.sso.boschid.EidpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

@Configuration
public class RepositoryConfiguration extends BaseConfiguration {

	@Value("${http.proxyHost:#{null}}")
	String proxyHost;

	@Value("${http.proxyPort:8080}")
	protected int proxyPort;

	@Value("${http.proxyUser:#{null}}")
	private String proxyUsername;

	@Value("${http.proxyPassword:#{null}}")
	private String proxyPassword;

	@Value("${repo.configFile}")
	private String repositoryConfigFile = null;

	@Bean
	public org.modeshape.jcr.RepositoryConfiguration repoConfiguration() throws Exception {
		return org.modeshape.jcr.RepositoryConfiguration.read(new ClassPathResource(repositoryConfigFile).getURL());
	}

	@Bean
	public AccessTokenProvider accessTokenProvider() {
		if (proxyHost != null) {
			return EidpUtils.proxiedAccessTokenProvider(proxyHost, proxyPort, proxyUsername, proxyPassword);
		} else {
			return EidpUtils.accessTokenProvider();
		}
	}
}
