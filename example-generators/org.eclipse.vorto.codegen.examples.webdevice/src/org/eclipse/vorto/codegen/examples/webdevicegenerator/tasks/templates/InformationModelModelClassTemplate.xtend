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
package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates;

import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil

/**
 * @author sgp0372
 *
 */
class InformationModelModelClassTemplate implements ITemplate<InformationModel> {
	
	override getContent(InformationModel infoModel) {
		return '''
		package «ModuleUtil.getInfoModelModelPackage(infoModel)»;
		
		public class «infoModel.name.toFirstUpper»Model {
			
			private String displayName = "«infoModel.displayname»";
			private String description = "«infoModel.description»";
			private String namespace = "«infoModel.namespace»";
			private String category = "«infoModel.category»";
			private String version = "«infoModel.version»";			
						
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
		}'''
	}

}
