package org.eclipse.vorto.codegen.service.aws

import java.util.Collections
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo
import org.eclipse.vorto.server.commons.DefaultConfigTemplate

class AWSConfigTemplate extends DefaultConfigTemplate {
	
	override getContent(GeneratorServiceInfo info) {
		super.getContent(info)
	}
	
	override getKeys() {
		return Collections.emptySet
	}
	
}
