package com.bosch.iotsuite.console.generator.application.templates.web

import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingMessageControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»MessageController.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/web'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».web;
		
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.messaging.handler.annotation.MessageMapping;
		import org.springframework.messaging.simp.SimpMessagingTemplate;
		import org.springframework.stereotype.Controller;
		
		import com.bosch.cr.integration.things.ThingIntegration;
		import com.bosch.cr.model.things.Thing;
		import com.example.iot.«element.name.toLowerCase».config.WebSocketConfig;
		
		@Controller
		public class «element.name»MessageController {
			
			private static final String SUBSCRIBER_PREFIX = "/«element.name.toFirstLower»/";
		
			private Logger log = LoggerFactory.getLogger(«element.name»MessageController.class);
			
			@Autowired
		    private SimpMessagingTemplate messagingTemplate;
			
			@Autowired
			private ThingIntegration thingsIntegration;
			
			@MessageMapping(WebSocketConfig.ENDPOINT+"/subscribe")
			public String subscribe(String thingId) throws Exception {
				try {
					log.info("Subscribing for thingId : " + thingId);
					this.thingsIntegration.forId(thingId).registerForFeatureChanges(getSubscriberId(thingId), featureChange -> {
						try {
								Thing crThing = thingsIntegration.forId(thingId).retrieve().get();
								com.bosch.iotsuite.management.things.model.Thing thing = com.bosch.iotsuite.management.things.model.Thing.newBuilder().fromThing(crThing).build();
								this.messagingTemplate.convertAndSend( "/topic/device/" + thingId, Utils.thingTo«element.name».apply(thing));
						} catch(Exception e) {
							log.error("Error while retrieving thing", e);
						}
					});
				} catch (Throwable e) {
					log.error("Error while subcribing to device", e);
				}
				
				return thingId;
			}
			
			@MessageMapping(WebSocketConfig.ENDPOINT+"/unsubscribe")
			public String unsubscribe(String thingId) throws Exception {
				
				try {
					log.info("Unsubscribing for thingId : " + thingId);
					thingsIntegration.forId(thingId).deregister(getSubscriberId(thingId));
				} catch (Throwable e) {
					log.error("Error while unsubcribing to thing", e);
				}
				
				return thingId;
			}
			
			private String getSubscriberId(String thingId) {
				return SUBSCRIBER_PREFIX + thingId;
			}
		}
		'''
	}
	
}