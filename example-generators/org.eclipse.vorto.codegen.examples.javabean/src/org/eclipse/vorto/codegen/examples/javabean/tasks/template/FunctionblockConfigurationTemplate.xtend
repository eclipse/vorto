package org.eclipse.vorto.codegen.examples.javabean.tasks.template

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.examples.templates.java.JavaClassFieldGetterTemplate
import org.eclipse.vorto.codegen.examples.templates.java.JavaClassFieldSetterTemplate
import org.eclipse.vorto.codegen.examples.templates.java.JavaClassFieldTemplate
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class FunctionblockConfigurationTemplate implements ITemplate<FunctionblockModel> {

	private JavaClassFieldTemplate propertyTemplate;
	private JavaClassFieldSetterTemplate propertySetterTemplate;
	private JavaClassFieldGetterTemplate propertyGetterTemplate;
	
	private String[] imports;
	private String packageName;
	private String implSuffix;
	
	new(String[] imports, String implSuffix, String packageName) {
		this.imports = imports;
		this.packageName = packageName;
		this.implSuffix = implSuffix;
		this.propertyTemplate = new JavaClassFieldTemplate();
		this.propertySetterTemplate = new JavaClassFieldSetterTemplate("set");
		this.propertyGetterTemplate = new JavaClassFieldGetterTemplate("get");
	}
	
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
		package «packageName»;
		
		«FOR String _import : imports»
		import «_import».*;
		«ENDFOR»
		
		public class «context.name»«implSuffix»{
			
			«IF context.functionblock.configuration != null»
			«FOR property : context.functionblock.configuration.properties»
				«propertyTemplate.getContent(property,invocationContext)»
			«ENDFOR»
			
			«FOR property : context.functionblock.configuration.properties»
				«propertySetterTemplate.getContent(property,invocationContext)»
				«propertyGetterTemplate.getContent(property,invocationContext)»
			«ENDFOR»
			«ENDIF»
		}
		'''
	}
}
