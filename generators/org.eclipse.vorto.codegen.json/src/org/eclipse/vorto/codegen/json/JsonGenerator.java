/*******************************************************************************
 * Copyright (c) 2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.json;

import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.codegen.json.templates.JsonIMTemplate;

public class JsonGenerator implements IVortoCodeGenerator {

	public GenerationResultZip generate(InformationModel infomodel, InvocationContext context,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		GenerationResultZip output = new GenerationResultZip(infomodel, getServiceKey());

		GeneratorTaskFromFileTemplate<InformationModel> imTemplate = 
				new GeneratorTaskFromFileTemplate<InformationModel>(new JsonIMTemplate());
		imTemplate.generate(infomodel, context, output);

		return output;
	}

	public String getServiceKey() {
		return "json";
	}
}
