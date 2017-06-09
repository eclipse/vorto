package org.eclipse.vorto.codegen.bosch.things.javaclient.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.bosch.things.javaclient.templates.ThingIntegrationUtilTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class ThingsIntegrationUtilGeneratorTask extends AbstractTemplateGeneratorTask<InformationModel> {

	@Override
	public String getFileName(InformationModel fragment) {
		return "ThingClientFactory.java";
	}

	@Override
	public String getPath(InformationModel fragment) {
		return "/simulator/src/main/java/com/example/things";
	}

	@Override
	public ITemplate<InformationModel> getTemplate() {
		return new ThingIntegrationUtilTemplate();
	}

}
