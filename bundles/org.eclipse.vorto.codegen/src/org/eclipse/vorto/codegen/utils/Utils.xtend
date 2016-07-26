/*******************************************************************************
 * Copyright (c) 2015,2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.utils

import org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute
import org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttributeType
import org.eclipse.vorto.core.api.model.datatype.Constraint
import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.datatype.PropertyAttribute
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.ReturnType
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory
import org.eclipse.vorto.core.api.model.model.ModelFactory

public class Utils {
	def static String getReturnType(ReturnType type) {
		if (type instanceof ReturnPrimitiveType) {
			return (type as ReturnPrimitiveType).returnType.getName();
		} else if (type instanceof ReturnObjectType) {
			return (type as ReturnObjectType).returnType.name
		}
	}

	def static String getPropertyType(Property property) {
		if (property.type instanceof PrimitivePropertyType) {
			return (property.type as PrimitivePropertyType).type.getName();
		} else if (property.type instanceof ObjectPropertyType) {
			return (property.type as ObjectPropertyType).type.getName();
		}
	}

	def static InformationModel disguiseFunctionblock(FunctionblockModel fbModel) {
		var infomodel = InformationModelFactory.eINSTANCE.createInformationModel();
		infomodel.setCategory(fbModel.getCategory());
		infomodel.setDescription(fbModel.getDescription());
		infomodel.setDisplayname(fbModel.getDisplayname());
		infomodel.setName(fbModel.getName());
		infomodel.setNamespace(fbModel.getNamespace() + ".informationmodels");
		infomodel.setVersion(fbModel.getVersion());

		var property = InformationModelFactory.eINSTANCE.createFunctionblockProperty();
		property.setType(fbModel);
		property.setName(fbModel.getName().toLowerCase());
		infomodel.properties.add(property);
		
		var reference = ModelFactory.eINSTANCE.createModelReference;
		reference.importedNamespace = fbModel.namespace + "."+fbModel. name
		reference.version = fbModel.version
		infomodel.references.add(reference)
		return infomodel;
	}

	def static boolean isSimpleNumeric(Property property) {
		if (property.type instanceof PrimitivePropertyType) {
			var primitiveType = (property.type as PrimitivePropertyType).type;
			if (primitiveType == PrimitiveType.INT) {
				return true;
			} else if (primitiveType == PrimitiveType.FLOAT) {
				return true;
			} else if (primitiveType == PrimitiveType.DOUBLE) {
				return true;
			} else if (primitiveType == PrimitiveType.LONG) {
				return true;
			} else if (primitiveType == PrimitiveType.SHORT) {
				return true;
			}
		}
		return false;
	}

	def static String getMinConstraint(Property property) {
		if ((property.constraintRule.constraints != null) && (property.constraintRule.constraints.size > 0)) {
			for (Constraint constraint : property.constraintRule.constraints) {
				if (constraint.type == ConstraintIntervalType.MIN) {
					return constraint.constraintValues;
				}
			}
			return "";
		}
	}

	def static String getMaxConstraint(Property property) {
		if ((property.constraintRule.constraints != null) && (property.constraintRule.constraints.size > 0)) {
			for (Constraint constraint : property.constraintRule.constraints) {
				if (constraint.type == ConstraintIntervalType.MAX) {
					return constraint.constraintValues;
				}
			}
			return "";
		}
	}

	def static String getMeasurementUnit(Property property) {
		var EnumLiteral literal = getEnumLiteralPropertyAttribute(property,
			EnumLiteralPropertyAttributeType.MEASUREMENT_UNIT);
		if (literal != null) {
			return literal.name;
		}
		return "";
	}

	def static boolean isReadable(Property property) {
		return getBooleanPropertyAttribute(property, BooleanPropertyAttributeType.READABLE);
	}

	def static boolean isWritable(Property property) {
		return getBooleanPropertyAttribute(property, BooleanPropertyAttributeType.WRITABLE);
	}

	def static boolean isEventable(Property property) {
		return getBooleanPropertyAttribute(property, BooleanPropertyAttributeType.EVENTABLE);
	}

	def static boolean getBooleanPropertyAttribute(Property property, BooleanPropertyAttributeType type) {
		if ((property.propertyAttributes != null) && (property.propertyAttributes.size > 0)) {
			for (PropertyAttribute pA : property.propertyAttributes) {
				if (pA instanceof BooleanPropertyAttribute) {
					var BooleanPropertyAttribute bPA = pA as BooleanPropertyAttribute;
					if ((bPA.type == type) && bPA.value) {
						return true;
					}
				}
			}
		}
		return false;
	}

	def static EnumLiteral getEnumLiteralPropertyAttribute(Property property, EnumLiteralPropertyAttributeType type) {
		if ((property.propertyAttributes != null) && (property.propertyAttributes.size > 0)) {
			for (PropertyAttribute pA : property.propertyAttributes) {
				if (pA instanceof EnumLiteralPropertyAttribute) {
					var EnumLiteralPropertyAttribute bPA = pA as EnumLiteralPropertyAttribute;
					if (bPA.type == type) {
						return bPA.value;
					}
				}
			}
		}
		return null;
	}
}
