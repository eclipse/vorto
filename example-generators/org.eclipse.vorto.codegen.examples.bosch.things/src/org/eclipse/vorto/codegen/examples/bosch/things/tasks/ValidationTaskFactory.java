package org.eclipse.vorto.codegen.examples.bosch.things.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.Event;
import org.eclipse.vorto.core.api.model.functionblock.Fault;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.Status;

public class ValidationTaskFactory {
	
	public static ICodeGeneratorTask<FunctionBlock> getStateValidationTask(String fileExt, String path) {
		return new ValidationGeneratorTask<FunctionBlock>("state" + fileExt, path, ValidationTemplates.stateValidationTemplate);
	}
	
	public static ICodeGeneratorTask<Configuration> getConfigurationValidationTask(String fileExt, String path) {
		return new ValidationGeneratorTask<Configuration>("state_configuration" + fileExt, path, ValidationTemplates.configurationValidationTemplate);
	}
	
	public static ICodeGeneratorTask<Status> getStatusValidationTask(String fileExt, String path) {
		return new ValidationGeneratorTask<Status>("state_status" + fileExt, path, ValidationTemplates.statusValidationTemplate);
	}
	
	public static ICodeGeneratorTask<Fault> getFaultValidationTask(String fileExt, String path) {
		return new ValidationGeneratorTask<Fault>("state_fault" + fileExt, path, ValidationTemplates.faultValidationTemplate);
	} 
	
	public static ICodeGeneratorTask<Event> getEventValidationTask(String fileExt, String path) {
		return new EventValidationGeneratorTask(fileExt, path, ValidationTemplates.eventValidationTemplate);
	}
	
	public static ICodeGeneratorTask<Operation> getOperationParametersValidationTask(String fileExt, String path) {
		return new OperationParametersValidationGeneratorTask(fileExt, path, ValidationTemplates.operationParametersValidationTemplate);
	}
	
	public static ICodeGeneratorTask<Operation> getOperationReturnTypeValidationTask(String fileExt, String path) {
		return new OperationReturnTypeValidationGeneratorTask(fileExt, path, ValidationTemplates.operationReturnTypeValidation);
	}
	
	private static class ValidationGeneratorTask<Element> extends AbstractTemplateGeneratorTask<Element> {

		protected String filename;
		private String path;
		private ITemplate<Element> template;
		
		public ValidationGeneratorTask(String filename, String path, ITemplate<Element> template) {
			this.filename = filename;
			this.path = path;
			this.template = template;
		}
		
		@Override
		public String getFileName(Element fragment) {
			return filename;
		}

		@Override
		public String getPath(Element fragment) {
			return path;
		}

		@Override
		public ITemplate<Element> getTemplate() {
			return template;
		}
	}
	
	private static class EventValidationGeneratorTask extends ValidationGeneratorTask<Event> {
		public EventValidationGeneratorTask(String filename, String path, ITemplate<Event> template) {
			super(filename, path, template);
		}
		
		@Override
		public String getFileName(Event event) {
			return event.getName() + filename;
		}
	}
	
	private static class OperationParametersValidationGeneratorTask extends ValidationGeneratorTask<Operation> {
		public OperationParametersValidationGeneratorTask(String filename, String path, ITemplate<Operation> template) {
			super(filename, path, template);
		}
		
		@Override
		public String getFileName(Operation operation) {
			return operation.getName() + filename;
		}
	}
	
	private static class OperationReturnTypeValidationGeneratorTask extends ValidationGeneratorTask<Operation> {
		public OperationReturnTypeValidationGeneratorTask(String filename, String path, ITemplate<Operation> template) {
			super(filename, path, template);
		}
		
		@Override
		public String getFileName(Operation operation) {
			return operation.getName()+ "-result" + filename;
		}
	}
}
