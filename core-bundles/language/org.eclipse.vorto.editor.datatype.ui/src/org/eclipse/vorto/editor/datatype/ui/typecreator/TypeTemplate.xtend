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
