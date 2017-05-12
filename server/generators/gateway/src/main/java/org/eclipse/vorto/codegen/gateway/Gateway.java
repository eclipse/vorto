package org.eclipse.vorto.codegen.gateway;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.vorto.codegen.examples.aws.AWSGenerator;
import org.eclipse.vorto.codegen.examples.bosch.things.BoschIoTThingsGenerator;
import org.eclipse.vorto.codegen.examples.coap.CoAPGenerator;
import org.eclipse.vorto.codegen.examples.ios.IOSPlatformGenerator;
import org.eclipse.vorto.codegen.examples.javabean.JavabeanGenerator;
import org.eclipse.vorto.codegen.examples.latex.LatexGenerator;
import org.eclipse.vorto.codegen.examples.lwm2m.LWM2MGenerator;
import org.eclipse.vorto.codegen.examples.markdown.MarkdownGenerator;
import org.eclipse.vorto.codegen.examples.mqtt.MQTTPlatformGenerator;
import org.eclipse.vorto.codegen.examples.thingworx.ThingWorxCodeGenerator;
import org.eclipse.vorto.codegen.examples.webui.WebUIGenerator;
import org.eclipse.vorto.codegen.gateway.model.Generator;
import org.eclipse.vorto.codegen.gateway.repository.GeneratorRepository;
import org.eclipse.vorto.codegen.gateway.service.VortoService;
import org.eclipse.vorto.codegen.prosystfi.ProSystGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Gateway {

	private static final Logger LOGGER = LoggerFactory.getLogger(Gateway.class);
	
	@Autowired
	private GeneratorRepository generatorRepo;
	
	@Autowired
	private VortoService vorto;
	
	@PostConstruct
	public void init() {
		try {
			generatorRepo.add(Generator.create("/generators/aws.properties", AWSGenerator.class));
			generatorRepo.add(Generator.create("/generators/bosch-things.properties", BoschIoTThingsGenerator.class));
			generatorRepo.add(Generator.create("/generators/coap.properties", CoAPGenerator.class));
			generatorRepo.add(Generator.create("/generators/ios.properties", IOSPlatformGenerator.class));
			generatorRepo.add(Generator.create("/generators/javabean.properties", JavabeanGenerator.class));
			generatorRepo.add(Generator.create("/generators/latex.properties", LatexGenerator.class));
			generatorRepo.add(Generator.create("/generators/lwm2m.properties", LWM2MGenerator.class));
			generatorRepo.add(Generator.create("/generators/markdown.properties", MarkdownGenerator.class));
			generatorRepo.add(Generator.create("/generators/mqtt.properties", MQTTPlatformGenerator.class));
			generatorRepo.add(Generator.create("/generators/prosystfi.properties", ProSystGenerator.class));
			generatorRepo.add(Generator.create("/generators/thingworx.properties", ThingWorxCodeGenerator.class));
			generatorRepo.add(Generator.create("/generators/webui.properties", WebUIGenerator.class));
			
			generatorRepo.list().stream().forEach(vorto::register);
			
		} catch(RuntimeException e) {
			LOGGER.error("Error registering a generator", e);
		}
	}
	
	@PreDestroy
	public void deInit() {
		generatorRepo.list().stream().forEach(vorto::deregister);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Gateway.class, args);
	}
}
