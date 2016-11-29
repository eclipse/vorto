package org.eclipse.vorto.codegen.examples.bosch.things.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.ConstraintTemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.EntityValidationTemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.EnumValidationTemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.PrimitiveTypeValidationTemplate;
import org.eclipse.vorto.codegen.examples.bosch.things.tasks.template.StatusValidationTemplate;
import org.eclipse.vorto.core.api.model.functionblock.Status;

public class StatusValidationTask extends AbstractTemplateGeneratorTask<Status> {
	
	private String jsonSchemaFileExtension;
	private String targetPath;

	public StatusValidationTask(String jsonFileExtension, String targetPath) {
		this.jsonSchemaFileExtension = jsonFileExtension;
		this.targetPath = targetPath;
	}
	
	@Override
	public String getFileName(Status status) {
		return "status" + jsonSchemaFileExtension;
	}

	@Override
	public String getPath(Status status) {
		return targetPath;
	}

	@Override
	public ITemplate<Status> getTemplate() {
		return new StatusValidationTemplate(
				new EnumValidationTemplate(), new EntityValidationTemplate(new EnumValidationTemplate(),
						new PrimitiveTypeValidationTemplate(), new ConstraintTemplate()),
				new PrimitiveTypeValidationTemplate(), new ConstraintTemplate());
	}

}
