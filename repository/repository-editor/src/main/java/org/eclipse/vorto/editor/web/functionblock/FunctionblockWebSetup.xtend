package org.eclipse.vorto.editor.web.functionblock

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.util.Modules
import org.eclipse.vorto.editor.functionblock.FunctionblockRuntimeModule
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup
import org.eclipse.vorto.editor.functionblock.ide.FunctionblockIdeModule
import org.eclipse.vorto.repository.core.IModelRepositoryFactory
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.util.Modules2

@FinalFieldsConstructor
class FunctionblockWebSetup extends FunctionblockStandaloneSetup {
	
	val IModelRepositoryFactory repositoryFactory
	
	override Injector createInjector() {
		val runtimeModule = new FunctionblockRuntimeModule()
		val webModule = new FunctionblockWebModule()
		var ideModule = new FunctionblockIdeModule()
		return Guice.createInjector(
			Modules2.mixin(runtimeModule, Modules.override(ideModule).with(webModule, new AbstractModule() {

				override protected configure() {
					bind(typeof(IModelRepositoryFactory)).toInstance(repositoryFactory);
				}

			})))
	}

}