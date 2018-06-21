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
package org.eclipse.vorto.codegen.gateway;

import org.eclipse.vorto.codegen.bosch.BoschIoTSuiteGenerator;
import org.eclipse.vorto.codegen.ditto.EclipseDittoGenerator;
import org.eclipse.vorto.codegen.gateway.model.Generator;
import org.eclipse.vorto.codegen.gateway.repository.GeneratorRepository;
import org.eclipse.vorto.codegen.gateway.service.VortoService;
import org.eclipse.vorto.codegen.gateway.utils.GatewayUtils;
import org.eclipse.vorto.codegen.hono.EclipseHonoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class GatewayInit implements ApplicationRunner, EnvironmentAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(GatewayInit.class);
	
	private Environment env;
	
	@Autowired
	private GeneratorRepository generatorRepo;
	
	@Autowired
	private VortoService vorto;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		try {
			generatorRepo.add(Generator.create("/generators/bosch.properties", BoschIoTSuiteGenerator.class));
			generatorRepo.add(Generator.create("/generators/ditto.properties", EclipseDittoGenerator.class));
			generatorRepo.add(Generator.create("/generators/hono.properties", EclipseHonoGenerator.class));
			generatorRepo.list().stream().forEach(GatewayUtils.checkEnvModifications(env));
			
			generatorRepo.list().stream().forEach(vorto::register);
			
		} catch(RuntimeException e) {
			LOGGER.error("Error registering a generator", e);
		}
	}

	@Override
	public void setEnvironment(Environment env) {
		this.env = env;
	}
}
