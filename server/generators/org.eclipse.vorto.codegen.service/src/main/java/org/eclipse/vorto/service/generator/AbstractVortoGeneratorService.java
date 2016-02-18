/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.service.generator;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.ServiceClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@ComponentScan(basePackages = { "org.eclipse.vorto.service.generator, org.eclipse.vorto.codegen.examples" })
@EnableConfigurationProperties
public abstract class AbstractVortoGeneratorService extends SpringBootServletInitializer  {
	
	private RestTemplate restTemplate;
	
	@Value("${vorto.service.repositoryUrl}") 
	private String repositoryBasePath;
	
	@Value("${vorto.service.classifier}")
	private ServiceClassifier classifier;
		
	@Value("${server.contextPath}")
	private String contextPath;
	
	@Autowired
	private IVortoCodeGenerator platformGenerator;
	
	@Value("${server.port}") 
	private int port;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractVortoGeneratorService.class);
	
	@PostConstruct
	public void start() {
		this.restTemplate = new RestTemplate();
		register(platformGenerator.getServiceKey());
	}
	
	@PreDestroy
	public void shutdown() {
		deRegister(platformGenerator.getServiceKey());
	}
	
	public void register(String serviceKey){
		final String serviceUrl = "http://localhost:"+port+contextPath+"/rest/generation";
		LOGGER.info("Registering {} with service url {}",serviceKey,serviceUrl);
		LOGGER.info("Repository Server Url: {}",repositoryBasePath);
		deRegister(serviceKey);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(serviceUrl, headers);
		
		restTemplate.put(repositoryBasePath + "/generation-router/register/{serviceKey}/{classifier}",entity,serviceKey,classifier.name());
	}
	
	public void deRegister(String serviceKey){
		restTemplate.put(repositoryBasePath + "/generation-router/deregister/{serviceKey}", String.class, serviceKey);
	}
}
