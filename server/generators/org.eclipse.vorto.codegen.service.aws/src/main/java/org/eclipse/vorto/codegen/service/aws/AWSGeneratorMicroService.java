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
package org.eclipse.vorto.codegen.service.aws;

import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.aws.AWSGenerator;
import org.eclipse.vorto.server.commons.IGeneratorConfigUITemplate;
import org.eclipse.vorto.service.generator.web.AbstractBackendCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
@SpringBootApplication
public class AWSGeneratorMicroService extends AbstractBackendCodeGenerator {

	@Bean
	public IVortoCodeGenerator awsGenerator() {
		return new AWSGenerator();
	}
	
	@Override
	public IGeneratorConfigUITemplate getConfigTemplate() {
		return new AWSConfigTemplate();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AWSGeneratorMicroService.class, args);
	}
}