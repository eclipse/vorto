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