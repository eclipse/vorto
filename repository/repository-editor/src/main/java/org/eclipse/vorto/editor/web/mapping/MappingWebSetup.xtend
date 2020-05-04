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
package org.eclipse.vorto.editor.web.mapping

import com.google.inject.Guice
import com.google.inject.Injector
import org.eclipse.vorto.editor.mapping.MappingRuntimeModule
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup
import org.eclipse.vorto.editor.mapping.ide.MappingIdeModule
import org.eclipse.xtext.util.Modules2

class MappingWebSetup extends MappingStandaloneSetup {
		
	override Injector createInjector() {
		val runtimeModule = new MappingRuntimeModule()
		val webModule = new MappingWebModule()
		var ideModule = new MappingIdeModule()
		return Guice.createInjector(
			Modules2.mixin(runtimeModule, ideModule,webModule))
	}
}