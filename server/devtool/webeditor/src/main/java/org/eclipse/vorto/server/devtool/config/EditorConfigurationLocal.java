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

import javax.annotation.PostConstruct;

import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService;
import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryServiceFS;
import org.eclipse.vorto.server.devtool.models.GlobalContext;
import org.eclipse.vorto.server.devtool.models.Role;
import org.eclipse.vorto.server.devtool.models.User;
import org.eclipse.vorto.server.devtool.service.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("!cloud")
public class EditorConfigurationLocal {

	@Value("${http.proxyHost}")
	private String proxyHost;

	@Value("${http.proxyPort}")
	private String proxyPort;
	
	@Value("${project.repository.path}")
	private String projectRepositoryPath;
	
	@Value("${reference.repository}")
	private String referenceRepository;
		
	@Value("${vorto.repository.base.path:http://vorto.eclipse.org}")
	private String repositoryBasePath;
		
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private PasswordEncoder encoder;
		
	static {
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
			public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
				if (hostname.equals("localhost")) {
					return true;
				}
				return false;
			}
		});
	}
	
	@Bean
	public IProjectRepositoryService projectRepositoryService() {
		return new ProjectRepositoryServiceFS(projectRepositoryPath);
	}

	@Bean
	public GlobalContext getGlobalContext(){
		return new GlobalContext(repositoryBasePath, referenceRepository);
	}
	
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
	
	@Bean(name = "DevClient")
	public RestTemplate devClientTemplate() {
		return new RestTemplate();
	}
			
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
}
