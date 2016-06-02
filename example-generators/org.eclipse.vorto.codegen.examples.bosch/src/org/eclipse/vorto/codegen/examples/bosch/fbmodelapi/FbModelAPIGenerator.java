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

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper;
import org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.modules.fbmodel.CXFCodeGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.modules.fbmodel.POMFbTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class FbModelAPIGenerator implements ICodeGeneratorTask<InformationModel> {

	@Override
	public void generate(InformationModel infomodel, InvocationContext mappingContext, IGeneratedWriter outputter) {
		FbModelWrapper wrappedfbm = new FbModelWrapper(infomodel
				.getProperties().get(0).getType());
		ChainedCodeGeneratorTask<FunctionblockModel> generator = new ChainedCodeGeneratorTask<FunctionblockModel>();
		generator.addTask(new GeneratorTaskFromFileTemplate<FunctionblockModel>(new POMFbTemplate()));
		generator.addTask(new CXFCodeGeneratorTask());
		generator.generate(wrappedfbm.getModel(), mappingContext, outputter);
	}
}
