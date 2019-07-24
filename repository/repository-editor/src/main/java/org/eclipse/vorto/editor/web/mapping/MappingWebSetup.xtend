package org.eclipse.vorto.editor.web.mapping

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.util.Modules
import org.eclipse.vorto.editor.mapping.MappingRuntimeModule
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup
import org.eclipse.vorto.editor.mapping.ide.MappingIdeModule
import org.eclipse.vorto.repository.core.IModelRepositoryFactory
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.util.Modules2

@FinalFieldsConstructor
class MappingWebSetup extends MappingStandaloneSetup {
	
	val IModelRepositoryFactory repositoryFactory
	
	override Injector createInjector() {
		val runtimeModule = new MappingRuntimeModule()
		val webModule = new MappingWebModule()
		var ideModule = new MappingIdeModule()
		return Guice.createInjector(
			Modules2.mixin(runtimeModule, Modules.override(ideModule).with(webModule, new AbstractModule() {

				override protected configure() {
					bind(typeof(IModelRepositoryFactory)).toInstance(repositoryFactory);
				}

			})))
	}
}