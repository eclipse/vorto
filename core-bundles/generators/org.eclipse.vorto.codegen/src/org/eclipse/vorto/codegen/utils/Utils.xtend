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
import org.eclipse.emf.common.util.BasicEList
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.functionblock.RefParam
import org.eclipse.vorto.core.api.model.datatype.Type
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.emf.common.util.EList
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock

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

	def static InformationModel wrapFunctionBlock(FunctionblockModel fbModel) {
		var infomodel = InformationModelFactory.eINSTANCE.createInformationModel();
		infomodel.setCategory(fbModel.getCategory());
		infomodel.setDescription(fbModel.getDescription());
		infomodel.setDisplayname(fbModel.getDisplayname());
		infomodel.setName(fbModel.getName());
		infomodel.setNamespace(fbModel.getNamespace());
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
		if ((property.constraintRule?.constraints != null) && (property.constraintRule?.constraints.size > 0)) {
			for (Constraint constraint : property.constraintRule?.constraints) {
				if (constraint.type == ConstraintIntervalType.MIN) {
					return constraint.constraintValues;
				}
			}
		}
		return "";
	}

	def static String getMaxConstraint(Property property) {
		if ((property.constraintRule?.constraints != null) && (property.constraintRule?.constraints.size > 0)) {
			for (Constraint constraint : property.constraintRule?.constraints) {
				if (constraint.type == ConstraintIntervalType.MAX) {
					return constraint.constraintValues;
				}
			}
		}
		return "";
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
	
	def static EList<Entity> getReferencedEntities(FunctionBlock fb) {
		var entities = new BasicEList<Entity>();
		for (Type type : getReferencedTypes(fb)) {
			if ((type instanceof Entity) && (!entities.contains(type as Entity))) {
				entities.add(type as Entity);
			}
		}
		return entities;
	}

	def static EList<Enum> getReferencedEnums(FunctionBlock fb) {
		var enums = new BasicEList<Enum>();
		for (Type type : getReferencedTypes(fb)) {
			if ((type instanceof Enum) && (!enums.contains(type as Enum))) {
				enums.add(type as Enum);
			}
		}
		return enums;
	}

	def static EList<Type> getReferencedTypes(Type type) {
		var types = new BasicEList<Type>();
		types.add(type);

		if (type instanceof Entity) {
			var entityType = type as Entity;
			for (Property property : entityType.getProperties()) {
				types.addAll(getReferencedTypes(property));
			}
			types.add(entityType.getSuperType());
		}
		return types;
	}

	def static EList<Type> getReferencedTypes(Property property) {
		var types = new BasicEList<Type>();
		if (property.getType() instanceof ObjectPropertyType) {
			var objectType = property.getType() as ObjectPropertyType;
			types.add(objectType.getType());
			if (objectType.getType() instanceof Entity) {
				types.addAll(getReferencedTypes(objectType.getType() as Entity));
			}
		}
		return types;
	}

	def static EList<Type> getReferencedTypes(FunctionBlock fb) {
		var types = new BasicEList<Type>();
		if (fb != null) {
			// Analyze the status properties...
			if (fb.getStatus() != null) {
				for (Property property : fb.getStatus().getProperties()) {
					types.addAll(getReferencedTypes(property));
				}
			}
			// Analyze the configuration properties...
			if (fb.getConfiguration() != null) {
				for (Property property : fb.getConfiguration().getProperties()) {
					types.addAll(getReferencedTypes(property));
				}
			}
			// Analyze the fault properties...
			if (fb.getFault() != null) {
				for (Property property : fb.getFault().getProperties()) {
					types.addAll(getReferencedTypes(property));
				}
			}

			// Analyze the operation types
			for (Operation op : fb.getOperations()) {
				if (op.getReturnType() instanceof ReturnObjectType) {
					types.addAll(getReferencedTypes((op.getReturnType() as ReturnObjectType).getReturnType()));
				}
				for (Param param : op.getParams()) {
					if (param instanceof RefParam) {
						types.addAll(getReferencedTypes((param as RefParam).getType()));
					}
				}
			}
		}
		return types;
	}
}
