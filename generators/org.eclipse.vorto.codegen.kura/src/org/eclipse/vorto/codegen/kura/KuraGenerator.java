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
import org.eclipse.vorto.codegen.kura.templates.ManifestTemplate;
import org.eclipse.vorto.codegen.kura.templates.PomTemplate;
import org.eclipse.vorto.codegen.kura.templates.bluetooth.DeviceBluetoothFinderTemplate;
import org.eclipse.vorto.codegen.kura.templates.bluetooth.DeviceTemplate;
import org.eclipse.vorto.codegen.kura.templates.cloud.FunctionblockTemplate;
import org.eclipse.vorto.codegen.kura.templates.cloud.IDataServiceTemplate;
import org.eclipse.vorto.codegen.kura.templates.cloud.bosch.BoschDataServiceTemplate;
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
		
		if (invocationContext.getConfigurationProperties().getOrDefault("boschcloud", "false").equalsIgnoreCase("true")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new PomTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new IDataServiceTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new BoschDataServiceTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new ThingClientFactoryTemplate()));
		}
		
		if (invocationContext.getConfigurationProperties().getOrDefault("bluetooth", "false").equalsIgnoreCase("true")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new MetatypeTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new DeviceBluetoothFinderTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new DeviceTemplate()));
		} else {
			generator.addTask(new GeneratorTaskFromFileTemplate<>(new DefaultAppTemplate()));
		}
		
		for (FunctionblockProperty fbProperty : context.getProperties()) {
			new GeneratorTaskFromFileTemplate<>(new FunctionblockTemplate()).generate(fbProperty.getType(), invocationContext, outputter);
		}
		
		generator.generate(context,invocationContext, outputter);
		
		return outputter;
	}

	@Override
	public String getServiceKey() {
		return "kura";
	}
}
