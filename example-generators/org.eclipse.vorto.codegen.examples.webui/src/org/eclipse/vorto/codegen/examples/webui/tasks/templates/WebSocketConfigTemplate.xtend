package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class WebSocketConfigTemplate implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		return "WebSocketConfig.java"
	}
	
	override getPath(InformationModel context) {
		return "webdevice.example/src/main/java/webdevice/example/config"
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
		package webdevice.example.config;
		
		import org.springframework.context.annotation.Configuration;
		import org.springframework.messaging.simp.config.MessageBrokerRegistry;
		import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
		import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
		import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
		
		@Configuration
		@EnableWebSocketMessageBroker
		public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
		
			@Override
			public void registerStompEndpoints(StompEndpointRegistry registry) {
				registry.addEndpoint("/fb").withSockJS();
			}
		
			@Override
			public void configureMessageBroker(MessageBrokerRegistry registry) {
				registry.setApplicationDestinationPrefixes("/webdevice");
				registry.enableSimpleBroker("/topic");
			}
		
		}
		'''
	}
	
}
