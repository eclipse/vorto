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
package org.eclipse.vorto.codegen.kura;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorInfo;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.kura.templates.BuildPropertiesTemplate;
import org.eclipse.vorto.codegen.kura.templates.DefaultAppTemplate;
import org.eclipse.vorto.codegen.kura.templates.EclipseClasspathTemplate;
import org.eclipse.vorto.codegen.kura.templates.EclipseProjectFileTemplate;
import org.eclipse.vorto.codegen.kura.templates.IDataServiceTemplate;
import org.eclipse.vorto.codegen.kura.templates.KuraCloudDataServiceTemplate;
import org.eclipse.vorto.codegen.kura.templates.ManifestTemplate;
import org.eclipse.vorto.codegen.kura.templates.PomTemplate;
import org.eclipse.vorto.codegen.kura.templates.bluetooth.ConfigurationTemplate;
import org.eclipse.vorto.codegen.kura.templates.bluetooth.DeviceBluetoothFinderTemplate;
import org.eclipse.vorto.codegen.kura.templates.bluetooth.DeviceFilterTemplate;
import org.eclipse.vorto.codegen.kura.templates.bluetooth.DeviceToInformationModelTransformerTemplate;
import org.eclipse.vorto.codegen.kura.templates.bluetooth.InformationModelConsumerTemplate;
import org.eclipse.vorto.codegen.kura.templates.cloud.FunctionblockTemplate;
import org.eclipse.vorto.codegen.kura.templates.cloud.InformationModelTemplate;
import org.eclipse.vorto.codegen.kura.templates.cloud.bosch.BoschDataServiceTemplate;
import org.eclipse.vorto.codegen.kura.templates.cloud.bosch.BoschHubClientTemplate;
import org.eclipse.vorto.codegen.kura.templates.cloud.bosch.BoschHubDataService;
import org.eclipse.vorto.codegen.kura.templates.cloud.bosch.ThingClientFactoryTemplate;
import org.eclipse.vorto.codegen.kura.templates.osgiinf.ComponentXmlTemplate;
import org.eclipse.vorto.codegen.kura.templates.osgiinf.MetatypeTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class KuraGenerator implements IVortoCodeGenerator {
	
	@Override
	public IGenerationResult generate(InformationModel context, InvocationContext invocationContext,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		GenerationResultZip outputter = new GenerationResultZip(context,getServiceKey());
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		generator.addTask(new GeneratorTaskFromFileTemplate<>(new EclipseClasspathTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<>(new EclipseProjectFileTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<>(new ManifestTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<>(new BuildPropertiesTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<>(new ComponentXmlTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<>(new IDataServiceTemplate()));
		
		if (invocationContext.getConfigurationProperties().getOrDefault("boschcloud", "false").equalsIgnoreCase("true")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new PomTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new BoschDataServiceTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new ThingClientFactoryTemplate()));
		} else if (invocationContext.getConfigurationProperties().getOrDefault("boschhub", "false").equalsIgnoreCase("true")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new PomTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new BoschHubDataService()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new BoschHubClientTemplate()));
		} else {
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new KuraCloudDataServiceTemplate()));
		}
		
		if (invocationContext.getConfigurationProperties().getOrDefault("bluetooth", "false").equalsIgnoreCase("true")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new MetatypeTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new DeviceBluetoothFinderTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new ConfigurationTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new DeviceFilterTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new DeviceToInformationModelTransformerTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new InformationModelConsumerTemplate()));
		} else {
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new DefaultAppTemplate()));
		}
		
		generator.addTask(new GeneratorTaskFromFileTemplate<>(new InformationModelTemplate()));
		for (FunctionblockProperty fbProperty : context.getProperties()) {
			new GeneratorTaskFromFileTemplate<>(new FunctionblockTemplate(context)).generate(fbProperty.getType(), invocationContext, outputter);
		}
		
		generator.generate(context,invocationContext, outputter);
		
		return outputter;
	}

	@Override
	public String getServiceKey() {
		return "kura";
	}
	
	@Override
	public GeneratorInfo getInfo() {
		return GeneratorInfo.basicInfo("Eclipse Kura","Generates a Kura Gateway application that reads data from the device (e.g. via bluetooth) and sends the data to a IoT Cloud backend.","Vorto Community");
	}
}
