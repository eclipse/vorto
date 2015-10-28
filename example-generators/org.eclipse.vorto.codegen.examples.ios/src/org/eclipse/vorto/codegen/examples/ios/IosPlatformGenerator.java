/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.ios;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.mapping.IMappingAware;
import org.eclipse.vorto.codegen.api.tasks.DatatypeGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.examples.ios.templates.CoreBluetoothDetectionTemplate;
import org.eclipse.vorto.codegen.examples.ios.templates.DeviceServiceTemplate;
import org.eclipse.vorto.codegen.examples.ios.templates.EntityClassTemplate;
import org.eclipse.vorto.codegen.examples.ios.templates.EnumClassTemplate;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.model.IMapping;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelIdFactory;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;

public class IosPlatformGenerator implements ICodeGenerator<InformationModel>, IMappingAware {

	private IMapping mapping = null;
	
	@Override
	public String getTargetPlatform() {
		return "ios";
	}

	@Override
	public void setMapping(IMapping mapping) {
		this.mapping = mapping;
		
	}

	@Override
	public void generate(final InformationModel ctx, IProgressMonitor monitor) {
		final IModelProject currentProject = getSelectedModelProject(ctx);
		String outputProjectName = currentProject.getProject().getName();
		EclipseProjectGenerator<InformationModel> generator = new EclipseProjectGenerator<InformationModel>(outputProjectName);
		generator.addTask(new DatatypeGeneratorTask(new EntityClassTemplate(), new EnumClassTemplate()));
		
		if (mapping.getAllRules().isEmpty()) {
			MessageDisplayFactory.getMessageDisplay().displayError("This generator requires a mapping with target 'ios'");
			return;
		}
		
		StereoTypeTarget target = (StereoTypeTarget)mapping.getAllRules().get(0).getTarget();
		
		if ("ble".equalsIgnoreCase(target.getName())) {
			generator.addTask(new CoreBluetoothDetectionTemplate());
			generator.addTask(new DeviceServiceTemplate());
		} else {
			MessageDisplayFactory.getMessageDisplay().displayWarning(target.getName()+" is not supported at the moment. Please use 'BLE' for binding device to bluetooth.");
			return;
		}
		
		generator.generate(ctx, monitor);
	}
	
	private IModelProject getSelectedModelProject(final InformationModel infomodel) {
		final ModelId modelId = ModelIdFactory.newInstance(infomodel);
		return ModelProjectServiceFactory.getDefault().getProjectByModelId(modelId);
	}

	@Override
	public String getName() {
		return "IOS Platform Generator";
	}

}
