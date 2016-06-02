/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.wizard.datatype

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.ui.context.IModelProjectContext

/**
 * This is template content when new entity/enum type is created typically from wizard.
 *
 */
class DataTypeFileTemplate implements ITemplate<IModelProjectContext> {
	
	private String typeName;	
	
	new(String typeName) {
		this.typeName = typeName;
	}
	
	override getContent(IModelProjectContext context,InvocationContext invocationContext) {
		//Namespace and version needs to be added..
		return '''
		namespace com.mycompany.type
		version 1.0.0
		displayname "«context.modelName»"
		description "«context.modelDescription»"
		category demo		
		«typeName» «context.modelName» {
			//Enter «typeName» details
		}
		'''
	}
	
}