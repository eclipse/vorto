package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.emf.common.util.EList
import java.util.Set
import java.util.HashSet
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.datatype.Property

class JavaClassGeneratorUtils {
	
	static def getImports(EList<Property> properties) {
		var Set<String> imports = new HashSet<String>
		for (property : properties) {
			if (property.type instanceof ObjectPropertyType) {
				var objectType =(property.type as ObjectPropertyType).type
				if (objectType instanceof Entity) {
					imports.add("import " + ModuleUtil.getEntityPackage(objectType as Entity)+ "." + objectType.name+";")
				}
				else if (objectType instanceof Enum) {
					imports.add("import " + ModuleUtil.getEnumPackage(objectType as Enum)+"."+objectType.name+";")
				}
			}
		}
		'''
		«FOR i : imports»
			«i»
		«ENDFOR»
		'''
	}
}