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
package org.eclipse.vorto.codegen.kura.templates.cloud

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

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
		'''«Utils.javaPackageBasePath»/cloud'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.javaPackage».cloud;

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