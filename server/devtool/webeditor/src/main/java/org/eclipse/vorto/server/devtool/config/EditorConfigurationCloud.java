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

import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService;
import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryServiceFS;
import org.eclipse.vorto.server.devtool.models.GlobalContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("cloud")
public class EditorConfigurationCloud {

	@Value("${project.repository.path}")
	private String projectRepositoryPath;
			
	@Value("${vorto.repository.base.path:http://vorto.eclipse.org}")
	private String repositoryBasePath;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public IProjectRepositoryService projectRepositoryService() {
		return new ProjectRepositoryServiceFS(projectRepositoryPath);
	}
	
	@Bean
	public GlobalContext getGlobalContext(){
		return new GlobalContext(repositoryBasePath);
	}

}
