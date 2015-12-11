/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/

package org.eclipse.vorto.editor.datatype.validation

import java.util.HashSet
import java.util.List
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.model.ModelPackage
import org.eclipse.vorto.editor.datatype.internal.ConstraintValidatorFactory
import org.eclipse.vorto.editor.datatype.internal.validation.PropertyConstraintMappingValidation
import org.eclipse.xtext.validation.Check

/**
 * Custom validation rules. 
 *
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
class DatatypeValidator extends AbstractDatatypeValidator {

public val propertyValidator = new PropertyConstraintMappingValidation

	@Check
	def checkConstraint(Property prop) {
		var list = prop.constraints
		
		if(list.length == 0) return;
		
		
			var primi = prop.type as PrimitivePropertyType
			for (i : list.size >.. 0) {
				var constraint = list.get(i)
				if (!propertyValidator.checkPropertyConstraints(primi.getType, constraint)) {
					error(propertyValidator.errorMessage, constraint, DatatypePackage.Literals.CONSTRAINT__TYPE)
				}else{
					var validator = ConstraintValidatorFactory.getValueValidator(constraint.type)
					if (!validator.evaluateValueType(primi.getType, constraint)) {
						error(validator.errorMessage, constraint, DatatypePackage.Literals.CONSTRAINT__CONSTRAINT_VALUES)
					}
				}
				
				if("MIMETYPE" == constraint.type.literal && "byte" == primi.getType.getName() ){
					if(!prop.multiplicity)
						error(DatatypeSystemMessage.ERROR_MIMETYPE_FOR_BYTE, constraint, DatatypePackage.Literals.CONSTRAINT__TYPE)
				}
			}
	}

	@Check
	def checkEnumName_Literal(Enum ent) {
		val name = ent.name
		if (isCamelCasedName(name)) {
			error(DatatypeSystemMessage.ERROR_ENUMNAME_INVALID_CAMELCASE, ent, ModelPackage.Literals.MODEL__NAME)
		}
		if(ent.getEnums().empty){
			error(DatatypeSystemMessage.ERROR_ENUM_CANNOT_BE_EMPTY, ent, DatatypePackage.Literals.ENUM__ENUMS)
		}
	}
	
	@Check
	def checkDuplicatedLiteral(Enum enu){
		var list = enu.enums
		var set = new HashSet<String>();
		for (var i = 0; i < list.length; i++) {
			if(!set.add(list.get(i).name)){
				error(DatatypeSystemMessage.ERROR_DUPLICATED_ENUM_LITERAL, list.get(i), DatatypePackage.Literals.ENUM_LITERAL__NAME)
			}
		}
	}

	@Check
	def checkDuplicatedConstraint(Property feature) {
		var set = new HashSet<String>();
		var list = feature.constraints;
		for (var i = 0; i < list.length; i ++) {
			var con = list.get(i);
			if (!set.add(con.type.literal)) {
				error(DatatypeSystemMessage.ERROR_DUPLICATED_CONSTRAINT, con, DatatypePackage.Literals.CONSTRAINT__TYPE)
			}
		}
	}

	@Check
	def checkEntityName(Entity entity) {
		val name = entity.name
		if (isCamelCasedName(name)) {
			error(DatatypeSystemMessage.ERROR_ENTITYNAME_INVALID_CAMELCASE, entity, ModelPackage.Literals.MODEL__NAME)
		}
		if (name.toLowerCase.endsWith('reply')) {
			error(DatatypeSystemMessage.ERROR_ENTITYNAME_SUFFIX_REPLY, entity, ModelPackage.Literals.MODEL__NAME)
		}
	}

	def boolean isCamelCasedName(String name) {
		!Character.isUpperCase((name).charAt(0))
	}
	

	def checkDuplicatedProperty(List<Property> props){
		var set = new HashSet<String>();
		
		for ( pp : props){
			if(!set.add(pp.name)){
				error(DatatypeSystemMessage.ERROR_DUPLICATED_PROPERTY_NAME, pp, DatatypePackage.Literals.PROPERTY__NAME)
			}
		}
	}
	
	@Check
	def checkPropertyName(Property property) {
		var name = property.name
		if (name.endsWith('TS')) {
			error(DatatypeSystemMessage.ERROR_PROPNAME_SUFFIX_TS, property, DatatypePackage.Literals.PROPERTY__NAME)
		}
	}
	
	
}
