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

import java.sql.Timestamp;

import javax.annotation.PostConstruct;

import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.sso.boschid.EidpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

@Configuration
@Profile({"local"})
public class RepositoryConfigurationLocal extends RepositoryConfiguration {
	
	@Autowired
	private IUserRepository userRepository;
	
	@PostConstruct
	public void setUpTestUser() {
		userRepository.save(newUser("admin", Role.ADMIN));
		userRepository.save(newUser("testuser", Role.USER));
		userRepository.save(newUser("testuser2", Role.USER));
	}
	
	private User newUser(String username, Role role) {
		User user = new User();
		
		user.setUsername(username);
		user.setRole(role);
		user.setDateCreated(new Timestamp(System.currentTimeMillis()));
		user.setAckOfTermsAndCondTimestamp(new Timestamp(System.currentTimeMillis()));
		user.setLastUpdated(new Timestamp(System.currentTimeMillis()));
		
		return user;
	}
	
	@Bean
	public AccessTokenProvider accessTokenProvider() {
		return EidpUtils.accessTokenProvider();
	}
}
