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
package org.eclipse.vorto.codegen.api.tasks;

public abstract class AbstractTemplateGeneratorTask<Context> implements
		ICodeGeneratorTask<Context> {

	public void generate(final Context ctx, final IOutputter outputter) {

		Generated generated = new Generated(getFileName(ctx), getPath(ctx),
				getTemplate().getContent(ctx));
		outputter.output(generated);
	}

	public abstract String getFileName(final Context ctx);

	public abstract String getPath(final Context ctx);

	public abstract ITemplate<Context> getTemplate();

}
