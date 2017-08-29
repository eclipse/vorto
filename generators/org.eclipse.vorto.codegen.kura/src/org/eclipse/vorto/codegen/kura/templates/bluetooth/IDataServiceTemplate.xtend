/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.codegen.kura.templates.bluetooth

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann
 */
class IDataServiceTemplate implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		'''IDataService.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.javaPackageBasePath»/cloud'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package «Utils.javaPackage».cloud;
		
		public interface IDataService {
			
			«FOR fbProperty : element.properties»
			/**
			 * publishes «fbProperty.name» data to the IoT Cloud backend
			 * @param resourceId 
			 * @param data
			 */
			void publish«fbProperty.name.toFirstUpper»(String resourceId, «fbProperty.type.name» data);
			
			«ENDFOR»
		}
		
		'''
	}
	
}