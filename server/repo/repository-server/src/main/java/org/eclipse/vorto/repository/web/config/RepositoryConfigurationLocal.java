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

import javax.annotation.PostConstruct;

import org.apache.catalina.connector.Connector;
import org.eclipse.vorto.repository.model.Role;
import org.eclipse.vorto.repository.model.User;
import org.eclipse.vorto.repository.security.eidp.EidpUtils;
import org.eclipse.vorto.repository.service.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

@Configuration
@Profile({"local", "local-https"})
public class RepositoryConfigurationLocal extends org.eclipse.vorto.repository.web.config.RepositoryConfiguration {
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@PostConstruct
	public void setUpTestUser() {
		User admin = new User();
		
		admin.setUsername("admin".toLowerCase());
		admin.setPassword( encoder.encode("admin"));
		admin.setHasWatchOnRepository(false);
		admin.setEmail("alexander.edelmann@bosch-si.com");
		admin.setRoles(Role.ADMIN);
			
		userRepository.save(admin);
		
		User user = new User();
		user.setUsername("testuser");
		user.setPassword(encoder.encode("testuser"));
		user.setRoles(Role.USER);
		
		userRepository.save(user);
	}
	
	@Bean
	public AccessTokenProvider accessTokenProvider() {
		return EidpUtils.proxiedAccessTokenProvider("rb-proxy-apac.bosch.com", 8080, "erm1sgp", "1m0ngnaw0ng123!");
	}
	
	@Bean
	public Connector redirectingConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setRedirectPort(8443);
		return connector;
	}
}
