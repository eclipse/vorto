/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class FaultClassTemplate implements ITemplate<FunctionblockModel> {
		
	override getContent(FunctionblockModel model) {
		'''
		package «ModuleUtil.getModelPackage(model)»;

		import org.codehaus.jackson.map.annotate.JsonSerialize;
		
		@JsonSerialize
		public class «model.name»Fault {			
		«IF model.functionblock.fault!=null»
			«FOR FaultField : model.functionblock.fault.properties»	
			«IF FaultField.type instanceof PrimitivePropertyType»			
				«var primitiveType = (FaultField.type as PrimitivePropertyType).getType»
				«var primitiveJavaType = PropertyUtil.toJavaFieldType(primitiveType)»
				
					private «primitiveJavaType» «FaultField.name» = «PropertyUtil.getDefaultValue(primitiveType)»;
				
					public «primitiveJavaType» get«FaultField.name.toFirstUpper»() {
						return «FaultField.name»;
					}
							
					public void set«FaultField.name.toFirstUpper»(«primitiveJavaType» «FaultField.name») {
						this.«FaultField.name» = «FaultField.name»;
					}		
		    «ENDIF»
			«ENDFOR»						
		«ENDIF»			
		}'''
	}
}