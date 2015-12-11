/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.tutorial.tasks.templates

import org.apache.commons.lang3.StringUtils
import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.mapping.Attribute
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget
import org.eclipse.vorto.core.model.IMapping

class LWM2MResourceEntityTemplate implements ITemplate<InformationModel> {
	IMapping mapping = null;

	new(IMapping mapping) {
		this.mapping = mapping;
	}

	def getMapping() {
		return this.mapping;
	}

	override getContent(InformationModel context) {
		var resourceType = this.mapping.getRulesByStereoType("ResourceType").get(0).target as StereoTypeTarget;
		var nameAttribute = this.getAttributeWithName(resourceType, "Name");

		var resourceMappingRules = this.mapping.getRulesByStereoType("Resource");

		return '''
<Resource name="«nameAttribute.value»">
	«FOR mappingRule : resourceMappingRules»
		«printContentForStereoTypeTarget(mappingRule.target as StereoTypeTarget)»
	«ENDFOR»
</Resource>	
		'''
	}

	def String printContentForStereoTypeTarget(StereoTypeTarget resourceStereoType) {
		return '''
	<Entity>
		<Name>«getAttributeValueSafely(resourceStereoType, "Type")»</Name>
		<Units>«getAttributeValueSafely(resourceStereoType, "Units")»</Units>
	</Entity>		
		''';
	}

	/**
	 * Returns Attribute value for given stereoType and attributeName, 
	 * or null if attribute with given attributeName not found
	 * 
	 */
	def private String getAttributeValueSafely(StereoTypeTarget stereoType, String attributeName) {
		var attribute = this.getAttributeWithName(stereoType, attributeName);
		if (attribute == null) {
			return "";
		} else {
			return attribute.value;
		}
	}

	def private Attribute getAttributeWithName(StereoTypeTarget stereoType, String attributeName) {
		for (Attribute attribute : stereoType.attributes) {
			if (StringUtils.equals(attribute.name, attributeName)) {
				return attribute;
			}
		}
		return null;
	}
}