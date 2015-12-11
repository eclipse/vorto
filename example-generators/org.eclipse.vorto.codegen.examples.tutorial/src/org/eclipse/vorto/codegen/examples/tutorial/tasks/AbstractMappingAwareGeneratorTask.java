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
package org.eclipse.vorto.codegen.examples.tutorial.tasks;

import org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.model.IMapping;

public abstract class AbstractMappingAwareGeneratorTask extends AbstractTemplateGeneratorTask<InformationModel> {

	private IMapping mapping = null;

	@Override
	public String getPath(final InformationModel model) {
		return "src-gen/";
	}

	public AbstractMappingAwareGeneratorTask(IMapping mapping) {
		this.mapping = mapping;
	}

	public IMapping getMapping() {
		return mapping;
	}
}