/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.templates.java.utils;

import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.Operation;

public class ModelHelper {
  private static final String GETTER_PREFIX = "get";
  private static final String SETTER_PREFIX = "set";

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
}
