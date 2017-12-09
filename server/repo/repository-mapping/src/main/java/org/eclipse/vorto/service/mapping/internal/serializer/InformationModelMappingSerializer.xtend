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

import java.util.HashSet
import org.eclipse.vorto.repository.api.ModelId
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification

/**
 * Creates a Information Model Payload Mapping DSL File 
 */
class InformationModelMappingSerializer extends AbstractSerializer {
	
	new (IMappingSpecification spec) {
		super(spec)
	}
	
	def override String serialize() {
		'''
		namespace «specification.infoModel.id.namespace».mapping
		version 1.0.0
		displayname «specification.infoModel.id.name»PayloadMapping
		description "Payload Mapping Specification for «specification.infoModel.displayName»"
		category payloadmapping
		
		using «specification.infoModel.id.prettyFormat.replace(":",";")»
		«var imports = new HashSet »
		«FOR fbProperty : specification.infoModel.functionblocks»
			«var done = imports.add("using " + (fbProperty.type as ModelId).namespace+".mapping"+"."+specification.infoModel.id.name+fbProperty.name+"PayloadMapping"+";"+(fbProperty.type as ModelId).version)»
		«ENDFOR»
		«FOR using : imports»
		«using»
		«ENDFOR»
		
		infomodelmapping «specification.infoModel.id.name»PayloadMapping {
			targetplatform «createTargetPlatformKey()»
			«FOR fbProperty : specification.infoModel.functionblocks»
			from «specification.infoModel.id.name».functionblocks.«fbProperty.name» to reference «(fbProperty.type as ModelId).name+"PayloadMapping"»
			«ENDFOR»
		}
		'''
	}
}
