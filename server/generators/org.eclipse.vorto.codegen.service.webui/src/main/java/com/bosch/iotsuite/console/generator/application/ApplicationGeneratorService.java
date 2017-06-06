package com.bosch.iotsuite.console.generator.application;

import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.service.generator.web.AbstractBackendCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApplicationGeneratorService extends AbstractBackendCodeGenerator {

	@Bean
	public IVortoCodeGenerator webuiGenerator() {
		return new ApplicationGenerator();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ApplicationGeneratorService.class, args);
	}
}
