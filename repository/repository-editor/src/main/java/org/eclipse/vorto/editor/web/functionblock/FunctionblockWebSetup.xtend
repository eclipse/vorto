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