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
package org.eclipse.vorto.codegen.examples.templates.java

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.codegen.api.mapping.InvocationContext

class JavaFunctionblockPropertySetterTemplate implements ITemplate<FunctionblockProperty>{

	var String setterPrefix;
	var String interfacePrefix;
	
	new(String setterPrefix, String interfacePrefix) {
		this.setterPrefix = setterPrefix;
		this.interfacePrefix = interfacePrefix;
	}
	
	override getContent(FunctionblockProperty fbProperty,InvocationContext invocationContext) {
		'''
				/**
				* Setter for «fbProperty.name».
				*/
				public void «setterPrefix»«fbProperty.name.toFirstUpper»(«interfacePrefix»«fbProperty.type.name» «fbProperty.name») {
					this.«fbProperty.name» = «fbProperty.name»;
				}
		'''
	}
}