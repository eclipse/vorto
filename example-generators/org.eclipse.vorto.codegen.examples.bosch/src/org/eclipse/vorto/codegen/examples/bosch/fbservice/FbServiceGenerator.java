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
package org.eclipse.vorto.codegen.examples.bosch.fbservice;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.api.tasks.eclipse.LocationWrapper;
import org.eclipse.vorto.codegen.examples.bosch.common.BoschM2MNature;
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper;
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.DummyBaseDriverGenerator;
import org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.FbModelAPIGenerator;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.BluePrintGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.DriverImplGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.EventMappingGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.FbImplGeneratorTask;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.POMTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.AbstractDeviceDriverTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.AbstractDeviceServiceTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.AbstractEventMappingTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.EventMappingContextTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.EventMappingsConfigurationTemplate;
import org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates.IEventMappingTemplate;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class FbServiceGenerator implements ICodeGenerator<InformationModel> {

	private static final String LATEST_M2M_PLATFORM_VERSION = null;
	private static final String SUFFIX = "-service";

	@Override
	public void generate(InformationModel infomodel,
			final IProgressMonitor monitor) {

		FunctionblockModel fbm = infomodel.getProperties().get(0).getType();

		try {
			new FbModelAPIGenerator().generate(infomodel, monitor);
		} catch (Exception ex) {
			// do not stop generation
		}

		try {
			new DummyBaseDriverGenerator().generate(fbm, monitor);
		} catch (Exception ex) {
			// do not stop generation
		}

		try {
			generateFunctionBlockImplementationProject(fbm, monitor);
		} catch (Exception ex) {
			// do not stop generation
		}
	}

	private void generateFunctionBlockImplementationProject(
			FunctionblockModel fbm, final IProgressMonitor monitor) {
		FbModelWrapper wrappedfbm = new FbModelWrapper(fbm);

		final String implProjectName = fbm.getNamespace() + "."
				+ wrappedfbm.getFunctionBlockName().toLowerCase() + SUFFIX;

		/**
		 * GENERATOR INVOCATION OF IMPL PROJECT
		 */

		FbModelWrapper modelWrapper = new FbModelWrapper(fbm);

		final String workspaceLocation = ResourcesPlugin.getWorkspace()
				.getRoot().getLocation().toString();

		new EclipseProjectGenerator<FunctionblockModel>(new LocationWrapper(
				workspaceLocation, implProjectName))
				.mavenNature(new POMTemplate(LATEST_M2M_PLATFORM_VERSION))
				.addNature(BoschM2MNature.M2M_NATURE_ID)
				.addTask(new AbstractDeviceDriverTemplate())
				.addTask(new AbstractDeviceServiceTemplate())
				.addTask(new AbstractEventMappingTemplate())
				.addTask(new EventMappingContextTemplate())
				.addTask(new EventMappingsConfigurationTemplate())
				.addTask(new IEventMappingTemplate())
				.addTask(new BluePrintGeneratorTask(modelWrapper))
				.addTask(new FbImplGeneratorTask(modelWrapper))
				.addTask(new DriverImplGeneratorTask(modelWrapper))
				.addTask(new EventMappingGeneratorTask(modelWrapper))
				.generate(modelWrapper.getModel(), monitor);

	}

	@Override
	public String getName() {
		return "Function Block Service Generator";
	}
}
