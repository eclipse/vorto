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
package org.eclipse.vorto.service.generator.web;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.ServiceClassifier;
import org.eclipse.vorto.server.commons.ui.DefaultConfigTemplate;
import org.eclipse.vorto.server.commons.ui.IGeneratorConfigUITemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = { "org.eclipse.vorto.service.generator" })
@EnableConfigurationProperties
public abstract class AbstractBackendCodeGenerator  {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${vorto.service.repositoryUrl}") 
	private String repositoryBasePath;
	
	@Value("${vorto.service.classifier}")
	private ServiceClassifier classifier;
		
	@Value("${server.contextPath}")
	private String contextPath;
	
	@Autowired
	private IVortoCodeGenerator platformGenerator;
	
	@Value("${server.host}") 
	private String serviceUrl;
	
	@Value("${server.port}") 
	private int port;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBackendCodeGenerator.class);
	
	@PostConstruct
	public void start() {
		registerWithRepository(platformGenerator.getServiceKey());
	}
	
	@Bean
	public IGeneratorConfigUITemplate getConfigTemplate() {
		return new DefaultConfigTemplate();
	}
	
	@PreDestroy
	public void shutdown() {
		deRegisterFromRepository(platformGenerator.getServiceKey());
	}
	
	public void registerWithRepository(String serviceKey){
		final String serviceUrl = "http://"+this.serviceUrl+":"+port+contextPath+"/rest/generation";
		LOGGER.info("Registering {} with service url {}",serviceKey,serviceUrl);
		LOGGER.info("Repository Server Url: {}",repositoryBasePath);
		deRegisterFromRepository(serviceKey);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(serviceUrl, headers);
		
		restTemplate.put(repositoryBasePath + "/generation-router/register/{serviceKey}/{classifier}",entity,serviceKey,classifier.name());
	}
	
	public void deRegisterFromRepository(String serviceKey){
		restTemplate.put(repositoryBasePath + "/generation-router/deregister/{serviceKey}", String.class, serviceKey);
	}

}

