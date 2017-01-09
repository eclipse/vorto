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
package org.eclipse.vorto.codegen.examples.lwm2m;

import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.examples.lwm2m.tasks.FunctionBlockLeshanGeneratorTask;
import org.eclipse.vorto.codegen.examples.lwm2m.tasks.FunctionBlockXmlGeneratorTask;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class LWM2MGenerator implements IVortoCodeGenerator {
	
	private static final String CONFIG_PARAM_SKIP_CLIENT = "skipClient";

	@Override
	public IGenerationResult generate(InformationModel infomodel, InvocationContext context,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		GenerationResultZip output = new GenerationResultZip(infomodel,getServiceKey());
		
		for (FunctionblockProperty fbProperty : infomodel.getProperties()) {
			new FunctionBlockXmlGeneratorTask().generate(fbProperty.getType(), context, output);
			if (context.getConfigurationProperties().getOrDefault(CONFIG_PARAM_SKIP_CLIENT, "false").equalsIgnoreCase("false")) { 
				new FunctionBlockLeshanGeneratorTask().generate(fbProperty.getType(), context, output);
			}
		}
					
		return output;
	}

	@Override
	public String getServiceKey() {
		return "lwm2m";
	}

}
