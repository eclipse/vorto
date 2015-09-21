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
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty

class OperationClassTemplate implements ITemplate<FunctionblockProperty> {
		
	override getContent(FunctionblockProperty fbProperty) {
		var FunctionblockModel model = fbProperty.type
		
		return '''
		package «ModuleUtil.getModelPackage(model)»;
		
		import java.util.List;
		import java.util.ArrayList;

		import org.codehaus.jackson.map.annotate.JsonSerialize;
		
		@JsonSerialize
		public class «ModuleUtil.getCamelCase(fbProperty.name)»Operation {
			private List<String> names = new ArrayList<String>();
			
			public «ModuleUtil.getCamelCase(fbProperty.name)»Operation() {
				«FOR operation : model.functionblock.operations»
					names.add("«operation.name»");
				«ENDFOR»
			}
			
			public List<String> getNames(){
				return names;
			}

			public void setNames(List<String> names) {
				this.names = names;
			}
		}'''
	}
}