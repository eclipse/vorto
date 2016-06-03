package org.eclipse.vorto.codegen.ui.wizard.generation.templates.server

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class PlatformGeneratorMainTemplate implements IFileTemplate<IGeneratorProjectContext> {
	
	override getFileName(IGeneratorProjectContext context) {
		return context.generatorName+"GeneratorMicroService.java"
	}
	
	override getPath(IGeneratorProjectContext context) {
		return "src/main/java/"+context.packageFolders+"/"+context.generatorName.toLowerCase+"/service"
	}
	
	override getContent(IGeneratorProjectContext context,InvocationContext invocationContext) {
		'''
		package «context.packageName».«context.generatorName.toLowerCase».service;
		
		import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
		import «context.packageName».«context.generatorName»;
		import org.eclipse.vorto.service.generator.web.AbstractBackendCodeGenerator;
		import org.springframework.boot.SpringApplication;
		import org.springframework.boot.autoconfigure.SpringBootApplication;
		import org.springframework.context.annotation.Bean;
		
		@SpringBootApplication
		public class «context.generatorName»GeneratorMicroService extends AbstractBackendCodeGenerator {
		
			@Bean
			public IVortoCodeGenerator «context.generatorName.toLowerCase»() {
				return new «context.generatorName»();
			}
			
			public static void main(String[] args) {
				SpringApplication.run(«context.generatorName»GeneratorMicroService.class, args);
			}
		}
		'''
	}
	
}