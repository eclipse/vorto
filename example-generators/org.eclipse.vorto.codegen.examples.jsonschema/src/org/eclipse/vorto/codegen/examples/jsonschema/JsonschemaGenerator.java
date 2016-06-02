/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.jsonschema;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.EventValidationTask;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.OperationParametersValidationTask;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.OperationReturnTypeValidationTask;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
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
		
		for (Operation op : fb.getOperations()) {
			generateForOperation(
					op, 
					targetPath,
					jsonFileExtension, 
					outputter);
		}

		for (Event event : fb.getEvents()) {
			generateForEvent(
					event, 
					targetPath, 
					jsonFileExtension, 
					outputter);
		}
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
