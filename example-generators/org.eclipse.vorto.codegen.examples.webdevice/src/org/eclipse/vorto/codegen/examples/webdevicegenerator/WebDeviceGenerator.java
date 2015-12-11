/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.webdevicegenerator;

import java.net.URL;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.AppScriptGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ConfigurationClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.EntityClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.EnumGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.FaultClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.FunctionBlockClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.IndexHtmlFileGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.InformationModelModelClassTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.InformationModelServiceClassTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.OperationClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ServiceClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.StatusClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.WebXmlGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.PomFileTemplate;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.osgi.framework.Bundle;

public class WebDeviceGenerator implements ICodeGenerator<InformationModel> {
	private static final String WEBAPP_PATH = "src/main/webapp/";

	@Override
	public void generate(InformationModel informationModel,
			IProgressMonitor monitor) {
		try {
			this.generateForInformationModel(informationModel, monitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (FunctionblockProperty fbProperty : informationModel
				.getProperties()) {
			this.generateForFunctionblockProperty(informationModel, fbProperty,
					monitor);
		}

	}

	private void generateForInformationModel(InformationModel informationModel,
			IProgressMonitor monitor) {
		ITemplate<InformationModel> POMFILE_TEMPLATE = new PomFileTemplate();
		new EclipseProjectGenerator<InformationModel>(
				ModuleUtil.getArtifactId(informationModel))
				.mavenNature(POMFILE_TEMPLATE)
				.addTask(new IndexHtmlFileGeneratorTask())
				.addTask(new InformationModelModelClassTask())
				.addTask(new InformationModelServiceClassTask())
				.addTask(new AppScriptGeneratorTask())
				.copy(getResource("resources/css"), WEBAPP_PATH)
				.copy(getResource("resources/script"), WEBAPP_PATH)
				.copy(getResource("resources/pages"), WEBAPP_PATH)
				.generate(informationModel, monitor);
	}

	private URL getResource(String path) {

		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL url = bundle.getEntry(path);
		return url;
	}

	private void generateForFunctionblockProperty(
			InformationModel informationModel,
			FunctionblockProperty fbProperty, IProgressMonitor monitor) {

		new EclipseProjectGenerator<FunctionblockProperty>(
				ModuleUtil.getArtifactId(informationModel))
				.addTask(new ServiceClassGeneratorTask())
				.addTask(new WebXmlGeneratorTask())
				.addTask(new FunctionBlockClassGeneratorTask())
				.addTask(new OperationClassGeneratorTask())
				.addTask(new ConfigurationClassGeneratorTask())
				.addTask(new StatusClassGeneratorTask())
				.addTask(new FaultClassGeneratorTask())
				.generate(fbProperty, monitor);

		FunctionblockModel fbModel = fbProperty.getType();

		if (fbModel.getFunctionblock() != null) {
			if (fbModel.getFunctionblock().getConfiguration() != null) {
				for (Property property : fbModel.getFunctionblock()
						.getConfiguration().getProperties()) {
					generateForProperty(informationModel, property, monitor);
				}
			}

			if (fbModel.getFunctionblock().getStatus() != null) {
				for (Property property : fbModel.getFunctionblock().getStatus()
						.getProperties()) {
					generateForProperty(informationModel, property, monitor);
				}
			}

			if (fbModel.getFunctionblock().getFault() != null) {
				for (Property property : fbModel.getFunctionblock().getFault()
						.getProperties()) {
					generateForProperty(informationModel, property, monitor);
				}
			}
		}
	}

	private void generateForProperty(InformationModel informationModel,
			Property property, IProgressMonitor monitor) {
		if (property.getType() instanceof ObjectPropertyType) {
			ObjectPropertyType objectType = (ObjectPropertyType) property
					.getType();
			if (objectType.getType() instanceof Entity) {
				generateForEntity(informationModel,
						(Entity) objectType.getType(), monitor);
			}
			else if (objectType.getType() instanceof Enum) {
				generateForEnum(informationModel,
						(Enum) objectType.getType(), monitor);
			}
		}
	}

	
	private void generateForEnum(InformationModel informationModel,
			Enum e, IProgressMonitor monitor) {

		new EclipseProjectGenerator<Enum>(
				ModuleUtil.getArtifactId(informationModel)).addTask(
				new EnumGeneratorTask()).generate(e, monitor);
	}
	
	private void generateForEntity(InformationModel informationModel,
			Entity entity, IProgressMonitor monitor) {

		new EclipseProjectGenerator<Entity>(
				ModuleUtil.getArtifactId(informationModel)).addTask(
				new EntityClassGeneratorTask()).generate(entity, monitor);
		for (Property property : entity.getProperties()) {

			// Ignore primitive data types - there is nothing to be generated
			if (property.getType() instanceof ObjectPropertyType) {
				ObjectPropertyType objectType = (ObjectPropertyType) property
						.getType();
				
				// If a data type already exists it will be overwritten
				if (objectType.getType() instanceof Entity) {
					generateForEntity(informationModel,
							(Entity) objectType.getType(), monitor);
				}
				if (objectType.getType() instanceof Enum) {
					generateForEnum(informationModel,
							(Enum) objectType.getType(), monitor);
				}
			}
		}
	}

	@Override
	public String getName() {
		return "Device Web UI Generator";
	}
}
