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
package org.eclipse.vorto.codegen.examples.jsonschema;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.ConfigurationValidationTask;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.EventValidationTask;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.FaultValidationTask;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.OperationParametersValidationTask;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.OperationReturnTypeValidationTask;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.StatusValidationTask;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class JsonschemaGenerator implements IVortoCodeGenerator {

	public static final String JSON_SCHEMA_FILE_EXTENSION 	= ".schema.json";
	public static final String TARGET_PATH 					= "json";

	public IGenerationResult generate(InformationModel infomodel,
			InvocationContext invocationContext) {

		GenerationResultZip zipOutputter = new GenerationResultZip(infomodel,
				getServiceKey());

		for (FunctionblockProperty fbp : infomodel.getProperties()) {
			FunctionBlock fb = fbp.getType().getFunctionblock();
			generateForFunctionblock(
					fb,  
					TARGET_PATH + "/" 
							+ fbp.getType().getNamespace() + "."
							+ fbp.getType().getName() + "_"
							+ fbp.getType().getVersion(),
					JSON_SCHEMA_FILE_EXTENSION,
					zipOutputter);
		}
		return zipOutputter;
	}

	public void generateForFunctionblock(
			FunctionBlock fb, 
			String targetPath, 
			String jsonFileExtension,
			IGeneratedWriter outputter) {
		if (fb == null) {
			throw new RuntimeException("fb is null");
		}
		
		if (fb.getOperations() != null) {
			for (Operation op : fb.getOperations()) {
				generateForOperation(
						op, 
						targetPath,
						jsonFileExtension, 
						outputter);
			}
		}
		
		Configuration configuration = fb.getConfiguration();
		if (configuration != null) {
			generateForConfiguration(configuration, targetPath, jsonFileExtension, outputter);
		}
		
		Status status = fb.getStatus();
		if (status != null) {
			generateForStatus(status, targetPath, jsonFileExtension, outputter);
		}
		
		Fault fault = fb.getFault();
		if (fault != null) {
			generateForFault(fault, targetPath, jsonFileExtension, outputter);
		}
		
		if (fb.getEvents() != null) {
			for (Event event : fb.getEvents()) {
				generateForEvent(
						event, 
						targetPath, 
						jsonFileExtension, 
						outputter);
			}
		}
	}

	private void generateForConfiguration(Configuration configuration, String targetPath, String jsonFileExtension,
			IGeneratedWriter outputter) {
		ChainedCodeGeneratorTask<Configuration> generator = new ChainedCodeGeneratorTask<Configuration>();
		generator.addTask(new ConfigurationValidationTask(jsonFileExtension, targetPath));
		generator.generate(configuration, null, outputter);
	}
	
	private void generateForFault(Fault fault, String targetPath, String jsonFileExtension,
			IGeneratedWriter outputter) {
		ChainedCodeGeneratorTask<Fault> generator = new ChainedCodeGeneratorTask<Fault>();
		generator.addTask(new FaultValidationTask(jsonFileExtension, targetPath));
		generator.generate(fault, null, outputter);
	}
	
	private void generateForStatus(Status status, String targetPath, String jsonFileExtension,
			IGeneratedWriter outputter) {
		ChainedCodeGeneratorTask<Status> generator = new ChainedCodeGeneratorTask<Status>();
		generator.addTask(new StatusValidationTask(jsonFileExtension, targetPath));
		generator.generate(status, null, outputter);
	}

	public void generateForEvent(
			Event event, 
			String targetPath, 
			String jsonFileExtension, 
			IGeneratedWriter outputter) 
	{
		ChainedCodeGeneratorTask<Event> generator = new ChainedCodeGeneratorTask<Event>();
		generator.addTask(new EventValidationTask(jsonFileExtension, targetPath));
		generator.generate(event, null, outputter);
	}

	public void generateForOperation(Operation op,
			String targetPath, String jsonFileExtension, IGeneratedWriter outputter) 
	{
		ChainedCodeGeneratorTask<Operation> generator = new ChainedCodeGeneratorTask<Operation>();
		generator.addTask(new OperationParametersValidationTask(jsonFileExtension, targetPath));
		generator.addTask(new OperationReturnTypeValidationTask(jsonFileExtension, targetPath));
		generator.generate(op, null, outputter);
	}

	@Override
	public String getServiceKey() {
		return "jsonschema";
	}

}
