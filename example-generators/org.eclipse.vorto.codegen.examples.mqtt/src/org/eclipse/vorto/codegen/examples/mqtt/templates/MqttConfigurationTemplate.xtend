package org.eclipse.vorto.codegen.examples.mqtt.templates

import java.util.UUID
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class MqttConfigurationTemplate implements IFileTemplate<FunctionblockModel> {

	override getFileName(FunctionblockModel context) {
		return '''MqttConfiguration«context.name».java'''
	}
	
	override getPath(FunctionblockModel context) {
		return "mqtt.example.client/src/main/java/mqtt/example/config"
	}
	
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
		package mqtt.example.config;
		
		import java.io.IOException;
		
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.beans.factory.annotation.Value;
		import org.springframework.context.annotation.Bean;
		import org.springframework.context.annotation.Configuration;
		import org.springframework.integration.annotation.ServiceActivator;
		import org.springframework.integration.channel.DirectChannel;
		import org.springframework.integration.core.MessageProducer;
		import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
		import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
		import org.springframework.messaging.Message;
		import org.springframework.messaging.MessageChannel;
		import org.springframework.messaging.MessageHandler;
		import org.springframework.messaging.MessagingException;
		
		import com.fasterxml.jackson.databind.ObjectMapper;
		
		import mqtt.example.*;
		import model.functionblocks.*;
		
		@Configuration
		public class MqttConfiguration«context.name» {
			
			@Value("${mqtt.connectionUrl}")
			private String connectionUrl;
			
			@Value("${mqtt.topic.«context.name.toLowerCase»}")
			private String topicName;
			
			@Autowired
			private IClientHandler clientHandler;
			
			@Bean
		    public MessageChannel mqttInputChannel«context.name»() {
		    	return new DirectChannel();
		    }
			
		    @Bean
		    public MessageProducer inbound«context.name»() {
		    	MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(this.connectionUrl, "client«context.name»_«UUID.randomUUID.toString»");
		    	adapter.addTopic(this.topicName);
		    	adapter.setCompletionTimeout(5000);
		    	adapter.setConverter(new DefaultPahoMessageConverter());
		    	adapter.setQos(1);
		    	adapter.setOutputChannel(mqttInputChannel«context.name»());
		    	return adapter;
		    }
		    
		    @Bean
		    @ServiceActivator(inputChannel = "mqttInputChannel«context.name»")
		    public MessageHandler handler«context.name»() {
		    	return new MessageHandler() {
		
		    		@Override
		    		public void handleMessage(Message<?> message) throws MessagingException {
		    				
						ObjectMapper mapper = new ObjectMapper();
						«context.name»Status status = null;
						
						try {
							status = mapper.readValue(message.getPayload().toString(), «context.name»Status.class);
							clientHandler.onDeviceStatusReceived(status);
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		}
		    	};
		    }
		}
		'''
	}
}