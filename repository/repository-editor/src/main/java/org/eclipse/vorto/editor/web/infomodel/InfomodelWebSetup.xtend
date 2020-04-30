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
package org.eclipse.vorto.editor.web.infomodel

import com.google.inject.Guice
import com.google.inject.Injector
import org.eclipse.vorto.editor.infomodel.InformationModelRuntimeModule
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup
import org.eclipse.vorto.editor.infomodel.ide.InformationModelIdeModule
import org.eclipse.xtext.util.Modules2

class InfomodelWebSetup extends InformationModelStandaloneSetup {
		
	override Injector createInjector() {
		val runtimeModule = new InformationModelRuntimeModule()
		val webModule = new InfomodelWebModule()
		var ideModule = new InformationModelIdeModule()
		return Guice.createInjector(
			Modules2.mixin(runtimeModule, ideModule, webModule))

	}

}