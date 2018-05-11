/*******************************************************************************
 *  Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *******************************************************************************/
package org.eclipse.vorto.codegen.mqtt.python

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.codegen.api.InvocationContext

class PythonInitTemplate implements IFileTemplate<Model> {
	override getFileName(Model model) {
		return "__init__.py";
	}
	
	override getPath(Model model) {
		return "model";
	}
	
	override getContent(Model element, InvocationContext context) {
		return ""
	}
}