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

class FunctionBlockClassTemplate implements ITemplate<FunctionblockProperty> {		
	override getContent(FunctionblockProperty fbProperty) {
		var FunctionblockModel model = fbProperty.getType()
		
		return '''
		package «ModuleUtil.getModelPackage(model)»;

		public class «fbProperty.name.toFirstUpper» {
			
			private String displayName = "«model.displayname»";
			private String description = "«model.description»";
			private String namespace = "«model.namespace»";
			private String category = "«model.category»";
			private String version = "«model.version»";
			
			private «fbProperty.name.toFirstUpper»Configuration configuration = new «fbProperty.name.toFirstUpper»Configuration();
			
			private «fbProperty.name.toFirstUpper»Status status = new «fbProperty.name.toFirstUpper»Status();
			
			private «fbProperty.name.toFirstUpper»Fault fault = new «fbProperty.name.toFirstUpper»Fault();
			
			private «fbProperty.name.toFirstUpper»Operation operation = new «fbProperty.name.toFirstUpper»Operation();
			
			public String getDisplayName() {
				return displayName;
			}
		
			public String getDescription() {
				return description;
			}
		
			public String getNamespace() {
				return namespace;
			}
		
			public String getCategory() {
				return category;
			}
		
			public String getVersion() {
				return version;
			}	
					
			public «fbProperty.name.toFirstUpper»Configuration getConfiguration() {
				return configuration;
			}
			
			public void setConfiguration(«fbProperty.name.toFirstUpper»Configuration configuration) {
				this.configuration = configuration;
			}
			
			public «fbProperty.name.toFirstUpper»Status getStatus() {
				return status;
			}
			
			public «fbProperty.name.toFirstUpper»Fault getFault() {
				return fault;
			}
			
			public «fbProperty.name.toFirstUpper»Operation getOperation() {
				return operation;
			}
		}'''
	}
}