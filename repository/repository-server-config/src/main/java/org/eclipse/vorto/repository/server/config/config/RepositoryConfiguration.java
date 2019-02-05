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
package org.eclipse.vorto.repository.server.config.config;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.actuator.GeneratorsHealthCheck;
import org.eclipse.vorto.repository.generation.impl.IGeneratorLookupRepository;
import org.eclipse.vorto.repository.sso.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RepositoryConfiguration extends BaseConfiguration {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${http.proxyHost:#{null}}")
	private String proxyHost;

	@Value("${http.proxyPort:8080}")
	protected int proxyPort;

	@Value("${http.proxyUser:#{null}}")
	private String proxyUsername;

	@Value("${http.proxyPassword:#{null}}")
	private String proxyPassword;

	@Value("${repo.configFile}")
	private String repositoryConfigFile = null;
	
	@Autowired
	private IUserAccountService userAccountService;
	
	@Value("${oauth2.verification.eidp.technicalUsers:}")
	private String[] ciamTechnicalUsers;
	
	@Value("${oauth2.verification.keycloak.technicalUsers:}")
	private String[] keycloakTechnicalUsers;

	@Bean
	public org.modeshape.jcr.RepositoryConfiguration repoConfiguration() throws Exception {
		return org.modeshape.jcr.RepositoryConfiguration.read(new ClassPathResource(repositoryConfigFile).getURL());
	}

	@Bean
	public AccessTokenProvider accessTokenProvider() {
		if (proxyHost != null && proxyUsername != null) {
			return TokenUtils.proxiedAccessTokenProvider(proxyHost, proxyPort, proxyUsername, proxyPassword);
		}else if(proxyHost != null && proxyUsername == null){
			return TokenUtils.proxiedAccessTokenProvider(proxyHost, proxyPort);

		} else {
			return TokenUtils.accessTokenProvider();
		}
	}
	
	@Bean
    public GeneratorsHealthCheck generatorsHealthCheck(
		final IGeneratorLookupRepository registeredGeneratorsRepository ) {
		return new GeneratorsHealthCheck( registeredGeneratorsRepository, new RestTemplate() );
    }
	
	@PostConstruct
	public void setupTechnicalUsers() {
		Stream.concat(Stream.of(ciamTechnicalUsers), Stream.of(keycloakTechnicalUsers)).forEach(user -> {
			if (!userAccountService.exists(user)) {
				LOGGER.info("Creating technical user: {}", user);
				userAccountService.create(user);
			}
		});
	}
}
