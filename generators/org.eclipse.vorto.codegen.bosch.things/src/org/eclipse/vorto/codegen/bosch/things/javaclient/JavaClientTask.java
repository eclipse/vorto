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
package org.eclipse.vorto.codegen.bosch.things.javaclient;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.bosch.things.javaclient.tasks.ThingsClientGeneratorTask;
import org.eclipse.vorto.codegen.bosch.things.javaclient.tasks.ThingsIntegrationUtilGeneratorTask;
import org.eclipse.vorto.codegen.bosch.things.javaclient.templates.FunctionblockTemplate;
import org.eclipse.vorto.codegen.bosch.things.javaclient.templates.LogbackTemplate;
import org.eclipse.vorto.codegen.bosch.things.javaclient.templates.PomTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class JavaClientTask implements ICodeGeneratorTask<InformationModel> {

	private static final String CONFIG_PARAM_SKIP_CLIENT = "skipClient";
	
	@Override
	public void generate(InformationModel element, InvocationContext context, IGeneratedWriter writer) {
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		generator.addTask(new GeneratorTaskFromFileTemplate<>(new PomTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<>(new LogbackTemplate()));
		generator.addTask(new ThingsClientGeneratorTask());
		generator.addTask(new ThingsIntegrationUtilGeneratorTask());
		
		generator.generate(element, context, writer);
		
		for (FunctionblockProperty fbProperty : element.getProperties()) {
			if (context.getConfigurationProperties().getOrDefault(CONFIG_PARAM_SKIP_CLIENT, "false").equalsIgnoreCase("false")) { 
				new GeneratorTaskFromFileTemplate<>(new FunctionblockTemplate()).generate(fbProperty.getType(), context, writer);
			}
		}	
	}
}
