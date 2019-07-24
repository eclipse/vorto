package org.eclipse.vorto.editor.web.datatype

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.util.Modules
import org.eclipse.vorto.editor.datatype.DatatypeRuntimeModule
import org.eclipse.vorto.editor.datatype.DatatypeStandaloneSetup
import org.eclipse.vorto.editor.datatype.ide.DatatypeIdeModule
import org.eclipse.vorto.repository.core.IModelRepositoryFactory
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.util.Modules2

@FinalFieldsConstructor
class DatatypeWebSetup extends DatatypeStandaloneSetup {

	val IModelRepositoryFactory repositoryFactory

	override Injector createInjector() {
		val runtimeModule = new DatatypeRuntimeModule()
		val webModule = new DatatypeWebModule()
		var ideModule = new DatatypeIdeModule()
		return Guice.createInjector(
			Modules2.mixin(runtimeModule, Modules.override(ideModule).with(webModule, new AbstractModule() {

				override protected configure() {
					bind(typeof(IModelRepositoryFactory)).toInstance(repositoryFactory);
				}

			})))
	}

}
