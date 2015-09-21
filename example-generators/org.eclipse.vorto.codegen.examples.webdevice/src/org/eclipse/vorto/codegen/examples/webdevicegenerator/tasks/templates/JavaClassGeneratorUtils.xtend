package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import java.util.HashSet
import java.util.Set
import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class JavaClassGeneratorUtils {
	
	static def getImports(EList<Property> properties) {
		var Set<String> imports = new HashSet<String>
		if(properties != null){
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
		}
		'''
		«FOR i : imports»
			«i»
		«ENDFOR»
		'''
	}
	
	static def getAllImportsForModel(FunctionblockModel model) {
		var EList<Property> allPropertiesList =  new BasicEList<Property>();
		if(model.functionblock.status!=null)
			allPropertiesList.addAll(model.functionblock.status.properties)
		
		if(model.functionblock.configuration!=null)
			allPropertiesList.addAll(model.functionblock.configuration.properties)
		
		if(model.functionblock.fault!=null)
			allPropertiesList.addAll(model.functionblock.fault.properties)
		
		return '''«getImports(allPropertiesList)»''' 
	}
	
	static def getImportsForStatusProperty(FunctionblockModel model) {
		if(model.functionblock.status!=null)
			return '''«getImports(model.functionblock.status.properties)»''' 
	}
	static def getImportsForConfigurationProperty(FunctionblockModel model) {
		if(model.functionblock.configuration!=null)
			return '''«getImports(model.functionblock.configuration.properties)»''' 
	}
	static def getImportsForFaultProperty(FunctionblockModel model) {
		if(model.functionblock.fault!=null)
			return '''«getImports(model.functionblock.fault.properties)»''' 
	}
}