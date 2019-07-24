package org.eclipse.vorto.editor.web.infomodel

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.util.Modules
import org.eclipse.vorto.editor.infomodel.InformationModelRuntimeModule
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup
import org.eclipse.vorto.editor.infomodel.ide.InformationModelIdeModule
import org.eclipse.vorto.repository.core.IModelRepositoryFactory
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.util.Modules2

@FinalFieldsConstructor
class InfomodelWebSetup extends InformationModelStandaloneSetup {
	
	val IModelRepositoryFactory repositoryFactory
	
	override Injector createInjector() {
		val runtimeModule = new InformationModelRuntimeModule()
		val webModule = new InfomodelWebModule()
		var ideModule = new InformationModelIdeModule()
		return Guice.createInjector(
			Modules2.mixin(runtimeModule, Modules.override(ideModule).with(webModule, new AbstractModule() {

				override protected configure() {
					bind(typeof(IModelRepositoryFactory)).toInstance(repositoryFactory);
				}

			})))
	}

}