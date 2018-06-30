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
package org.eclipse.vorto.perspective.util;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.core.ui.model.IModelProject;

public class NullModelProject implements IModelProject {

	@Override
	public IProject getProject() {
		return null;
	}

	@Override
	public void refresh(IProgressMonitor monitor) {
	}

	@Override
	public List<IModelElement> getModelElements() {
		return Collections.emptyList();
	}

	@Override
	public List<IModelElement> getModelElementsByType(ModelType modelType) {
		return Collections.emptyList();
	}

	@Override
	public IModelElement getModelElementById(ModelId modelId) {
		return null;
	}

	@Override
	public List<MappingModel> getMapping(String targetPlatform) {
		return Collections.emptyList();
	}

	@Override
	public IModelElement addModelElement(ModelId modelId, InputStream inputStream) {
		return null;
	}

	@Override
	public boolean exists(ModelId modelId) {
		return false;
	}

}
