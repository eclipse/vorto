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
package org.eclipse.vorto.codegen.bosch.things.alexa;

import org.eclipse.emf.common.util.EList;
import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.bosch.things.alexa.templates.AlexaIndentSchemaTemplate;
import org.eclipse.vorto.codegen.bosch.things.alexa.templates.AlexaSkillLambdaTemplate;
import org.eclipse.vorto.codegen.bosch.things.alexa.templates.AlexaSlotTypeTemplate;
import org.eclipse.vorto.codegen.bosch.things.alexa.templates.AlexaUtterancesTemplate;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class AlexaThingsTask implements ICodeGeneratorTask<InformationModel> {

	@Override
	public void generate(InformationModel infomodel, InvocationContext context, IGeneratedWriter writer) {
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new AlexaIndentSchemaTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new AlexaSkillLambdaTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new AlexaUtterancesTemplate()));
		
		generateCustomSlotTypes(infomodel, context, writer);
		
		generator.generate(infomodel, context, writer);
	}

	private void generateCustomSlotTypes(InformationModel infomodel, InvocationContext context,
			IGeneratedWriter writer) {
		for (FunctionblockProperty fbModel : infomodel.getProperties()) {
			 EList<org.eclipse.vorto.core.api.model.datatype.Enum> enums = Utils.getReferencedEnums(fbModel.getType().getFunctionblock());
			 for (org.eclipse.vorto.core.api.model.datatype.Enum enumeration : enums) {
				 ICodeGeneratorTask<org.eclipse.vorto.core.api.model.datatype.Enum> task = new GeneratorTaskFromFileTemplate<>(new AlexaSlotTypeTemplate());
				 task.generate(enumeration, context, writer);
			 }
		}
	}

}
