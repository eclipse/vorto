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

import javax.annotation.PreDestroy;

import org.eclipse.vorto.codegen.aws.AWSGenerator;
import org.eclipse.vorto.codegen.bosch.things.BoschIoTThingsGenerator;
import org.eclipse.vorto.codegen.coap.CoAPGenerator;
import org.eclipse.vorto.codegen.gateway.model.Generator;
import org.eclipse.vorto.codegen.gateway.repository.GeneratorRepository;
import org.eclipse.vorto.codegen.gateway.service.VortoService;
import org.eclipse.vorto.codegen.gateway.templates.AWSConfigTemplate;
import org.eclipse.vorto.codegen.gateway.templates.BoschThingsConfigTemplate;
import org.eclipse.vorto.codegen.gateway.templates.KuraConfigTemplate;
import org.eclipse.vorto.codegen.gateway.templates.WebUIConfigTemplate;
import org.eclipse.vorto.codegen.gateway.utils.GatewayUtils;
import org.eclipse.vorto.codegen.ios.IOSPlatformGenerator;
import org.eclipse.vorto.codegen.javabean.JavabeanGenerator;
import org.eclipse.vorto.codegen.kura.KuraGenerator;
import org.eclipse.vorto.codegen.latex.LatexGenerator;
import org.eclipse.vorto.codegen.lwm2m.LWM2MGenerator;
import org.eclipse.vorto.codegen.markdown.MarkdownGenerator;
import org.eclipse.vorto.codegen.mqtt.MQTTPlatformGenerator;
import org.eclipse.vorto.codegen.prosystfi.ProSystGenerator;
import org.eclipse.vorto.codegen.protobuf.ProtobufGenerator;
import org.eclipse.vorto.codegen.thingworx.ThingWorxCodeGenerator;
import org.eclipse.vorto.codegen.webdevice.WebDeviceGenerator;
import org.eclipse.vorto.codegen.webui.WebUIGenerator;
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
			generatorRepo.add(Generator.create("/generators/aws.properties", AWSGenerator.class, new AWSConfigTemplate()));
			generatorRepo.add(Generator.create("/generators/bosch-things.properties", BoschIoTThingsGenerator.class, new BoschThingsConfigTemplate()));
			generatorRepo.add(Generator.create("/generators/coap.properties", CoAPGenerator.class));
			generatorRepo.add(Generator.create("/generators/ios.properties", IOSPlatformGenerator.class));
			generatorRepo.add(Generator.create("/generators/javabean.properties", JavabeanGenerator.class));
			generatorRepo.add(Generator.create("/generators/latex.properties", LatexGenerator.class));
			generatorRepo.add(Generator.create("/generators/lwm2m.properties", LWM2MGenerator.class));
			generatorRepo.add(Generator.create("/generators/markdown.properties", MarkdownGenerator.class));
			generatorRepo.add(Generator.create("/generators/mqtt.properties", MQTTPlatformGenerator.class));
			generatorRepo.add(Generator.create("/generators/prosystfi.properties", ProSystGenerator.class));
			generatorRepo.add(Generator.create("/generators/thingworx.properties", ThingWorxCodeGenerator.class));
			generatorRepo.add(Generator.create("/generators/webui.properties", WebUIGenerator.class, new WebUIConfigTemplate()));
			generatorRepo.add(Generator.create("/generators/webdevice.properties", WebDeviceGenerator.class));
			generatorRepo.add(Generator.create("/generators/kura.properties", KuraGenerator.class, new KuraConfigTemplate()));
			generatorRepo.add(Generator.create("/generators/protobuf.properties", ProtobufGenerator.class));
			generatorRepo.list().stream().forEach(GatewayUtils.checkEnvModifications(env));
			
			generatorRepo.list().stream().forEach(vorto::register);
			
		} catch(RuntimeException e) {
			LOGGER.error("Error registering a generator", e);
		}
	}
	
	@PreDestroy
	public void deInit() {
		generatorRepo.list().stream().forEach(vorto::deregister);
	}

	@Override
	public void setEnvironment(Environment env) {
		this.env = env;
	}
}
