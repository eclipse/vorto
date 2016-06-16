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

package org.eclipse.vorto.editor.functionblock.validation

import com.google.inject.Inject
import java.util.HashSet
import java.util.List
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EStructuralFeature
import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Type
import org.eclipse.vorto.core.api.model.functionblock.Configuration
import org.eclipse.vorto.core.api.model.functionblock.Event
import org.eclipse.vorto.core.api.model.functionblock.Fault
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.Status
import org.eclipse.vorto.core.api.model.model.ModelPackage
import org.eclipse.vorto.editor.datatype.validation.DatatypeSystemMessage
import org.eclipse.xtext.validation.Check
import org.eclipse.vorto.editor.datatype.validation.PropertyConstraintMappingValidation
import org.eclipse.vorto.editor.datatype.validation.ConstraintValidatorFactory

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
	def checkOpNameAgainstEntityName(FunctionblockModel model) {
		
		var listEE= getAllTypeFromReferencedFile(model);
		
		listEE.addAll(model.entities)
		listEE.addAll(model.enums)
		
		var set = getNonDuplicateLowerCasedNameSet(listEE)
		var ops = model.functionblock.operations
		
		for(op : ops){
			if(set.contains(op.name.toLowerCase)){
				error(SystemMessage.ERROR_OPERATION_SAME_NAME_AS_TYPE, op,FunctionblockPackage.Literals.OPERATION__NAME)
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
		if (!functionblock.namespace.matches("([a-z0-9]*\\.)*[a-z0-9]*")) {
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
	
	def setHelper(TypeHelper helper){
		this.helper = helper
	}
	
	def getHelper(){
		this.helper
	}

}
