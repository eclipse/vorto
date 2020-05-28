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
package org.eclipse.vorto.codegen.hono.python

import org.eclipse.vorto.core.api.model.model.Model
import org.eclipse.vorto.plugin.generator.InvocationContext

class PythonInitTemplate extends PythonTemplate <Model> {
	override getFileName(Model model) {
		return "__init__.py";
	}
	
	override getPath(Model model) {
		return rootPath;
	}
	
	override getContent(Model element, InvocationContext context) {
		return ""
	}
}