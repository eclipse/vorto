package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class GeneratorRunnerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'GeneratorRunner.java'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner/src/main/java/com/mycompany/runner'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.mycompany.runner;
		
		import org.springframework.boot.SpringApplication;
		import org.springframework.boot.autoconfigure.SpringBootApplication;
		import org.springframework.context.annotation.Bean;
		import org.springframework.context.annotation.ComponentScan;
		import org.springframework.context.annotation.FilterType;
		import org.springframework.web.client.RestTemplate;
		
		@SpringBootApplication
		@ComponentScan(basePackages={"org.eclipse.vorto.codegen.spi.config",
									"org.eclipse.vorto.codegen.spi.controllers",
									"org.eclipse.vorto.codegen.spi.repository",
									"org.eclipse.vorto.codegen.spi.service","com.mycompany.runner"},excludeFilters = {@ComponentScan.Filter(
			    type = FilterType.ASSIGNABLE_TYPE)})
		public class GeneratorRunner {
		
			@Bean
			public RestTemplate restTemplate() {
				return new RestTemplate();
			}
			
			public static void main(String[] args) {
				SpringApplication.run(GeneratorRunner.class, args);
			}
		}
		'''
	}
	
}