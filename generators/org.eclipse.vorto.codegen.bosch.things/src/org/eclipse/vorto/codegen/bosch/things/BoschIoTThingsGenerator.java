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
package org.eclipse.vorto.codegen.bosch.things;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.bosch.things.alexa.AlexaThingsTask;
import org.eclipse.vorto.codegen.bosch.things.javaclient.JavaClientTask;
import org.eclipse.vorto.codegen.bosch.things.schema.SchemaValidatorTask;
import org.eclipse.vorto.codegen.utils.GenerationResultBuilder;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class BoschIoTThingsGenerator implements IVortoCodeGenerator {

	private static final String FALSE = "false";
	private static final String TRUE = "true";
	private static final String SIMULATOR = "simulator";
	private static final String SCHEMAVALIDATOR = "validation";
	private static final String ALEXA = "alexa";

	public IGenerationResult generate(InformationModel infomodel,
			InvocationContext invocationContext,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {

		GenerationResultZip zipOutputter = new GenerationResultZip(infomodel, getServiceKey());

		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		if (hasNoTarget(invocationContext)) {
			generator.addTask(new SchemaValidatorTask());
		} else {
			if (invocationContext.getConfigurationProperties().getOrDefault(SIMULATOR, FALSE).equalsIgnoreCase(TRUE)) {
				generator.addTask(new JavaClientTask());
			}
			
			if (invocationContext.getConfigurationProperties().getOrDefault(SCHEMAVALIDATOR, FALSE).equalsIgnoreCase(TRUE)) {
				generator.addTask(new SchemaValidatorTask());
			}
			
			if (invocationContext.getConfigurationProperties().getOrDefault(ALEXA, FALSE).equalsIgnoreCase(TRUE)) {
				generator.addTask(new AlexaThingsTask());
			}
		}
		
		generator.generate(infomodel, invocationContext, zipOutputter);
		
		return GenerationResultBuilder.from(zipOutputter).build();
	}

	private boolean hasNoTarget(InvocationContext invocationContext) {
		return (invocationContext.getConfigurationProperties().getOrDefault(SIMULATOR, FALSE).equalsIgnoreCase(FALSE) &&
		        invocationContext.getConfigurationProperties().getOrDefault(SCHEMAVALIDATOR, FALSE).equalsIgnoreCase(FALSE) &&
		        invocationContext.getConfigurationProperties().getOrDefault(ALEXA, FALSE).equalsIgnoreCase(FALSE));
	}

	@Override
	public String getServiceKey() {
		return "boschiotthings";
	}

}
