/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.examples.leshan;

import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.mapping.InvocationContext;
import org.eclipse.vorto.codegen.examples.leshan.tasks.FunctionBlockGeneratorTask;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * @author Shaodong Ying (Robert Bosch (SEA) Pte. Ltd)
 */
public class LeshanGenerator implements IVortoCodeGenerator {

	@Override
	public IGenerationResult generate(InformationModel infomodel, InvocationContext context) {
		GenerationResultZip output = new GenerationResultZip(infomodel,getServiceKey());
		for (FunctionblockProperty fbProperty : infomodel.getProperties()) {		
			new FunctionBlockGeneratorTask().generate(fbProperty.getType(), context, output);
		}
		return output;
	}

	@Override
	public String getServiceKey() {
		return "leshan";
	}

}
