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
import org.eclipse.vorto.core.api.model.datatype.Presence
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.mapping.Attribute
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget
import org.eclipse.vorto.core.model.IMapping

class LWM2MObjectTypeCompleteTemplate implements ITemplate<InformationModel> {
	IMapping mapping = null;

	new(IMapping mapping) {
		this.mapping = mapping;
	}

	def getMapping() {
		return this.mapping;
	}

	override getContent(InformationModel context) {
		var objectTypeStereoType = this.mapping.getRulesByStereoType("ObjectType").get(0);
		var objectTypeTarget = objectTypeStereoType.target as StereoTypeTarget;
		var typeAttribute = this.getAttributeWithName(objectTypeTarget, "Type");
		var multipleInstancesAttribute = this.getAttributeWithName(objectTypeTarget, "MultipleInstances");
		var mandatoryAttribute = this.getAttributeWithName(objectTypeTarget, "Mandatory");

		return '''		
<LWM2M xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://openmobilealliance.org/tech/profiles/LWM2M.xsd" >
  <Object ObjectType="«typeAttribute.value»">
		<Name>«context.name»</Name>
		<MultipleInstances>«multipleInstancesAttribute.value»</MultipleInstances>
		<Mandatory>«mandatoryAttribute.value»</Mandatory>
		<Description>«context.description»</Description>
		«printFunctionBlockContents(context)»
	</Object>
</LWM2M>
'''
	}
	
	def String printFunctionBlockContents(InformationModel context) {
		var functionBlockModel = context.properties.get(0).type;
		return '''
	<Resources>
	«FOR property : functionBlockModel.functionblock.status.properties»				   
	«var  mappingRules = this.mapping.getRulesByModelObject(property)»
	«IF mappingRules.size>0»
		«var  mappingRule = this.mapping.getRulesByModelObject(property).get(0)»
		«var  stereoType= mappingRule.target as StereoTypeTarget»
	    «printPropertyStereoType(property, stereoType)»
    «ENDIF»		   
	«ENDFOR»
	</Resources>	
		'''
	}

	def String printPropertyStereoType(Property property, StereoTypeTarget stereoType) {
		return '''
	<Item>
		<Name>«stereoType.name»</Name>
		<Operations>«getAttributeValueSafely(stereoType, "Operations")»</Operations>
		<MultipleInstances>«toMultiplicityString(property.multiplicity)»</MultipleInstances>
		<Mandatory>«toPresentString(property.presence)»</Mandatory>
		<Type>«getAttributeValueSafely(stereoType, "Type")»</Type>
		<RangeEnumeration></RangeEnumeration>
		<Units>«getAttributeValueSafely(stereoType, "Units")»</Units>
		<Description><![CDATA[«property.description»]]></Description>
	</Item>
	'''
	}

	def String toPresentString(Presence presence) {
		if (presence.isMandatory) {
			return "Mandatory";
		} else {
			return "Optional";
		}
	}

	def String toMultiplicityString(boolean isMultiple) {
		if (isMultiple) {
			return "Multiple";
		} else {
			return "Single";
		}
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