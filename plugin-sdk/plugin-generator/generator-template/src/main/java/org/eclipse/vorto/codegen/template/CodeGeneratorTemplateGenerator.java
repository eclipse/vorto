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
package org.eclipse.vorto.codegen.template;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.template.plugin.GeneratorMainTemplate;
import org.eclipse.vorto.codegen.template.plugin.HelloWorldTemplate;
import org.eclipse.vorto.codegen.template.runner.GenPropertiesFileTemplate;
import org.eclipse.vorto.codegen.template.runner.GeneratorConfigurationTemplate;
import org.eclipse.vorto.codegen.template.runner.GeneratorRunnerTemplate;
import org.eclipse.vorto.codegen.template.runner.LocalPropertiesFileTemplate;
import org.eclipse.vorto.codegen.template.runner.PomTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class CodeGeneratorTemplateGenerator implements IVortoCodeGenerator {

	@Override
	public IGenerationResult generate(InformationModel model, InvocationContext context,
			IVortoCodeGenProgressMonitor monitor) {
		
		final String serviceKey = context.getConfigurationProperties().getOrDefault("serviceKey", "myplatform").replace(" ", "");
		
		GenerationResultZip output = new GenerationResultZip(model, getServiceKey());
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		// Generates the parent project
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new org.eclipse.vorto.codegen.template.parent.PomTemplate()));
		
		// Generates the Vorto Code Generator Runner Project
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new GeneratorConfigurationTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new GeneratorRunnerTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new GenPropertiesFileTemplate(serviceKey)));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new LocalPropertiesFileTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new PomTemplate()));
		
		// Generates the Vorto Code Generator Plugin Project
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new org.eclipse.vorto.codegen.template.plugin.PomTemplate(serviceKey)));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new GeneratorMainTemplate(serviceKey)));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new HelloWorldTemplate(serviceKey)));

		generator.generate(model, context, output);
		return output;
	}
	@Override
	public String getServiceKey() {
		return "generator_template";
	}

	@Override
	public GeneratorInfo getInfo() {
		return GeneratorInfo.basicInfo("Vorto Generator Template",
				"Generates a Vorto Code Generator template",
				"Vorto Team");
	}
}
