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
import org.eclipse.vorto.codegen.examples.markdown.tasks.MarkdownInformationModelGeneratorTask

class MarkdownGenerator implements ICodeGenerator<InformationModel> {

	public static final String MARKDOWN_PROJECT_SUFFIX 	= "_Markdown";
	public static final String MARKDOWN_FILE_EXTENSION 	= ".md";
	public static final String MARKDOWN_TARGET_PATH 	= "markdown";
	
	override generate(InformationModel infomodel, IProgressMonitor monitor) {
		new EclipseProjectGenerator(infomodel.getName()+MARKDOWN_PROJECT_SUFFIX)
			.addTask(new MarkdownInformationModelGeneratorTask(MARKDOWN_FILE_EXTENSION, MARKDOWN_TARGET_PATH))
			.javaNature().generate(infomodel,monitor);
	}
	
	override getName() {
		return "MarkdownGenerator";
	}
}
