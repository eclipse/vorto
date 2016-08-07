package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.examples.webui.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class ApplicationMainTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		return "Application.java";
	}
	
	override getPath(InformationModel context) {
		return "webdevice.example/src/main/java/webdevice/example"
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
		package webdevice.example;
		
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.boot.SpringApplication;
		import org.springframework.boot.autoconfigure.SpringBootApplication;
		import org.springframework.boot.builder.SpringApplicationBuilder;
		import org.springframework.boot.context.web.SpringBootServletInitializer;
		import org.springframework.context.annotation.Bean;
		import org.springframework.context.annotation.Import;
		import org.springframework.messaging.simp.SimpMessagingTemplate;
		import org.springframework.scheduling.annotation.EnableScheduling;
		import org.springframework.scheduling.annotation.Scheduled;
		import org.springframework.stereotype.Service;
				
		import model.functionblocks.*;
		
		@SpringBootApplication
		@EnableScheduling
		public class Application extends SpringBootServletInitializer {
		
			@Autowired
			private SimpMessagingTemplate template;
			
			public static void main(String[] args) {
				SpringApplication.run(Application.class, args);
			}
			
			@Override
			protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
				return application.sources(Application.class);
			}
			
			/* Uncomment if you have generated MQTT client code with the Vorto MQTT Code Generator
			@Bean
			public IClientHandler myClientHandler() {
				return new IClientHandler() {
			
					@Override
					public void onDeviceStatusReceived(Object statusEvent) {
						«FOR fbm : ModuleUtil.getFunctionBlocksUsingStatus(context)»
						if (statusEvent instanceof «fbm.name»Status) {
							template.convertAndSend("/topic/«fbm.name»Status", statusEvent);
						}
						«ENDFOR»
					}
				};
			}
			*/
			
			@Service
			public static class ScheduleTask {
			
				@Autowired
				private SimpMessagingTemplate template;
			
				// this sends deviceInfo status to websocket subscribers
				@Scheduled(fixedRate = 5000)
				public void sendDeviceStatusEvents() {
					// sends the message to /topic/message
					«FOR fbm : ModuleUtil.getFunctionBlocksUsingStatus(context)»
					this.template.convertAndSend("/topic/«fbm.name»Status", new «fbm.name»Status()); //TODO: create instances with data
					«ENDFOR»
					
				}
			
			}
		}
		'''
	}
	
}
