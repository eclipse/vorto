/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.repository.web.core.validation;

import java.util.Map;

import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.PrimitiveType;
import org.eclipse.vorto.model.runtime.FunctionblockData;
import org.eclipse.vorto.model.runtime.InfomodelData;

public class ValidationHelper {

	private IMappingSpecification specification;

	public ValidationHelper(IMappingSpecification specification) {
		this.specification = specification;
	}

	public void validate(InfomodelData data) throws ValidationProblem {

		for (ModelProperty fbProperty : specification.getInfoModel().getFunctionblocks()) {
			final FunctionblockData fbData = data.get(fbProperty.getName());

			if (fbData != null) {
				final FunctionblockModel fbModel = specification.getFunctionBlock(fbProperty.getName());

				for (ModelProperty statusProperty : fbModel.getStatusProperties()) {
					checkProperty(fbData.getStatus(), statusProperty,
							fbProperty.getName() + "/status");
				}

				for (ModelProperty configProperty : fbModel.getConfigurationProperties()) {
					checkProperty(fbData.getConfiguration(), configProperty,
							fbProperty.getName() + "/configuration");
				}
			}
			
		}
	}

	private void checkProperty(Map<String, Object> properties, ModelProperty property, String path) {
		if (property.isMandatory()
				&& (!properties.containsKey(property.getName()) || properties.get(property.getName()) == null)) {
			throw new ValidationProblem("Mandatory field " + path + "/" + property.getName() + " is missing");
		} else {
			Object propertyValue = properties.get(property.getName());
			if (propertyValue != null) {
				if (property.getType() instanceof PrimitiveType) {
					checkPrimitiveTypeValue(path, propertyValue, property);
				} else if (property.getType() instanceof ModelId) {
					// FIXME: validate entities / enums
				}
			}
		}
	}

	private static void checkPrimitiveTypeValue(String path, Object propertyValue, ModelProperty property) {
		PrimitiveType type = (PrimitiveType) property.getType();
		if (type == PrimitiveType.STRING && !(propertyValue instanceof String)) {
			throw new ValidationProblem("Field " + path + "/" + property.getName() + " must be of type 'String'");
		} else if (type == PrimitiveType.BOOLEAN && !(propertyValue instanceof Boolean)) {
			throw new ValidationProblem("Field " + path + "/" + property.getName() + " must be of type 'Boolean'");
		} else if (type == PrimitiveType.DOUBLE && !(propertyValue instanceof Double)) {
			throw new ValidationProblem("Field " + path + "/" + property.getName() + " must be of type 'Double'");
		} else if (type == PrimitiveType.FLOAT && !(propertyValue instanceof Double)) {
			throw new ValidationProblem("Field " + path + "/" + property.getName() + " must be of type 'Float'");
		} else if (type == PrimitiveType.INT && !isInteger(propertyValue)) {
			throw new ValidationProblem("Field " + path + "/" + property.getName() + " must be of type 'Integer'");
		} else if (type == PrimitiveType.LONG && !isLong(propertyValue)) {
			throw new ValidationProblem("Field " + path + "/" + property.getName() + " must be of type 'Long'");
		} else if (type == PrimitiveType.BASE64_BINARY && !(propertyValue instanceof String)) {
			throw new ValidationProblem(
					"Field " + path + "/" + property.getName() + " must be a Base64-encoded 'String'");
		}
	}

	private static boolean isInteger(Object value) {
		try {
			Integer.parseInt(value.toString());
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	private static boolean isLong(Object value) {
		try {
			Long.parseLong(value.toString());
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
}
