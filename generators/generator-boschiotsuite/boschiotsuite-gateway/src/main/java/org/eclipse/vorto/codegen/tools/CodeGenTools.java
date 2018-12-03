/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 * 
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.tools;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Event;

public class CodeGenTools {
  private static final String GETTER_PREFIX = "get";
  private static final String SETTER_PREFIX = "set";

  public static boolean isGetter(Operation operation, FunctionBlock fb) {
    return operation.getName().length() >= GETTER_PREFIX.length()
        && operation.getName().substring(0, GETTER_PREFIX.length()).equalsIgnoreCase(GETTER_PREFIX);
  }

  public static boolean isSetter(Operation operation, FunctionBlock fb) {
    return operation.getName().length() >= SETTER_PREFIX.length()
        && operation.getName().substring(0, SETTER_PREFIX.length()).equalsIgnoreCase(SETTER_PREFIX);
  }


  public static boolean isReadable(Property property, FunctionBlock fb) {
    String getterName = GETTER_PREFIX + property.getName();
    return findOperationByName(getterName, fb) != null;
  }

  public static boolean isWritable(Property property, FunctionBlock fb) {
    String setterName = SETTER_PREFIX + property.getName();
    return findOperationByName(setterName, fb) != null;
  }

  public static boolean isEventable(Property property, FunctionBlock fb) {
    return findEventByName(property.getName(), fb) != null;
  }

  public static Operation findOperationByName(String operationName, FunctionBlock fb) {
    for (Operation operation : fb.getOperations()) {
      if (operation.getName().equalsIgnoreCase(operationName)) {
        return operation;
      }
    }
    return null;
  }

  public static Event findEventByName(String eventName, FunctionBlock fb) {
    for (Event event : fb.getEvents()) {
      if (event.getName().equalsIgnoreCase(eventName)) {
        return event;
      }
    }
    return null;
  }

  public static EList<Entity> getReferencedEntities(FunctionBlock fb) {
    EList<Entity> entities = new BasicEList<Entity>();
    for (Type type : getReferencedTypes(fb)) {
      if (type instanceof Entity) {
        entities.add((Entity) type);
      }
    }
    return entities;
  }

  public static EList<Enum> getReferencedEnums(FunctionBlock fb) {
    EList<Enum> enums = new BasicEList<Enum>();
    for (Type type : getReferencedTypes(fb)) {
      if (type instanceof Enum) {
        enums.add((Enum) type);
      }
    }
    return enums;
  }

  public static EList<Type> getReferencedTypes(Entity entity) {
    EList<Type> types = new BasicEList<Type>();
    for (Property property : entity.getProperties()) {
      types.addAll(getReferencedTypes(property));
    }
    types.add(entity.getSuperType());
    return types;
  }

  public static EList<Type> getReferencedTypes(Property property) {
    EList<Type> types = new BasicEList<Type>();
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
    EList<Type> types = new BasicEList<Type>();
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
    }
    return types;
  }
}
