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
package org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks

import org.eclipse.vorto.codegen.api.tasks.Generated
import org.eclipse.vorto.codegen.api.tasks.IOutputter
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.EventMappingRuleTemplate
import org.eclipse.vorto.core.api.model.functionblock.Event
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class EventMappingGeneratorTask extends AbstractGeneratorTask<FunctionblockModel> {
	
	private String SRC_LOC = "/src/main/java/";
	
	new(FbModelWrapper model) {
		super(model)
	}
	
	override generate(FunctionblockModel fbm,IOutputter outputter) {
		for (Event event : fbm.functionblock.events) {
			outputter.output(new Generated(getEventMappingFileName(event),location,EventMappingRuleTemplate.generate(context,event)));	
		}
	}
		
	def String getEventMappingFileName(Event event) {
		'''«event.name»Mapping.java'''
	}
	
	def getLocation() {
		SRC_LOC + getPackageStructure() + "/";
	}
	
	def String getPackageStructure() {
		return context.javaPackageName.toString().replaceAll("\\.", "/") + "/internal/mapping";
	}
}