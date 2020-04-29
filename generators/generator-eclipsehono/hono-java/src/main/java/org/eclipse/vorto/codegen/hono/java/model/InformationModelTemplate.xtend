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
package org.eclipse.vorto.codegen.hono.java.model

import org.eclipse.vorto.codegen.hono.java.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.plugin.generator.InvocationContext
import org.eclipse.vorto.plugin.generator.utils.IFileTemplate

/**
 * 
 * @author Erle Czar Mantos - Robert Bosch (SEA) Pte. Ltd.
 *
 */
class InformationModelTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name».java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»/model'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.getJavaPackage(element)».model;

public class «element.name» {
	«FOR fbProperty : element.properties»
	private «fbProperty.type.name» «fbProperty.name»;
	«ENDFOR»
	
	private String resourceId;
	
	public «element.name»(String resourceId) {
		this.resourceId = resourceId;
	}
	
	«FOR fbProperty : element.properties»
	public «fbProperty.type.name» get«fbProperty.name.toFirstUpper»() {
		return «fbProperty.name»;
	}

	public void set«fbProperty.name.toFirstUpper»(«fbProperty.type.name» «fbProperty.name») {
		this.«fbProperty.name» = «fbProperty.name»;
	}
	
	«ENDFOR»
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	public String getResourceId() {
		return resourceId;
	}
}
'''
	}
	
}
