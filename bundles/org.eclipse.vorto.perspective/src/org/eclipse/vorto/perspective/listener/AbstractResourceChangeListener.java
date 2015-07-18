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
package org.eclipse.vorto.perspective.listener;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.perspective.IModelContentProvider;
import org.eclipse.vorto.perspective.util.TreeViewerTemplate;

public abstract class AbstractResourceChangeListener implements IResourceChangeListener {
	
	protected IModelContentProvider contentProvider;
	
	protected TreeViewerTemplate template;
	
	public AbstractResourceChangeListener(IModelContentProvider contentProvider, TreeViewerTemplate template) {
		this.contentProvider = contentProvider;
		this.template = template;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IModelProject modelProject = null;
		if (event.getResource() != null && event.getResource() instanceof IProject) {
			modelProject = getModelProjectOrNull(event.getResource());
		} else if (event.getDelta() != null && event.getDelta().getAffectedChildren()[0].getResource() instanceof IProject) {
			modelProject = getModelProjectOrNull(event.getDelta().getAffectedChildren()[0].getResource());
		}
		
		if (modelProject != null) {
			processChange(modelProject);
		}
	}
	
	private IModelProject getModelProjectOrNull(IResource resource) {
		if (resource instanceof IProject) {
			try {
				return ModelProjectServiceFactory.getDefault().getProjectFromEclipseProject((IProject) resource);
			} catch (IllegalArgumentException ex) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	protected abstract void processChange(IModelProject project);
}
