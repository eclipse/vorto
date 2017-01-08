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

import org.eclipse.vorto.repository.model.Role;
import org.eclipse.vorto.repository.model.User;
import org.eclipse.vorto.repository.service.IUserRepository;
import org.modeshape.jcr.RepositoryConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("local")
public class LocalConfiguration {

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
	public RepositoryConfiguration repoConfiguration() throws Exception {
		return RepositoryConfiguration.read(new ClassPathResource("vorto-repository-config-file.json").getURL());
	}
}
