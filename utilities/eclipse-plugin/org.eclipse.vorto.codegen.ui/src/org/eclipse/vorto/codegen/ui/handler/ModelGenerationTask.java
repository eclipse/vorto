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
package org.eclipse.vorto.codegen.ui.handler;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.ui.context.IModelProjectContext;

public class ModelGenerationTask extends
		AbstractTemplateGeneratorTask<IModelProjectContext> {

	private final ITemplate<IModelProjectContext> fileTemplate;
	private final String modelFolder;

	public ModelGenerationTask(String fileSuffix,
			ITemplate<IModelProjectContext> template, String modelFolder) {
		fileTemplate = template;
		this.modelFolder = modelFolder;
	}

	@Override
	public String getFileName(IModelProjectContext ctx) {
		return ctx.getModelId().getFileName();
	}

	@Override
	public String getPath(IModelProjectContext ctx) {
		return modelFolder;
	}

	@Override
	public ITemplate<IModelProjectContext> getTemplate() {
		return fileTemplate;
	}

}
