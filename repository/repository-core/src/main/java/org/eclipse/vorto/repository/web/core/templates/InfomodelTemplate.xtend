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
package org.eclipse.vorto.repository.web.core.templates

import java.util.HashSet
import java.util.List
import org.eclipse.vorto.model.ModelId
import org.eclipse.vorto.model.ModelProperty

class InfomodelTemplate {

	def String createModelTemplate(ModelId modelId, List<ModelProperty> properties) {
		'''
			vortolang 1.0
			namespace «modelId.namespace»
			version «modelId.version»
			displayname "«modelId.name»"
			description "Information Model for «modelId.name»"
			
			«FOR fb : extractReferencedIds(properties)»
				using «fb.namespace».«fb.name»; «fb.version»
			«ENDFOR»
				
			infomodel «modelId.name» {
			
				functionblocks {
					«FOR property : properties»
						«property.name» as «(property.type as ModelId).name»
					«ENDFOR»
				}
			}
		'''
	}

	private def extractReferencedIds(List<ModelProperty> properties) {
		var result = new HashSet<ModelId>();
		for (ModelProperty p : properties) {
			result.add(p.type as ModelId);
		}
		return result;
	}
}
