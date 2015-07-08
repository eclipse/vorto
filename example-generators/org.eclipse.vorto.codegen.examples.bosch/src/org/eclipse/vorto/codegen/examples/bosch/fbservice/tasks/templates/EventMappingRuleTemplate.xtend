/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates

import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.functionblock.Event

class EventMappingRuleTemplate {
	
	
		
	public static def String generate(FbModelWrapper context,Event event) {
		'''
		package «context.javaPackageName».internal.mapping;
		
		import org.osgi.service.event.Event;
		import org.w3c.dom.Document;
		import org.w3c.dom.Element;

		import «getPackage(context)»«context.model.name»Properties;
		
		import com.bosch.ism.ICustomEvent;
		import com.bosch.ism.ICustomEventBuilder;
		import «context.javaPackageName».api.mapping.AbstractEventMapping;
		import «context.javaPackageName».api.mapping.EventMappingContext;
		
		public class «event.name»Mapping extends AbstractEventMapping<«context.model.name»Properties> {
		
			@Override
			public ICustomEvent map(Event baseDriverEvent, EventMappingContext<«context.model.name»Properties> context) {
				ICustomEventBuilder eventBuilder = customEventBuilderFactory.create(context.getInformationModelInstance().getId());
		
				Document document = createDocument();
				Element payload = document.createElement("Payload");
		
				for (String propertyName : baseDriverEvent.getPropertyNames()) {
					Element propertyElement = document.createElement(propertyName);
					propertyElement.setTextContent(baseDriverEvent.getProperty(propertyName).toString());
					payload.appendChild(propertyElement);
				}
		
				eventBuilder.setTopic("CBE/«context.model.name»/«event.name»");		
				return eventBuilder.setPayload(payload).build();
			}
		}
		
		'''
	}
	
	def static getPackage(FbModelWrapper context) {
		context.javaPackageName.toString() + '''._«context.majorVersion».''';
	}
}