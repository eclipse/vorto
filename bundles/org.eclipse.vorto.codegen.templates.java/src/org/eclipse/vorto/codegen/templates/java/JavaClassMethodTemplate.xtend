/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.templates.java

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.codegen.templates.java.utils.ValueMapper

class JavaClassMethodTemplate implements ITemplate<Operation>{
	
	var ITemplate<Param> parameter
	
	new(ITemplate<Param> parameter) {
		this.parameter = parameter;
	}
	
	override getContent(Operation op) {
		'''
			/**
			* «op.description»
			*/
			
			«IF op.returnType instanceof ReturnObjectType»
				«var objectType = op.returnType as ReturnObjectType»
				public «objectType.returnType.name» «op.name»(«getParameterString(op)») {
					«IF objectType.returnType instanceof Entity»
						«objectType.returnType.name» result = new «objectType.returnType.name»();
						// Add your code here.
						
						return result;
					«ELSEIF objectType.returnType instanceof Enum»
						// Add your code here.
						
						return «objectType.returnType.name».«(objectType.returnType as Enum).enums.get(0).name.toUpperCase»;
					«ENDIF»
				}
			«ELSEIF op.returnType instanceof ReturnPrimitiveType»
				«var primitiveType = op.returnType as ReturnPrimitiveType»
				public «primitiveType.returnType.getName» «op.name»(«getParameterString(op)») {
					«IF ValueMapper.getInitialValue(primitiveType.returnType).equalsIgnoreCase("")» 
						«primitiveType.returnType.getName» result;
					«ELSE»
						«primitiveType.returnType.getName» result = «ValueMapper.getInitialValue(primitiveType.returnType)»;
					«ENDIF»
					// Add your code here.
					
					return result;
				}
			«ELSE»
				public void «op.name»(«getParameterString(op)») {
					// Add your code here.
				}
			«ENDIF»
		'''
	}
	
	public def String getParameterString(Operation op) {
		var String result="";
		for (param : op.params) {
			result =  result + ", " + parameter.getContent(param);
		}
		if (result.isNullOrEmpty) {
			return "";
		}
		else {
			return result.substring(2, result.length).replaceAll("\n", "").replaceAll("\r", "");
		}
	}
}