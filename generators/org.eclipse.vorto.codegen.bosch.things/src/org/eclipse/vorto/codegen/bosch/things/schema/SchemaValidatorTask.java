package org.eclipse.vorto.codegen.bosch.things.schema;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.bosch.things.schema.tasks.ValidationTaskFactory;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class SchemaValidatorTask implements ICodeGeneratorTask<InformationModel> {

	public static final String JSON_SCHEMA_FILE_EXTENSION 	= ".schema.json";
	public static final String TARGET_PATH 					= "feature";
	
	@Override
	public void generate(InformationModel infomodel, InvocationContext invocationContext, IGeneratedWriter writer) {
		
		for (FunctionblockProperty fbp : infomodel.getProperties()) {
			FunctionBlock fb = fbp.getType().getFunctionblock();
			
			generateForFunctionblock(
					fb, invocationContext,
					TARGET_PATH + "/" + fbp.getType().getNamespace() + "." + fbp.getType().getName() + "_" + fbp.getType().getVersion(),
					JSON_SCHEMA_FILE_EXTENSION,
					writer);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void generateForFunctionblock(
			FunctionBlock fb, 
			InvocationContext context,
			String targetPath, 
			String jsonFileExt,
			IGeneratedWriter outputter) {
		if (fb == null) {
			throw new RuntimeException("fb is null");
		}
		
		String stateTargetPath = targetPath + "/state";
		String operationTargetPath = targetPath + "/operations";
		String eventTargetPath = targetPath + "/events";
		
		Configuration configuration = fb.getConfiguration();
		if (configuration != null) {
			generateTask(configuration, context, outputter, ValidationTaskFactory.getConfigurationValidationTask(jsonFileExt, stateTargetPath));
		}
		
		Status status = fb.getStatus();
		if (status != null) {
			generateTask(status, context, outputter, ValidationTaskFactory.getStatusValidationTask(jsonFileExt, stateTargetPath));
		}
		
		Fault fault = fb.getFault();
		if (fault != null) {
			generateTask(fault, context, outputter, ValidationTaskFactory.getFaultValidationTask(jsonFileExt, stateTargetPath));
		}
		
		if (configuration != null || status != null || fault != null) {
			generateTask(fb, context, outputter, ValidationTaskFactory.getStateValidationTask(jsonFileExt, stateTargetPath));
		}
		
		if (fb.getEvents() != null) {
			for (Event event : fb.getEvents()) {
				generateTask(event, context, outputter, ValidationTaskFactory.getEventValidationTask(jsonFileExt, eventTargetPath));
			}
		}
		
		if (fb.getOperations() != null) {
			for (Operation op : fb.getOperations()) {
				generateTask(op, context, outputter, 
						ValidationTaskFactory.getOperationParametersValidationTask(jsonFileExt, operationTargetPath),
						ValidationTaskFactory.getOperationReturnTypeValidationTask(jsonFileExt, operationTargetPath));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private <K> void generateTask(K element, InvocationContext context, IGeneratedWriter outputter, ICodeGeneratorTask<K>... tasks) {
		ChainedCodeGeneratorTask<K> generator = new ChainedCodeGeneratorTask<K>();
		for(ICodeGeneratorTask<K> task : tasks) {
			generator.addTask(task);
		}
		generator.generate(element, context, outputter);
	}
}
