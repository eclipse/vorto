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

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorInfo;
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
import org.eclipse.vorto.codegen.webui.templates.model.FeatureTemplate;
import org.eclipse.vorto.codegen.webui.templates.model.JavaClassGeneratorTask;
import org.eclipse.vorto.codegen.webui.templates.model.JavaEnumGeneratorTask;
import org.eclipse.vorto.codegen.webui.templates.model.ThingTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.AngularGageTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.ApplicationConfigTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.IndexHtmlTemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.css.StyleTemplace;
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
import org.eclipse.vorto.codegen.webui.templates.service.DataServiceTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.QueryTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.BoschThingsDataServiceTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.ThingBuilderTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.ThingClientBuilderTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.ThingClientTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.ThingsQueryTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.internal.AsyncInvocationTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.internal.DefaultThingClient;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.internal.ThingsInvocationTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.internal.model.FeatureImplTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.internal.model.ThingImplTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.internal.model.ThingSearchResultImplTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.model.ThingSearchResultTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.bosch.model.ThingTypeAwareTemplate;
import org.eclipse.vorto.codegen.webui.templates.service.sample.SampleDataServiceTemplate;
import org.eclipse.vorto.codegen.webui.templates.web.HistoryDataControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.web.IdentityControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.web.ThingControllerTemplate;
import org.eclipse.vorto.codegen.webui.templates.web.ThingMessageControllerTemplate;
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
		 * Web static css templates
		 */
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new StyleTemplace()));
		
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
		
		/**
		 * IoT Cloud Platform Integration stuff
		 */
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new QueryTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new DataServiceTemplate()));
		
		if (context.getConfigurationProperties().getOrDefault("boschcloud", "false").equalsIgnoreCase("true")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingsQueryTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingClientTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingClientBuilderTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingBuilderTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new org.eclipse.vorto.codegen.webui.templates.service.bosch.model.ThingTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new org.eclipse.vorto.codegen.webui.templates.service.bosch.model.FeatureTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingSearchResultTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingTypeAwareTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new FeatureImplTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingImplTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingSearchResultImplTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new AsyncInvocationTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new DefaultThingClient()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new ThingsInvocationTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new BoschThingsDataServiceTemplate()));
		} else {
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new SampleDataServiceTemplate()));
		}
		
		generator.generate(model, context, output);
		
		
		ChainedCodeGeneratorTask<FunctionblockProperty> fbPropertyGenerators = new ChainedCodeGeneratorTask<FunctionblockProperty>();
		for (FunctionblockProperty property : model.getProperties()) {
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

	@Override
	public String getServiceKey() {
		return "webui";
	}
	
	@Override
	public GeneratorInfo getInfo() {
		return GeneratorInfo.basicInfo("Web-based Device Dashboard",
			"Generates a web-based dashboard that integrates with IoT Cloud backends and visualizes device data", "Vorto Community");
	}
}
