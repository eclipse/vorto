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

import java.util.Set;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.perspective.IModelContentProvider;
import org.eclipse.vorto.perspective.util.TreeViewerCallback;
import org.eclipse.vorto.perspective.util.TreeViewerTemplate;

public class ChangeModelProjectListener extends AbstractResourceChangeListener {

	private TreeViewer treeViewer;
	
	public ChangeModelProjectListener(IModelContentProvider contentProvider, TreeViewerTemplate template, TreeViewer treeViewer) {
		super(contentProvider, template);
		this.treeViewer = treeViewer;
	}

	@Override
	protected void processChange(final IModelProject project) {
		template.update(new TreeViewerCallback() {

			@Override
			public void doUpdate(TreeViewer treeViewer) {
				treeViewer.setInput(contentProvider.getContent());
				expandProject(project);
			}
		});	

	}
	
	protected void expandProject(IModelProject modelProject) {
		IModelProject inputModelProject = this.getProjectFromTreeViewer(modelProject.getId());
		if (inputModelProject != null) {
			treeViewer.expandToLevel(inputModelProject, 2);
		}
	}

	@SuppressWarnings("rawtypes")
	private IModelProject getProjectFromTreeViewer(ModelId modelId) {
		Set inputs = (Set) treeViewer.getInput();
		for (Object input : inputs) {
			if (input instanceof IModelProject) {
				if (((IModelProject) input).getId().getName().equals(modelId.getName())) {
					return ((IModelProject) input);
				}
			}
		}
		return null;
	}

}
