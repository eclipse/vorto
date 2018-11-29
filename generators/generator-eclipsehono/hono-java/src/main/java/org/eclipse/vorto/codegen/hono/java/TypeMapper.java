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
package org.eclipse.vorto.codegen.hono.java;

import java.util.Arrays;
import java.util.List;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.PropertyType;

public class TypeMapper {

  private static final List<String> keywords =
      Arrays.asList("abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
          "class", "const", "continue", "default", "do", "double", "else", "extends", "false",
          "final", "finally", "float", "for", "goto", "if", "implements", "import", "instanceof",
          "int", "interface", "long", "native", "new", "null", "package", "private", "protected",
          "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized",
          "this", "throw", "throws", "transient", "true", "try", "void", "volatile", "while");

  public static String mapSimpleDatatype(PropertyType type) {
    if (type instanceof ObjectPropertyType) {
      return "String";
    } else {
      PrimitivePropertyType primitiveType = (PrimitivePropertyType) type;
      if (primitiveType.getType().compareTo(PrimitiveType.STRING) == 0) {
        return "String";
      } else if (primitiveType.getType().compareTo(PrimitiveType.DATETIME) == 0) {
        return "java.util.Date";
      } else {
        return primitiveType.getType().getLiteral();
      }
    }
  }

  public static String getRandomValue(PropertyType type) {
    if (type instanceof ObjectPropertyType) {
      return "\"\"";
    } else {
      PrimitivePropertyType primitiveType = (PrimitivePropertyType) type;
      if (primitiveType.getType().compareTo(PrimitiveType.STRING) == 0) {
        return "\"\"";
      } else if (primitiveType.getType().compareTo(PrimitiveType.BOOLEAN) == 0) {
        return "new java.util.Random().nextBoolean()";
      } else if (primitiveType.getType().compareTo(PrimitiveType.INT) == 0) {
        return "new java.util.Random().nextInt(100)";
      } else if (primitiveType.getType().compareTo(PrimitiveType.FLOAT) == 0) {
        return "Math.round(new java.util.Random().nextFloat()*(float)100)";
      } else if (primitiveType.getType().compareTo(PrimitiveType.DOUBLE) == 0) {
        return "Math.round(new java.util.Random().nextDouble()*(double)100)";
      } else if (primitiveType.getType().compareTo(PrimitiveType.DATETIME) == 0) {
        return "new java.util.Date()";
      } else if (primitiveType.getType().compareTo(PrimitiveType.SHORT) == 0) {
        return "new java.util.Random().nextInt(100)";
      } else if (primitiveType.getType().compareTo(PrimitiveType.BASE64_BINARY) == 0) {
        return "null";
      } else if (primitiveType.getType().compareTo(PrimitiveType.BYTE) == 0) {
        return "(byte) new java.util.Random().nextInt(100)";
      }
    }
    return "";
  }

  public static String checkKeyword(String name) {
    return keywords.contains(name) ? name + "_" : name;
  }
}
