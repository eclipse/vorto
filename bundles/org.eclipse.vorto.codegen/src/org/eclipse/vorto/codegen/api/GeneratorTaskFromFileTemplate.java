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
package org.eclipse.vorto.codegen.api;

/**
 * 
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 *
 */
public class GeneratorTaskFromFileTemplate<Context> extends AbstractTemplateGeneratorTask<Context> implements ICodeGeneratorTask<Context> {

	private IFileTemplate<Context> fileTemplate;

	public GeneratorTaskFromFileTemplate(IFileTemplate<Context> template) {
			this.fileTemplate = template;
	}

	@Override
	public String getFileName(Context ctx) {
		return fileTemplate.getFileName(ctx);
	}

	@Override
	public String getPath(Context ctx) {
		return fileTemplate.getPath(ctx);
	}

	@Override
	public ITemplate<Context> getTemplate() {
		return fileTemplate;
	}
}