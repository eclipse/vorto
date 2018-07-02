/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.importer.ipso

import org.eclipse.vorto.repository.api.ModelInfo

class MappingTemplate {
	
	def String create(LWM2M.Object source, ModelInfo modelInfo) {
		'''
		namespace «modelInfo.id.namespace»
		version «modelInfo.id.version»
		displayname "«modelInfo.displayName»"
		description "LWM2M Mapping Model for «modelInfo.displayName»"
		
		using «modelInfo.id.namespace».«modelInfo.id.name»;«modelInfo.id.version»
		
		functionblockmapping «modelInfo.id.name»_lwm2m {
			targetplatform lwm2m
			
			from «modelInfo.id.name» to Object with {Name: "«source.name»", ObjectID: "«source.objectID»", ObjectURL: "«source.objectURN»", MultipleInstances: "«source.multipleInstances»", Mandatory: "«source.mandatory»", Description2: "«source.description2.replace("\"","'")»"}
			
			«FOR item : source.getResources.item»
				«IF item.operations == "RW" || item.operations == "W"»
					from «modelInfo.id.name».configuration.«parseName(item.name).toFirstLower» to Resource with {ID: "«item.id»", Name: "«item.name»", Units: "«item.units.replace("\"","'")»"}
				«ENDIF»
			«ENDFOR»
			
			«FOR item : source.getResources.item»
				«IF item.operations == "R"»
					from «modelInfo.id.name».status.«parseName(item.name).toFirstLower» to Resource with {ID: "«item.id»", Name: "«item.name»", Units: "«item.units.replace("\"","'")»"}
				«ENDIF»
			«ENDFOR»
			
			«FOR item : source.getResources.item»
				«IF item.operations == "E"»
					from «modelInfo.id.name».operation.«parseName(item.name).toFirstLower» to Resource with {ID: "«item.id»", Name: "«item.name»", MultipleInstances: "«item.multipleInstances»", Mandatory: "«item.mandatory»"}
				«ENDIF»
			«ENDFOR»
		}
		'''
	}
	
	def parseName(String name) {
		return name.replace(" ","").replace("/","").replace("-","")
	}
	
}