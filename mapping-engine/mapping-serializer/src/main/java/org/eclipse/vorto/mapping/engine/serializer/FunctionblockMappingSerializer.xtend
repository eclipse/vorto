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
package org.eclipse.vorto.mapping.engine.serializer

import java.util.HashMap
import java.util.List
import java.util.Map
import java.util.stream.Collectors
import org.apache.commons.text.StringEscapeUtils
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification
import org.eclipse.vorto.repository.api.ModelId
import org.eclipse.vorto.repository.api.content.FunctionblockModel
import org.eclipse.vorto.repository.api.content.Stereotype

/**
 * Creates a Functionblock Model Payload Mapping DSL File 
 */
class FunctionblockMappingSerializer extends AbstractSerializer {
	
	private String propertyName
	private FunctionblockModel fbm;
	
	new (IMappingSpecification spec, String targetPlaform, String propertyName) {
		super(spec,targetPlaform);
		this.propertyName = propertyName;
		this.fbm = spec.getFunctionBlock(propertyName);
	}
	
	def override String serialize() {
		'''
		namespace «specification.infoModel.id.namespace».mapping
		version 1.0.0
		displayname "«propertyName»PayloadMapping"
		description "«targetPlatform.toLowerCase.toFirstUpper» Payload Mapping for the «propertyName» property of the «specification.infoModel.displayName»"
		category payloadmapping
		
		using «fbm.id.namespace».«fbm.id.name»;«fbm.id.version»
		
		functionblockmapping «propertyName.toFirstUpper»PayloadMapping«targetPlatform.toLowerCase.toFirstUpper» {
			targetplatform «targetPlatform»
			«IF specification.getFunctionBlock(propertyName).getStereotype("functions").present && !specification.getFunctionBlock(propertyName).getStereotype("functions").get().attributes.isEmpty»
				from «fbm.id.name» to functions with {«createFunctions(specification.getFunctionBlock(propertyName).getStereotype("functions").get)»}
			«ENDIF»
			«FOR stereotype : specification.getFunctionBlock(propertyName).stereotypes»
				«IF !stereotype.name.equalsIgnoreCase("functions")»
				from «fbm.id.name» to «stereotype.name» with {«createContent(stereotype.attributes)»}
				«ENDIF»
			«ENDFOR»
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
			«FOR operation : fbm.operations»
				«FOR stereotype : filterEmptyStereotypes(operation.stereotypes)»
				from «fbm.id.name».operations.«operation.name» to «stereotype.name» with {«createContent(stereotype.attributes)»}
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
				content.append(key).append(":").append("\""+escapeQuotes(functionsStereotype.attributes.get(key))+"\"");
				if (iter.hasNext) {
					content.append(",");
				}
			}
		}
		return content.toString;
	}
	
	def escapeQuotes(String value) {
		return StringEscapeUtils.escapeJava(value)
	}
	
	private def filterEmptyStereotypes(List<Stereotype> stereotypes) {
		return stereotypes.stream.filter[!attributes.isEmpty].filter[!getNonEmptyAttributes(attributes).isEmpty].collect(Collectors.toList);
	}
	
	private def getNonEmptyAttributes(Map<String,String> attributes) {
		var newMap = new HashMap<String,String>()
		for (String key : attributes.keySet) {
			if (!attributes.get(key).empty) {
				newMap.put(key,attributes.get(key))
			}
		}
	    return newMap;
		
	}
	
	private def String createContent(Map<String,String> attributes) {
		var content = new StringBuilder();
		for (var iter = attributes.keySet.iterator;iter.hasNext;) {
			var key = iter.next;
			if ((key.equalsIgnoreCase("xpath") || key.equalsIgnoreCase("condition")) && attributes.get(key).equals("")) {
			} else {
				content.append(key).append(":").append("\""+escapeQuotes(attributes.get(key))+"\"");
				if (iter.hasNext) {
					content.append(",");
				}
			}
			
		}
		return content.toString;
	}
	
	override getModelId() {
		return new ModelId(propertyName.toFirstUpper+"PayloadMapping"+targetPlatform.toLowerCase.toFirstUpper,specification.infoModel.id.namespace+".mapping","1.0.0");
	}
}
