/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.examples.lwm2m.utils;

import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;

public class TypeMapper {
	public static String mapSimpleDatatype(PrimitiveType type) {
		if (type.compareTo(PrimitiveType.STRING) ==0) {
			return "String";
		}
		else if (type.compareTo(PrimitiveType.DATETIME)==0) {
			return "java.util.Date";
		}
		else
		{
			return type.getLiteral();
		}
	}
	
	public static String getInitialValue(PrimitiveType type) {
		if (type.compareTo(PrimitiveType.STRING) ==0) {
			return "\"\"";
		} else if (type.compareTo(PrimitiveType.BOOLEAN) ==0) {
			return "false";
		} else if (type.compareTo(PrimitiveType.INT) ==0) {
			return "0";
		} else if (type.compareTo(PrimitiveType.FLOAT) ==0) {
			return "0.0f";
		} else if (type.compareTo(PrimitiveType.DOUBLE) ==0) {
			return "0.0d";
		} else if (type.compareTo(PrimitiveType.DATETIME) ==0) {
			return "new java.util.Date()";
		} else if (type.compareTo(PrimitiveType.SHORT) ==0) {
			return "0";
		} else if (type.compareTo(PrimitiveType.BASE64_BINARY) ==0) {
			return "null";
		} else if (type.compareTo(PrimitiveType.BYTE) == 0) {
			return "0";
		}
		return "";
	}
}
