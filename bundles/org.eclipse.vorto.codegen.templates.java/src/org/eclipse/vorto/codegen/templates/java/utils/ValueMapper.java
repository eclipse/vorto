/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.templates.java.utils;

import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;

public class ValueMapper {
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
		}
		else if (type.compareTo(PrimitiveType.BOOLEAN) ==0) {
			return "false";
		}
		return "";
	}
}
