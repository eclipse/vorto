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

class FunctionBlockClassTemplate implements ITemplate<FunctionblockModel> {		
	override getContent(FunctionblockModel model) {
		'''
		package «ModuleUtil.getModelPackage(model)»;

		public class «model.name» {
			
			private String displayName = "«model.functionblock.displayname»";
			private String description = "«model.functionblock.description»";
			private String namespace = "«model.namespace»";
			private String category = "«model.functionblock.category»";
			private String version = "«model.version»";
			
			private «model.name»Configuration configuration = new «model.name»Configuration();
			
			private «model.name»Status status = new «model.name»Status();
			
			private «model.name»Fault fault = new «model.name»Fault();
			
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
					
			public «model.name»Configuration getConfiguration() {
				return configuration;
			}
			
			public «model.name»Status getStatus() {
				return status;
			}
			
			public «model.name»Fault getFault() {
				return fault;
			}
		}'''
	}
}