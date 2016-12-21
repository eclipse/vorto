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

import org.eclipse.vorto.codegen.api.ICodeGeneratorTask
import org.eclipse.vorto.codegen.api.IGenerationResult
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.api.SingleGenerationResult
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException
import org.eclipse.vorto.codegen.examples.markdown.tasks.MarkdownInformationModelGeneratorTask
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class MarkdownGenerator implements IVortoCodeGenerator {

	private static final String MARKDOWN_FILE_EXTENSION 	= ".md";
	private static final String MARKDOWN_TARGET_PATH 	= "markdown";
	
	private static final ICodeGeneratorTask<InformationModel> TASK = new MarkdownInformationModelGeneratorTask(MARKDOWN_FILE_EXTENSION, MARKDOWN_TARGET_PATH);
	
	override IGenerationResult generate(InformationModel infomodel, InvocationContext invocationContext,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		var output = new SingleGenerationResult("text/x-markdown");
		TASK.generate(infomodel, invocationContext, output);
		return output;
	}
	
	override getServiceKey() {
		return "markdown"
	}
	
}
