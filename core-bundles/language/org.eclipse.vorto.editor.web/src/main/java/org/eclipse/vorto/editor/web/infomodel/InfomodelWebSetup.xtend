package org.eclipse.vorto.editor.web.infomodel

import com.google.inject.Guice
import com.google.inject.Injector
import org.eclipse.vorto.editor.infomodel.InformationModelRuntimeModule
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup
import org.eclipse.vorto.editor.infomodel.ide.InformationModelIdeModule
import org.eclipse.xtext.util.Modules2

class InfomodelWebSetup extends InformationModelStandaloneSetup {
	override Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new InformationModelRuntimeModule, new InformationModelIdeModule, new InfomodelWebModule))
	}
}