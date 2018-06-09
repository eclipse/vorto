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
package org.eclipse.vorto.perspective.listener;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.vorto.core.ui.model.IModelProject;
import org.eclipse.vorto.core.ui.model.ModelParserFactory;
import org.eclipse.vorto.core.ui.model.VortoModelProject;
import org.eclipse.vorto.perspective.view.ILocalModelWorkspace;

public class RemoveModelProjectListener implements IResourceChangeListener {

	private ILocalModelWorkspace localModelBrowser;
	
	public RemoveModelProjectListener(ILocalModelWorkspace localModelBrowser) {
		this.localModelBrowser = localModelBrowser;
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (isVortoModelProjectChangeEvent(event)) {
			IModelProject deletedModelProject = getModelProjectFromEvent(event);
			localModelBrowser.getProjectBrowser().removeProject(deletedModelProject);
		}
	}
	
	private IModelProject getModelProjectFromEvent(IResourceChangeEvent event) {
		IProject project = (IProject)event.getResource();
		return new VortoModelProject(project, ModelParserFactory.getInstance().getModelParser());
	}
	
	private boolean isVortoModelProjectChangeEvent(IResourceChangeEvent event) {
		return event.getResource() != null && event.getResource() instanceof IProject && VortoModelProject.isVortoModelProject((IProject)event.getResource());
	}
}
