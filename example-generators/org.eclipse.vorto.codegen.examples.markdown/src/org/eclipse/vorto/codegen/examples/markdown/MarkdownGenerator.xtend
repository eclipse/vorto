/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.markdown

import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.vorto.codegen.api.ICodeGenerator
import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.examples.markdown.tasks.InformationModelGeneratorTask
import org.eclipse.vorto.codegen.examples.markdown.templates.FunctionBlockTemplate
import org.eclipse.vorto.codegen.examples.markdown.templates.EntityTemplate
import org.eclipse.vorto.codegen.examples.markdown.templates.EnumTemplate

class MarkdownGenerator implements ICodeGenerator<InformationModel> {

	override generate(InformationModel infomodel, IProgressMonitor monitor) {
		val EntityTemplate entityTemplate = new EntityTemplate();
		val EnumTemplate enumTemplate = new EnumTemplate();
		val FunctionBlockTemplate fbTemplate = new FunctionBlockTemplate();
		new EclipseProjectGenerator(infomodel.getName()+"Documentation")
			.addTask(new InformationModelGeneratorTask(".md", "docs", fbTemplate, entityTemplate, enumTemplate))
			.javaNature().generate(infomodel,monitor);
	}
	
	override getName() {
		return "MarkdownGenerator";
	}
}
