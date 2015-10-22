package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.datatype.Enum

class EnumTemplate implements ITemplate<Enum> {
		
	override getContent(Enum e) {
		return '''
		package «ModuleUtil.getEnumPackage(e)»;
		public enum «ModuleUtil.getCamelCase(e.name)» {
			«IF e.enums.length > 0»
				«FOR i:0 ..< e.enums.length -1»
					«e.enums.get(i).name»,
				«ENDFOR»
			«ENDIF»
			«e.enums.last.name»
		}
		'''
	}
}