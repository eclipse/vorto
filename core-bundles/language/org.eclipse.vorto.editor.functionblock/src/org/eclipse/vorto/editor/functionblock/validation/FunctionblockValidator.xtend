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
package org.eclipse.vorto.editor.functionblock.validation

import com.google.inject.Inject
import java.util.HashSet
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.datatype.Type
import org.eclipse.vorto.core.api.model.functionblock.Configuration
import org.eclipse.vorto.core.api.model.functionblock.Event
import org.eclipse.vorto.core.api.model.functionblock.Fault
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
import org.eclipse.vorto.core.api.model.functionblock.RefParam
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.Status
import org.eclipse.vorto.core.api.model.model.ModelPackage
import org.eclipse.vorto.editor.datatype.validation.ConstraintValidatorFactory
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage
import org.eclipse.vorto.editor.datatype.validation.PropertyConstraintMappingValidation
import org.eclipse.vorto.editor.datatype.validation.ValidatorUtils
import org.eclipse.xtext.validation.Check
import org.eclipse.emf.common.util.EList
import org.eclipse.vorto.core.api.model.datatype.PropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.emf.ecore.util.EcoreUtil
import java.util.ArrayList
import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
import org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute

/**
 * Custom validation rules. 
 *
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
class FunctionblockValidator extends AbstractFunctionblockValidator {
	
	public val propertyValidator = new PropertyConstraintMappingValidation
	
	@Inject
	private TypeHelper helper;
	
	@Check
	def checkEntityandEnum(FunctionblockModel model) {
		var list = getAllTypeFromReferencedFile(model);

		var set = getNonDuplicateLowerCasedNameSet(list)

		var entities = model.entities
		var enums = model.enums

		for (en : entities) {
			if (!set.add(en.name.toLowerCase))
				error(SystemMessage.ERROR_TYPE_NAME_DUPLICATED, en, ModelPackage.Literals.MODEL__NAME)
		}

		for (en : enums) {
			if (!set.add(en.name.toLowerCase))
				error(SystemMessage.ERROR_TYPE_NAME_DUPLICATED, en, ModelPackage.Literals.MODEL__NAME)
		}

	}

	def List<Type> getAllTypeFromReferencedFile(EObject eObject) {
		return helper.getAllTypeFromReferencedTypeFile(eObject);
	}

	def HashSet<String> getNonDuplicateLowerCasedNameSet(List<Type> list) {
		var set = new HashSet<String>();
		for (e : list) {
			set.add(e.name.toLowerCase); //ignoring duplicated entities' and enums' name from type file
		}
		return set
	}

	@Check
	def checkFunctionBlockName(FunctionblockModel model) {
		val name = model.name
		if (isCamelCasedName(name)) {
			error(SystemMessage.ERROR_FBNAME_INVALID, model, ModelPackage.Literals.MODEL__NAME)
		}
	}

	@Check
	def checkDuplicateParameter(Operation operation) {
		var set = new HashSet<String>();
		for (var i = 0; i < operation.params.length; i++) {
			var param = operation.params.get(i)
			if (!set.add(param.name)) {
				error(SystemMessage.ERROR_DUPLICATED_PARAMETER_NAME, param, FunctionblockPackage.Literals.PARAM__NAME)
			}
		}
	}

	@Check
	def checkDuplicateOperation(Operation op) {
		var set = new HashSet<String>();
		var fb = op.eContainer() as FunctionBlock;
		var ops = fb.operations

		for (var i = 0; i < ops.length; i++) {
			var method = ops.get(i)
			if (!set.add(method.name)) {
				error(SystemMessage.ERROR_DUPLICATED_METHOD_NAME, method, FunctionblockPackage.Literals.OPERATION__NAME)
			}
		}
	}

	@Check
	def checkVersionPattern(FunctionblockModel functionblock) {
		if (!functionblock.version.matches("\\d+\\.\\d+\\.\\d+(\\-.+)?")) {
			error(SystemMessage.ERROR_VERSION_PATTERN, functionblock,
				ModelPackage.Literals.MODEL__VERSION)
		}
	}

	@Check
	def checkNamespacePattern(FunctionblockModel functionblock) {
		if (!functionblock.namespace.matches("([a-z0-9_]*\\.)*[a-z0-9_]*")) {
			error(SystemMessage.ERROR_NAMESPACE_PATTERN, functionblock,
				ModelPackage.Literals.MODEL__VERSION)
		}
	}
	
	@Check
	def checkParametersConstraint(Operation op) {
		var parameters = op.params;
		if(parameters.length == 0) return;
		for (parameter : parameters) {
			if (parameter instanceof PrimitiveParam) {
				var primitiveParam = parameter as PrimitiveParam
				val primitiveType = primitiveParam.type
				var constraintsList = primitiveParam.constraintRule.constraints
				for (constraint : constraintsList) {
					checkForConstraint(primitiveType, constraint, parameter, primitiveParam.type.getName, parameter.multiplicity, FunctionblockPackage.Literals.PRIMITIVE_PARAM__CONSTRAINT_RULE)
				}
			}
		}
	}
	
	@Check
	def checkReturnTypeConstraint(Operation op) {
		var returnType = op.returnType
		if (returnType instanceof ReturnPrimitiveType) {
			var returnPrimitiveType = returnType as ReturnPrimitiveType
			val parameterName = returnPrimitiveType.returnType.getName
			var constraintsList = returnPrimitiveType.constraintRule.constraints
			for (constraint : constraintsList) {
				checkForConstraint(returnPrimitiveType.returnType, constraint, returnPrimitiveType, parameterName, returnPrimitiveType.multiplicity, FunctionblockPackage.Literals.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE)
			}
		}
	}		

	def checkForConstraint(PrimitiveType primitiveType, Constraint constraint, EObject source, String parameterName, boolean isMultiplcity, EStructuralFeature feature) {
		if (!isValidConstraintType(primitiveType, constraint)) {
			error(propertyValidator.errorMessage, source, feature)
		} else {
			var validator = ConstraintValidatorFactory.getValueValidator(constraint.type)
			if (!isValidConstraintValue(validator, primitiveType, constraint)) {
				error(validator.errorMessage, source, feature)
			}
		}
		if(isMimeConstraint(parameterName, constraint)) {
			if(!isMultiplcity) {
				error(DatatypeSystemMessage.ERROR_MIMETYPE_FOR_BYTE, source,feature )
			}
		}
	}		
	
	@Check
	def checkPropsIn(Configuration c) {
		checkDuplicatedProperty(c.properties)
	}

	@Check
	def checkPropsIn(Status s) {
		checkDuplicatedProperty(s.properties)
	}

	@Check
	def checkPropsIn(Fault f) {
		checkDuplicatedProperty(f.properties)
	}

	@Check
	def checkPropsIn(Event e) {
		checkDuplicatedProperty(e.properties)
	}

	@Check
	def checkPropsIn(Entity e) {
		checkDuplicatedProperty(e.properties)
	}
	
	@Check
	def checkCircularRefInSuperType(FunctionblockModel functionblock) {
		if (functionblock.superType !== null) {
			try {
				if (ValidatorUtils.hasCircularReference(functionblock, functionblock.superType, FbValidatorUtils.modelToChildrenSupplierFunction)) {
					error(DatatypeSystemMessage.ERROR_SUPERTYPE_CIRCULAR_REF, functionblock, FunctionblockPackage.Literals.FUNCTIONBLOCK_MODEL__SUPER_TYPE);
				}	
			} catch(Exception e) {
				e.printStackTrace
			}
		}
	}
	
	@Check
	def checkRefParamIsImported(RefParam refParam) {
		val topParent = ValidatorUtils.getParentOfType(refParam, FunctionblockModel) as FunctionblockModel
		if (topParent !== null && !ValidatorUtils.isTypeInReferences(refParam.type, topParent.references)) {
			error(SystemMessage.ERROR_REF_PARAM_NOT_IMPORTED, refParam, FunctionblockPackage.Literals.REF_PARAM__TYPE);
		}
	}
	
	@Check
	def checkReturnTypeIsImported(ReturnObjectType returnType) {
		val topParent = ValidatorUtils.getParentOfType(returnType, FunctionblockModel) as FunctionblockModel
		if (topParent !== null && !ValidatorUtils.isTypeInReferences(returnType.returnType, topParent.references)) {
			error(SystemMessage.ERROR_OBJECT_RETURN_TYPE_NOT_IMPORTED, returnType, FunctionblockPackage.Literals.RETURN_OBJECT_TYPE__RETURN_TYPE);
		}
	}
	
	def setHelper(TypeHelper helper){
		this.helper = helper
	}
	
	def getHelper(){
		this.helper
	}

	def String getPropertyName(PropertyType propertyType) {
		if (propertyType instanceof PrimitivePropertyType) {
			return propertyType.type.literal
		} else if (propertyType instanceof DictionaryPropertyType) {
			return "dict" + getPropertyName(propertyType.keyType) + getPropertyName(propertyType.valueType)
		} else if (propertyType instanceof ObjectPropertyType) {
			return propertyType.getType().name
		}
	}

	def validateOvewrittenPropertyAttr(Property baseProperty, Property extProperty) {
		for (propAttr : extProperty.propertyAttributes) {
			for (basePropAttr : baseProperty.propertyAttributes) {
				if (propAttr instanceof BooleanPropertyAttribute && basePropAttr instanceof BooleanPropertyAttribute) {
					if ((propAttr as BooleanPropertyAttribute).getType().equals(
						(basePropAttr as BooleanPropertyAttribute).getType())) {
						error(SystemMessage.ERROR_OVERWRITTEN_PROPERTY_ATTRIBUTE_TYPE, extProperty,
							DatatypePackage.Literals.PROPERTY__PROPERTY_ATTRIBUTES)
					}
				} else if (propAttr instanceof EnumLiteralPropertyAttribute &&
					basePropAttr instanceof EnumLiteralPropertyAttribute) {
					if ((propAttr as EnumLiteralPropertyAttribute).getType().getName().equals(
						(basePropAttr as EnumLiteralPropertyAttribute).getType().getName())) {
						error(SystemMessage.ERROR_OVERWRITTEN_PROPERTY_ATTRIBUTE_TYPE, extProperty,
							DatatypePackage.Literals.PROPERTY__PROPERTY_ATTRIBUTES)
					}
				}
			}
		}
	}

	def ArrayList<String> validateOverriddenConstraints(Property baseProperty, Property extProperty) {
		var validatedConstraints = new ArrayList<String>()
		if (baseProperty.constraintRule === null) {
			return validatedConstraints
		}
		var constraintsMap = baseProperty.constraintRule.constraints.toMap[type].mapValues[it]
		for (costraint : extProperty.constraintRule.constraints) {
			var constraintId = baseProperty.name + costraint.type.getName
			if (!validatedConstraints.contains(constraintId)) {
				validatedConstraints.add(constraintId)
				var baseConstraint = constraintsMap.get(costraint.type)
				if (baseConstraint !== null) {
					if (costraint.type == ConstraintIntervalType.MIN) {
						if (Double.parseDouble(costraint.constraintValues) <
							Double.parseDouble(baseConstraint.constraintValues)) {
							error(SystemMessage.ERROR_OVERWRITTEN_CONSTRAINT_MIN_TOO_SMALL, costraint,
								DatatypePackage.Literals.CONSTRAINT__TYPE)
						}
					} else if (costraint.type == ConstraintIntervalType.MAX) {
						if (Double.parseDouble(costraint.constraintValues) >
							Double.parseDouble(baseConstraint.constraintValues)) {
							error(SystemMessage.ERROR_OVERWRITTEN_CONSTRAINT_MAX_TOO_BIG, costraint,
								DatatypePackage.Literals.CONSTRAINT__TYPE)
						}
					} else if (costraint.type == ConstraintIntervalType.STRLEN) {
						if (Double.parseDouble(costraint.constraintValues) >
							Double.parseDouble(baseConstraint.constraintValues)) {
							error(SystemMessage.ERROR_OVERWRITTEN_CONSTRAINT_STRLEN, costraint,
								DatatypePackage.Literals.CONSTRAINT__TYPE)
						}
					} else if (costraint.type == ConstraintIntervalType.NULLABLE) {
						if (costraint.constraintValues.equals("true") &&
							baseConstraint.constraintValues.equals("false")) {
							error(SystemMessage.ERROR_OVERWRITTEN_CONSTRAINT_NULLABLE, costraint,
								DatatypePackage.Literals.CONSTRAINT__TYPE)
						} else {
							error(SystemMessage.ERROR_OVERWRITTEN_CONSTRAINT_ALREADY_DEFINED, costraint,
								DatatypePackage.Literals.CONSTRAINT__TYPE)
						}
					} else {
						error(SystemMessage.ERROR_OVERWRITTEN_CONSTRAINT_ALREADY_DEFINED, costraint,
							DatatypePackage.Literals.CONSTRAINT__TYPE)
					}
				}
			}
		}
		return validatedConstraints
	}

	def ArrayList<String> validateOverriddenProperties(EList<Property> properties, EList<Property> extProperties,
		ArrayList<String> validatedConstraints) {
		var propertiesMap = properties.toMap[name].mapValues[it]

		var equalHelper = new EcoreUtil.EqualityHelper()
		for (property : extProperties) {
			var baseProperty = propertiesMap.get(property.name)
			if (baseProperty !== null) {
				if (!equalHelper.equals(property.presence, baseProperty.presence)) {
					error(SystemMessage.ERROR_INCOMPATIBLE_PRESENCE, property,
						DatatypePackage.Literals.PROPERTY__PRESENCE)
				}
				if (property.multiplicity != baseProperty.multiplicity) {
					error(SystemMessage.ERROR_INCOMPATIBLE_MULTIPLICITY, property,
						DatatypePackage.Literals.PROPERTY__MULTIPLICITY)
				}
				if (!equalHelper.equals(property.type, baseProperty.type)) {
					error(SystemMessage.ERROR_INCOMPATIBLE_TYPE, property, DatatypePackage.Literals.PROPERTY__TYPE)
				}
				validateOvewrittenPropertyAttr(baseProperty, property)
				validatedConstraints.addAll(validateOverriddenConstraints(baseProperty, property))
			}
		}
		return validatedConstraints
	}

	def equalsParamList(List<Param> list1, List<Param> list2) {
		var size = list1.size();
		if (size != list2.size()) {
			return false;
		}
		var equalHelper = new EcoreUtil.EqualityHelper();
		for (var i = 0; i < size; i++) {
			if (!equalHelper.equals(list1.get(i), list2.get(i))) {
				return false;
			}
		}
		return true;
	}

	def validateOverriddenOperations(EList<Operation> operations, EList<Operation> extOperations) {
		var operationsMap = operations.toMap[name].mapValues[it]
		var equalHelper = new EcoreUtil.EqualityHelper()

		for (operation : extOperations) {
			var baseOperation = operationsMap.get(operation.name)
			if (baseOperation !== null) {
				if (!equalHelper.equals(operation.presence, baseOperation.presence)) {
					error(SystemMessage.ERROR_INCOMPATIBLE_PRESENCE, operation,
						FunctionblockPackage.Literals.OPERATION__PRESENCE)
				}
				if (operation.breakable != baseOperation.breakable) {
					error(SystemMessage.ERROR_INCOMPATIBLE_BREAKABLE, operation,
						FunctionblockPackage.Literals.OPERATION__BREAKABLE)
				}
				if (!equalHelper.equals(operation.presence, baseOperation.presence)) {
					error(SystemMessage.ERROR_INCOMPATIBLE_PRESENCE, operation,
						FunctionblockPackage.Literals.OPERATION__PRESENCE)
				}
				if (!equalsParamList(operation.params, baseOperation.params)) {
					error(SystemMessage.ERROR_INCOMPATIBLE_PARMS, operation,
						FunctionblockPackage.Literals.OPERATION__PARAMS)
				}
				if (!equalHelper.equals(operation.returnType, baseOperation.returnType)) {
					error(SystemMessage.ERROR_INCOMPATIBLE_RETURN_TYPE, operation,
						FunctionblockPackage.Literals.OPERATION__RETURN_TYPE)
				}
			}
		}
	}

	def validateOverriddenEvents(EList<Event> events, EList<Event> extEvents) {
		var validatedConstraints = new ArrayList<String>()
		var eventsMap = events.toMap[name].mapValues[it]
		for (event : extEvents) {
			var baseEvent = eventsMap.get(event.name)
			if (baseEvent !== null) {
				validatedConstraints = validateOverriddenProperties(baseEvent.properties, event.properties,
					validatedConstraints)
			}
		}
	}

	def getParentFunctionBlocks(FunctionblockModel baseFunctionblockModel) {
		var functionBlocks = new ArrayList<FunctionBlock>()
		var lastFunctionblockModel = baseFunctionblockModel
		while (lastFunctionblockModel.superType !== null) {
			if (lastFunctionblockModel === lastFunctionblockModel.superType) {
				return functionBlocks
			}
			lastFunctionblockModel = lastFunctionblockModel.superType
			functionBlocks.add(lastFunctionblockModel.functionblock)
		}
		return functionBlocks
	}

	@Check
	def checkConfigurationOverriddenProperties(FunctionblockModel baseFunctionblockModel) {
		var validatedConstraints = new ArrayList<String>()
		var baseFb = baseFunctionblockModel.functionblock;
		var parentFunctionBlocks = getParentFunctionBlocks(baseFunctionblockModel)
		for (parentFb : parentFunctionBlocks) {
			if (parentFb.configuration !== null && baseFb.configuration !== null) {
				validateOverriddenProperties(parentFb.configuration.properties, baseFb.configuration.properties,
					validatedConstraints)
			}
		}
	}

	@Check
	def checkStatusOverriddenProperties(FunctionblockModel baseFunctionblockModel) {
		var validatedConstraints = new ArrayList<String>()
		var baseFb = baseFunctionblockModel.functionblock;
		var parentFunctionBlocks = getParentFunctionBlocks(baseFunctionblockModel)
		for (parentFb : parentFunctionBlocks) {
			if (parentFb.status !== null && baseFb.status !== null) {
				validateOverriddenProperties(parentFb.status.properties, baseFb.status.properties, validatedConstraints)
			}
		}
	}

	@Check
	def checkFaultOverriddenProperties(FunctionblockModel baseFunctionblockModel) {
		var validatedConstraints = new ArrayList<String>()
		var baseFb = baseFunctionblockModel.functionblock;
		var parentFunctionBlocks = getParentFunctionBlocks(baseFunctionblockModel)
		for (parentFb : parentFunctionBlocks) {
			if (parentFb.fault !== null && baseFb.fault !== null) {
				validateOverriddenProperties(parentFb.fault.properties, baseFb.fault.properties, validatedConstraints)
			}
		}
	}

	@Check
	def checkEventsOverriddenProperties(FunctionblockModel baseFunctionblockModel) {
		var baseFb = baseFunctionblockModel.functionblock;
		var parentFunctionBlocks = getParentFunctionBlocks(baseFunctionblockModel)
		for (parentFb : parentFunctionBlocks) {
			validateOverriddenEvents(parentFb.events, baseFb.events)
		}
	}

	@Check
	def checkOperationsOverriddenProperties(FunctionblockModel baseFunctionblockModel) {
		var baseFb = baseFunctionblockModel.functionblock;
		var parentFunctionBlocks = getParentFunctionBlocks(baseFunctionblockModel)
		for (parentFb : parentFunctionBlocks) {
			validateOverriddenOperations(parentFb.operations, baseFb.operations)
		}
	}

}