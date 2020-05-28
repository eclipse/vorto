/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.vorto.plugin.generator.utils.javatemplates

import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.ITemplate

class JavaFunctionblockPropertySetterDeclarationTemplate implements ITemplate<FunctionblockProperty>{

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
			public void «setterPrefix»«fbProperty.name.toFirstUpper»(«interfacePrefix»«fbProperty.type.name» «fbProperty.name»);
		'''
	}
}
