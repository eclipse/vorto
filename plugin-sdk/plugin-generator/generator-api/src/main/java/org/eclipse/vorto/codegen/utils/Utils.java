/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.codegen.utils;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.BooleanPropertyAttributeType;
import org.eclipse.vorto.core.api.model.datatype.Constraint;
import org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.PropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Param;
import org.eclipse.vorto.core.api.model.functionblock.RefParam;
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType;
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType;
import org.eclipse.vorto.core.api.model.functionblock.ReturnType;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.model.ModelFactory;
import org.eclipse.vorto.core.api.model.model.ModelReference;

public class Utils {
  public static String getReturnType(ReturnType type) {
    if (type instanceof ReturnPrimitiveType) {
      return ((ReturnPrimitiveType) type).getReturnType().getName();
    } else if (type instanceof ReturnObjectType) {
      return ((ReturnObjectType) type).getReturnType().getName();
    } else {
      return null;
    }
  }

  public static String getPropertyType(Property property) {
    if (property.getType() instanceof PrimitivePropertyType) {
      return ((PrimitivePropertyType) property.getType()).getType().getName();
    } else if (property.getType() instanceof ObjectPropertyType) {
      return ((ObjectPropertyType) property.getType()).getType().getName();
    } else {
      return null;
    }
  }

  public static InformationModel wrapFunctionBlock(FunctionblockModel fbModel) {
    InformationModel infomodel = InformationModelFactory.eINSTANCE.createInformationModel();
    infomodel.setCategory(fbModel.getCategory());
    infomodel.setDescription(fbModel.getDescription());
    infomodel.setDisplayname(fbModel.getDisplayname());
    infomodel.setName(fbModel.getName() + "IM");
    infomodel.setNamespace(fbModel.getNamespace());
    infomodel.setVersion(fbModel.getVersion());

    FunctionblockProperty property =
        InformationModelFactory.eINSTANCE.createFunctionblockProperty();
    property.setType(fbModel);
    property.setName(fbModel.getName().toLowerCase());
    infomodel.getProperties().add(property);

    ModelReference reference = ModelFactory.eINSTANCE.createModelReference();
    reference.setImportedNamespace(fbModel.getNamespace() + "." + fbModel.getName());
    reference.setVersion(fbModel.getVersion());
    infomodel.getReferences().add(reference);
    return infomodel;
  }

  public static boolean isSimpleNumeric(Property property) {
    if (property.getType() instanceof PrimitivePropertyType) {
      PrimitiveType primitiveType = ((PrimitivePropertyType) property.getType()).getType();
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

  public static String getMinConstraint(Property property) {
    if ((property.getConstraintRule() != null
        && property.getConstraintRule().getConstraints() != null)
        && (property.getConstraintRule() != null
            && property.getConstraintRule().getConstraints().size() > 0)) {
      for (Constraint constraint : property.getConstraintRule().getConstraints()) {
        if (constraint.getType() == ConstraintIntervalType.MIN) {
          return constraint.getConstraintValues();
        }
      }
    }
    return "";
  }

  public static String getMaxConstraint(Property property) {
    if ((property.getConstraintRule() != null
        && property.getConstraintRule().getConstraints() != null)
        && (property.getConstraintRule() != null
            && property.getConstraintRule().getConstraints().size() > 0)) {
      for (Constraint constraint : property.getConstraintRule().getConstraints()) {
        if (constraint.getType() == ConstraintIntervalType.MAX) {
          return constraint.getConstraintValues();
        }
      }
    }
    return "";
  }

  public static String getMeasurementUnit(Property property) {
    EnumLiteral literal = getEnumLiteralPropertyAttribute(property,
        EnumLiteralPropertyAttributeType.MEASUREMENT_UNIT);
    if (literal != null) {
      return literal.getName();
    }
    return "";
  }

  public static boolean isReadable(Property property) {
    return getBooleanPropertyAttribute(property, BooleanPropertyAttributeType.READABLE);
  }

  public static boolean isWritable(Property property) {
    return getBooleanPropertyAttribute(property, BooleanPropertyAttributeType.WRITABLE);
  }

  public static boolean isEventable(Property property) {
    return getBooleanPropertyAttribute(property, BooleanPropertyAttributeType.EVENTABLE);
  }

  public static boolean getBooleanPropertyAttribute(Property property,
      BooleanPropertyAttributeType type) {
    if ((property.getPropertyAttributes() != null)
        && (property.getPropertyAttributes().size() > 0)) {
      for (PropertyAttribute pA : property.getPropertyAttributes()) {
        if (pA instanceof BooleanPropertyAttribute) {
          BooleanPropertyAttribute bPA = (BooleanPropertyAttribute) pA;
          if (bPA.getType() == type && bPA.isValue()) {
            return true;
          }
        }
      }
    }
    return false;
  }

  public static EnumLiteral getEnumLiteralPropertyAttribute(Property property,
      EnumLiteralPropertyAttributeType type) {
    if ((property.getPropertyAttributes() != null)
        && (property.getPropertyAttributes().size() > 0)) {
      for (PropertyAttribute pA : property.getPropertyAttributes()) {
        if (pA instanceof EnumLiteralPropertyAttribute) {
          EnumLiteralPropertyAttribute bPA = (EnumLiteralPropertyAttribute) pA;
          if (bPA.getType() == type) {
            return bPA.getValue();
          }
        }
      }
    }
    return null;
  }

  public static EList<Entity> getReferencedEntities(FunctionBlock fb) {
    BasicEList<Entity> entities = new BasicEList<Entity>();
    for (Type type : getReferencedTypes(fb)) {
      if ((type instanceof Entity) && (!entities.contains((Entity) type))) {
        entities.add((Entity) type);
      }
    }
    return entities;
  }

  public static EList<Enum> getReferencedEnums(FunctionBlock fb) {
    BasicEList<Enum> enums = new BasicEList<Enum>();
    for (Type type : getReferencedTypes(fb)) {
      if ((type instanceof Enum) && (!enums.contains((Enum) type))) {
        enums.add((Enum) type);
      }
    }
    return enums;
  }

  public static EList<Type> getReferencedTypes(Type type) {
    BasicEList<Type> types = new BasicEList<Type>();
    types.add(type);

    if (type instanceof Entity) {
      Entity entityType = (Entity) type;
      for (Property property : entityType.getProperties()) {
        types.addAll(getReferencedTypes(property));
      }
      types.add(entityType.getSuperType());
    }
    return types;
  }

  public static EList<Type> getReferencedTypes(Property property) {
    BasicEList<Type> types = new BasicEList<Type>();
    if (property.getType() instanceof ObjectPropertyType) {
      ObjectPropertyType objectType = (ObjectPropertyType) property.getType();
      types.add(objectType.getType());
      if (objectType.getType() instanceof Entity) {
        types.addAll(getReferencedTypes((Entity) objectType.getType()));
      }
    }
    return types;
  }

  public static EList<Type> getReferencedTypes(FunctionBlock fb) {
    BasicEList<Type> types = new BasicEList<Type>();
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
          types.addAll(getReferencedTypes(((ReturnObjectType) op.getReturnType()).getReturnType()));
        }
        for (Param param : op.getParams()) {
          if (param instanceof RefParam) {
            types.addAll(getReferencedTypes(((RefParam) param).getType()));
          }
        }
      }
    }
    return types;
  }
}
