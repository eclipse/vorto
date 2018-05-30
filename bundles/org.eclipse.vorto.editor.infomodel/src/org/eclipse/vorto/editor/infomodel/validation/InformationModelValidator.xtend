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
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage
import org.eclipse.vorto.editor.datatype.validation.ValidatorUtils
import org.eclipse.xtext.validation.Check
import org.eclipse.vorto.core.api.model.model.ModelPackage
import java.util.ArrayList

/**
 * Information Model Validation rules. 
 */
class InformationModelValidator extends AbstractInformationModelValidator {
	
	@Check
	def checkInformationModelName(InformationModel model) {
		val name = model.name
		if (isCamelCasedName(name)) {
			error(SystemMessage.ERROR_IMNAME_INVALID, model, ModelPackage.Literals.MODEL__NAME)
		}
	}

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

	@Check
	def checkStatusExtendOverriddenProperties(InformationModel informationModel) {
		var validatedConstraints = new ArrayList<String>()
		for (fbProperty : informationModel.properties) {
			if (fbProperty.extendedFunctionBlock !== null) {
				var baseFb = fbProperty.type.functionblock;
				var extendedFb = fbProperty.extendedFunctionBlock;
				validateOverriddenProperties(baseFb.status.properties, extendedFb.status.properties, validatedConstraints)
			}
		}
	}

	@Check
	def checkConfigurationExtendOverriddenProperties(InformationModel informationModel) {
		var validatedConstraints = new ArrayList<String>()
		for (fbProperty : informationModel.properties) {
			if (fbProperty.extendedFunctionBlock !== null) {
				var baseFb = fbProperty.type.functionblock;
				var extendedFb = fbProperty.extendedFunctionBlock;
				validateOverriddenProperties(baseFb.configuration.properties, extendedFb.configuration.properties, validatedConstraints)
			}
		}
	}

	@Check
	def checkFaultExtendOverriddenProperties(InformationModel informationModel) {
		var validatedConstraints = new ArrayList<String>()
		for (fbProperty : informationModel.properties) {
			if (fbProperty.extendedFunctionBlock !== null) {
				var baseFb = fbProperty.type.functionblock;
				var extendedFb = fbProperty.extendedFunctionBlock;
				validateOverriddenProperties(baseFb.fault.properties, extendedFb.fault.properties, validatedConstraints)
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
