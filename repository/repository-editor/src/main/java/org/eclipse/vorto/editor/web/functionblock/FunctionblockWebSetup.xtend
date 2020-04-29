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
package org.eclipse.vorto.editor.web.functionblock

import com.google.inject.Guice
import com.google.inject.Injector
import org.eclipse.vorto.editor.functionblock.FunctionblockRuntimeModule
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup
import org.eclipse.vorto.editor.functionblock.ide.FunctionblockIdeModule
import org.eclipse.xtext.util.Modules2

class FunctionblockWebSetup extends FunctionblockStandaloneSetup {
		
	override Injector createInjector() {
		val runtimeModule = new FunctionblockRuntimeModule()
		val webModule = new FunctionblockWebModule()
		var ideModule = new FunctionblockIdeModule()
		return Guice.createInjector(Modules2.mixin(runtimeModule, ideModule, webModule))
	}

}