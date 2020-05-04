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

import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.stream.Collectors
import org.apache.commons.text.StringEscapeUtils
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification
import org.eclipse.vorto.model.ModelId
import org.eclipse.vorto.model.Stereotype

/**
 * Creates a Information Model Payload Mapping DSL File 
 */
class InformationModelMappingSerializer extends AbstractSerializer {
	
	new (IMappingSpecification spec, ModelId modelId, String targetPlatform) {
		super(spec,modelId, targetPlatform)
	}
	
	override String serialize() {
		'''
		vortolang 1.0
		
		namespace «modelId.namespace»
		version «modelId.version»
		displayname "«specification.infoModel.id.name» Payload Mapping"
		description "Payload Mapping for «specification.infoModel.id.name»"
		category payloadmapping
		
		using «specification.infoModel.id.namespace».«specification.infoModel.id.name»;«specification.infoModel.id.version»
		«var imports = new HashSet »
		«FOR fbProperty : specification.infoModel.functionblocks»
			«var status = imports.add(MappingIdUtils.getIdForProperty(modelId,fbProperty))»
		«ENDFOR»
		«FOR importId : imports»
		using «importId.namespace».«importId.name»;«importId.version»
		«ENDFOR»
		
		infomodelmapping «modelId.name» {
			targetplatform «targetPlatform»
			«FOR stereotype : filterEmptyStereotypes(specification.infoModel.stereotypes)»
			from «specification.infoModel.id.name» to «stereotype.name» with {«createContent(stereotype.attributes)»}
			«ENDFOR»
			«FOR fbProperty : specification.infoModel.functionblocks»
			from «specification.infoModel.id.name».functionblocks.«fbProperty.name» to reference «fbProperty.name.toFirstUpper+"PayloadMapping"»
			«ENDFOR»
		}
		'''
	}
	
	private def filterEmptyStereotypes(List<Stereotype> stereotypes) {
		return stereotypes.stream.filter[!attributes.isEmpty].collect(Collectors.toList);
	}
	
	private def String createContent(Map<String,String> attributes) {
		var content = new StringBuilder();
		for (var iter = attributes.keySet.iterator;iter.hasNext;) {
			var key = iter.next;
			content.append(key).append(":").append("\""+escapeQuotes(attributes.get(key))+"\"");
			if (iter.hasNext) {
				content.append(",");
			}
		}
		return content.toString;
	}
	
	def escapeQuotes(String value) {
		return StringEscapeUtils.escapeJava(value)
	}
}
