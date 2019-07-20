package org.eclipse.vorto.editor.web.datatype

import com.google.inject.Guice
import com.google.inject.Injector
import org.eclipse.vorto.editor.datatype.DatatypeRuntimeModule
import org.eclipse.vorto.editor.datatype.DatatypeStandaloneSetup
import org.eclipse.vorto.editor.datatype.ide.DatatypeIdeModule
import org.eclipse.xtext.util.Modules2

class DatatypeWebSetup extends DatatypeStandaloneSetup {
			
	override Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new DatatypeRuntimeModule, new DatatypeIdeModule, new DatatypeWebModule))
	}
	
}