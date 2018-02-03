/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.service.mapping.internal.serializer

import java.util.List
import java.util.Map
import java.util.stream.Collectors
import org.eclipse.vorto.repository.api.content.FunctionblockModel
import org.eclipse.vorto.repository.api.content.Stereotype
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification

/**
 * Creates a Functionblock Model Payload Mapping DSL File 
 */
class FunctionblockMappingSerializer extends AbstractSerializer {
	
	private String propertyName
	private FunctionblockModel fbm;
	
	new (IMappingSpecification spec, String propertyName) {
		super(spec);
		this.propertyName = propertyName;
		this.fbm = spec.getFunctionBlock(propertyName);
	}
	
	def override String serialize() {
		'''
		namespace «specification.infoModel.id.namespace».mapping
		version 1.0.0
		displayname "«propertyName»PayloadMapping"
		description "Payload Mapping for the «propertyName» property of the «specification.infoModel.displayName»"
		category payloadmapping
		
		using «fbm.id.prettyFormat.replace(":",";")»
		
		functionblockmapping «propertyName.toFirstUpper»PayloadMapping {
			targetplatform «createTargetPlatformKey()»
			«IF specification.getFunctionBlock(propertyName).getStereotype("functions").present && !specification.getFunctionBlock(propertyName).getStereotype("functions").get().attributes.isEmpty»
				from «fbm.id.name» to functions with {«createFunctions(specification.getFunctionBlock(propertyName).getStereotype("functions").get)»}
			«ENDIF»
			«FOR statusProperty : fbm.statusProperties»
				«FOR stereotype : filterEmptyStereotypes(statusProperty.stereotypes)»
				from «fbm.id.name».status.«statusProperty.name» to «stereotype.name» with {«createContent(stereotype.attributes)»}
				«ENDFOR»		
			«ENDFOR»
			«FOR configProperty : fbm.configurationProperties»
				«FOR stereotype : filterEmptyStereotypes(configProperty.stereotypes)»
				from «fbm.id.name».configuration.«configProperty.name» to «stereotype.name» with {«createContent(stereotype.attributes)»}
				«ENDFOR»		
			«ENDFOR»
		}
		'''
	}
	
	private def String createFunctions(Stereotype functionsStereotype) {
		var content = new StringBuilder();
		for (var iter = functionsStereotype.attributes.keySet.iterator;iter.hasNext;) {
			var key = iter.next;
			if (!"_namespace".equals(key)) {
				content.append(key).append(":").append("\""+functionsStereotype.attributes.get(key)+"\"");
				if (iter.hasNext) {
					content.append(",");
				}
			}
		}
		return content.toString;
	}
	
	private def filterEmptyStereotypes(List<Stereotype> stereotypes) {
		return stereotypes.stream.filter[attributes.containsKey("xpath") && !attributes.get("xpath").equals("")].collect(Collectors.toList);
	}
	
	private def String createContent(Map<String,String> attributes) {
		var content = new StringBuilder();
		for (var iter = attributes.keySet.iterator;iter.hasNext;) {
			var key = iter.next;
			content.append(key).append(":").append("\""+attributes.get(key)+"\"");
			if (iter.hasNext) {
				content.append(",");
			}
		}
		return content.toString;
	}
	
	
}
