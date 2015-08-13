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
package org.eclipse.vorto.perspective.dnd;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.model.DatatypeModelProject;
import org.eclipse.vorto.core.model.FunctionblockModelProject;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.InformationModelProject;
import org.eclipse.vorto.perspective.dnd.dropaction.ModelProjectDropAction;
import org.eclipse.vorto.perspective.dnd.dropaction.RepositoryResourceDropAction;

public class DatatypeProjectDropListener extends ViewerDropAdapter {

	private Map<String, IDropAction> dropActions = initializeDropActions();

	public DatatypeProjectDropListener(Viewer viewer) {
		super(viewer);
	}

	private Map<String, IDropAction> initializeDropActions() {
		Map<String, IDropAction> dropActions = new HashMap<String, IDropAction>();
		IDropAction modelProjectDropAction = new ModelProjectDropAction();
		dropActions.put(DatatypeModelProject.class.getName(),
				modelProjectDropAction);
		dropActions.put(FunctionblockModelProject.class.getName(),
				modelProjectDropAction);
		dropActions.put(InformationModelProject.class.getName(),
				modelProjectDropAction);
		dropActions.put(ModelResource.class.getName(),
				new RepositoryResourceDropAction());
		return dropActions;
	}

	@Override
	public boolean performDrop(Object data) {
		IModelProject targetProject = (IModelProject) this.getCurrentTarget();

		if (data instanceof IStructuredSelection) {
			Object droppedResource = ((IStructuredSelection) data)
					.getFirstElement();
			IDropAction dropAction = dropActions.get(droppedResource.getClass()
					.getName());
			if (dropAction != null) {
				return dropAction.performDrop(targetProject, droppedResource);
			}
		}

		return false;
	}

	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		return target instanceof FunctionblockModelProject
				|| target instanceof DatatypeModelProject;
	}
}
