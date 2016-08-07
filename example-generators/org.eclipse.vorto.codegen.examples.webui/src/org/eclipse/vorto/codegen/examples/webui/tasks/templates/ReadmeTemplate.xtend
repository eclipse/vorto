package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class ReadmeTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		return "README.md";
	}
	
	override getPath(InformationModel context) {
		return "webdevice.example"
	}
	
	override getContent(InformationModel context,InvocationContext invocationContext) {
		'''
		# Getting Started
		
		The Web Device Generator automatically generates AngularJS binding code to UI elements, specific for functionblocks. It also schedules a Timer task to simulate events passed to the Web Sockets of your app. Feel free to create Dummy instance data in the Application.java file.
		
		## Consume MQTT events in the web application
		
		The web device application can easily be "glued" together with MQTT event subscriptions for a device. All you need to do is follow the next 5 steps:
		
		### 1. Invoke Vorto MQTT Generator 
		### 2. Add the generated MQTT Client Bundle in the pom.xml
		
			<dependency>
				<groupId>mqtt.example</groupId>
				<artifactId>mqtt.example.client</artifactId>
				<version>1.0.0-SNAPSHOT</version>
			</dependency>
		
		### 3. Add MQTT specific server and topic configuration to the application.yml
		
		For detailed information about the key of the topic, take a look at the generated MQTT Configuration for the specific functionblock.
		
		Example configuration for a functionblock 'LightSensor':
		
			mqtt:
		  	  connectionUrl: tcp://localhost:1883
		  	topic:
		      lightsensor: mytopic
		
		### 4. Reference the MQTT Configuration from your Spring Boot Application
		
		Find the Spring Boot Application main class (Application.java) and add the Spring Boot Configuration class, LightSensor in this example:
		
			@Import(MqttConfigurationLightSensor.class)
			public class Application extends SpringBootServletInitializer {
			...
		
		### 5. Uncomment the ClientHandler Subscription to the MQTT event in the Application.java
		'''
	}
	
}
