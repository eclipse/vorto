package com.bosch.iotsuite.console.generator.application.templates.config

import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class WebSocketConfigTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''WebSocketConfig.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/config'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».config;
		
		import org.springframework.context.annotation.Configuration;
		import org.springframework.messaging.simp.config.MessageBrokerRegistry;
		import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
		import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
		import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
		
		@Configuration
		@EnableWebSocketMessageBroker
		public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
		
			public static final String ENDPOINT = "/«element.name.toLowerCase»endpoint";
			
			@Override
			public void registerStompEndpoints(StompEndpointRegistry registry)  {
				registry.addEndpoint(ENDPOINT).withSockJS();
			}
		
			@Override
			public void configureMessageBroker(MessageBrokerRegistry registry) {
				registry.setApplicationDestinationPrefixes("/«element.name.toLowerCase»");
				registry.enableSimpleBroker("/topic/device");
			}
		
		}
		'''
	}
}