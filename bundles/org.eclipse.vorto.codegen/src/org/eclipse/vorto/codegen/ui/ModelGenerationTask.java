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
package org.eclipse.vorto.codegen.ui;

import org.eclipse.vorto.codegen.api.context.IModelProjectContext;
import org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;

public class ModelGenerationTask extends
		AbstractTemplateGeneratorTask<IModelProjectContext> {

	public static final String SRC_MODELS = "src/models/";
	private final String suffix;
	private final ITemplate<IModelProjectContext> fileTemplate;

	public ModelGenerationTask(String fileSuffix,
			ITemplate<IModelProjectContext> template) {
		suffix = fileSuffix.startsWith(".") ? fileSuffix : "."
				.concat(fileSuffix);
		fileTemplate = template;
	}

	@Override
	public String getFileName(IModelProjectContext ctx) {
		return ctx.getModelName() + suffix;
	}

	@Override
	public String getPath(IModelProjectContext ctx) {
		return SRC_MODELS;
	}

	@Override
	public ITemplate<IModelProjectContext> getTemplate() {
		return fileTemplate;
	}

}
