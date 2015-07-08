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
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ConfigurationClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.FaultClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.FunctionBlockClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.IndexHtmlFileGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ModuleUtil;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.ServiceClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.StatusClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.WebXmlGeneratorTask;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.PomFileTemplate;
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

		for (FunctionblockProperty fbm : informationModel.getProperties()) {
			this.generateForFunctionBlock(informationModel, fbm.getType(),
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
				.copy(getResource("resources/css"), WEBAPP_PATH)
				.copy(getResource("resources/script"), WEBAPP_PATH)
				.generate(informationModel, monitor);
	}

	private URL getResource(String path) {

		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL url = bundle.getEntry(path);
		return url;
	}

	private void generateForFunctionBlock(InformationModel informationModel,
			FunctionblockModel fbm, IProgressMonitor monitor) {

		new EclipseProjectGenerator<FunctionblockModel>(
				ModuleUtil.getArtifactId(informationModel))
				.addTask(new ServiceClassGeneratorTask())
				.addTask(new WebXmlGeneratorTask())
				.addTask(new FunctionBlockClassGeneratorTask())
				.addTask(new ConfigurationClassGeneratorTask())
				.addTask(new StatusClassGeneratorTask())
				.addTask(new FaultClassGeneratorTask()).generate(fbm, monitor);
	}

	@Override
	public String getName() {
		return "Device Web UI Generator";
	}
}
