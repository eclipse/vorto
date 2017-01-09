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
package org.eclipse.vorto.codegen.examples.ios;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.DatatypeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.api.mapping.IMapped;
import org.eclipse.vorto.codegen.examples.ios.templates.CoreBluetoothDetectionTemplate;
import org.eclipse.vorto.codegen.examples.ios.templates.DeviceServiceTemplate;
import org.eclipse.vorto.codegen.examples.ios.templates.EntityClassTemplate;
import org.eclipse.vorto.codegen.examples.ios.templates.EnumClassTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class IOSPlatformGenerator implements IVortoCodeGenerator {
	
	@Override
	public IGenerationResult generate(InformationModel context, InvocationContext invocationContext,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		GenerationResultZip outputter = new GenerationResultZip(context,getServiceKey());
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		generator.addTask(new DatatypeGeneratorTask(new EntityClassTemplate(), new EnumClassTemplate()));
		
		IMapped<InformationModel> mappedElement = invocationContext.getMappedElement(context, "binding");
		
		if (mappedElement.hasAttribute("ble")) {
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new CoreBluetoothDetectionTemplate()));
			generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new DeviceServiceTemplate()));
		}
				
		generator.generate(context,invocationContext, outputter);
		
		return outputter;
	}

	@Override
	public String getServiceKey() {
		return "ios";
	}
}
