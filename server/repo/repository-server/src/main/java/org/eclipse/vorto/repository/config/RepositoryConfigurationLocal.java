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
package org.eclipse.vorto.repository.config;

import javax.annotation.PostConstruct;

import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.account.impl.User;
import org.eclipse.vorto.repository.sso.boschid.EidpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;

@Configuration
@Profile({"local", "local-https"})
public class RepositoryConfigurationLocal extends org.eclipse.vorto.repository.config.RepositoryConfiguration {
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@PostConstruct
	public void setUpTestUser() {
		userRepository.save(newUser("admin", "admin", false, "alexander.edelmann@bosch-si.com", Role.ADMIN));
		userRepository.save(newUser("testuser", "testuser", false, "erleczars.mantos@bosch-si.com", Role.USER));
		userRepository.save(newUser("testuser2", "testuser2", false, "testuser2@my.com", Role.USER));
	}
	
	private User newUser(String username, String password, boolean hasWatchOnRepo, String email, Role role) {
		User user = new User();
		
		user.setUsername(username);
		user.setPassword(encoder.encode(password));
		user.setHasWatchOnRepository(hasWatchOnRepo);
		user.setEmail(email);
		user.setRoles(role);
		
		return user;
	}
	
	@Bean
	public AccessTokenProvider accessTokenProvider() {
		return EidpUtils.accessTokenProvider();
	}
}
