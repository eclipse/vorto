package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.EnumTemplate;
import org.eclipse.vorto.core.api.model.datatype.Enum;

public class EnumGeneratorTask extends
		AbstractTemplateGeneratorTask<Enum> {
	
	
	@Override
	public String getFileName(final Enum e) {
		return StringUtils.capitalize(e.getName()) + ".java";
	}

	@Override
	public String getPath(final Enum e) {
		return ModuleUtil.getEnumPath(e);
	}

	@Override
	public ITemplate<Enum> getTemplate() {
		return new EnumTemplate();
	}
}
