package org.eclipse.vorto.editor.web.mapping

import com.google.inject.Guice
import com.google.inject.Injector
import org.eclipse.vorto.editor.mapping.MappingRuntimeModule
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup
import org.eclipse.vorto.editor.mapping.ide.MappingIdeModule
import org.eclipse.xtext.util.Modules2

class MappingWebSetup extends MappingStandaloneSetup {
	override Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new MappingRuntimeModule, new MappingIdeModule, new MappingWebModule))
	}
}