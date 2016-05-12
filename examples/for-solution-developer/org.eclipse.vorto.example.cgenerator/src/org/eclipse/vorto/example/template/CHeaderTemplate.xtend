package org.eclipse.vorto.example.template

import java.util.UUID
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

/**
 * @author Ying Shaodong - Robert Bosch (SEA) Pte. Ltd.
 */
class CHeaderTemplate implements IFileTemplate<FunctionblockModel> {

	override getFileName(FunctionblockModel context) {
		return '''«context.name».h'''
	}
	
	override getPath(FunctionblockModel context) {
		return "output"
	}
	
	override getContent(FunctionblockModel context) {
		'''
		#ifndef «context.name.toUpperCase»_H_
		#define «context.name.toUpperCase»_H_

		float get«context.name»(); 

		#endif // «context.name.toUpperCase»_H_

		'''
	}
}
