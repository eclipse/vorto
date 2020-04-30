/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.codegen.templates.java

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.templates.java.utils.ValueMapper
import org.eclipse.vorto.core.api.model.datatype.Entity
import org.eclipse.vorto.core.api.model.datatype.Enum
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.Param
import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * Use Plugin SDK API instead!
 */
@Deprecated
class JavaClassMethodTemplate implements ITemplate<Operation>{
	
	var ITemplate<Param> parameter
	
	new(ITemplate<Param> parameter) {
		this.parameter = parameter;
	}
	
	override getContent(Operation op,InvocationContext invocationContext) {
		'''
			/**
			* «op.description»
			*/
			
			«IF op.returnType instanceof ReturnObjectType»
				«var objectType = op.returnType as ReturnObjectType»
				public «objectType.returnType.name» «op.name»(«getParameterString(op,invocationContext)») {
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
				public «primitiveType.returnType.getName» «op.name»(«getParameterString(op,invocationContext)») {
					«IF ValueMapper.getInitialValue(primitiveType.returnType).equalsIgnoreCase("")» 
						«primitiveType.returnType.getName» result;
					«ELSE»
						«primitiveType.returnType.getName» result = «ValueMapper.getInitialValue(primitiveType.returnType)»;
					«ENDIF»
					// Add your code here.
					
					return result;
				}
			«ELSE»
				public void «op.name»(«getParameterString(op,invocationContext)») {
					// Add your code here.
				}
			«ENDIF»
		'''
	}
	
	public def String getParameterString(Operation op,InvocationContext invocationContext) {
		var String result="";
		for (param : op.params) {
			result =  result + ", " + parameter.getContent(param,invocationContext);
		}
		if (result.isNullOrEmpty) {
			return "";
		}
		else {
			return result.substring(2, result.length).replaceAll("\n", "").replaceAll("\r", "");
		}
	}
}