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
package org.eclipse.vorto.codegen.webui;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.Generated;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.codegen.webui.templates.PomTemplate;
import org.eclipse.vorto.codegen.webui.templates.ThingApplicationTemplate;
import org.eclipse.vorto.codegen.webui.templates.config.LocalConfigurationTemplate;
import org.eclipse.vorto.codegen.webui.templates.config.SwaggerConfigurationTemplate;
import org.eclipse.vorto.codegen.webui.templates.config.WebSecurityConfigTemplate;
import org.eclipse.vorto.codegen.webui.templates.config.WebSocketConfigTemplate;
import org.eclipse.vorto.codegen.webui.templates.dao.CrudRepositoryTemplate;
import org.eclipse.vorto.codegen.webui.templates.model.FeatureStatusPropertyTemplate;
import org.eclipse.vorto.codegen.webui.templates.model.FeatureTemplate;
import org.eclipse.vorto.codegen.webui.templates.model.JavaClassGeneratorTask;
import org.eclipse.vorto.codegen.webui.templates.model.JavaEnumGeneratorTask;
import org.eclipse.vorto.codegen.webui.templates.model.ThingTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.AngularGageTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.ApplicationConfigTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.IndexHtmlTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.js.ApiControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.js.AppTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.js.BrowserControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.js.DetailsControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.js.LocationControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.js.LoginControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.partials.ApiTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.partials.BrowserTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.partials.DetailsTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.partials.LocatorTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.partials.LoginTemplate;
import org.eclipse.vorto.codegen.webui.templates.web.HistoryDataControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.web.IdentityControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.web.ThingControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.web.ThingMessageControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.web.UtilsTemplate;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class WebUIGenerator implements IVortoCodeGenerator {

	@Override
	public IGenerationResult generate(InformationModel model, InvocationContext context, IVortoCodeGenProgressMonitor monitor) {
		GenerationResultZip output = new GenerationResultZip(model, getServiceKey());
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		/**
		 * config templates
		 */
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new LocalConfigurationTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new WebSecurityConfigTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new WebSocketConfigTemplate()));
		
		/**
		 * model templates
		 */
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingTemplate()));
		
		/**
		 * web controller templates
		 */
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new IdentityControllerTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingControllerTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingMessageControllerTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new UtilsTemplate()));
		
		/**
		 * Root Application templates
		 */
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new PomTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingApplicationTemplate()));
		
		/**
		 * Web static javascript templates
		 */
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new AppTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new BrowserControllerTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new DetailsControllerTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new LoginControllerTemplate()));
		
		
		
		/**
		 * Web static html partial templates
		 */
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new IndexHtmlTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new BrowserTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new LocatorTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new LoginTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new DetailsTemplate()));
		
		/**
		 * Swagger stuff
		 */
		if (context.getConfigurationProperties().getOrDefault("swagger", "true").equalsIgnoreCase("true")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new SwaggerConfigurationTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ApiControllerTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ApiTemplate()));
		}
		
		/**
		 * History stuff
		 */
		if (context.getConfigurationProperties().getOrDefault("persistence", "true").equalsIgnoreCase("true")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new CrudRepositoryTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new HistoryDataControllerTemplate()));
		}
				
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ApplicationConfigTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new AngularGageTemplate()));
		
		generator.generate(model, context, output);
		
		
		ChainedCodeGeneratorTask<FunctionblockProperty> fbPropertyGenerators = new ChainedCodeGeneratorTask<FunctionblockProperty>();
		for (FunctionblockProperty property : model.getProperties()) {
			fbPropertyGenerators.addTask(new GeneratorTaskFromFileTemplate<>(new FeatureStatusPropertyTemplate()));
			fbPropertyGenerators.addTask(new GeneratorTaskFromFileTemplate<>(new FeatureTemplate()));
			fbPropertyGenerators.addTask(new GeneratorTaskFromFileTemplate<>(new LocationControllerTemplate()));
			
			fbPropertyGenerators.generate(property, context, output);
			
			FunctionBlock fb = property.getType().getFunctionblock();
			
			for (Entity entity : Utils.getReferencedEntities(fb)) {
				generateForEntity(model, entity, output);
			}
			for (Enum en : Utils.getReferencedEnums(fb)) {
				generateForEnum(model, en, output);
			}
		}
		
		return output;
	}
	
	private void generateForEntity(InformationModel infomodel, Entity entity, IGeneratedWriter outputter) {
		new JavaClassGeneratorTask(infomodel).generate(entity, null, outputter);
	}

	private void generateForEnum(InformationModel infomodel, Enum en, IGeneratedWriter outputter) {
		new JavaEnumGeneratorTask(infomodel).generate(en,null, outputter);
				
	}

	private void copyTemplateResources(InformationModel model, GenerationResultZip writer) {
		copyResource("/templates/static/dist/js/angular-gage.min.js", "angular-gage.min.js", model.getName().toLowerCase()+"-solution/src/main/resources/static/dist/js", writer);
		
		copyResource("/templates/application-local.yml", "application-local.yml", model.getName().toLowerCase()+"-solution/src/main/resources", writer);
		copyResource("/templates/application.yml", "application.yml", model.getName().toLowerCase()+"-solution/src/main/resources", writer);
	
	}

	private void copyResource(String resource, String fileName, String output, GenerationResultZip writer) {
		try {
			byte[] content = IOUtils.toByteArray(getClass().getResourceAsStream(resource));
			writer.write(new Generated(fileName, output, content));
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String getServiceKey() {
		return "webui";
	}
}
