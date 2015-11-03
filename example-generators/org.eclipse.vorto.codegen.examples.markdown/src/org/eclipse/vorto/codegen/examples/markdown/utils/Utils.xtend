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
package org.eclipse.vorto.codegen.examples.markdown.utils

import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.functionblock.ReturnType
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType

class Utils {
	def static String getReturnType(ReturnType type) {
		if (type instanceof ReturnPrimitiveType) {
			return (type as ReturnPrimitiveType).returnType.getName();
		}
		else if (type instanceof ReturnObjectType) {
			return (type as ReturnObjectType).returnType.name
		}
	}
	
	def static String getPropertyType(Property property) {
		if (property.type instanceof PrimitivePropertyType) {
			return (property.type as PrimitivePropertyType).type.getName();
		}
		else if (property.type instanceof ObjectPropertyType) {
			return (property.type as ObjectPropertyType).type.getName();
		}
	}
}