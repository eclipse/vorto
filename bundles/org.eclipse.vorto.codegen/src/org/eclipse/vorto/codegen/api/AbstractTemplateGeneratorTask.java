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

import org.eclipse.vorto.codegen.api.mapping.InvocationContext;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public abstract class AbstractTemplateGeneratorTask<InformationModelFragment> implements ICodeGeneratorTask<InformationModelFragment> {

	public void generate(final InformationModelFragment fragmentModel, final InvocationContext context, final IGeneratedWriter writer) {

		Generated generated = new Generated(getFileName(fragmentModel), getPath(fragmentModel), getTemplate().getContent(fragmentModel,context));
		writer.write(generated);
	}

	public abstract String getFileName(final InformationModelFragment fragment);

	public abstract String getPath(final InformationModelFragment fragment);

	public abstract ITemplate<InformationModelFragment> getTemplate();

}
