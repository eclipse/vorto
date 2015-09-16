package org.eclipse.vorto.codegen.examples.tutorial;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.mapping.IMappingAware;
import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.examples.tutorial.tasks.LWM2MObjectTypeGenerateTask;
import org.eclipse.vorto.codegen.examples.tutorial.tasks.LWM2MResourceGenerateTask;
import org.eclipse.vorto.codegen.examples.tutorial.tasks.LWM2MObjectTypeCompleteGenerateTask;
import org.eclipse.vorto.codegen.examples.tutorial.tasks.LWM2MResourceEntityGenerateTask;
import org.eclipse.vorto.codegen.examples.tutorial.tasks.LWM2MResourceEnumGenerateTask;
import org.eclipse.vorto.codegen.ui.display.IMessageDisplay;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.model.IMapping;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.MappingResourceFactory;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelIdFactory;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;

public class ExampleMappingGenerator  implements IMappingAware, ICodeGenerator<InformationModel> {
	IMessageDisplay messageDisplay = MessageDisplayFactory.getMessageDisplay();
	private InformationModel informationModel = null;
	
	protected IMapping mapping;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.codegen.api.mapping.IMappingAware#getTargetPlatform()
	 */
	@Override
	public String getTargetPlatform() {
		return "LWM2MObjectTypeComplete";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.vorto.codegen.api.ICodeGenerator#generate(java.lang.Object,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void generate(InformationModel ctx, IProgressMonitor monitor) {
		this.informationModel = ctx;
		String outputProjectName = this.getSelectedModelProject().getProject().getName();
		new EclipseProjectGenerator<InformationModel>(outputProjectName).addTask(getObjectTypeGenerateTask())
				.addTask(getResourceFromFunctionBlockGenerateTask()).addTask(getResourceTypeFromEntityGenerateTask())
				.addTask(getResourceTypeFromEnumGenerateTask()).addTask(getResourceTypeCompleteGenerateTask())
				.generate(ctx, monitor);

	}

	private LWM2MObjectTypeGenerateTask getObjectTypeGenerateTask() {
		String mappingTargetPlatform = "LWM2MObjectType";
		IMapping taskMapping = MappingResourceFactory.getInstance().createMapping(getSelectedModelProject(),
				mappingTargetPlatform);

		logTask(mappingTargetPlatform, taskMapping);

		return new LWM2MObjectTypeGenerateTask(taskMapping);
	}

	private LWM2MResourceGenerateTask getResourceFromFunctionBlockGenerateTask() {
		String mappingTargetPlatform = "LWM2MResource";
		IMapping taskMapping = MappingResourceFactory.getInstance().createMapping(getSelectedModelProject(),
				mappingTargetPlatform);

		logTask(mappingTargetPlatform, taskMapping);

		return new LWM2MResourceGenerateTask(taskMapping);
	}

	private LWM2MResourceEntityGenerateTask getResourceTypeFromEntityGenerateTask() {
		String mappingTargetPlatform = "LWM2MResourceEntity";
		IMapping taskMapping = MappingResourceFactory.getInstance().createMapping(getSelectedModelProject(),
				mappingTargetPlatform);

		logTask(mappingTargetPlatform, taskMapping);
		return new LWM2MResourceEntityGenerateTask(taskMapping);
	}

	private LWM2MResourceEnumGenerateTask getResourceTypeFromEnumGenerateTask() {
		String mappingTargetPlatform = "LWM2MResourceEnum";
		IMapping taskMapping = MappingResourceFactory.getInstance().createMapping(getSelectedModelProject(),
				mappingTargetPlatform);
		logTask(mappingTargetPlatform, taskMapping);
		return new LWM2MResourceEnumGenerateTask(taskMapping);
	}

	private LWM2MObjectTypeCompleteGenerateTask getResourceTypeCompleteGenerateTask() {
		logTask(this.getTargetPlatform(), this.mapping);
		return new LWM2MObjectTypeCompleteGenerateTask(this.mapping);
	}

	private void logTask(String mappingTargetPlatform, IMapping taskMapping) {
		if (taskMapping.getAllRules().size() == 0) {
			messageDisplay
					.displayError("Mapping definition with targetplatform '" + mappingTargetPlatform + "' not found!");
		} else {
			messageDisplay.display("Add task : " + mappingTargetPlatform + " with mapping targetplatform set to "
					+ mappingTargetPlatform);
		}
	}

	private IModelProject getSelectedModelProject() {
		ModelId modelId = ModelIdFactory.newInstance(this.informationModel);
		return ModelProjectServiceFactory.getDefault().getProjectByModelId(modelId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.vorto.codegen.api.ICodeGenerator#getName()
	 */
	@Override
	public String getName() {
		return "ExampleMappingGenerator";
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.vorto.codegen.api.mapping.IMappingAware#setMapping(org.
	 * eclipse.vorto.core.model.IMapping)
	 */
	@Override
	public void setMapping(IMapping mapping) {
		this.mapping = mapping;
	}
}
