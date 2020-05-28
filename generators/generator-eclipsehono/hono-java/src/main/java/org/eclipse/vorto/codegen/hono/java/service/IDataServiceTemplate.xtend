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
package org.eclipse.vorto.codegen.hono.java.service

import org.eclipse.vorto.codegen.hono.java.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelIdFactory
import org.eclipse.vorto.core.api.model.model.ModelType
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate

/**
 * @author Alexander Edelmann
 */
class IDataServiceTemplate implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		'''IDataService.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»/service'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package «Utils.getJavaPackage(element)».service;
		
		«FOR reference : element.references»
		«var modelId = ModelIdFactory.newInstance(ModelType.Functionblock,reference)»
		import «Utils.getJavaPackage(element)».model.«modelId.name»;
		«ENDFOR»
		
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
