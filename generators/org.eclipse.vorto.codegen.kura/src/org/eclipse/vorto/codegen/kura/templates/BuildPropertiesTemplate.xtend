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
package org.eclipse.vorto.codegen.kura.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class BuildPropertiesTemplate implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		'''build.properties'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getBasePath(context)»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		output.. = bin/
		bin.includes = META-INF/,\
		               .,\
		               OSGI-INF/,\
		               «IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true") ||
		               	   context.configurationProperties.getOrDefault("boschhub","false").equalsIgnoreCase("true")»
		               lib/,\
		               secret/,\
		               «ENDIF»
		               build.properties
		source.. = src/
		src.includes = bin/,\
		               OSGI-INF/,\
		               META-INF/,\
		               «IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")  ||
		               	   context.configurationProperties.getOrDefault("boschhub","false").equalsIgnoreCase("true")»
		               lib/,\
		               secret/,\
		               «ENDIF»
		               build.properties
		'''
	}
	
}