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
package org.eclipse.vorto.editor.datatype.validation

import java.util.HashSet
import java.util.List
import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.core.api.model.model.ModelPackage
import org.eclipse.vorto.editor.datatype.internal.validation.ConstraintValueValidator
import org.eclipse.xtext.validation.Check

/**
 * Custom validation rules. 
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
class DatatypeValidator extends AbstractDatatypeValidator {

	public val propertyValidator = new PropertyConstraintMappingValidation

	@Check
	def checkCircularRefInObjectPropertyType(ObjectPropertyType ref) {
		if (ref.type !== null) {
			try {
				val parent = ValidatorUtils.getParentOfType(ref, Model) as Model;
				if (parent !== null) {
					if (ValidatorUtils.hasCircularReference(parent as Model, ref.type, ValidatorUtils.entityTypeToChildrenSupplierFunction)) {
						error(DatatypeSystemMessage.ERROR_OBJ_PROPERTY_CIRCULAR_REF, ref, DatatypePackage.Literals.OBJECT_PROPERTY_TYPE__TYPE);
					}
				}	
			} catch(Exception e) {
				e.printStackTrace
			}
		}
	}
	
	@Check
	def checkCircularRefInSuperType(Entity entity) {
		if (entity.superType !== null) {
			try {
				if (ValidatorUtils.hasCircularReference(entity, entity.superType, ValidatorUtils.entityTypeToChildrenSupplierFunction)) {
					error(DatatypeSystemMessage.ERROR_SUPERTYPE_CIRCULAR_REF, entity, DatatypePackage.Literals.ENTITY__SUPER_TYPE);
				}	
			} catch(Exception e) {
				e.printStackTrace
			}
		}
	}

	@Check
	def checkConstraint(Property prop) {

		var constraints = prop.constraintRule.constraints

		if(constraints.length == 0 || prop.type instanceof ObjectPropertyType) return;
		var primi = prop.type as PrimitivePropertyType
		var isMultiplcity = prop.multiplicity;
		for (constraint : constraints) {
			verifyConstraintForType(primi, constraint, isMultiplcity)
		}
	}

	def verifyConstraintForType(PrimitivePropertyType primitivePropertyType, Constraint constraint,
		boolean isMultiplcity) {
		if (!isValidConstraintType(primitivePropertyType.type, constraint)) {
			error(propertyValidator.errorMessage, constraint, DatatypePackage.Literals.CONSTRAINT__TYPE)
		} else {
			var validator = ConstraintValidatorFactory.getValueValidator(constraint.type)
			if (!isValidConstraintValue(validator, primitivePropertyType.type, constraint)) {
				error(validator.errorMessage, constraint, DatatypePackage.Literals.CONSTRAINT__CONSTRAINT_VALUES)
			}
		}

		if (isMimeConstraint(primitivePropertyType.type.getName(), constraint)) {
			if (!isMultiplcity)
				error(DatatypeSystemMessage.ERROR_MIMETYPE_FOR_BYTE, constraint,
					DatatypePackage.Literals.CONSTRAINT__TYPE)
		}
	}

	def isValidConstraintType(PrimitiveType primitiveType, Constraint constraint) {
		return propertyValidator.checkPropertyConstraints(primitiveType, constraint)
	}

	def isValidConstraintValue(ConstraintValueValidator validator, PrimitiveType primitiveType, Constraint constraint) {
		return validator.evaluateValueType(primitiveType, constraint)
	}

	def isMimeConstraint(String primitiveTypeName, Constraint constraint) {
		return "MIMETYPE" == constraint.type.literal && "byte" == primitiveTypeName
	}

	@Check
	def checkEnumName_Literal(Enum ent) {
		val name = ent.name
		if (isCamelCasedName(name)) {
			error(DatatypeSystemMessage.ERROR_ENUMNAME_INVALID_CAMELCASE, ent, ModelPackage.Literals.MODEL__NAME)
		}
	}

	@Check
	def checkDuplicatedLiteral(Enum enu) {
		var list = enu.enums
		var set = new HashSet<String>();
		for (var i = 0; i < list.length; i++) {
			if (!set.add(list.get(i).name)) {
				error(DatatypeSystemMessage.ERROR_DUPLICATED_ENUM_LITERAL, list.get(i),
					DatatypePackage.Literals.ENUM_LITERAL__NAME)
			}
		}
	}

	@Check
	def checkDuplicatedConstraint(Property feature) {
		var set = new HashSet<String>();
		var list = feature.constraintRule.constraints;
		for (var i = 0; i < list.length; i++) {
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
	}

	def boolean isCamelCasedName(String name) {
		!Character.isUpperCase((name).charAt(0))
	}

	def checkDuplicatedProperty(List<Property> props) {
		var set = new HashSet<String>();

		for (pp : props) {
			if (!set.add(pp.name)) {
				error(DatatypeSystemMessage.ERROR_DUPLICATED_PROPERTY_NAME, pp, DatatypePackage.Literals.PROPERTY__NAME)
			}
		}
	}
	
	@Check
	def checkPropertyIfInReferences(Property property) {
		if (property.type instanceof ObjectPropertyType) {
			var topParent = ValidatorUtils.getParentOfType(property, Model) as Model
			if (topParent !== null && !ValidatorUtils.isTypeInReferences((property.type as ObjectPropertyType).type, topParent.references)) {
				error(DatatypeSystemMessage.ERROR_PROPERTY_TYPE_NOT_IMPORTED, property, DatatypePackage.Literals.PROPERTY__TYPE)
			}	
		}
	}
}
