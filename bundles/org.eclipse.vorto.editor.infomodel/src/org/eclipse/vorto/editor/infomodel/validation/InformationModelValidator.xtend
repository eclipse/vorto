/*******************************************************************************
 * Copyright (c) 2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.editor.infomodel.validation

import java.util.HashSet
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage
import org.eclipse.vorto.editor.datatype.validation.ValidatorUtils
import org.eclipse.xtext.validation.Check
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PropertyType
import org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage
import java.util.List
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.core.api.model.functionblock.Event

/**
 * Information Model Validation rules. 
 */
class InformationModelValidator extends AbstractInformationModelValidator {

	@Check
	def checkDuplicateFunctionBlock(InformationModel informationModel) {
		var fbNamesSet = new HashSet<String>()
		var functionblockProperties = informationModel.properties
		for (var i = 0; i < functionblockProperties.length; i++) {
			var functionblockProperty = functionblockProperties.get(i)
			if (!fbNamesSet.add(functionblockProperty.name)) {
				error(SystemMessage.ERROR_DUPLICATED_FUNCTIONBLOCK_NAME, informationModel,
					InformationModelPackage.Literals.FUNCTIONBLOCK_PROPERTY__NAME)
			}
		}
	}

	@Check
	def checkFunctionBlockIsImported(FunctionblockProperty property) {
		val topParent = ValidatorUtils.getParentOfType(property, InformationModel) as InformationModel
		if (topParent !== null && !ValidatorUtils.isTypeInReferences(property.type, topParent.references)) {
			error(SystemMessage.ERROR_FUNCTIONBLOCK_NOT_IMPORTED, property,
				InformationModelPackage.Literals.FUNCTIONBLOCK_PROPERTY__TYPE)
		}
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

	def validateOverriddenProperties(EList<Property> properties, EList<Property> extProperties) {
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
					error(SystemMessage.ERROR_INCOMPATIBLE_TYPE, property,
						DatatypePackage.Literals.PROPERTY__TYPE)
				}
			}
		}
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
		var eventsMap = events.toMap[name].mapValues[it]
		for (event : extEvents) {
			var baseEvent = eventsMap.get(event.name)
			if (baseEvent !== null) {
				validateOverriddenProperties(baseEvent.properties, event.properties)
			}
		}
	}

	@Check
	def checkStatusExtendOverriddenProperties(InformationModel informationModel) {
		for (fbProperty : informationModel.properties) {
			if (fbProperty.extendedFunctionBlock !== null) {
				var baseFb = fbProperty.type.functionblock;
				var extendedFb = fbProperty.extendedFunctionBlock;
				validateOverriddenProperties(baseFb.status.properties, extendedFb.status.properties)
			}
		}
	}

	@Check
	def checkConfigurationExtendOverriddenProperties(InformationModel informationModel) {
		for (fbProperty : informationModel.properties) {
			if (fbProperty.extendedFunctionBlock !== null) {
				var baseFb = fbProperty.type.functionblock;
				var extendedFb = fbProperty.extendedFunctionBlock;
				validateOverriddenProperties(baseFb.configuration.properties, extendedFb.configuration.properties)
			}
		}
	}

	@Check
	def checkFaultExtendOverriddenProperties(InformationModel informationModel) {
		for (fbProperty : informationModel.properties) {
			if (fbProperty.extendedFunctionBlock !== null) {
				var baseFb = fbProperty.type.functionblock;
				var extendedFb = fbProperty.extendedFunctionBlock;
				validateOverriddenProperties(baseFb.fault.properties, extendedFb.fault.properties)
			}
		}
	}

	@Check
	def checkEventsExtendOverriddenProperties(InformationModel informationModel) {
		for (fbProperty : informationModel.properties) {
			if (fbProperty.extendedFunctionBlock !== null) {
				var baseFb = fbProperty.type.functionblock;
				var extendedFb = fbProperty.extendedFunctionBlock;
				validateOverriddenEvents(baseFb.events, extendedFb.events)
			}
		}
	}

	@Check
	def checkOverrideOperationType(InformationModel informationModel) {
		for (fbProperty : informationModel.properties) {
			if (fbProperty.extendedFunctionBlock !== null) {
				var baseFb = fbProperty.type.functionblock;
				var extendedFb = fbProperty.extendedFunctionBlock;
				validateOverriddenOperations(baseFb.operations, extendedFb.operations)
			}
		}
	}
}
