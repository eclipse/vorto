/** 
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.editor.datatype.ui.typecreator

class TypeTemplate {
	String name
	String datatype

	new(String type, String name) {
		this.datatype = type
		this.name = name
	}

	def String getTemplate() {
		var StringBuilder a = new StringBuilder()
		a.append("namespace")
		a.append(" com.mycompany")
		a.append("\n")
		a.append("version")
		a.append(" 1.0.0")
		a.append("\n")
		a.append(datatype).append(" ").append(name).append(Character.valueOf('{').charValue)
		a.append("\n")
		a.append("}")
		return a.toString()
	}
}
