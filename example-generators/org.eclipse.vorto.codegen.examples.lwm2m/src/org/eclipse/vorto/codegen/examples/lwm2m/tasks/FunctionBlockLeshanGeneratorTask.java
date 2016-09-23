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
package org.eclipse.vorto.codegen.examples.lwm2m.tasks;

import org.eclipse.vorto.codegen.api.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.ITemplate;
import org.eclipse.vorto.codegen.examples.lwm2m.templates.FunctionBlockLeshanTemplate;
import org.eclipse.vorto.codegen.examples.lwm2m.utils.ModuleUtil;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

/**
 * @author Shaodong Ying (Robert Bosch (SEA) Pte. Ltd)
 */
public class FunctionBlockLeshanGeneratorTask extends AbstractTemplateGeneratorTask<FunctionblockModel> {

	@Override
	public String getFileName(final FunctionblockModel model) {
		return model.getName() + ".java";
	}

	@Override
	public String getPath(final FunctionblockModel model) {
		 return ModuleUtil.getLeshanPath();
	}

	@Override
	public ITemplate<FunctionblockModel> getTemplate() {
		return new FunctionBlockLeshanTemplate();
	}

}
