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

package org.eclipse.vorto.codegen.internal.ui.wizard.generation.tasks;

import org.eclipse.vorto.codegen.api.context.IGeneratorProjectContext;
import org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.internal.ui.wizard.generation.templates.JsonFileTemplate;

public class JsonFileTask extends
		AbstractTemplateGeneratorTask<IGeneratorProjectContext> {

	private static final String XTEND_ENDING = ".xtend";

	@Override
	public String getFileName(final IGeneratorProjectContext wizardPage) {
		return "JsonGenerator" + XTEND_ENDING;
	}

	@Override
	public String getPath(final IGeneratorProjectContext wizardPage) {
		return "/src/" + wizardPage.getPackageFolders();
	}

	@Override
	public ITemplate<IGeneratorProjectContext> getTemplate() {
		return new JsonFileTemplate();
	}
}