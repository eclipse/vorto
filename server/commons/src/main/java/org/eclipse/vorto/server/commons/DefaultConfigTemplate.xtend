package org.eclipse.vorto.server.commons

import java.util.Collections
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo
import org.eclipse.vorto.server.commons.IGeneratorConfigUITemplate

class DefaultConfigTemplate implements IGeneratorConfigUITemplate {
	
	override getContent(GeneratorServiceInfo info) {
		'''
		'''
	}
	
	override getKeys() {
		return Collections.emptySet
	}
	
}
