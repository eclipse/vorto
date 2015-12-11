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
package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.vorto.codegen.api.tasks.AbstractTemplateGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.ITemplate;
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.EntityClassTemplate;
import org.eclipse.vorto.core.api.model.datatype.Entity;

public class EntityClassGeneratorTask extends
		AbstractTemplateGeneratorTask<Entity> {
	@Override
	public String getFileName(final Entity entity) {
		return StringUtils.capitalize(entity.getName()) + ".java";
	}

	@Override
	public String getPath(final Entity entity) {
		return ModuleUtil.getEntityPath(entity);
	}

	@Override
	public ITemplate<Entity> getTemplate() {
		return new EntityClassTemplate();
	}
}
