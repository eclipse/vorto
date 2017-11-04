/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.artik.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class DeviceManifestTemplate implements IFileTemplate<InformationModel> {
	
	
	static final String STEREOTYPE = "TYPE"
	static final String ATTRIBUTE_KEY = "name"
	
	override getFileName(InformationModel model) {
		'''«model.name»Manifest.groovy'''
	}
	
	override getPath(InformationModel model) {
	}
	
	override getContent(InformationModel model, InvocationContext context) {
		'''
		import groovy.json.JsonSlurper
		
		import com.samsung.sami.manifest.Manifest
		import com.samsung.sami.manifest.fields.*
		import com.samsung.sami.manifest.actions.Action
		import com.samsung.sami.manifest.actions.Actionable
		import static com.samsung.sami.manifest.groovy.JsonUtil.*

		public class «model.name»Manifest implements Manifest, Actionable {
		  «FOR fbProperty : model.properties»
		  	«IF fbProperty.type.functionblock.status != null»
		  		«FOR statusProperty : fbProperty.type.functionblock.status.properties»
		  		«IF statusProperty.type instanceof PrimitivePropertyType && !context.getMappedElement(statusProperty,STEREOTYPE).hasAttribute(ATTRIBUTE_KEY)»
		  			static final «fbProperty.name.toUpperCase»_«statusProperty.name.toUpperCase» = new FieldDescriptor("«fbProperty.name»«statusProperty.name.toFirstUpper»", "«IF statusProperty.description != null»«statusProperty.description»«ENDIF»", «toType(statusProperty.type as PrimitivePropertyType)»)
		  		«ENDIF»
		  		«ENDFOR»
		  	«ENDIF»
		  «ENDFOR»
		  
		  @Override
		  List<Field> normalize(String input) {
		    def slurper = new JsonSlurper()

		    def json = slurper.parseText(input)
		
			def fields = []
			
			«FOR fbProperty : model.properties»
				«IF fbProperty.type.functionblock.status != null»
					«FOR statusProperty : fbProperty.type.functionblock.status.properties»
						«IF statusProperty.type instanceof PrimitivePropertyType && !context.getMappedElement(statusProperty,STEREOTYPE).hasAttribute(ATTRIBUTE_KEY)»
						addToList(fields, json,«fbProperty.name.toUpperCase»_«statusProperty.name.toUpperCase»)
						«ELSEIF statusProperty.type instanceof PrimitivePropertyType && context.getMappedElement(statusProperty,STEREOTYPE).hasAttribute(ATTRIBUTE_KEY)»
						addToList(fields, json,«context.getMappedElement(statusProperty,STEREOTYPE).getAttributeValue(ATTRIBUTE_KEY,"[---->INSERT TYPE HERE]")»)
					    «ENDIF»
					«ENDFOR»
				«ENDIF»
			«ENDFOR»
			
			return fields
		  }
		
		  @Override
		  List<FieldDescriptor> getFieldDescriptors() {
		    return [
		    «FOR fbProperty : model.properties»
		    	«IF fbProperty.type.functionblock.status != null»
		    		«FOR statusProperty : fbProperty.type.functionblock.status.properties SEPARATOR ","»
		    			«IF statusProperty.type instanceof PrimitivePropertyType && !context.getMappedElement(statusProperty,STEREOTYPE).hasAttribute(ATTRIBUTE_KEY)»
		    			«fbProperty.name.toUpperCase»_«statusProperty.name.toUpperCase»
		    			«ELSEIF statusProperty.type instanceof PrimitivePropertyType && context.getMappedElement(statusProperty,STEREOTYPE).hasAttribute(ATTRIBUTE_KEY)»
		    			«context.getMappedElement(statusProperty,STEREOTYPE).getAttributeValue(ATTRIBUTE_KEY,"[---->INSERT TYPE HERE]")»
		    			«ENDIF»
		    		«ENDFOR»
		    	«ENDIF»
		    «ENDFOR»
		    ]
		  }
		
		
		  @Override
		  List<Action> getActions() {
		    return [
		      «FOR fbProperty : model.properties»
		      	«FOR operation : fbProperty.type.functionblock.operations SEPARATOR ","»
		      		new Action("«fbProperty.name»_«operation.name»",«IF operation.description != null»"«operation.description»"«ELSE»""«ENDIF»)
		      	«ENDFOR»
		      «ENDFOR»
		    ]
		  }
		
		}
		'''
	}
	
	def toType(PrimitivePropertyType propertyType) {
		if (propertyType.type == PrimitiveType.BOOLEAN) {
			return "Boolean.class"
		} else if (propertyType.type == PrimitiveType.STRING) {
			return "String.class"
		} else if (propertyType.type == PrimitiveType.INT) {
			return "Integer.class"
		} else if (propertyType.type == PrimitiveType.LONG) {
			return "Long.class"
		} else if (propertyType.type == PrimitiveType.DOUBLE) {
			return "Double.class"
		} else if (propertyType.type == PrimitiveType.FLOAT) {
			return "Float.class"
		} else {
			return "String.class"
		}
	}
	
}
