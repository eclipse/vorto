package org.eclipse.vorto.codegen.service.aws

import java.util.Collections
import org.eclipse.vorto.codegen.api.GeneratorServiceInfo
import org.springframework.stereotype.Service
import org.eclipse.vorto.service.generator.web.DefaultConfigTemplate

class AWSConfigTemplate extends DefaultConfigTemplate {
	
	override getContent(GeneratorServiceInfo info) {
		super.getContent(info)
	}
	
	override getKeys() {
		return Collections.emptySet
	}
	
}
