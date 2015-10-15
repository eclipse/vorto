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
package org.eclipse.vorto.codegen.templates.java

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty

class JavaFunctionblockPropertyGetterTemplate implements ITemplate<FunctionblockProperty>{

	var String getterPrefix;
	var String interfacePrefix;
	
	new(String getterPrefix, String interfacePrefix) {
		this.getterPrefix = getterPrefix;
		this.interfacePrefix = interfacePrefix;
	}
	
	override getContent(FunctionblockProperty fbProperty) {
		'''
				/**
				* Getter for «fbProperty.name».
				*/
				public «interfacePrefix»«fbProperty.type.name» «getterPrefix»«fbProperty.name.toFirstUpper»() {
					return «fbProperty.name»;
				}
		'''
	}
}