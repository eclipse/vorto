package com.bosch.iotsuite.console.generator.application;

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
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.springframework.core.io.ClassPathResource;

import com.bosch.iotsuite.console.generator.application.templates.PomTemplate;
import com.bosch.iotsuite.console.generator.application.templates.ThingApplicationTemplate;
import com.bosch.iotsuite.console.generator.application.templates.config.CloudConfigurationTemplate;
import com.bosch.iotsuite.console.generator.application.templates.config.LocalConfigurationTemplate;
import com.bosch.iotsuite.console.generator.application.templates.config.SwaggerConfigurationTemplate;
import com.bosch.iotsuite.console.generator.application.templates.config.WebSecurityConfigTemplate;
import com.bosch.iotsuite.console.generator.application.templates.config.WebSocketConfigTemplate;
import com.bosch.iotsuite.console.generator.application.templates.dao.CrudRepositoryTemplate;
import com.bosch.iotsuite.console.generator.application.templates.model.FeatureStatusPropertyTemplate;
import com.bosch.iotsuite.console.generator.application.templates.model.FeatureTemplate;
import com.bosch.iotsuite.console.generator.application.templates.model.JavaClassGeneratorTask;
import com.bosch.iotsuite.console.generator.application.templates.model.JavaEnumGeneratorTask;
import com.bosch.iotsuite.console.generator.application.templates.model.ThingTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.IndexHtmlTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.js.ApiControllerTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.js.AppTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.js.BrowserControllerTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.js.DetailsControllerTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.js.LocationControllerTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.js.LoginControllerTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.partials.ApiTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.partials.BrowserTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.partials.DetailsTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.partials.LocatorTemplate;
import com.bosch.iotsuite.console.generator.application.templates.resources.partials.LoginTemplate;
import com.bosch.iotsuite.console.generator.application.templates.web.HistoryDataControllerTemplate;
import com.bosch.iotsuite.console.generator.application.templates.web.IdentityControllerTemplate;
import com.bosch.iotsuite.console.generator.application.templates.web.ThingControllerTemplate;
import com.bosch.iotsuite.console.generator.application.templates.web.ThingMessageControllerTemplate;
import com.bosch.iotsuite.console.generator.application.templates.web.UtilsTemplate;

public class ApplicationGenerator implements IVortoCodeGenerator {

	@Override
	public IGenerationResult generate(InformationModel model, InvocationContext context, IVortoCodeGenProgressMonitor monitor) {
		GenerationResultZip output = new GenerationResultZip(model, getServiceKey());
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		/**
		 * config templates
		 */
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new CloudConfigurationTemplate()));
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
		if (context.getConfigurationProperties().getOrDefault("history", "true").equalsIgnoreCase("true")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new CrudRepositoryTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new HistoryDataControllerTemplate()));
		}
				
		copyTemplateResources(model,output);
		
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
		
		copyResource("/templates/static/img/avatar.png", "avatar.png", model.getName().toLowerCase()+"-solution/src/main/resources/static/img", writer);
		copyResource("/templates/static/img/marker-icon.png", "marker-icon.png", model.getName().toLowerCase()+"-solution/src/main/resources/static/img", writer);
		copyResource("/templates/static/img/noImageIcon.png", "noImageIcon.png", model.getName().toLowerCase()+"-solution/src/main/resources/static/img", writer);
		
		copyResource("/templates/application-cloud.yml", "application-cloud.yml", model.getName().toLowerCase()+"-solution/src/main/resources", writer);
		copyResource("/templates/application-local.yml", "application-local.yml", model.getName().toLowerCase()+"-solution/src/main/resources", writer);
		copyResource("/templates/application.yml", "application.yml", model.getName().toLowerCase()+"-solution/src/main/resources", writer);
	
	}

	private void copyResource(String resource, String fileName, String output, GenerationResultZip writer) {
		try {
			byte[] content = IOUtils.toByteArray(new ClassPathResource(resource).getInputStream());
			writer.write(new Generated(fileName, output, content));
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String getServiceKey() {
		return "iotapp";
	}
}
