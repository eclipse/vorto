/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates;

import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;

public class PropertyUtil {
	public static String toJavaFieldType(PrimitiveType primitiveType) {
		if (primitiveType == PrimitiveType.STRING) {
			return "String";
		} else if (primitiveType == PrimitiveType.DATETIME) {
			return "java.util.Date";
		} else if (primitiveType == PrimitiveType.BASE64_BINARY) {
			return "byte[]";
		} else {
			return primitiveType.getLiteral();
		}
	}

	public static String getDefaultValue(PrimitiveType primitiveType) {
		if (primitiveType == PrimitiveType.BOOLEAN) {
			return "false";
		} else if (primitiveType == PrimitiveType.STRING) {
			return "\"\"";
		} else if (primitiveType == PrimitiveType.SHORT) {
			return "0";
		} else if (primitiveType == PrimitiveType.INT) {
			return "0";
		} else if (primitiveType == PrimitiveType.LONG) {
			return "0";
		} else if (primitiveType == PrimitiveType.FLOAT) {
			return "0";
		} else if (primitiveType == PrimitiveType.DOUBLE) {
			return "0";
		} else if (primitiveType == PrimitiveType.BYTE) {
			return "0";
		} else if (primitiveType == PrimitiveType.BASE64_BINARY) {
			return "new byte[]{}";
		} else if (primitiveType == PrimitiveType.DATETIME) {
			return "java.util.Calendar.getInstance().getTime()";
		} else {
			return "null";
		}
	}

}
