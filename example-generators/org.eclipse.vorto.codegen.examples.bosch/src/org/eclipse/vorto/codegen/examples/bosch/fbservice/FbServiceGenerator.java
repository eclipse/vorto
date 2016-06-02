/*******************************************************************************
 *  Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.examples.bosch.fbservice;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.DummyBaseDriverGenerator;
import org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.FbModelAPIGenerator;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.BluePrintGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.DriverImplGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.EventMappingGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.FbImplGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.POMGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.AbstractDeviceDriverTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.AbstractDeviceServiceTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.AbstractEventMappingTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.EventMappingContextTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.EventMappingsConfigurationTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.IEventMappingTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class FbServiceGenerator implements IVortoCodeGenerator {

	private static final String LATEST_M2M_PLATFORM_VERSION = null;
	
	@Override
	public IGenerationResult generate(InformationModel infomodel, InvocationContext invocationContext) {

		GenerationResultZip zipOutput = new GenerationResultZip(infomodel,getServiceKey());
		
		FunctionblockModel fbm = infomodel.getProperties().get(0).getType();

		try {
			new FbModelAPIGenerator().generate(infomodel, invocationContext, zipOutput);
		} catch (Exception ex) {
			// do not stop generation
			ex.printStackTrace();
		}

		try {
			new DummyBaseDriverGenerator().generate(fbm, invocationContext, zipOutput);
		} catch (Exception ex) {
			// do not stop generation
			ex.printStackTrace();
		}

		try {
			generateFunctionBlockImplementationProject(fbm, invocationContext, zipOutput);
		} catch (Exception ex) {
			// do not stop generation
			ex.printStackTrace();
		}
		
		return zipOutput;
	}

	private void generateFunctionBlockImplementationProject(
			FunctionblockModel fbm, InvocationContext mappingContext, IGeneratedWriter zipOutput) {

		/**
		 * GENERATOR INVOCATION OF IMPL PROJECT
		 */

		FbModelWrapper modelWrapper = new FbModelWrapper(fbm);
		ChainedCodeGeneratorTask<FunctionblockModel> generator = new ChainedCodeGeneratorTask<FunctionblockModel>();
		generator.addTask(new POMGeneratorTask(modelWrapper, LATEST_M2M_PLATFORM_VERSION));
		generator.addTask(new GeneratorTaskFromFileTemplate<FunctionblockModel>(new AbstractDeviceDriverTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<FunctionblockModel>(new AbstractDeviceServiceTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<FunctionblockModel>(new AbstractEventMappingTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<FunctionblockModel>(new EventMappingContextTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<FunctionblockModel>(new EventMappingsConfigurationTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<FunctionblockModel>(new IEventMappingTemplate()));
		generator.addTask(new BluePrintGeneratorTask(modelWrapper));
		generator.addTask(new FbImplGeneratorTask(modelWrapper));
		generator.addTask(new DriverImplGeneratorTask(modelWrapper));
		generator.addTask(new EventMappingGeneratorTask(modelWrapper));
		generator.generate(fbm, mappingContext, zipOutput);
	}

	@Override
	public String getServiceKey() {
		return "bosch";
	}
}
