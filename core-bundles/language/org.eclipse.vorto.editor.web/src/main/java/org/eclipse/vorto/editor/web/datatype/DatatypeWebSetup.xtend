package org.eclipse.vorto.editor.web.datatype

import com.google.inject.Guice
import com.google.inject.Injector
import org.eclipse.vorto.editor.datatype.DatatypeRuntimeModule
import org.eclipse.vorto.editor.datatype.DatatypeStandaloneSetup

class DatatypeWebSetup extends DatatypeStandaloneSetup {
			
	override Injector createInjector() {
		val runtimeModule = new DatatypeRuntimeModule()
		val webModule = new DatatypeWebModule()
		return Guice.createInjector(runtimeModule,webModule);
	}
	
}