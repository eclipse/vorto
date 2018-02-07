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
package org.eclipse.vorto.codegen.hono;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.hono.model.FunctionblockTemplate;
import org.eclipse.vorto.codegen.hono.model.InformationModelTemplate;
import org.eclipse.vorto.codegen.hono.service.IDataServiceTemplate;
import org.eclipse.vorto.codegen.hono.service.hono.HonoDataService;
import org.eclipse.vorto.codegen.hono.service.hono.HonoMqttClientTemplate;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * Generates a Java Client that sends a JSON to the Hono MQTT Connector. The data is compliant to a Vorto & Ditto format.
 *
 */
public class EclipseHonoGenerator implements IVortoCodeGenerator {

	@Override
	public IGenerationResult generate(InformationModel model, InvocationContext context, IVortoCodeGenProgressMonitor monitor) {
		GenerationResultZip output = new GenerationResultZip(model, getServiceKey());
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new PomFileTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new Log4jTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new CertificateTemplate()));
		
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new AppTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new IDataServiceTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new HonoDataService()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new HonoMqttClientTemplate()));
		generator.addTask(new GeneratorTaskFromFileTemplate<InformationModel>(new InformationModelTemplate()));
		
		generator.generate(model, context, output);
		
		for (FunctionblockProperty fbProperty : model.getProperties()) {
			new GeneratorTaskFromFileTemplate<>(new FunctionblockTemplate(model)).generate(fbProperty.getType(), context, output);
		}
		
		return output;
	}
	

	@Override
	public String getServiceKey() {
		return "eclipsehono";
	}
}
