package org.eclipse.vorto.codegen.examples.jsonschema.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.template.ConfigurationValidationTemplate;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.template.ConstraintTemplate;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.template.EntityValidationTemplate;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.template.EnumValidationTemplate;
import org.eclipse.vorto.codegen.examples.jsonschema.tasks.template.PrimitiveTypeValidationTemplate;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;

public class ConfigurationValidationTask extends AbstractTemplateGeneratorTask<Configuration> {
	
	private String jsonSchemaFileExtension;
	private String targetPath;

	public ConfigurationValidationTask(String jsonFileExtension, String targetPath) {
		this.jsonSchemaFileExtension = jsonFileExtension;
		this.targetPath = targetPath;
	}


	@Override
	public String getFileName(Configuration configuration) {
		return "configuration" + jsonSchemaFileExtension;
	}

	@Override
	public String getPath(Configuration configuration) {
		return targetPath;
	}

	@Override
	public ITemplate<Configuration> getTemplate() {
		return new ConfigurationValidationTemplate(
				new EnumValidationTemplate(), new EntityValidationTemplate(new EnumValidationTemplate(),
						new PrimitiveTypeValidationTemplate(), new ConstraintTemplate()),
				new PrimitiveTypeValidationTemplate(), new ConstraintTemplate());
	}

}
