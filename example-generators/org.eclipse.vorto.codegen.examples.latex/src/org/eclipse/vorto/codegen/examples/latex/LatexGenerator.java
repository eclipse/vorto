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
package org.eclipse.vorto.codegen.examples.latex;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.examples.latex.tasks.LatexInformationModelGeneratorTask;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;


public class LatexGenerator implements IVortoCodeGenerator {
	public static final String LATEX_PROJECT_SUFFIX 	= "_latex";
	public static final String LATEX_FILE_EXTENSION 	= ".tex";
	public static final String LATEX_TARGET_PATH 		= "latex";

	public IGenerationResult generate(InformationModel infomodel, InvocationContext invocationContext,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		
		GenerationResultZip zipOutputter = new GenerationResultZip(infomodel,getServiceKey());
		
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		generator.addTask(new LatexInformationModelGeneratorTask(LATEX_FILE_EXTENSION, LATEX_TARGET_PATH));
		generator.generate(infomodel ,invocationContext, zipOutputter);
		
		return zipOutputter;
	}

	@Override
	public String getServiceKey() {
		return "latex";
	}

	

}
