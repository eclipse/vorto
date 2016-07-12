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
package org.eclipse.vorto.perspective.command;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.perspective.util.ImageUtil;
import org.eclipse.vorto.perspective.view.ILocalModelWorkspace;

public abstract class DeleteModelAction extends Action {

	private ILocalModelWorkspace workspace;

	public DeleteModelAction(ILocalModelWorkspace workspace) {
		super("Delete", ImageDescriptor.createFromImage(ImageUtil.getImage("delete.gif")));
		this.workspace = workspace;
	}
	
	public static Action newInstance(ILocalModelWorkspace workspace, final TreeViewer viewer, final IModelElement model) {
		return new DeleteModelAction(workspace) {
			@Override
			protected TreeViewer getViewer() {
				return viewer;
			}

			@Override
			protected IModelElement getSelectedElement() {
				return model;
			}
		};
	}

	@Override
	public void run() {
		IModelElement modelElement = getSelectedElement();

		final boolean isOKOrYes = MessageDialog.open(MessageDialog.QUESTION, getViewer().getControl().getShell(),
				"Delete Model", "Are you sure you want to delete the model " + modelElement.getId().getName() + " ?",
				SWT.NONE);

		if (isOKOrYes) {
			deleteResource(getViewer().getControl().getShell(), modelElement.getModelFile());
		}
	}

	private void deleteResource(Shell shell, final IResource selectedResource) {

		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(shell);
		try {
			progressDialog.run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(final IProgressMonitor monitor) {

					monitor.beginTask("Delete Model", 100);

					try {
						selectedResource.delete(true, monitor);
					} catch (CoreException e) {
						throw new RuntimeException(e);
					}

					monitor.done();
				}

			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract IModelElement getSelectedElement();

	protected abstract TreeViewer getViewer();
}
