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

import org.apache.catalina.connector.Connector;
import org.eclipse.vorto.repository.security.eidp.EidpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

@Configuration
public class RepositoryConfiguration {

	@Value("${repo.configFile}")
	private String repositoryConfigFile = null;
	
	@Bean
	public org.modeshape.jcr.RepositoryConfiguration repoConfiguration() throws Exception {
		return org.modeshape.jcr.RepositoryConfiguration.read(new ClassPathResource(repositoryConfigFile).getURL());
	}
	
	@Bean
	public AccessTokenProvider accessTokenProvider() {
		return EidpUtils.accessTokenProvider();
	}
	
	@Bean
	public Connector redirectingConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(80);
		connector.setRedirectPort(443);
		return connector;
	}
}
