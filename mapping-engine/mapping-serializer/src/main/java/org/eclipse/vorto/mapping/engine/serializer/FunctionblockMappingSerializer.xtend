/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.mapping.engine.serializer

import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.stream.Collectors
import org.apache.commons.text.StringEscapeUtils
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification
import org.eclipse.vorto.model.EntityModel
import org.eclipse.vorto.model.FunctionblockModel
import org.eclipse.vorto.model.ModelId
import org.eclipse.vorto.model.ModelProperty
import org.eclipse.vorto.model.Stereotype

/**
 * Creates a Functionblock Model Payload Mapping DSL File 
 */
class FunctionblockMappingSerializer extends AbstractSerializer {
	
	String propertyName
	FunctionblockModel fbm;
	
	new (IMappingSpecification spec, ModelId modelId, String targetPlaform, String propertyName) {
		super(spec,modelId,targetPlaform);
		this.propertyName = propertyName;
		this.fbm = spec.getFunctionBlock(propertyName);
	}
	
	override String serialize() {
		'''
		vortolang 1.0
		
		namespace «modelId.namespace»
		version «modelId.version»
		displayname "«propertyName.toFirstUpper» Payload Mapping"
		description "Maps the «propertyName.toFirstUpper» payload of the «specification.infoModel.id.prettyFormat»"
		category payloadmapping
		
		using «fbm.id.namespace».«fbm.id.name»;«fbm.id.version»
		
		«var imports = new HashSet »
		«FOR property : fbm.properties»
		«IF isEntityProperty(property)»
		«var status = imports.add(MappingIdUtils.getIdForProperty(modelId,property))»
		«ENDIF»
		«ENDFOR»
		«FOR using : imports»
		using «using.namespace».«using.name»;«using.version»
		«ENDFOR»
		
		functionblockmapping «modelId.name» {
			targetplatform «targetPlatform»
			«IF specification.getFunctionBlock(propertyName).getStereotype("functions").present && !specification.getFunctionBlock(propertyName).getStereotype("functions").get().attributes.isEmpty»
				from «fbm.id.name» to functions with {«createFunctions(specification.getFunctionBlock(propertyName).getStereotype("functions").get)»}
			«ENDIF»
			«IF specification.getFunctionBlock(propertyName).getStereotype("condition").present && !specification.getFunctionBlock(propertyName).getStereotype("condition").get().attributes.isEmpty»
				from «fbm.id.name» to condition with {«createCondition(specification.getFunctionBlock(propertyName).getStereotype("condition").get)»}
			«ENDIF»
			«FOR stereotype : specification.getFunctionBlock(propertyName).stereotypes»
				«IF !stereotype.name.equalsIgnoreCase("functions") && !stereotype.name.equalsIgnoreCase("condition") »
				from «fbm.id.name» to «stereotype.name» with {«createContent(stereotype.attributes)»}
				«ENDIF»
			«ENDFOR»
			«FOR statusProperty : fbm.statusProperties»
				«IF isEntityProperty(statusProperty)»
				from «fbm.id.name».status.«checkIfKeyword(statusProperty.name)» to reference «statusProperty.name.toFirstUpper+"PayloadMapping"»
				«ELSE»
				«FOR stereotype : filterEmptyStereotypes(statusProperty.stereotypes)»
				from «fbm.id.name».status.«checkIfKeyword(statusProperty.name)» to «stereotype.name» with {«createContent(stereotype.attributes)»}
				«ENDFOR»
				«ENDIF»		
			«ENDFOR»
			«FOR configProperty : fbm.configurationProperties»
				«IF isEntityProperty(configProperty)»
				from «fbm.id.name».configuration.«checkIfKeyword(configProperty.name)» to reference «configProperty.name.toFirstUpper+"PayloadMapping"»
				«ELSE»
				«FOR stereotype : filterEmptyStereotypes(configProperty.stereotypes)»
				from «fbm.id.name».configuration.«checkIfKeyword(configProperty.name)» to «stereotype.name» with {«createContent(stereotype.attributes)»}
				«ENDFOR»
				«ENDIF»		
			«ENDFOR»
			«FOR operation : fbm.operations»
				«FOR stereotype : filterEmptyStereotypes(operation.stereotypes)»
				from «fbm.id.name».operations.«operation.name» to «stereotype.name» with {«createContent(stereotype.attributes)»}
				«ENDFOR»		
			«ENDFOR»
		}
		'''
	}
		
	def boolean isEntityProperty(ModelProperty property) {
		return property.type instanceof EntityModel
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
	
	private def String createCondition(Stereotype conditionStereotype) {
		var content = new StringBuilder();
		content.append("value").append(":").append("\""+escapeQuotes(conditionStereotype.attributes.get("value"))+"\"");
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
}
