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
 
package org.eclipse.vorto.codegen.ui.wizard.generation.templates

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class PluginXMLFileTemplate implements ITemplate<IGeneratorProjectContext>{

	public override String getContent(IGeneratorProjectContext context) {
		return '''
		<?xml version="1.0" encoding="UTF-8"?>
			<plugin>
			   <extension
			         id="«context.projectName»"
			         name="«context.generatorName»"
			         point="org.eclipse.vorto.codegen.org_eclipse_vorto_codegen_Generators">
			      <client
			            class="«context.getPackageName».«context.generatorName»"
			            menuLabel="Invoke «context.generatorName»">
			      </client>
			   </extension>
			
			</plugin>
		'''
	}

}
