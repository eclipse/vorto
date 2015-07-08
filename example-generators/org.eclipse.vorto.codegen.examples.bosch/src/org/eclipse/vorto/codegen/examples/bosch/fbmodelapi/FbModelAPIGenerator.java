/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.bosch.fbmodelapi;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.examples.bosch.common.BoschM2MModelNature;
import org.eclipse.vorto.codegen.examples.bosch.common.BoschM2MNature;
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper;
import org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.modules.fbmodel.CXFCodeGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.modules.fbmodel.POMFbTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class FbModelAPIGenerator implements ICodeGenerator<InformationModel> {

	private static final String ADDITIONAL_SOURCES_FOLDER = "target/generated-sources";
	private static final String SRC_GEN_FOLDER = "src-gen";

	private static final String SUFFIX = "-model";

	@Override
	public void generate(InformationModel infomodel,
			final IProgressMonitor monitor) {
		FbModelWrapper wrappedfbm = new FbModelWrapper(infomodel
				.getProperties().get(0).getType());
		final String modelProjectName = wrappedfbm.getModel().getNamespace()
				+ "." + wrappedfbm.getFunctionBlockName().toLowerCase()
				+ SUFFIX;

		new EclipseProjectGenerator<FunctionblockModel>(modelProjectName)
				.addFolder(SRC_GEN_FOLDER)
				.addFolder(ADDITIONAL_SOURCES_FOLDER)
				.mavenNature(new POMFbTemplate(), ADDITIONAL_SOURCES_FOLDER,
						SRC_GEN_FOLDER).addNature(BoschM2MNature.M2M_NATURE_ID)
				.addNature(BoschM2MModelNature.M2M_MODEL_NATURE_ID)
				.addTask(new CXFCodeGeneratorTask())
				.generate(wrappedfbm.getModel(), monitor);
	}

	@Override
	public String getName() {
		return "Function Block Model API Generator";
	}
}
