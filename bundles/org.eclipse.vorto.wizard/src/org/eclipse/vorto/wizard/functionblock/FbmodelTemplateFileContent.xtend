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
 package org.eclipse.vorto.wizard.functionblock

import org.eclipse.vorto.codegen.api.context.IModelProjectContext
import org.eclipse.vorto.codegen.api.tasks.ITemplate

class FbmodelTemplateFileContent implements ITemplate<IModelProjectContext> {
		
	override getContent(IModelProjectContext context) {
		return '''
	namespace com.mycompany.fb
	version «context.modelVersion»
	functionblock «context.modelName» {
		displayname "«context.modelName»"
		description "«context.modelDescription»"
		category demo

		configuration{ 
			//Please enter functionblock configuration details.
		}

		status{ 
			//Please enter functionblock status details.
		}

		fault{
			//Please enter functionblock fault configuration.
		}

		operations{
			//Please enter functionblock operations.
		}
	}	
		'''
	}
	
}