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
package org.eclipse.vorto.codegen.service.bosch.fbmodelapi.modules.fbmodel.helper

import java.util.regex.Pattern
import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.Presence
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.Property

class PropertyElementBuilder {
	
	public def String buildProperty(Property property) {
		
		if (property.description != null || isWithConstraints(property)) {
			'''
			<xs:element «getPropertyElementHeader(property)»>
			   «IF property.description != null»
			   <xs:annotation>
			      <xs:documentation>«property.description»</xs:documentation>
			   </xs:annotation>
			   «ENDIF»
			   «IF isWithConstraints(property)»
			   <xs:simpleType>
			      <xs:restriction base="xs:«(property.type as PrimitivePropertyType).getType»">
			   	     «FOR constraint : ConstraintExceptionAdapter.getValidConstraints(property)»
			   	     «buildConstraint(constraint)»
			   	     «ENDFOR»
			   	  </xs:restriction>
			   </xs:simpleType>
			   «ENDIF»   
			</xs:element>'''
		} else {
			'''<xs:element «getPropertyElementHeader(property)»/>'''
		}

	}
	
	private def CharSequence getPropertyElementHeader(Property property) {
		if (property.type instanceof PrimitivePropertyType) {
			if (isMultipleByte(property)) {
				return specialHandlingForMultipleByte(property)
			}
		}

		'''name="«property.name»"«getElementDataType(property)» «defineOccurance(property)»'''

	}
	
	private def isMultipleByte(Property property) {
		var propType = (property.type as PrimitivePropertyType).getType.getName()
		propType == "byte" && property.multiplicity
	}
	
	private def String defineOccurance(Property property) {
		val presence = property.presence;
		'''«defineMultiplicity(property.multiplicity)» «printsMinOccur(presence)»''';
	}
	
	private def printsMinOccur(Presence presence)
		'''minOccurs="«IF presence.mandatory»1«ELSE»0«ENDIF»"'''
	
	
	private def CharSequence getElementDataType(Property property) {
		if (isWithConstraints(property)) {
			return ""
		}

		''' «getDataType(property)»'''

	}
	
	private def isWithConstraints(Property property) {
		property.constraints != null 
			&& ConstraintExceptionAdapter.returnValidConstraintLength(property) > 0
	}
	
	private def CharSequence getDataType(Property property) {
		if (property.type instanceof ObjectPropertyType) '''type="tns:«(property.type as ObjectPropertyType).getType.name»"''' else '''type="xs:«(property.type as PrimitivePropertyType).
			getType»"'''
	}
	
	private def CharSequence specialHandlingForMultipleByte(Property property) {
		'''name="«property.name»" type="xs:base64Binary" maxOccurs="1" minOccurs="1"'''
	}
	
	private def String buildConstraint(Constraint constraint) {
		var typeStr = constraint.type.literal
		var restriction = ""
		var rawValue = constraint.constraintValues
		switch typeStr {
			case 'MAX':
				restriction = 'maxInclusive'
			case 'MIN':
				restriction = 'minInclusive'
			case 'REGEX': {
				restriction = 'pattern'
				var matcher3 = Pattern.compile("['|\"](.*)['|\"]", Pattern.DOTALL).matcher(rawValue)
				if (matcher3.find())
					rawValue = matcher3.group(1)
			}
			case 'STRLEN':
				restriction = 'maxLength'
		}

		'''<xs:«restriction» value="«rawValue»"/>'''
	}
	
	private def defineMultiplicity(boolean multiplicity) {
		'''maxOccurs="«IF multiplicity»unbounded«ELSE»1«ENDIF»"'''
	}
}