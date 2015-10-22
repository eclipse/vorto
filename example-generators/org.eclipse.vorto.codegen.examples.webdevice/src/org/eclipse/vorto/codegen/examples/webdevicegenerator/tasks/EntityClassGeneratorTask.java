package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.EntityClassTemplate;
import org.eclipse.vorto.core.api.model.datatype.Entity;

public class EntityClassGeneratorTask extends
		AbstractTemplateGeneratorTask<Entity> {
	@Override
	public String getFileName(final Entity entity) {
		return StringUtils.capitalize(entity.getName()) + ".java";
	}

	@Override
	public String getPath(final Entity entity) {
		return ModuleUtil.getEntityPath(entity);
	}

	@Override
	public ITemplate<Entity> getTemplate() {
		return new EntityClassTemplate();
	}
}
